import java.awt.*;

public class ProgramObject {
    private Point point;
    private int width;
    private int height;

    public ProgramObject(int y, int x, int width, int height) {
        this.point=new Point(x,y);
        this.width = width;
        this.height = height;
    }

    public int getY() {
        return this.point.getY();
    }

    public void setY(int y) {
        this.point.setY(y);
    }

    public int getX() {
        return this.point.getX();
    }

    public void setX(int x) {
        this.point.setX(x);
    }

    public void paint(Graphics g) {
        g.fillRect( this.getX(), this.getY(), this.width, this.height);
    }
}
