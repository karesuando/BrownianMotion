// Author: Tome Boye
// Course: OOP, DA3002
// Lab2: Brownian Motion

import java.util.Calendar;
import java.util.Random;
import java.awt.Dimension;

/** This class represents the model of the application containing the data 
    used in the simulation. It is used by the view and controller classes.*/
class BrownianMotionModel
 {
    private int stationaryParticles;
    private final int width;
    private final int height;
    private final int particleCount;
    private final Particle[] particles;

    /** Creates all particles each located at a random location inside a
        circular region around the center of the view.
     */
    public BrownianMotionModel(final int width, final int height, final int particleCount)
    {
        this.width = width;
        this.height = height;
        this.particleCount = particleCount;
        this.stationaryParticles = 0;
        this.particles = new Particle[particleCount];
        for (int i = 0; i < particleCount; i++)
        {
            this.particles[i] = new Particle();
        }
    }

    /** Resets the animation  */
    public void reInitialize()
    {
        for (int i = 0; i < particleCount; i++)
        {
            this.particles[i] = new Particle();
        }
        this.stationaryParticles = 0;
    }

    public int getStationaryCounter()
    {
        return ++this.stationaryParticles;
    }

    /** Returns the initial number of particles in the simulation */
    public Integer getParticleCount()
    {
        return this.particleCount;
    }

    /** Returns the collection of particles */
    public Particle[] getParticles()
    {
        return this.particles;
    }
    
    /** Moves each particle a fixed distance in a random direction */
    void moveParticles()
    {
        for (Particle particle : this.particles)
        {
            particle.randomMove();
        }
    }

    /** Returns the dimensions of the view */
    Dimension getDimension() {
        return new Dimension(this.width, this.height);
    }
    
    /** Changes the distance particles move at each time step. 
     @param value Distance the particle moves each time step.
    */
    void setDistanceTomove(double value)
    {
        Particle.setDistanceTomove(value);
    }
}