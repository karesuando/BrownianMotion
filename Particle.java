// Author: Tome Boye
// Course: OOP, DA3002
// Lab2: Brownian Motion

import java.awt.Color;
import java.awt.Graphics;
import java.util.Calendar;
import java.util.Random;
import java.util.EventListener;
import java.util.EventObject;
import javax.swing.event.EventListenerList;

/** Represents a particle in the Brownian motion simulation application. Particle
 * objects generate a SimulationEvent message every time the particle stops moving to 
 * all listeners.
*/
public class Particle
{
    private Double xPos;
    private Double yPos;
    private Boolean isMoving;
    private final int PARTICLE_WIDTH = 2;  // pixels
    private final int PARTICLE_HEIGHT = 2; // pixels
    private static final int SCREEN_WIDTH = 800;  // pixels
    private static final int SCREEN_HEIGHT = 800; // pixels
    private static Random random;
    private static Double distanceToMove;  // pixels
    private static final Boolean availablePixelPos[][];
    private static final EventListenerList listenerList;

    /** Initializes all class variables */
    static {
        Calendar calendar = Calendar.getInstance();
        random = new Random(calendar.getTimeInMillis());
        distanceToMove  = 3.0;
        listenerList = new EventListenerList();

        // availablePixelPos is a boolean matrix where an element indicates if a pixel
        // is part of a region near an edge or a motionless particle.
        availablePixelPos = new Boolean[SCREEN_WIDTH][SCREEN_HEIGHT];

        // Initialize all elements (except border elements) of the matrix to true.
        for (int i = 1; i < SCREEN_WIDTH - 1; i++)
        {
            for (int j = 1; j < SCREEN_HEIGHT - 1; j++)
            {
                availablePixelPos[i][j] = true;
            }
        }

        // Initialize border elements of the matrix to false to indicate that a 
        // particle that has moved into this region has collided with an edge. 
        // When a particle hits and edge or another motionless particle it will 
        // stop moving and remain motionless for the rest of the simulation.
        for (int i = 0; i < SCREEN_HEIGHT; i++)
        {
            availablePixelPos[0][i] = false;
            availablePixelPos[SCREEN_WIDTH - 1][i] = false;
        }
        for (int i = 0; i < SCREEN_WIDTH; i++)
        {
            availablePixelPos[i][0] = false;
            availablePixelPos[i][SCREEN_HEIGHT - 1] = false;
        }
    }

    /** Initializes a Particle object and places it at random x,y coordinates. */
    public Particle()
    {
        Double radius     = 150 * random.nextDouble();
        Double phi        = random.nextDouble() * 2*Math.PI;
        this.xPos         = SCREEN_WIDTH/2 + radius * Math.cos(phi);
        this.yPos         = SCREEN_HEIGHT/2 + radius * Math.sin(phi);
        this.isMoving     = true;
    }

    /** Initializes a Particle object and places it at the specified x,y coordinates 
      @param xPos
      @param yPos
    */
    public Particle(Double xPos, Double yPos)
    {
        this.xPos         = xPos;
        this.yPos         = yPos;
        this.isMoving     = true;
    }

    /** Sets the distance the particle moves at each time step.
     * @param distance Distance the particle moves at each time step (in pixels)
     */
    public static void setDistanceTomove(Double distance)
    {
        distanceToMove = distance;
    }

    /** Adds a SimulationListener to the particle 
     @param listener An object that wants to listen simulation events.
    */
    public static void addSimulationEventListener(SimulationListener listener) 
    {
        listenerList.add(SimulationListener.class, listener);
    }

    /**
    Moves the particle a fixed distance in a random direction. If the particle
    collides with an edge or a motionless particle it will stop moving and remain
    motionless for the rest of the simulation. 
    */
    public void randomMove()
    {
        if (this.isMoving)
        {
            // Calculate the new x and y coordinates of the particle,
            // L*cos(phi) and L*sin(phi), where phi is a random angle 
            // 0 < phi < 2pi and L is the distance the particle moves 
            // at each time step.

            Double phi    = random.nextDouble() * 2*Math.PI; 
            Double dx     = distanceToMove * Math.cos(phi);
            Double dy     = distanceToMove * Math.sin(phi);
            this.xPos     = Math.max(0, Math.min(this.xPos + dx, SCREEN_WIDTH - PARTICLE_WIDTH));
            this.yPos     = Math.max(0, Math.min(this.yPos + dy, SCREEN_HEIGHT - PARTICLE_HEIGHT));
            // Check if the particle has collided with an edge or a motionless particle
            int x         = (int)Math.round(this.xPos);
            int y         = (int)Math.round(this.yPos);
            this.isMoving = availablePixelPos[x][y] 
                         && availablePixelPos[x + 1][y] 
                         && availablePixelPos[x][y + 1] 
                         && availablePixelPos[x + 1][y + 1];
            if (! this.isMoving)
            {
                // Particle has collided with an edge or a motionless particle. Mark a 1-pixel 
                // region around the particle and the interior as unavailable.
                availablePixelPos[x][y] = false;
                availablePixelPos[x + 1][y]  = false;
                availablePixelPos[x][y + 1] = false;
                availablePixelPos[x + 1][y + 1] = false;
                int x1 = Math.max(x - 1, 1);
                int y1 = Math.max(y - 1, 1);
                int x2 = Math.min(x + 2, SCREEN_WIDTH - 2);
                int y2 = Math.min(y + 2, SCREEN_HEIGHT - 2);
                for (int xi = x1; xi <= x2; xi++)
                {
                    availablePixelPos[xi][y1] = false;
                    availablePixelPos[xi][y2] = false;
                }
                for (int yi = y1; yi <= y2; yi++)
                {
                    availablePixelPos[x1][yi] = false;
                    availablePixelPos[x2][yi] = false;
                }
                this.fireParticleStoppedEvent(new SimulationEvent(this, 1));
            }
        }
    }

    /** Draws the particle at its current location in the view. Moving 
     * particles are drawn in blue while motionless ones are drawn in red.
     * @param gc Graphics context object
    */
    public void paint(Graphics gc)
    {
        int x = (int)Math.round(this.xPos);
        int y = (int)Math.round(this.yPos);
  
        gc.setColor(this.isMoving? Color.BLUE : Color.RED);
        gc.fillRect(x, y, PARTICLE_WIDTH, PARTICLE_HEIGHT);
    }

    /** Notifies all listeners by calling the simulationEventHandler method.  
     * @param event An SimulationEvent obect 
    */
    private void fireParticleStoppedEvent(SimulationEvent event) 
    {
        Object[] listeners = this.listenerList.getListenerList();

        // loop through each listener and pass on the event if needed
        int numberOfListeners = listeners.length;
        for (int i = 0; i < numberOfListeners; i+=2) 
        {
            if (listeners[i] == SimulationListener.class) 
            {
                // pass the event to the listeners event dispatch method
                ((SimulationListener)listeners[i+1]).simulationEventHandler(event);
            }            
        }
    }
}
