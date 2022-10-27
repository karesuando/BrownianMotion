// Author: Tome Boye
// Course: OOP, DA3002
// Lab2: Brownian Motion

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/** SliderListener handles events that are generated when a user moves a slider 
    to change the time step or the distance particles move in the simulation.  */
public class SliderListener implements ChangeListener
{
    private BrownianMotionModel model;
    private BrownianMotionView view;

    public SliderListener(BrownianMotionModel model, BrownianMotionView view)
    {
        this.model = model;
        this.view = view;
    }

    public void stateChanged(ChangeEvent event) 
    {
        JSlider source = (JSlider)event.getSource();
        if (!source.getValueIsAdjusting()) 
        {
            int sliderValue = (int)source.getValue();

            switch (source.getName())
            {
            case "timestep":
                this.view.setTimeStep(sliderValue);
                break;
            
            case "distance":
                this.model.setDistanceTomove(sliderValue);
                break;

            default:
                break;
            }
        }    
    }
}