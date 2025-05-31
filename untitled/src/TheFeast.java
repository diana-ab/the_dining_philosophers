import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TheFeast {
    public static final int CANTER_TABLE_X = 400;
    public static final int CANTER_TABLE_Y = 300;
    public static final int RADIUS_TABLE = 200;
    public static final int NUM_PHILOSOPHERS = 5;
    public static final String[] NAMES = {"John Lock", "Plato", "Socrates", "Nietzsche", "Descartes"};

    private List<Philosopher> philosophers;
    private Map<Integer, Fork> forks;


    public TheFeast() {
        this.forks = this.generateFork();
        this.philosophers = this.generatePhilosopher();

        this.feast();

    }

    private void feast() {
        new Thread(() -> {
            while (true) {
                System.out.println(this.philosophers.get(0));
                System.out.println(this.philosophers.get(1));
                Utils.sleep(1000);
            }
        }).start();
    }


    public List<Philosopher> getPhilosophers() {
        return philosophers;
    }

    public Map<Integer, Fork> getForks() {
        return forks;
    }

    private List<Philosopher> generatePhilosopher() {
        List<Philosopher> tempPhilosophers = new ArrayList<>();
        for (int i = 0; i < NUM_PHILOSOPHERS; i++) {
            double angle = 2 * Math.PI * i / NUM_PHILOSOPHERS;
            int x = (int) (CANTER_TABLE_X + RADIUS_TABLE * Math.cos(angle));
            int y = (int) (CANTER_TABLE_Y + RADIUS_TABLE * Math.sin(angle));

            Fork leftFork = forks.get((i + 1));
            Fork rightFork = forks.get((i + 1) % NUM_PHILOSOPHERS + 1);

            Philosopher tempPhilo = new Philosopher(NAMES[i], leftFork, rightFork, x, y);
            tempPhilosophers.add(tempPhilo);
        }
        return tempPhilosophers;
    }


    private Map<Integer, Fork> generateFork() {
//        Map<Integer, Fork> tempForks = new HashMap<>();
//        for (int i = 1; i <= NUM_PHILOSOPHERS; i++) {
//            tempForks.put(i, new Fork(i, 0, 0));
//        }
//        return tempForks;



        Map<Integer, Fork> tempForks = new HashMap<>();
        int forkRadius = RADIUS_TABLE ;

        for (int i = 1; i <= NUM_PHILOSOPHERS; i++) {
            double forkAngle = 2 * Math.PI * (i - 1) / NUM_PHILOSOPHERS;
            int forkX = (int) (CANTER_TABLE_X + forkRadius * Math.cos(forkAngle));
            int forkY = (int) (CANTER_TABLE_Y + forkRadius * Math.sin(forkAngle));
            tempForks.put(i, new Fork(i, forkX, forkY));
        }
        return tempForks;

    }

}
