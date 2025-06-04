import java.awt.*;


public class Fork extends ProgramObject {
    public static final int WIDTH = 10;
    public static final int HEIGHT = 40;
    public static final int FORK_SEPARATION = 8;
    public static final double ALPHA = 0.3;


    private int originalX;
    private int originalY;
    private int number;
    private Philosopher heldBy;
    private int targetX;
    private int targetY;


    public Fork(int number, int x, int y) {
        super(x, y, WIDTH, HEIGHT);
        this.heldBy = null;
        this.number = number;
        this.originalX = x;
        this.originalY = y;
        this.targetX = x;
        this.targetY = y;
    }

    public Philosopher getHeldBy() {
        return heldBy;
    }

    public void setHeldBy(Philosopher heldBy) {
        this.heldBy = heldBy;
    }

    public String toString() {
        if (this.heldBy == null) {
            return "Fork " + this.number
                    + " is not currently held by anyone";

        } else {
            return "Fork " + this.number +
                    " is currently held by " +
                    this.heldBy.getName();

        }
    }

    public void updatePosition() {
        if (heldBy != null) {
            int philosopherCenterX = heldBy.getX() + Philosopher.WIDTH / 2;
            int philosopherCenterY = heldBy.getY() + Philosopher.HEIGHT / 2;
            this.targetX = (int) (originalX * (1 - ALPHA) + philosopherCenterX * ALPHA);
            this.targetY = (int) (originalY * (1 - ALPHA) + philosopherCenterY * ALPHA);

            double angle = Math.toRadians(30 * number);
            this.targetX += (int) (FORK_SEPARATION * Math.cos(angle));
            this.targetY += (int) (FORK_SEPARATION * Math.sin(angle));

            this.setX(targetX);
            this.setY(targetY);
        } else {
            this.setX(originalX);
            this.setY(originalY);
        }
    }

    public int getNumber() {
        return number;
    }

    @Override
    public void paint(Graphics g) {
        if (heldBy != null) {
            g.setColor(Color.RED);
        } else {
            g.setColor(Color.green);
        }
        g.fillRect(this.getX(), this.getY(), WIDTH, HEIGHT);

        g.setFont(new Font("Arial", Font.BOLD, 15));

        g.drawString(String.valueOf(this.number), this.getX() + 20, this.getY() + 20);
    }
}
