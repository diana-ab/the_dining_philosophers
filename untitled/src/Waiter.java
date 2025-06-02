import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Waiter {

    private List <Philosopher> setingPhilosopher;
    private int maxSetingPhilosophers;
    private Map<Fork,Philosopher>  saveForkForPhilo;

    public Waiter(int cherNumber) {
        this.maxSetingPhilosophers =cherNumber-1;
        this.setingPhilosopher=new ArrayList<>();
        this.saveForkForPhilo=new HashMap<>();


    }



    public synchronized void requestSeat(Philosopher p) throws InterruptedException {
        while (this.setingPhilosopher.size() >= this.maxSetingPhilosophers) {
            wait();
        }
        this.setingPhilosopher.add(p);
        this.updateState();
    }

    public synchronized void leaveSeat(Philosopher p) {
        this.setingPhilosopher.remove(p);
        this.updateState();
    }

    private Philosopher findTheStarvingPhilosopher(){
        if (setingPhilosopher.isEmpty()) return null;
        Philosopher theStarving = this.setingPhilosopher.get(0);
        for (Philosopher philosopher : this.setingPhilosopher){
            if(philosopher.getEatingCount()<theStarving.getEatingCount()){
                theStarving=philosopher;
            }
        }
        return theStarving;
    }


    private void removeForFromReservationsFor(Philosopher philosopher) {
        Fork leftFork =philosopher.getLeftFork1();
        Fork rightFork = philosopher.getRightFork2();
        this.saveForkForPhilo.remove(leftFork, philosopher);
        this.saveForkForPhilo.remove(rightFork ,philosopher);


    }

    public synchronized void takeForkFromPhilo(Philosopher philosopher) {
        Fork left = philosopher.getLeftFork1();
        Fork right = philosopher.getRightFork2();
        if (left.getHeldBy() == philosopher) {left.setHeldBy(null);}
        if (right.getHeldBy() == philosopher) {right.setHeldBy(null);}
        this.removeForFromReservationsFor(philosopher);
        this.updateState();
    }
    private boolean theForkReservedForSomeoneElse(Philosopher philosopher ,Fork fork){
        if (this.saveForkForPhilo.containsKey(fork)){
            if (this.saveForkForPhilo.get(fork)!=philosopher)
            {
                return true;}
        }
        return false;
    }


    public void reservForksToHungriestPhilo() {
        Philosopher mostHungry = findTheStarvingPhilosopher();
        if (mostHungry == null) return;
        Fork left = mostHungry.getLeftFork1();
        Fork right = mostHungry.getRightFork2();

        if (!theForkReservedForSomeoneElse(mostHungry, left)
                && !theForkReservedForSomeoneElse(mostHungry, right)
                ) {
            this.saveForkForPhilo.put(left, mostHungry);
            this.saveForkForPhilo.put(right, mostHungry);
        }
    }

    private void updateState() {
        notifyAll();
    }

    public synchronized boolean requestFork(Philosopher p, Fork fork) {
        if (fork.getHeldBy() != null) return false;
        if (this.theForkReservedForSomeoneElse(p, fork)) return false;
        return true;
    }









    public boolean ifLeftForkAvailable(Philosopher philosopher){
        Fork leftFork1=philosopher.getLeftFork1();
        if(!requestFork(philosopher,leftFork1)){return false;}
        philosopher.getLeftFork1().setHeldBy(philosopher);
        return true;
    }

    public boolean ifRightForkAvailable(Philosopher philosopher){
        Fork rightFork2=philosopher.getRightFork2();
        if(!requestFork(philosopher,rightFork2)){return false;}
        philosopher.getRightFork2().setHeldBy(philosopher);
        return true;
    }















}
