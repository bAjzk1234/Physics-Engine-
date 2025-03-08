import java.awt.Frame;

public class solid {

    private int solidX, solidY;
    private double speedX, speedY;
    private double gravity;
    private int width, height;
    private int weight;
    private double bounce_factor;


    public solid(int x, int y, int width, int height, int weight) {
        this.solidX = x;
        this.solidY = y;
        this.speedX = 0;
        this.speedY = 0;
        this.width = width;
        this.height = height;
        this.weight = weight;

        this.gravity = determine_gravity();
        this.bounce_factor = determine_bounceFactor();
    }

    private double determine_gravity() {
        return 0.01 * weight; // More weight → stronger gravity
    }

    private double determine_bounceFactor() {
        return Math.max(0.3, 1.5 / weight); // More weight → less bounce
    }

    public void apply_gravity() {
        speedY += gravity;
        solidY += (int) speedY;
    
        // Ensure it doesn't go below ground
        if (solidY + height > Constants.FRAME_HEIGHT) {
            solidY = Constants.FRAME_HEIGHT - height;
            speedY = -speedY * bounce_factor; // Apply bounce
        }
    }
    
    public void bounce() {
        // Wall Collision
        if (solidX < 0 || solidX + width > Constants.FRAME_WIDTH) {
            speedX = -speedX * bounce_factor;
        }
        // Ground Collision
        if (solidY + height > Constants.FRAME_HEIGHT) {
            speedY = -speedY * bounce_factor;
            solidY = Constants.FRAME_HEIGHT - height;
        }

        solidX += (int) speedX;
        solidY += (int) speedY;
    }

    // Check for collision with particles
    public boolean check_collision(particles p) {
        return (p.getX() + p.PARTICLE_SIZE > solidX && p.getX() < solidX + width && p.getY() + p.PARTICLE_SIZE > solidY && p.getY() < solidY + height);
    }

    public void reactTo_particle_collision(particles p) {
        if (check_collision(p) && weight < p.getWeight()) {
            p.speedY = -p.speedY * bounce_factor; // Reverse particle's Y speed
            this.speedY -= gravity / 2; // Move solid upwards slightly
    
            // Move solid in the direction of the particle
            this.speedX += p.speedX * 0.3;
        }
    }

    public boolean check_ground_state() {
        return solidY  != Frame.HEIGHT;
    }

    public boolean solid_hitbox() {
        return solidX >= 0 && solidX + width <= Constants.FRAME_WIDTH && solidY >= 0 && solidY + height <= Constants.FRAME_HEIGHT;
    }

    public int getSolidX() {
        return solidX;
    }

    public int getSolidY() {
        return solidY;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
       return height;
    }

    public double getSpeedY() {
        return speedY;
    }

    public int get_weight() {
        return weight;
    }

}
