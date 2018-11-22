import java.util.List;

/**
 * A simple model of a poisonBerry. It can't move and decrease the food value of every species
 * eating it.
 */
public class PoisonBerry extends Plant
{
    // water level poisonBerries need to get to in order to reproduce.
    private final static int MAX_WATER_LEVEL = 6;
    // Food value got by whatever eats a poisonBerry.
    // (in fact, it decreases the food value).
    private final static int FOOD_VALUE = -3;


    /**
     * Create a new poisonBerry.
     *
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public PoisonBerry(Field field, Location location)
    {
        super(field, location);
    }

    /**
     * Generate and return a new poisonBerry in the field and location specified.
     * @param field The field where it has to appear.
     * @param location The location where it has to appear.
     * @return A new instance of PoisonBerry.
     */
    protected PoisonBerry giveBirthUnique(Field field, Location loc)
    {
        return new PoisonBerry(field, loc);
    }

    /**
     * Plants don't move, but they react to weather. They don't even
     * die but when something eats them.
     * @param newPoisonBerries A list to receive newly born poisonBerries.
     * @param time Day time.
     * @param weather The current weather.
     * @Override
     */
    public void act(List<Species> newPoisonBerries, int time, Weather weather)
    {
        // according to the weather poisonBerry react differently.
        switch(weather){
            case RAINY:
                setWaterLevel(getWaterLevel() + 2);
                break;
            default:
                if(getWaterLevel()>0){
                    setWaterLevel(getWaterLevel() - 1);
                }
        }

        // When the max water level is reached it reproduces.
        if(isActive() && getWaterLevel() >= MAX_WATER_LEVEL) {
            setWaterLevel(getWaterLevel() % MAX_WATER_LEVEL);
            giveBirth(newPoisonBerries);

            // poisonBerries cannot die by overcrowding
        }
    }

    public int getFoodValue(){
        return FOOD_VALUE;
    }
}
