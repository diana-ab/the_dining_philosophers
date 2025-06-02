import java.awt.*;

public class Fork extends ProgramObject {
    public static final int WIDTH = 10;
    public static final int HEIGHT = 40;



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
            this.targetX = heldBy.getX() + Philosopher.WIDTH / 2;
            this.targetY = heldBy.getY() + Philosopher.HEIGHT / 2;
            this.setX(targetX);
            this.setY(targetY);
        } else {
            this.setX((this.originalX));
            this.setY((this.originalY));
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
            g.setColor(Color.BLACK);
        }
        g.fillRect(this.getX(), this.getY(), WIDTH, HEIGHT);
    }
}
