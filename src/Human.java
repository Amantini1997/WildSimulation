import java.util.List;
import java.util.Random;
import java.util.Iterator;

/**
 * A simple model of a human.
 * Human age, move, breed, and die.
 */
public class Human extends Creature
{
    // Characteristics shared by all humans.

    // The age at which a human can start to breed.
    private static final int BREEDING_AGE = 15;
    // The age to which a human can live.
    private static final int MAX_AGE = 60;
    // The likelihood of a human breeding.
    private static final double BREEDING_PROBABILITY = 0.5;
    // Food value got by whatever eats a human.
    private static final int FOOD_VALUE = 30;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 4;
    // The maximum amount of food a human is able to store.
    // In fact, it's the number of hours he can survive without eating before dying.
    private static final int MAX_FOOD_LEVEL = 7;

    // A shared Random object, if required.
    private Random rand = new Random();

    /**
     * Create a new human. Human may be created with age
     * zero (a new born) or with a random age.
     *
     * @param randomAge If true, the human will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Human(boolean randomAge, Field field, Location location)
    {
        super(field, location,(randomAge)?(int)(Math.random()*MAX_AGE)+1:0);
        if(randomAge) {
            setFoodLevel(rand.nextInt(MAX_FOOD_LEVEL));
        }
        else {
            setFoodLevel(MAX_FOOD_LEVEL);
        }
    }

    /**
     * Check if a species is a human.
     * @param species The species to check.
     * @return 1 if it's a human, 0 otherwise
     * @Override
     */
    public int compareTo(Species species){
        return (species instanceof Human)? 1 : 0;
    }

    /**
     * This is what the human does most of the time - it runs
     * around and hunt. Sometimes it will breed or die of old age.
     * @param newHumans A list to receive newly born humans.
     * @param time Day time.
     * @param weather The current weather.
     * @Override
     */
    public void act(List<Species> newHumans, int time, Weather weather)
    {
        incrementAge();
        incrementHunger();
        super.act(newHumans, time, weather);
    }

    /**
     * Make this human more hungry. This could result in the human's death.
     */
    protected void incrementHunger()
    {
        super.incrementHunger();
    }

    /**
     * If the food value exceeds the max food level, it is set to max food level.
     * @Override
     */
    protected void foodLevelCheck(){
        if(getFoodLevel() > MAX_FOOD_LEVEL){
            setFoodLevel(MAX_FOOD_LEVEL);
        }
    }

    @Override
    public int getFoodValue(){
        return returnFoodValue();
    }

    private static int returnFoodValue(){
        return FOOD_VALUE;
    }

    /**
     * Look for food adjacent to the current location.
     * Only the first live species is eaten.
     * @return Where food was found, or null if it wasn't.
     */
    protected boolean findSpecificFood(Object species)
    {
        // if species is a plant
        if(species instanceof Plant) {
            if(species instanceof Wheat){
                Wheat wheat = (Wheat) species;
                if(wheat.isActive()) {
                    setFoodLevel(getFoodLevel() + wheat.getFoodValue());
                    foodLevelCheck();
                    wheat.setDead();
                    return true;
                }
            }else if (species instanceof PoisonBerry){
                PoisonBerry berry = (PoisonBerry) species;
                if(berry.isActive()) {
                    berry.setDead();
                    return true;
                }
            }
        }

        // if species is a sheep
        if(species instanceof Sheep) {
            Sheep sheep = (Sheep) species;
            if(sheep.isActive()) {
                setFoodLevel(getFoodLevel() + sheep.getFoodValue());
                sheep.setDead();
                foodLevelCheck();
                return true;
            }
        }

        return false;
    }

    @Override
    protected int getBreedingAge(){
        return BREEDING_AGE;
    }

    @Override
    protected int getMaxAge(){
        return MAX_AGE;
    }

    /**
     * Generate and return a new Human in the field and location specified.
     * @param field The field where it has to appear.
     * @param location The location where it has to appear.
     * @return A new instance of Human.
     */
    public Human giveBirthUnique(Field field,Location loc)
    {
        return new Human(false, field, loc);
    }

    @Override
    protected double getBreedingProbability(){
        return BREEDING_PROBABILITY;
    }

    @Override
    protected int getMaxLitterSize(){
        return MAX_LITTER_SIZE;
    }

    @Override
    protected int getMaxFoodLevel(){
        return MAX_FOOD_LEVEL;
    }
}
