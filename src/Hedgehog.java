import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * A simple model of a hedgehog.
 * Hedgehog's age, move, breed, and die.
 */
public class Hedgehog extends Creature
{
    // Characteristics shared by all hedgehogs (class variables).

    // The age at which a hedgehog can start to breed.
    private static final int BREEDING_AGE = 6;
    // The age to which a hedgehog can live.
    private static final int MAX_AGE = 12;
    // The likelihood of a hedgehog breeding.
    private static final double BREEDING_PROBABILITY = 0.2;
    // Food value got by whatever eats a hedgehog.
    private static final int FOOD_VALUE = 1;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 3;
    // The maximum amount of food a hedgehog is able to store.
    // In fact, it's the number of hours he can survive without eating before dying.
    private static final int MAX_FOOD_LEVEL = 5;

    // A shared Random object, if required.
    private Random rand = new Random();

    /**
     * Create a new hedgehog. A hedgehog may be created with age
     * zero (a new born) or with a random age.
     *
     * @param randomAge If true, the hedgehog will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Hedgehog(boolean randomAge, Field field, Location location)
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
     * This is what the hedgehog does most of the time - it runs
     * around and hunt. Sometimes it will breed or die of old age.
     * @param newHedgehogs A list to receive newly born hedgehogs.
     * @param time Day time.
     * @param weather The current weather.
     * @Override
     */
    @Override
    public void act(List<Species> newHedgehogs, int time, Weather weather)
    {
        incrementAge();
        incrementHunger();
        super.act(newHedgehogs,time, weather);
    }

    /**
     * Make this hegdehog more hungry. This could result in the hedgehog's death.
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
        return FOOD_VALUE;
    }

    /**
     * Look for food adjacent to the current location.
     * Only the first live species is eaten.
     * @return Where food was found, or null if it wasn't.
     */
    protected boolean findSpecificFood(Object species)
    {
        // hedgehogs only eat wheat
        if(species instanceof Wheat){
                Wheat wheat = (Wheat) species;
                if(wheat.isActive()) {
                    setFoodLevel(getFoodLevel() + wheat.getFoodValue());
                    wheat.setDead();
                    foodLevelCheck();
                    return true;
                }
        }
        return false;
    }

    /**
     * Generate and return a new Hedgehog in the field and location specified.
     * @param field The field where it has to appear.
     * @param location The location where it has to appear.
     * @return A new instance of Hedgehog.
     */
    public Hedgehog giveBirthUnique(Field field,Location loc)
    {
        return new Hedgehog(false, field, loc);
    }

    @Override
    protected int getMaxAge(){
        return MAX_AGE;
    }

    @Override
    protected int getBreedingAge(){
        return BREEDING_AGE;
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
