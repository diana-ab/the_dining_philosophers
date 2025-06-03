import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TheFeast {
    public static final int RADIUS_TABLE = 150;
    public static final int NUM_PHILOSOPHERS = 5;
    public static final String[] NAMES = {"Hillel the Elder", "Socrates", "Confucius", "Plato", "Akiva"};

    private List<Philosopher> philosophers;
    private Map<Integer, Fork> forks;
    private Waiter waiter;
    private int centerTableX;
    private int centerTableY;


    public TheFeast(int endingX, int endingY) {
        this.centerTableX = endingX / 2;
        this.centerTableY = endingY / 2;
        this.waiter = new Waiter(NUM_PHILOSOPHERS);
        this.forks = this.generateFork();
        this.philosophers = this.generatePhilosopher();
        this.waiter.setPhilosophers(this.philosophers);
        this.feast();
    }

    private void feast() {
        new Thread(() -> {
            while (true) {

                System.out.println(philosophers.get(1));
                System.out.println(philosophers.get(0));
                System.out.println(philosophers.get(2));
                System.out.println(philosophers.get(3));
                System.out.println(philosophers.get(4));
                this.waiter.balanceHunger();
                this.findTheNotActivePhilo();
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


    private double calculateAngle(double index) {
        return 2 * Math.PI * index / NUM_PHILOSOPHERS;
    }


    private Point calculatePhilosopherPosition(int index) {
        int clockwiseIndex = (NUM_PHILOSOPHERS - index) % NUM_PHILOSOPHERS;
        double angle = calculateAngle(clockwiseIndex + 0.5);
        int baseRadius = RADIUS_TABLE + 110;

        double balance = 20 * Math.abs(Math.cos(angle));

        int radius = baseRadius + (int) balance;

        int x = (int) (centerTableX + radius * Math.cos(angle) - Philosopher.WIDTH / 2);
        int y = (int) (centerTableY + radius * Math.sin(angle) - Philosopher.HEIGHT / 2);

        return new Point(x, y);
    }

    private Point calculateForkPosition(int index) {
        double angle = calculateAngle(index);

        int forkRadius = RADIUS_TABLE - 40;

        int x = (int) (centerTableX + forkRadius * Math.cos(angle));
        int y = (int) (centerTableY + forkRadius * Math.sin(angle));

        return new Point(x, y);
    }


    private Map<Integer, Fork> generateFork() {
        Map<Integer, Fork> tempForks = new HashMap<>();
        for (int i = 0; i < NUM_PHILOSOPHERS; i++) {
            Point pos = calculateForkPosition(i);
            tempForks.put(i, new Fork(i, pos.getX(), pos.getY()));
        }
        return tempForks;
    }

    private List<Philosopher> generatePhilosopher() {
        List<Philosopher> tempPhilosophers = new ArrayList<>();

        for (int i = 0; i < NUM_PHILOSOPHERS; i++) {
            Point pos = calculatePhilosopherPosition(i);


            int leftForkIndex = (i + 1) % NUM_PHILOSOPHERS;
            int rightForkIndex = i;

            if (leftForkIndex < rightForkIndex) {
                int temp = leftForkIndex;
                leftForkIndex = rightForkIndex;
                rightForkIndex = temp;
            }

            Fork leftFork = forks.get(leftForkIndex);
            Fork rightFork = forks.get(rightForkIndex);

            Philosopher philosopher = new Philosopher(
                    NAMES[i] + String.valueOf(i), leftFork, rightFork,
                    pos.getX(),
                    pos.getY(),
                    this.waiter
            );

            tempPhilosophers.add(philosopher);
        }

        return tempPhilosophers;
    }


    public int getCenterTableX() {
        return centerTableX;
    }


    public int getCenterTableY() {
        return centerTableY;
    }

    private void findTheNotActivePhilo() {
        for (Philosopher p : this.philosophers) {
            if (!p.isActivePhilosopher()) {
                p.finshEating();
            }
        }

    }
}
