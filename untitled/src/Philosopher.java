import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static java.awt.Color.*;

public class Philosopher extends ProgramObject {
    public static final int THINKING = 1;
    public static final int WAITING_FOR_FORK_1 = 2;
    public static final int WAITING_FOR_FORK_2 = 3;
    public static final int EATING = 4;
    public static final int WIDTH = 150;
    public static final int HEIGHT = 100;
    public static final int X_LOCATION_FOR_WRITING = 5;
    public static final int Y_LOCATION_FOR_WRITING = 15;
    private static final int WAITING_TIME = 5000;
    private static final int SLEEP = 100;


    private String name;
    private int statusForPhilo;
    private int eatingCount;
    private Fork leftFork1;
    private Fork rightFork2;
    private boolean activePhilosopher;
    private Thread thread;
    private Map<Integer, PhilosopherStatus> statusMap;
    private Waiter waiter;


    public Philosopher(String name, Fork leftFork1, Fork rightFork2, int x, int y, Waiter waiter) {
        super(x, y, WIDTH, HEIGHT);
        this.name = name;
        this.waiter = waiter;
        this.statusForPhilo = THINKING;
        this.eatingCount = 0;
        this.leftFork1 = leftFork1;
        this.rightFork2 = rightFork2;
        this.activePhilosopher = true;


        statusMap = new HashMap<>();
        statusMap.put(THINKING, new PhilosopherStatus("thinking", LIGHT_GRAY));
        statusMap.put(WAITING_FOR_FORK_1, new PhilosopherStatus("waiting for left fork:" + leftFork1.getNumber(), ORANGE));
        statusMap.put(WAITING_FOR_FORK_2, new PhilosopherStatus("waiting for right fork:" + rightFork2.getNumber(), ORANGE));
        statusMap.put(EATING, new PhilosopherStatus("eating", GREEN));

        this.activatePhilosopher();
    }

    private void startAction() {
        this.thread = new Thread(() -> {
            Random random = new Random();
            while (this.activePhilosopher) {
                this.think(random, WAITING_TIME);
                this.waitForChar();
                if (breakLoopIfNecessary()) {
                    return;
                }

                this.waitForFork(this.leftFork1);
                if (breakLoopIfNecessary()) {
                    return;
                }
                this.sleepInRandomTime(random, (int) (WAITING_TIME / 2.5));
                this.waitForFork(this.rightFork2);
                if (breakLoopIfNecessary()) {
                    return;
                }
                this.eat(random, WAITING_TIME);
                this.finshEating();
            }
        });
        this.thread.start();
    }


    private void sleepInRandomTime(Random random, int limit) {
        Utils.sleep(random.nextInt(limit));
    }

    private void think(Random random, int limit) {
        this.statusForPhilo = THINKING;
        sleepInRandomTime(random, limit);
    }

    private void waitForFork(Fork fork) {
        this.statusForPhilo = this.leftFork1 == fork ? WAITING_FOR_FORK_1 : WAITING_FOR_FORK_2;

        while (this.leftFork1 == fork ? !this.waiter.ifLeftForkAvailable(this) :
                !this.waiter.ifRightForkAvailable(this)) {
            Utils.sleep(SLEEP);
            if (breakLoopIfNecessary()) {
                return;
            }
        }
    }

    private void waitForChar() {
        try {
            this.waiter.requestSeat(this);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void eat(Random random, int limit) {
        this.statusForPhilo = EATING;
        this.sleepInRandomTime(random, limit);
        this.eatingCount++;
    }

    public void finshEating() {
        this.releaseFork();
        this.waiter.leaveSeat(this);
        this.statusForPhilo = THINKING;
    }

    private PhilosopherStatus getStatusInfo() {
        return statusMap.getOrDefault(this.statusForPhilo, new PhilosopherStatus("", WHITE));
    }

    private boolean breakLoopIfNecessary() {
        if (!this.activePhilosopher) {
            this.releaseFork();
            this.statusForPhilo = THINKING;
            return true;
        }
        return false;
    }

    public String getName() {
        return this.name;
    }

    public String toString() {

        PhilosopherStatus info = getStatusInfo();
        return new SimpleDateFormat("HH:mm:ss").format(new Date()) +
                " Philosopher " + this.name +
                " is currently " +
                info.getText() + " (total times he ate: " + this.eatingCount + ")";
    }

    public boolean isActivePhilosopher() {
        return activePhilosopher;
    }

    public void activatePhilosopher() {
        this.activePhilosopher = true;
        this.startAction();
        System.out.println("start " + this.name);
    }

    public void deactivatePhilosopher() {
        this.activePhilosopher = false;
        this.finshEating();
        System.out.println("stop " + this.name);

    }

    private void releaseFork() {
        this.waiter.takeForkFromPhilo(this);
    }

    public void paint(Graphics g) {
        PhilosopherStatus info = getStatusInfo();
        g.setColor(info.getColor());
        super.paint(g);
        g.setColor(Color.BLACK);
        g.drawRect(this.getX(), this.getY(), WIDTH, HEIGHT);
        g.setFont(new Font("Arial", Font.BOLD, 15));

        g.drawString(this.name, this.getX() + X_LOCATION_FOR_WRITING, this.getY() + Y_LOCATION_FOR_WRITING);

        g.setFont(new Font("Arial", Font.PLAIN, 15));

        g.drawString(info.getText(), this.getX() + X_LOCATION_FOR_WRITING, this.getY() + (Y_LOCATION_FOR_WRITING * 2));
        g.drawString("ate: " + this.eatingCount, this.getX() + X_LOCATION_FOR_WRITING, this.getY() + (Y_LOCATION_FOR_WRITING * 3));
    }

    public Fork getLeftFork1() {
        return leftFork1;
    }

    public Fork getRightFork2() {
        return rightFork2;
    }

    public int getEatingCount() {
        return eatingCount;
    }

    public boolean isEating() {
        if (this.statusForPhilo == EATING) {
            return true;
        } else {
            return false;
        }
    }

}
