import javax.swing.*;
import java.util.*;

public class Waiter {
    private static final int MAX_GAP = 2;
    private static final long COOLDOWN_FOR_GAP = 5000;

    private Map<Philosopher, Long> blockedTheFatUntil;
    private List<Philosopher> sittingPhilosopher;
    private int maxSittingPhilosophers;
    private Map<Fork, Philosopher> saveForkForPhilo;
    private List<Philosopher> allPhilosopher;


    public Waiter(int cherNumber) {
        this.maxSetingPhilosophers =cherNumber-1;
        this.setingPhilosopher=new ArrayList<>();
        this.saveForkForPhilo=new HashMap<>();


    }

    public void setPhilosophers(List<Philosopher> philosophers) {
        this.allPhilosopher = philosophers;
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
        return reservd;
    }


    public void balanceHunger() {
        Philosopher mostHungry = findTheStarvingPhilosopher();
        if (mostHungry == null || !mostHungry.isActivePhilosopher()) return;
        Philosopher theFat = findTheFat();
        reservedFork(mostHungry);
        int gap = theFat.getEatingCount() - mostHungry.getEatingCount();
        if (gap >= MAX_GAP && !this.blockedTheFatUntil.containsKey(theFat)) {
            this.blockedTheFatUntil.put(theFat, System.currentTimeMillis() + COOLDOWN_FOR_GAP);
        }
        this.blockedTheFatUntil.entrySet().removeIf(entry -> System.currentTimeMillis() > entry.getValue());

    }

    private void reservedFork(Philosopher philosopher) {
        Fork left = philosopher.getLeftFork1();
        Fork right = philosopher.getRightFork2();
        if (!theForkReservedForSomeoneElse(philosopher, left)
                && !theForkReservedForSomeoneElse(philosopher, right)
        ) {
            this.saveForkForPhilo.put(left, philosopher);
            this.saveForkForPhilo.put(right, philosopher);
        }

    }

    private void updateState() {
        notifyAll();
    }

    public synchronized boolean requestFork(Philosopher p, Fork fork) {
        boolean freeFork = true;
        if (fork.getHeldBy() != null) {
            freeFork = false;
        } else if (this.theForkReservedForSomeoneElse(p, fork)) {
            freeFork = false;
        }
        return freeFork;
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

    private Philosopher findTheFat() {
        Philosopher theFat = this.allPhilosopher.get(0);
        for (Philosopher p : this.allPhilosopher) {
            if (p.getEatingCount() > theFat.getEatingCount()) {
                theFat = p;
            }
        }
        return theFat;

    }



}
