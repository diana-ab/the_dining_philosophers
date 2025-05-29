import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PhilosophersPanel extends JPanel {
    private List<Philosopher> philosophers;
    private Map<Integer, Fork> forks;
    private TheFeast feast;
    private List<ToggleButton> buttonList;

    public PhilosophersPanel(int x, int y, int width, int height) {
        this.setBounds(x, y, width, height);
        this.feast = new TheFeast();
        this.philosophers = feast.getPhilosophers();
        this.forks = feast.getForks();

        this.buttonList = this.generateButtons();
        for (ToggleButton button : this.buttonList) {
            this.add(button);
        }
        Timer repaintTimer = new Timer(100, e -> repaint());
        repaintTimer.start();

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
            //repaint();

        }

        for (Fork fork : forks.values()) {
            fork.updatePosition();
            fork.paint(graphics);
            //repaint();

        }
    }
}

