import java.awt.Color;
import java.util.ArrayList;

class particles {

    private int particleX, particleY; // Position of particle
    public double speedX = 0, speedY = 0; // Initial velocity
    private final double gravity = 0.3; // Gravity acceleration
    private final double bounce_factor = 0.8; // Energy loss on bounce
    public final int PARTICLE_SIZE = 4; // Particle size (width/height)
    public Color particle_color; // Color of particle

    protected final int FRAME_WIDTH = 800 - 8;  // 800 - 8px for side borders
    protected final int FRAME_HEIGHT = 600 - 30; // 600 - 30px (title bar) - 8px (bottom border)

    public particles(int x, int y) {
        this.particleX = x;
        this.particleY = y;
        this.particle_color = new Color((int)(Math.random() * 255), (int)(Math.random() * 255), (int)(Math.random() * 255));
    }

    public void particle_movement() {
        // Move particle
        particleX += speedX;
        particleY += speedY;
    }

    public boolean no_collision() {
        // Check if particle is within bounds
        return particleX >= 0 && particleX + PARTICLE_SIZE<= FRAME_WIDTH && particleY >= 0 && particleY + PARTICLE_SIZE <= FRAME_HEIGHT;
    }

    public void applyGravity() {
        speedY += gravity; // Apply gravity acceleration
        particleY += (int) speedY; // Cast accerlation to position
    }

    public void particles_collision(ArrayList<particles> particlesList) {

        // Bounce particle off each other 
        for (particles p : particlesList) {

            if (p != this) {    // No collision with itself

                // Calculate distance between particles
                int dx = p.getX() - particleX;
                int dy = p.getY() - particleY;
                double distance = Math.sqrt(dx * dx + dy * dy);

                // If distance is less than particle size, bounce off
                if (distance < PARTICLE_SIZE) {

                    // Calculate angle between particles by using arctan
                    double angle = Math.atan2(dy, dx);

                    // Calculate target position for both particles
                    double targetX = particleX + Math.cos(angle) * PARTICLE_SIZE;
                    double targetY = particleY + Math.sin(angle) * PARTICLE_SIZE;

                    // Calculate acceleration for both particles
                    double ax = (targetX - p.getX()) * 0.01;
                    double ay = (targetY - p.getY()) * 0.01;

                    speedX -= ax;
                    speedY -= ay;
                    p.speedX += ax;
                    p.speedY += ay;
                }
            }
        }
    }

    public void bounce() {
        int nextX = particleX + (int) speedX;
        int nextY = particleY + (int) speedY;
    
        // Reverse direction if out of bounds
        if (nextX < 0 || nextX + PARTICLE_SIZE > FRAME_WIDTH) {
            speedX = -speedX;
        }
        if (nextY < 0 || nextY + PARTICLE_SIZE > FRAME_HEIGHT) {
            speedY = -speedY * bounce_factor; // Reduce speed when bouncing off ground
            particleY = FRAME_HEIGHT - PARTICLE_SIZE; // Ensure it stays within bounds
        }
    
        particleX += (int) speedX;
        particleY += (int) speedY;
    }
    
    public void delete_all_particles(ArrayList<particles> particlesList) {
        particlesList.clear();
    }

    public void delete_particle(ArrayList<particles> particlesList, particles p) {
        particlesList.remove(p);
    }

    public void delete_stopped_particles(ArrayList<particles> particlesList) {
        ArrayList<particles> updatedList = new ArrayList<>();

        for (particles p : particlesList) {
            if (!( (Math.abs(p.speedY) >= 0) && (p.particleY + PARTICLE_SIZE == FRAME_HEIGHT) )) {
                updatedList.add(p);  // Keep the particles that are not stopped
            } 
        }
        particlesList.clear();  // Clear the original list
        particlesList.addAll(updatedList);  // Add remaining particles back to the original list
    }
    

    public int getX() {
        return particleX;
    }

    public int getY() {
        return particleY;
    }
    
    public Color get_particle_color() {
        return particle_color;
    }
}