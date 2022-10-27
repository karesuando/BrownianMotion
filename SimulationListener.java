import java.util.EventListener;
import java.util.EventObject;

public interface SimulationListener extends EventListener
{
    void simulationEventHandler(SimulationEvent event);
}