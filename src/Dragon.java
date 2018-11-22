import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * A simple model of a dragon.
 * Dragon's age, move, breed, and die.
 */
public class Dragon extends Creature
{
    // Characteristics shared by all Dragon (class variables).

    // The age at which a dragon can start to breed.
    private static final int BREEDING_AGE = 100;
    // The age to which a dragon can live.
    private static final int MAX_AGE = 400;
    // The likelihood of a dragon breeding.
    private static final double BREEDING_PROBABILITY = 0.2;
    // Food value got by whatever eats a human.
    private static final int FOOD_VALUE = 100;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 2;
    // The maximum amount of food a dragon is able to store.
    // In fact, it's the number of hours he can survive without eating before dying.
    private static final int MAX_FOOD_LEVEL = 150;

    // A shared Random object, if required.
    private Random rand = new Random();

    /**
     * Create a new dragon. A dragon may be created with age
     * zero (a new born) or with a random age.
     *
     * @param randomAge If true, the dragon will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Dragon(boolean randomAge, Field field, Location location)
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
     * This is what the dragon does most of the time - it runs
     * around and hunt. Sometimes it will breed or die of old age.
     * Dragon cannot move when it's windy.
     * @param newDragons A list to receive newly born dragons.
     * @param time Day time.
     * @param weather The current weather.
     * @Override
     */
    @Override
    public void act(List<Species> newSpecies, int time, Weather weather)
    {
        incrementAge();
        incrementHunger();
        switch(weather){
            case WINDY:
                break;
            default:
                super.act(newSpecies,time, weather);
                break;
        }
    }

    /**
     * Make this fox more hungry. This could result in the fox's death.
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

    /**
     * Look for food adjacent to the current location.
     * Only the first live animal is eaten.
     * @return Where food was found, or null if it wasn't.
     */
    protected boolean findSpecificFood(Object species)
    {
        if(species instanceof Human){
            Human human = (Human) species;
            if(human.isActive()){
                setFoodLevel(getFoodLevel() + human.getFoodValue());
                human.setDead();
                foodLevelCheck();
                return true;
            }
        }

        if(species instanceof WhiteWalker){
            WhiteWalker whiteWalker = (WhiteWalker) species;
            if(whiteWalker.isActive()){
                setFoodLevel(getFoodLevel() + whiteWalker.getFoodValue());
                whiteWalker.setDead();
                foodLevelCheck();
                return true;
            }
        }

        if(species instanceof Sheep) {
            Sheep sheep = (Sheep) species;
            if(sheep.isActive()){
                setFoodLevel(getFoodLevel()+ sheep.getFoodValue());
                foodLevelCheck();
                sheep.setDead();
                return true;
            }
        }

        return false;
    }

    /**
     * Check whether or not this dragon is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param new dragons A list to return newly born dragons.
     */
    public Dragon giveBirthUnique(Field field,Location loc)
    {
        return new Dragon(false, field, loc);
    }

    @Override
    public int getFoodValue(){
        return FOOD_VALUE;
    }

    /**
     * Check if a species is a dragon.
     * @param species The species to check.
     * @return 1 if it's a dragon, 0 otherwise
     * @Override
     */
    @Override
    public int compareTo(Species species){
        return (species instanceof Dragon)? 1 : 0;
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
