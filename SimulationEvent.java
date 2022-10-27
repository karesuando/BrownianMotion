import java.util.EventObject;

public class SimulationEvent extends EventObject 
{
    public SimulationEvent(Object source, Integer value) 
    {
        super(source);
        this.value = value;
    }

    public Integer getValue()
    {
        return this.value;
    }

    private final Integer value;
}