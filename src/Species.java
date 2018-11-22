import java.util.List;

/**
 * An interface describing all the species.
 */

public interface Species extends Comparable<Species>
{
    //it is supposed to return have a species acting.
    void act(List<Species> newSpecies, int time, Weather weather);

    //it is supposed to return whether a species is alive.
    boolean isActive();

    //it is supposed to return the food value of a species.
    int getFoodValue();

    //it is supposed to kill a species and remove it from the field.
    void setDead();

    //it is supposed to return the field containing the species.
    Field getField();

    //it is supposed to return the location containing the species.
    Location getLocation();

    //it is supposed to return an int value indicating
    // whether two species are the same.
    int compareTo(Species s);

    //it is supposed to return the True if the species is male.
    boolean getIsMale();
}
