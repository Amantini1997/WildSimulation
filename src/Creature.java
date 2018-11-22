import java.util.List;
import java.util.Iterator;
import java.util.Random;
import java.util.ArrayList;

/**
 * A class representing shared characteristics of creatures.
 */
public abstract class Creature implements Species
{
    // Flag to indicate whether the creature is either male or female.
    private final boolean isMale;
    // Whether the creature is alive or not.
    private boolean alive;
    // The creature's field.
    private Field field;
    // The creature's position in the field.
    private Location location;
    // The creature's age.
    private int age;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    // List containing the classes of all the species that need a partner.
    private List<Class> partnerNeeder;
    // current level of food.
    private int foodLevel;

    /**
     * Create a new creature at location in field.
     *
     * @param field The field currently occupied.
     * @param location The location within the field.
     * @param age the age of the creature.
     */
    public Creature(Field field, Location location, int age)
    {
        alive = true;
        this.field = field;
        setLocation(location);
        this.age = age;
        this.isMale = (Math.random()>0.5)?true:false;
        foodLevel = getMaxFoodLevel();
        definePartnerNeeders();
    }

    /**
     * Defining all the species needing a partener to breed.
     */
    private void definePartnerNeeders(){
        partnerNeeder = new ArrayList<Class>();
        partnerNeeder.add(Dragon.class);
        partnerNeeder.add(Human.class);
    }

    /**
     * Check if a species if this and another species are the same.
     * @param species The species to check.
     * @return 1 if True, 0 otherwise.
     * @Override
     */
    public int compareTo(Species species){
        return (this == species) ? 1 : 0;
    }

    protected abstract int getMaxFoodLevel();

    /**
     * Make this creature act - that is: make it do
     * whatever it wants/needs to do.
     * @param newCreatures A list to receive newly born creatures.
     * @Override
     */
    public void act(List<Species> newCreatures,int time, Weather weather){

        if(isActive()) {
            giveBirth(newCreatures);
            // Move towards a source of food if found.
            Location newLocation = findFood();
            if(newLocation == null) {
                // No food found - try to move to a free location.
                newLocation = getField().freeAdjacentLocation(getLocation());
            }
            // See if it was possible to move.
            if(newLocation != null) {
                setLocation(newLocation);
            }
            else if(!(this instanceof WhiteWalker)) {
                // Overcrowding.
                setDead();
            }
        }
    }

    /**
     * @Override
     */
    public boolean isActive(){
        return alive;
    }

    abstract protected void foodLevelCheck();

    /**
     * Indicate that the creature is no longer alive.
     * It is removed from the field.
     */
    public void setDead()
    {
        alive = false;
        if(location != null) {
            field.clear(location);
            location = null;
            field = null;
        }
    }

    /**
     * Return the creature's location.
     * @return The creature's location.
     * @Override
     */
    public Location getLocation()
    {
        return location;
    }

    protected int getAge(){
        return age;
    }

    protected void setAge(int age){
        this.age = age;
    }

    /**
     * Check whether or not this creature is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newCreatures A list to return newly born creatures.
     */
    protected void giveBirth(List<Species> newCreatures)
    {
        // New creatures are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            boolean breedable = false;

            if(checkPartner()){
                breedable = true;
            }

            if(breedable){
                newCreatures.add(giveBirthUnique(field, loc));
            }
        }
    }

    public boolean checkPartner(){
        if(partnerNeeder.contains(this.getClass())){
            Field field = getField();
            List<Location> adjacent = field.adjacentLocations(getLocation());
            Iterator<Location> it = adjacent.iterator();
            while(it.hasNext()) {
                Location where = it.next();
                Species species = (Species) field.getObjectAt(where);
                if(this.compareTo(species)==1){
                    if(isActive() && getIsMale() != species.getIsMale()) {
                        return false;
                    }
                }
            }

            return true;
        }
        return true;
    }

    protected abstract Creature giveBirthUnique(Field field, Location loc);

    /**
     * @return True if the creature is male, false otherwise.
     */
    public boolean getIsMale(){
        return isMale;
    }

    /**
     * Increase the age.
     * This could result in the rabbit's death.
     */
    protected void incrementAge()
    {
        age++;
        if(age > getMaxAge()) {
            setDead();
        }
    }

    protected abstract int getMaxAge();

    /**
     * A fox can breed if it has reached the breeding age.
     */
    protected boolean canBreed()
    {
        return age >= getBreedingAge();
    }

    protected abstract int getBreedingAge();

    /**
     * Place the creature at the new location in the given field.
     * @param newLocation The creature's new location.
     */
    protected void setLocation(Location newLocation)
    {
        if(location != null) {
            field.clear(location);
        }
        location = newLocation;
        field.place(this, newLocation);
    }

    /**
     * Return the creature's field.
     * @return The creature's field.
     * @Override
     */
    public Field getField()
    {
        return field;
    }

    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * @return The number of births (may be zero).
     */
    protected int breed()
    {
        int births = 0;
        if(canBreed() && rand.nextDouble() <= getBreedingProbability()) {
            births = rand.nextInt(getMaxLitterSize()) + 1;
        }
        return births;
    }

    protected abstract double getBreedingProbability();

    protected abstract int getMaxLitterSize();

    protected void incrementHunger(){
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }

    protected abstract boolean findSpecificFood(Object species);

    protected Location findFood(){
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object species = field.getObjectAt(where);
            if(findSpecificFood(species)){
                return where;
            }
        }
        return null;
    }

    protected void setFoodLevel(int foodLevel){
        this.foodLevel = foodLevel;
    }

    protected int getFoodLevel(){
        return foodLevel;
    }
}
