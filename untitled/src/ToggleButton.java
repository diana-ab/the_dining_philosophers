import javax.swing.*;

public class ToggleButton extends JButton {
    public static final int WIDTH = 80;
    public static final int HEIGHT = 40;

    private Philosopher philosopher;


    public ToggleButton(Philosopher philosopher) {
        super();
        this.philosopher = philosopher;
        this.setSize(WIDTH, HEIGHT);
        this.setText("Stop " + this.philosopher.getName());
        this.setActionListener();
    }


    private void updateAction() {
        if (this.philosopher.isActivePhilosopher()) {
            this.philosopher.deactivatePhilosopher();
            this.setText("Start " + this.philosopher.getName());

        } else {
            this.philosopher.activatePhilosopher();
            this.setText("Stop " + this.philosopher.getName());
        }
    }

    private void setActionListener() {
        this.addActionListener(e -> updateAction());
    }
}
