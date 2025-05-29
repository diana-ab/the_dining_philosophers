import java.awt.*;

public class ProgramObject {
    private int x;
    private int y;
    private int width;
    private int height;

    public ProgramObject(int y, int x, int width, int height) {
        this.y = y;
        this.x = x;
        this.width = width;
        this.height = height;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }
    public void paint(Graphics g) {
        g.fillRect( this.x, this.y, this.width, this.height);
    }
}
