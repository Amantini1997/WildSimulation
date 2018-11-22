import java.util.List;

/**
 * A simple model of a whiteWalker.
 * WhiteWalkers age, move, eat humans, and die.
 * WhiteWalkers can't breed, but they don't die by starvation.
 */
public class WhiteWalker extends Creature
{
    // Characteristics shared by all whiteWalkers (class variables).

    // The age at which a whiteWalker can start to breed.
    // Actually they don't reproduce, but we keep it for maintenability as well as MAX_AGE and MAX_LITTER_SIZE
    private static final int BREEDING_AGE = 1;
    // The age to which a whiteWalker can live.
    private static final int MAX_AGE = 1;
    // WhiteWalker cannot breed.
    private static final double BREEDING_PROBABILITY = 0;
    // Food value got by whatever eats a white walker.
    private static final int FOOD_VALUE = 10;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 0;

    /**
     * Create a whiteWalker. A whiteWalker can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     *
     * @param randomAge No matter what it is, WhiteWalker doesn't have hunger level and/or age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public WhiteWalker(boolean randomAge, Field field, Location location)
    {
        super(field, location,0);
    }

    /**
     * This is what the whiteWalker does at every hour: it hunts for
     * humans.
     * @param newWhiteWalkers A list to return newly born whiteWalkers.
     * @param time Day time.
     * @param weather Current weather.
     */
    public void act(List<Species> newWhiteWalkers, int time, Weather weather)
    {
        super.act(newWhiteWalkers, time, weather);
    }

    /**
     * Check whether an objects fits to be eaten (right species that has to be alive), and, in case it fits, kill it.
     * @param species Any object
     * @return True if that species is edible, false otherwise.
     * @Override
     */
    protected boolean findSpecificFood(Object species)
    {
        if(species instanceof Human) {
            Human human = (Human) species;
            if(human.isActive()) {
                human.setDead();
                return true;
            }
        }
        return false;
    }

    /**
     * Generate and return a new WhiteWalker in the field and location specified.
     * @param field The field where it has to appear.
     * @param location The location where it has to appear.
     * @return A new instance of WhiteWalker.
     */
    public WhiteWalker giveBirthUnique(Field field,Location loc)
    {
        return new WhiteWalker(false, field, loc);
    }

    @Override
    public int getFoodValue(){
        return FOOD_VALUE;
    }

    // ALL the following methods are useless for a white walker,
    // but necessary to extend Creature

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
    protected void foodLevelCheck(){
    }

    @Override
    protected int getMaxFoodLevel(){
        return 0;
    }
}
