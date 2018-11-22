import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * A simple model of a sheep.
 * Sheeps age, move, breed, and die.
 */
public class Sheep extends Creature
{
    // Characteristics shared by all sheeps.

    // The age at which a sheep can start to breed.
    private static final int BREEDING_AGE = 5;
    // The age to which a sheep can live.
    private static final int MAX_AGE = 30;
    // The likelihood of a sheep breeding.
    private static final double BREEDING_PROBABILITY = 0.6;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 4;
    // Food value got by whatever eats a sheeep.
    private static final int FOOD_VALUE = 20;
    // The maximum amount of food a human is able to store.
    // In fact, it's the number of hours he can survive without eating before dying.
    private static final int MAX_FOOD_LEVEL = 5;

    // A shared Random object, if required.
    private Random rand = new Random();

    /**
     * Create a new sheep. A sheep may be created with age
     * zero (a new born) or with a random age.
     *
     * @param randomAge If true, the sheep will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Sheep(boolean randomAge, Field field, Location location)
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
     * This is what the sheep does most of the time - it runs
     * around and hunt. Sometimes it will breed or die of old age.
     * @param newSheeps A list to receive newly born sheeps.
     * @param time Day time.
     * @param weather The current weather.
     * @Override
     */
    public void act(List<Species> newSheeps, int time, Weather weather)
    {
        incrementAge();
        incrementHunger();
        super.act(newSheeps,time, weather);
    }

    /**
     * Make this sheep more hungry. This could result in the sheep's death.
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
        // sheeps only eat plants
        if(species instanceof Plant) {
            if(species instanceof Wheat){
                Wheat wheat = (Wheat) species;
                if(wheat.isActive()) {
                    setFoodLevel(getFoodLevel() + wheat.getFoodValue());
                    wheat.setDead();
                    foodLevelCheck();
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
        return false;
    }

    /**
     * Generate and return a new Sheep in the field and location specified.
     * @param field The field where it has to appear.
     * @param location The location where it has to appear.
     * @return A new instance of Sheep.
     */
    public Sheep giveBirthUnique(Field field,Location loc)
    {
        return new Sheep(false, field, loc);
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
