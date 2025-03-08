import java.awt.Color;
import java.util.ArrayList;

class particles {

    private int particleX, particleY; // Position of particle
    public double speedX = 0, speedY = 0; // Initial velocity
    private double gravity; // Gravity acceleration
    private double bounce_factor; // Energy loss on bounce
    public int PARTICLE_SIZE; // Particle size (width/height)
    public Color particle_color; // Color of particle
    public int particle_weight;; // Weight of particle
    private double airResistance;


    public particles(int x, int y) {
        this.particleX = x;
        this.particleY = y;
        this.particle_weight = (int) (Math.random() * 20) + 5;
        this.gravity = determine_gravity();
        this.bounce_factor = determine_bounceFactor();
        this.PARTICLE_SIZE = (int) (0.5 * particle_weight);
        this.airResistance = 0.995 - (PARTICLE_SIZE / 1000);
        this.particle_color = new Color((int)(Math.random() * 255), (int)(Math.random() * 255), (int)(Math.random() * 255));
    }

    public particles(int x, int y, int size) {
        this.particleX = x;
        this.particleY = y;
        this.particle_weight = (int) (Math.random() * 20) + 5;
        this.gravity = determine_gravity();
        this.bounce_factor = determine_bounceFactor();
        this.PARTICLE_SIZE = (int) (size * 0.1 * particle_weight);
        this.airResistance = 0.995 - (PARTICLE_SIZE / 1000);
        this.particle_color = new Color((int)(Math.random() * 255), (int)(Math.random() * 255), (int)(Math.random() * 255));
    }

    public void particle_movement() {
        // Move particle
        particleX += speedX;
        particleY += speedY;
    }

    
    private double determine_gravity() {
        return 0.05 * particle_weight; // More weight means stronger gravity
    }

    private double determine_bounceFactor() {
        return Math.max(0.5, 1.5/ particle_weight); // More weight means less bounce
    }

    public void apply_gravity() {
        speedY += gravity;
        speedY *= airResistance; // Apply air resistance
        particleY += (int) speedY;

        // Soft ground effect
        if (particleY + PARTICLE_SIZE >= Constants.FRAME_HEIGHT) {
            speedY = -speedY * bounce_factor;
            speedX *= 0.9; // Ground friction
            if (Math.abs(speedY) < 1) {
                particleY += 1; // Simulate slight sinking effect
            }
        }
    }

    public boolean no_collision() {
        // Check if particle is within bounds
        return particleX >= 0 && particleX + PARTICLE_SIZE<= Constants.FRAME_WIDTH && particleY >= 0 && particleY + PARTICLE_SIZE <= Constants.FRAME_HEIGHT;
    }

    public void particles_collision(ArrayList<particles> particlesList) {
        for (particles p : particlesList) {
            if (p != this) {
                int dx = p.getX() - particleX;
                int dy = p.getY() - particleY;
                double distance = Math.sqrt(dx * dx + dy * dy);
    
                if (distance < PARTICLE_SIZE) {
                    // Calculate angle between particles
                    double angle = Math.atan2(dy, dx);
    
                    // Compute velocity components along normal and tangent
                    double v1n = speedX * Math.cos(angle) + speedY * Math.sin(angle);
                    double v2n = p.speedX * Math.cos(angle) + p.speedY * Math.sin(angle);
    
                    // Swap velocities (elastic collision)
                    double temp = v1n;
                    v1n = v2n;
                    v2n = temp;
    
                    // Convert back to X and Y
                    speedX = v1n * Math.cos(angle);
                    speedY = v1n * Math.sin(angle);
                    p.speedX = v2n * Math.cos(angle);
                    p.speedY = v2n * Math.sin(angle);
                }
            }
        }
    }    
    
    public void bounce() {
        int nextX = particleX + (int) speedX;
        int nextY = particleY + (int) speedY;

        if (nextX < 0 || nextX + PARTICLE_SIZE > Constants.FRAME_WIDTH) {
            speedX = -speedX * bounce_factor;
        }

        if (nextY + PARTICLE_SIZE >= Constants.FRAME_HEIGHT) {
            particleY = Constants.FRAME_HEIGHT - PARTICLE_SIZE;
            double bounceAngle = Math.toRadians(30 + Math.random() * 30);
            double speed = Math.sqrt(speedX * speedX + speedY * speedY) * bounce_factor;
            if (Math.random() > 0.5) {
                speedX = speed * Math.cos(bounceAngle);
            } else {
                speedX = -speed * Math.cos(bounceAngle);
            }
            speedY = -speed * Math.sin(bounceAngle);
            speedX *= 0.9;
        }

        if (nextY < 0) {
            speedY = -speedY * bounce_factor;
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
            boolean is_stopped = Math.abs(p.speedY) < 0.2 && p.particleY + p.PARTICLE_SIZE >= Constants.FRAME_HEIGHT - 1;
            boolean out_of_bounds = p.getX() < -p.PARTICLE_SIZE || p.getX() > Constants.FRAME_WIDTH + p.PARTICLE_SIZE || p.getY() < -p.PARTICLE_SIZE || p.getY() > Constants.FRAME_HEIGHT + p.PARTICLE_SIZE;
    
            if (!is_stopped && !out_of_bounds) {
                updatedList.add(p); // Keep particles that are still in motion and within bounds
            }
        }
        
        particlesList.clear();  // Clear original list
        particlesList.addAll(updatedList);  // Add only remaining particles
    }

    public void force_gravity(solid solidObject) {

        if (solidObject.check_particle_under_collision(this)) {
            if(particleY + PARTICLE_SIZE >= Constants.FRAME_HEIGHT) {
                particleY = Constants.FRAME_HEIGHT - PARTICLE_SIZE;
                speedY = 0;
            }
            else{
                particleY = solidObject.getSolidY() + PARTICLE_SIZE;
                speedY += 2*solidObject.getSpeedY();
            }
        }
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

    public int getWeight() {
        return particle_weight;
    }
}