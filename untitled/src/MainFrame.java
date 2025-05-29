import javax.swing.*;

public class MainFrame extends JFrame {
    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 800;
    public static final int WINDOW_X = 0;
    public static final int WINDOW_Y = 0;


    private PhilosophersPanel panel;

    public MainFrame() {
        this.setTitle("DINING PHILOSOPHERS");
        this.setResizable(false);

        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.panel = new PhilosophersPanel(WINDOW_X, WINDOW_Y, WINDOW_WIDTH, WINDOW_HEIGHT);
        this.add(panel);

        this.setVisible(true);

    }

}
