import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PhilosophersPanel extends JPanel {
    private static final int SLEEP_REPAINT =20;

    private List<Philosopher> philosophers;
    private Map<Integer, Fork> forks;
    private TheFeast feast;
    private List<ToggleButton> buttonList;


    public PhilosophersPanel(int x, int y, int width, int height) {
        this.setBounds(x, y, width, height);
        this.feast = new TheFeast(width, height);
        this.philosophers = feast.getPhilosophers();
        this.forks = feast.getForks();

        this.buttonList = this.generateButtons();
        for (ToggleButton button : this.buttonList) {
            this.add(button);
        }
        this.repaintThread();
    }

    public void repaintThread() {
        Thread repaintThread = new Thread(() -> {
            while (true) {
                Utils.sleep(SLEEP_REPAINT);


                SwingUtilities.invokeLater(() -> {
                    this.repaint();
                });
            }
        });
        repaintThread.setDaemon(true);
        repaintThread.start();
    }

    private List<ToggleButton> generateButtons() {
        List<ToggleButton> buttons = new ArrayList<>();
        for (int i = 0; i < this.philosophers.size(); i++) {
            buttons.add(new ToggleButton(this.feast.getPhilosophers().get(i)));
        }
        return buttons;
    }

    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        for (Philosopher p : this.philosophers) {
            p.paint(graphics);
        }

        for (Fork fork : forks.values()) {
            fork.updatePosition();
            fork.paint(graphics);
        }
        graphics.setColor(Color.RED);

        graphics.drawOval(
                this.feast.getCenterTableX() - TheFeast.RADIUS_TABLE,
                this.feast.getCenterTableY() - TheFeast.RADIUS_TABLE,
                2 * TheFeast.RADIUS_TABLE,
                2 * TheFeast.RADIUS_TABLE);

        for (int i = 0; i < philosophers.size(); i++) {
            Philosopher current = philosophers.get(i);
            Philosopher next = philosophers.get((i + 1) % philosophers.size());
            if (current.isEating() && next.isEating()) {
                System.err.println("⚠️ Warning: " + current.getName() + " and " + next.getName() + " are eating together!");
            }
        }
    }
}

