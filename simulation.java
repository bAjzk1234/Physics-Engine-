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
            p.applyGravity(); // Apply gravity first
            p.bounce(); // Then check for bounces
            p.particles_collision(particlesList); // Check for collisions off other particles
        }
        repaint();
    }
    
    

    private void start_animation() {
        timer = new Timer(20, e -> {
            if (Spacebar_sate) {
            
                move_particles();
                repaint();
            }
        });
    
        timer.start();
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
                    restart_simulation(); // Restart when spacebar is pressed
                }
            }
        });
    }
    
    private void restart_simulation() {
        if (timer != null) {
            timer.stop(); // Stop current animation
        }
    
        particlesList.clear(); // clear particles
    
        // Reinitialize particles 
        int centerX = 375;
        int centerY = 275;
        int radius = 125;
        int numParticles = 1000;
    
        for (int i = 0; i < numParticles; i++) {
            double angle = 2 * Math.PI * i / numParticles;
            int x = (int) (centerX + radius * Math.cos(angle));
            int y = (int) (centerY + radius * Math.sin(angle));
            store_particles(new particles(x, y));
        }
    
        repaint(); // Refresh display
        start_animation(); // Restart animation
    }
    public static void main(String[] args) {
        simulation a = new simulation();
        a.panel(800, 600);
        
        // Particles 
        // Define circle parameters
        int centerX = 375;  // Circle center X
        int centerY = 275;  // Circle center Y
        int radius = 125;   // Circle radius
        int numParticles = 200; // Number of particles around the circle

        // Distribute particles evenly around the circle
        for (int i = 0; i < numParticles; i++) {
            double angle = 2 * Math.PI * i / numParticles; // Compute angle
            int x = (int) (Math.random() * 800); // Compute X position
            int y = (int) (Math.random() * 600); // Compute Y position
            a.store_particles(new particles(x, y));
        }

        // Start animation
        a.start_animation();
        a.spacebar_listen();
            
    }
}
