// Author: Tome Boye
// Course: OOP, DA3002
// Lab2: Brownian Motion

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BoxLayout;
import java.awt.event.*;
import javax.swing.BorderFactory;

/** An extension of the JPanel class representing the controller of the application. 
    It contains the buttons, sliders and other components for user interaction. It 
    implements the ActionListener interface for handling button and slider events.
    By implementing the SimulationListener interface the controller can also listen for 
    events such as when a particle becomes staionary.
*/
public class BrownianMotionController extends JPanel implements ActionListener, SimulationListener
{
    private JLabel timeStepLabel;
    private JLabel distanceLabel;
    private JLabel textFieldLabel1;
    private JLabel textFieldLabel2;
    private JButton startButton;
    private JButton stopButton;
    private JSlider timeStepSlider;
    private JSlider distanceSlider;
    private JTextField textFieldStat;
    private JTextField textFieldMove;
    private BrownianMotionView view;
    private BrownianMotionModel model;
    private final Integer particleCount;

    /** Creates and initializes all components (buttons, sliders, text fields ) and manages the layout.
        @param model Reference to an BrownianMotionModel object
        @param view Reference to an BrownianMotionView object
    */
    public BrownianMotionController(BrownianMotionModel model, BrownianMotionView view)
    {
        Box box = Box.createVerticalBox();
        setLayout(new FlowLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        this.model = model;
        this.view = view;
        this.particleCount = model.getParticleCount();
        this.stopButton = new JButton("Stop animation");
        this.startButton = new JButton("Start animation");
        this.timeStepSlider = new JSlider(JSlider.VERTICAL, 0, 500, 100);
        this.timeStepSlider.setName("timestep");
        this.timeStepSlider.setPreferredSize(new Dimension(50, 400));
        this.timeStepSlider.setMajorTickSpacing(100);
        this.timeStepSlider.setMinorTickSpacing(10);
        this.timeStepSlider.setPaintTicks(true);
        this.timeStepSlider.setPaintLabels(true);
        this.timeStepSlider.setSnapToTicks(true);
        this.distanceSlider = new JSlider(JSlider.VERTICAL, 0, 50, 3);
        this.distanceSlider.setName("distance");
        this.distanceSlider.setPreferredSize(new Dimension(50, 400));
        this.distanceSlider.setMajorTickSpacing(5);
        this.distanceSlider.setMinorTickSpacing(1);
        this.distanceSlider.setPaintTicks(true);
        this.distanceSlider.setPaintLabels(true);
        this.distanceSlider.setSnapToTicks(true);
        this.timeStepLabel = new JLabel("Time step (ms)");
        this.distanceLabel = new JLabel("Distance (pixels)");
        this.startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.stopButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.timeStepSlider.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.distanceSlider.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.timeStepLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.distanceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.timeStepSlider.addChangeListener(new SliderListener(this.model, this.view));
        this.distanceSlider.addChangeListener(new SliderListener(this.model, this.view));
        box.add(this.startButton);
        box.add(Box.createVerticalStrut(20));
        box.add(this.timeStepSlider);
        box.add(Box.createVerticalStrut(5));
        box.add(this.timeStepLabel);
        box.add(Box.createVerticalStrut(40));
        this.textFieldLabel1 = new JLabel("Stationary particles");
        this.textFieldStat = new JTextField("0");
        this.textFieldLabel1.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.textFieldStat.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.textFieldStat.setEditable(false);
        box.add(this.textFieldLabel1);
        box.add(this.textFieldStat);
        this.add(box);
        box = Box.createVerticalBox();
        box.add(this.stopButton);
        box.add(Box.createVerticalStrut(20));
        box.add(this.distanceSlider);
        box.add(Box.createVerticalStrut(5));
        box.add(this.distanceLabel);
        box.add(Box.createVerticalStrut(40));
        this.textFieldLabel2 = new JLabel("Moving particles");
        this.textFieldMove = new JTextField(this.particleCount.toString());
        this.textFieldLabel2.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.textFieldMove.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.textFieldMove.setEditable(false);
        box.add(this.textFieldLabel2);
        box.add(this.textFieldMove);
        this.add(box);
        this.startButton.addActionListener(this);
        this.stopButton.addActionListener(this);
        this.stopButton.setEnabled(false);

        // Add the controller to listen for particle events
        Particle.addSimulationEventListener(this);
    }

    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == this.startButton)
        {
            this.view.startAnimation();
            this.startButton.setEnabled(false);
            this.stopButton.setEnabled(true);
        }
        else if (e.getSource() == this.stopButton)
        {
            this.view.stopAnimation();
            this.startButton.setEnabled(true);
            this.stopButton.setEnabled(false);
        }
    }

    public void simulationEventHandler(SimulationEvent event)
    {
        Integer stationaryCount = this.model.getStationaryCounter();
        Integer movingCount = this.particleCount - stationaryCount;
        this.textFieldStat.setText(stationaryCount.toString());
        this.textFieldMove.setText(movingCount.toString());
        if (movingCount == 0)
        {
            // All particles have stopped moving. Stop the animation.
            // Disable stop button and enable start button. Pressing start
            // button will reset the simulation.
            this.view.stopAnimation();
            this.startButton.setEnabled(true);
            this.stopButton.setEnabled(false);
            this.model.reInitialize();
            this.textFieldStat.setText("0");
            String text = this.model.getParticleCount().toString();
            this.textFieldMove.setText(text);
        }
    }
}