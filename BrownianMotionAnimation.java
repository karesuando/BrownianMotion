// Author: Tome Boye
// Course: OOP, DA3002
// Lab2: Brownian Motion

import javax.swing.SwingUtilities;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import java.awt.BorderLayout;

/**  The application class of the Browninan motion simulation application. It
     creates the model, view and controller objects and runs the application in
     a seperate thread.
*/
class BrownianMotionAnimation extends JFrame
{
    private final int WIDTH = 800;
    private final int HEIGHT = 800;
    private final int PARTICLE_COUNT = 250;

    public BrownianMotionAnimation()
    {
        super("Brownian Motion Simulator");
        BrownianMotionView view;
        BrownianMotionModel model;
        BrownianMotionController controller;

        model      = new BrownianMotionModel(WIDTH, HEIGHT, 3000);
        view       = new BrownianMotionView(model);
        controller = new BrownianMotionController(model, view);
        add(controller, BorderLayout.LINE_START);
        add(view, BorderLayout.CENTER);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
        pack();
    }

    /** Entry point of the application. */
    public static void main (String[] arg)
    {
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
            createAndRunAnimation();
        }
    });
    }

    private static void createAndRunAnimation()
    {
      BrownianMotionAnimation animation;

      animation = new BrownianMotionAnimation();
    }
}