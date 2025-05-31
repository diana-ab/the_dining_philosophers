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
        public static final int WIDTH = 140;
        public static final int HEIGHT = 100;
        public static final int X_LOCATION_FOR_WRITING= 5;
        public static final int Y_LOCATION_FOR_WRITING= 15;



        private String name;
        private int status;
        private int eatingCount;
        private Fork leftFork1;
        private Fork rightFork2;
        private boolean activePhilosopher;
        private Thread thread;
        private Map<Integer, PhilosopherStatus> statusMap;



        public Philosopher(String name, Fork leftFork1, Fork rightFork2, int x, int y) {
            super(x, y, WIDTH, HEIGHT);
            this.name = name;
            this.status = THINKING;
            this.eatingCount = 0;
            this.leftFork1 = leftFork1;
            this.rightFork2 = rightFork2;
            this.activePhilosopher = true;


            statusMap = new HashMap<>();
            statusMap.put(THINKING, new PhilosopherStatus("thinking", LIGHT_GRAY));
            statusMap.put(WAITING_FOR_FORK_1, new PhilosopherStatus("Waiting for the left fork: fork num"+ leftFork1.getNumber(), ORANGE));
            statusMap.put(WAITING_FOR_FORK_2, new PhilosopherStatus("Waiting for the right fork: fork num" + rightFork2.getNumber(), ORANGE));
            statusMap.put(EATING, new PhilosopherStatus("eating", GREEN));

            this.activatePhilosopher();
        }

        private PhilosopherStatus getStatusInfo() {
            return statusMap.getOrDefault(this.status, new PhilosopherStatus("", WHITE));
        }


        private void startAction() {
            this.thread = new Thread(() -> {
                Random random = new Random();
                while (this.activePhilosopher) {
                    Utils.sleep(random.nextInt(5000));
                    this.status = WAITING_FOR_FORK_1;
                    while (this.leftFork1.getHeldBy() != null) {
                        Utils.sleep(100);
                        if (breakLoopIfNecessary()) {
                            System.out.println("return first");
                            return;
                        }
                    }
                    if (breakLoopIfNecessary()) {
                        System.out.println("return first");
                        return;
                    }
                    this.leftFork1.setHeldBy(this);
                    Utils.sleep(random.nextInt(2000));
                    this.status = WAITING_FOR_FORK_2;
                    while (this.rightFork2.getHeldBy() != null) {
                        Utils.sleep(100);
                        if (breakLoopIfNecessary()) {
                            System.out.println("return first");
                            return;
                        }
                    }
                    if (breakLoopIfNecessary()) {
                        System.out.println("return second");
                        return;
                    }
                    this.rightFork2.setHeldBy(this);
                    this.status = EATING;
                    Utils.sleep(random.nextInt(5000));
                    this.releaseFork();
                    this.eatingCount++;
                    this.status = THINKING;
                }
            });
            this.thread.start();
        }

        private boolean breakLoopIfNecessary() {
            if (!this.activePhilosopher) {
                this.releaseFork();
                this.status = THINKING;
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
                    " Philosopher " + this.name + " is currently " +
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
            System.out.println("stop " + this.name);

        }

        private void releaseFork() {
            Philosopher holder1 = this.leftFork1.getHeldBy();
            Philosopher holder2 = this.rightFork2.getHeldBy();
            this.leftFork1.setHeldBy(holder1 == this ? null : holder1);
            this.rightFork2.setHeldBy(holder2 == this ? null : holder2);
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

            g.drawString(info.getText(), this.getX() + X_LOCATION_FOR_WRITING, this.getY() + (Y_LOCATION_FOR_WRITING*2));
            g.drawString("ate: " + this.eatingCount, this.getX() + X_LOCATION_FOR_WRITING, this.getY() + (Y_LOCATION_FOR_WRITING*3));
        }

        public void setLeftFork1(Fork leftFork1) {
            this.leftFork1 = leftFork1;
        }

        public void setRightFork2(Fork rightFork2) {
            this.rightFork2 = rightFork2;
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

        public void setEatingCount(int eatingCount) {
            this.eatingCount = eatingCount;
        }
    }
