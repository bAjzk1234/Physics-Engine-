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
    private int preset_counter = 0;
    private boolean simulation_state = false;

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

        // Check simulation state
        if (!simulation_state) start_screen(g); // Show the start screen
        else {
         
            UI_description(g);

            // Draw each particle with its color
            for (particles p : particlesList) {
                g.setColor(p.get_particle_color());
                g.fillOval(p.getX(), p.getY(), p.PARTICLE_SIZE, p.PARTICLE_SIZE); 
            }
        }
}

    public void move_particles() {
        for (particles p : particlesList) {
            p.applyGravity(); // Apply gravity first
            p.bounce(); // Then check for bounces
            p.particles_collision(particlesList); // Check for collisions off other particles
        }
        prticle_reference.delete_stopped_particles(particlesList); // Delete particles that have stopped moving
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
                    if (!simulation_state) {
                        simulation_state = true;  // Set the flag to start the game
                        restart_simulation(preset_counter); // Start the simulation with the selected preset
                        repaint();  // Repaint to update the screen
                    } else {
                        preset_counter = (preset_counter + 1) % 4;  // Toggle presets
                        restart_simulation(preset_counter);
                    }
                    repaint();
                }
            }
        });
    }
    
    private void restart_simulation(int preset) {
        if (timer != null) {
            timer.stop(); // Stop current animation
        }
    
        particlesList.clear();
    
        preset_selection(preset); // Select preset
    
        repaint(); // Refresh display
        start_animation(); // Restart animation
    }

    private void preset_selection(int preset) {
        switch (preset) {
            case 0:
                init_circle_particles();
            break;

            case 1:
                init_wave_particles();
            break;

            case 2:
                init_random_particles();
            break;

            case 3:
                init_square();
            break;
        }
    }

    private void UI_description(Graphics g) {
        g.setColor(Color.WHITE);

        // set font
        g.setFont(new Font("Arial", Font.ITALIC, 22));
        g.drawString("Press SPACEBAR to restart", 10, 20);

        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.setColor(Color.green);
        g.drawString("Particles: " + particlesList.size(), 650, 20);
    }

    private void start_screen(Graphics g) {
        // Display start screen
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.ITALIC, 32));
        g.drawString("Press SPACEBAR to start", 200, 300);
    }

    private void init_circle_particles() {
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
    }

    private void init_wave_particles() {
        int numParticles = 1000;

        for (int i = 0; i < numParticles; i++) {
            int x = i;
            int y = 275 + (int) (50 * Math.sin(i * 0.1));
            store_particles(new particles(x, y));
        }
    }

    private void init_random_particles() {
        int numParticles = 1000;

        for (int i = 0; i < numParticles; i++) {
            int x = (int) (Math.random() * 800);
            int y = (int) (Math.random() * 600);
            store_particles(new particles(x, y));
        }
    }

    private void init_square() {
        int numParticles = 1000;
        int squareSize = 300;  // Size of the square (width and height)
        int startX = 225;  // X-coordinate of the top-left corner of the square
        int startY = 150;  // Y-coordinate of the top-left corner of the square
    
        for (int i = 0; i < numParticles; i++) {
            // Generate random X and Y coordinates within the square
            int x = startX + (int) (Math.random() * squareSize);  // X between startX and startX + squareSize
            int y = startY + (int) (Math.random() * squareSize);  // Y between startY and startY + squareSize
            
            // Create a new particle and add it to the list
            store_particles(new particles(x, y));
        }
    }
    
    public static void main(String[] args) {
        simulation a = new simulation();
        a.panel(800, 600);

        a.spacebar_listen();
            
    }
}
