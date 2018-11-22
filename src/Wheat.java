import java.util.List;
import java.util.Iterator;

/**
 * A simple model of wheat.
 * 
 * @author Alessandro Amantini and Ido Benzvi
 * @version 2018.02.21
 */
public class Wheat extends Plant
{
    // Food value got by whatever eats wheat.
    private static final int FOOD_VALUE = 5;

    // water level wheat needs to get to in order to reproduce.
    private static final int MAX_WATER_LEVEL = 2;

    /**
     * Create a new wheat.
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Wheat(Field field, Location location)
    {
        super(field, location);
    } 

    /**
     * Generate and return a new wheat in the field and location specified.
     * @param field The field where it has to appear.
     * @param location The location where it has to appear.
     * @return A new instance of Wheat.
     */
    protected Wheat giveBirthUnique(Field field, Location loc)
    {
        return new Wheat(field, loc);
    }
    
    @Override
    public int getFoodValue(){
        return FOOD_VALUE; 
    }

    /**
     * Plants don't move, but they react to weather. They don't even
     * die but when something eats them.
     * @param newWheat A list to receive newly born wheat.
     * @param time Day time.
     * @param weather The current weather.
     * @Override
     */
    public void act(List<Species> newWheat, int time, Weather weather)
    {
        // according to the weather. wheat reacts differently
        switch(weather){
            case RAINY:
                setWaterLevel(getWaterLevel() + 1);
                break;
            default:
                break;
        }

        
        // When the max water level is reached it reproduces. In our simulation,
        // that level is reached since they born, so they always reproduce,
        // in order to balance the species.
        if(isActive() && getWaterLevel() >= MAX_WATER_LEVEL) {
            setWaterLevel(getWaterLevel() % MAX_WATER_LEVEL);
            giveBirth(newWheat); 

            // wheat can die by overcrowding
            Location newLocation = getField().freeAdjacentLocation(getLocation());
            if(newLocation == null){
                setDead();
            }
        }
    }
}
