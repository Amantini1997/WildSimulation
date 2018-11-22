import java.util.List;

/**
 * Plant class. Plant die only if something eats them and they can't move.
 */
public abstract class Plant implements Species
{
    // instance variables - replace the example below with your own
    private int x;
    // Whether the plant is alive.
    private boolean alive;
    // Plant's field
    private Field field;
    // Plant's location.
    private Location location;
    // Plant's current level of water.
    private int waterLevel;

    /**
     * Constructor that create plants in specific field and location.
     * @param field The field where the plant has to appear
     * @param location The location where the plant has to appear
     */
    public Plant(Field field, Location location)
    {
        alive = true;
        this.field = field;
        setLocation(location);
        waterLevel = 2;
    }

    /**
     * Place the plant at the new location in the given field.
     * @param newLocation The plant's new location.
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
     * plants don't need partner, being all male they can't reproduce according to that feature.
     * @return true.
     * @Override
     */
    public boolean getIsMale(){
        return true;
    }

    /**
     * Return the plant's location.
     * @return The plant's location.
     * @Override
     */
    public Location getLocation()
    {
        return location;
    }

    /**
     * Indicate that the plant is no longer alive.
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
     * Check whether the plant is alive or not.
     * @return true if the plant is still alive.
     */
    public boolean isActive()
    {
        return alive;
    }

    /**
     * Check whether or not this plant is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newPlants A list to return newly born plants.
     */
    protected void giveBirth(List<Species> newPlants)
    {
        // New foxes are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        while(free.size() > 0) {
            Location loc = free.remove(0);
            newPlants.add(giveBirthUnique(field, loc));
        }
    }

    /**
     * Return the plant's field.
     * @return The plant's field.
     * @Override
     */
    public Field getField()
    {
        return field;
    }

    /**
     * Generate and returns a new instance of Plant
     * @return New instance of Plant.
     */
    protected abstract Plant giveBirthUnique(Field field, Location loc);

    /**
     * Check if a species is a plant.
     * @param species The species to check.
     * @return 1 if it's a plant, 0 otherwise
     * @Override
     */
    public int compareTo(Species species){
        return this == (species)? 1 : 0;
    }

    protected int getWaterLevel(){
        return waterLevel;
    }

    protected void setWaterLevel(int waterLevel){
        this.waterLevel = waterLevel;
    }
}
