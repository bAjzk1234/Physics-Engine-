class particles {

    private int particleX, particleY; // Position of particle
    private int speedX = 8, speedY = 8; // Speed of particle in X and Y direction
    public final int PARTICLE_SIZE = 5; // Particle size (width/height)

    protected final int FRAME_WIDTH = 800 - 8;  // 800 - 8px for side borders
    protected final int FRAME_HEIGHT = 600 - 30; // 600 - 30px (title bar) - 8px (bottom border)

    public particles(int x, int y) {
        this.particleX = x;
        this.particleY = y;
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

    public void bounce() {

        int nextX = particleX + speedX;
        int nextY = particleY + speedY;
    
        // Check if the next position would go out of bounds, then reverse direction BEFORE moving
        if (nextX < 0 || nextX + PARTICLE_SIZE > FRAME_WIDTH) {
            speedX = -speedX;
        }
        if (nextY < 0 || nextY + PARTICLE_SIZE > FRAME_HEIGHT) {
            speedY = -speedY;
        }
    
        particleX += speedX;
        particleY += speedY;
    }
    

    public int getX() {
        return particleX;
    }

    public int getY() {
        return particleY;
    }
}