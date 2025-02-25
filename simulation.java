import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class simulation extends JPanel implements ActionListener {

    private JFrame frame;
    private ArrayList<particles> particlesList = new ArrayList<>(); // ArrayList to store particles
    private particles prticle_reference = new particles(10, 10);
    private boolean Spacebar_sate = true;
    private Timer timer;

    public void panel(int width, int height) { 
        frame = new JFrame("Simulation");

        // Adjust frame size to account for particle size
        int adjustedWidth = width + prticle_reference.PARTICLE_SIZE; 
        int adjustedHeight = height + prticle_reference.PARTICLE_SIZE; 

        // Setup frame
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(adjustedWidth, adjustedHeight);
        frame.getContentPane().setBackground(Color.BLACK);

        // Add this JPanel to the frame
        frame.add(this);

        // Center the frame on screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - frame.getWidth()) / 2;
        int y = (screenSize.height - frame.getHeight()) / 2;
        frame.setLocation(x, y);

        frame.setVisible(true);
    }

    void store_particles(particles p) {
        particlesList.add(p); // Add particle to ArrayList
        repaint(); // Repaint each time a particle is added
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Fill the entire background with black
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        // Draw particles
        g.setColor(Color.WHITE);
        for (particles p : particlesList) {
            g.fillOval(p.getX(), p.getY(), 5, 5);
        }
    }

    public void move_particles() {
        for (particles p : particlesList) {
            if (p.no_collision()) {
                p.particle_movement();
            }
            else {
                p.bounce();
            }

        }
    }

    private void start_animation() {
        if (timer == null) { // Prevent creating multiple timers
            timer = new Timer(20, e -> {
                if (Spacebar_sate) {
                    move_particles();
                    repaint();
                }
            });
        }
        timer.start();
    }

    private void stop_animation() {
        if (timer != null) {
            timer.stop();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
    }

    private void spacebar_listen() {
        this.setFocusable(true);
        this.requestFocusInWindow(); // Ensure panel gets focus
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    toggle_spacebar_state();
                }
            }
        });
    }

    private boolean toggle_spacebar_state() {
        return Spacebar_sate = !Spacebar_sate;
    }

    public static void main(String[] args) {
        simulation a = new simulation();
        a.panel(800, 600);
        
        // Particles 
        // Define circle parameters
        int centerX = 375;  // Circle center X
        int centerY = 275;  // Circle center Y
        int radius = 125;   // Circle radius
        int numParticles = 150; // Number of particles around the circle

        // Distribute particles evenly around the circle
        for (int i = 0; i < numParticles; i++) {
            double angle = 2 * Math.PI * i / numParticles; // Compute angle
            int x = (int) (centerX + radius * Math.cos(angle));
            int y = (int) (centerY + radius * Math.sin(angle));
            a.store_particles(new particles(x, y));
        }

        // Start animation
        a.start_animation();
        a.spacebar_listen();
            
    }
}
