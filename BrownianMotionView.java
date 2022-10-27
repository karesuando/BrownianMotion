// Author: Tome Boye
// Course: OOP, DA3002
// Lab2: Brownian Motion

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.Timer;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/** An extension of the JPanel class representing the view of the application. The
    view class is responsible for moving the particles and drawing them on the screen
    at regular intervals using a timer. The class implements the ActionListener 
    interface for handling timer events.
 */
public class BrownianMotionView extends JPanel implements ActionListener
{
    private final int INITIAL_TIME_STEP = 100;
    private final Timer timer;
    private final BrownianMotionModel model;

    public BrownianMotionView(BrownianMotionModel model)
    {
        this.model = model;
        this.timer = new Timer(INITIAL_TIME_STEP, this);
        setBackground(Color.black);
        setPreferredSize(this.model.getDimension());
        setBorder(BorderFactory.createLineBorder(Color.WHITE));
    }

    public Dimension getPreferredSize() 
    {
        return this.model.getDimension();
    }

    public void actionPerformed(ActionEvent e)
    {
        this.model.moveParticles();
        repaint();
    }

    /** Changes the timers between-event delay */
    public void setTimeStep(int ms)
    {
        this.timer.setDelay(ms);
    }

    /** Starts the simulation by starting the timer. The timer generates
        ActionEvents handled by the actionPerformed() method which is responsible
        for moving the particles and drawing them on the screen.
    */
    public void startAnimation()
    {
        this.timer.start();
    }

    /** Stops the simulation by stopping rhe timer */
    public void stopAnimation()
    {
        this.timer.stop();
    }

    public void paintComponent(Graphics gc)
	{
        super.paintComponent(gc);
        for (Particle particle : model.getParticles())
        {
            particle.paint(gc);
        }
    }
}