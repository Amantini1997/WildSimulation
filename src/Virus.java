import java.util.HashSet;
import java.util.List;

/**
 * Virus is a desease affecting creatures by incrementing their hunger.
 * It doesn't affect species costantly, but only at specific hours of the day.
 * Species can randomly get healed from the desease.
 */
public class Virus
{
    // Collection containing all the creatures affected by the virus.
    // Being a set, the same creature cannot be present twice.
    private HashSet<Creature> creatures;

    /**
     * Public constructor initializing the collection.
     */
    public Virus(){
        creatures = new HashSet<>();
    }

    /**
     * A new creature is infected.
     * WhiteWalkers are immune.
     * @param creature The creature to infect.
     */
    public void infect(Creature creature)
    {

        //WhiteWalker are immune from the virus
        if(!(creature instanceof WhiteWalker)){
            creatures.add(creature);
        }
    }

        /**
         * Run over creatures. The creatures affected by virus can either die and/or heal
         * according to an arbitrary probability, and in that case they won't be considered for the
         * next iteration(the next hour); otherwise they are still affecetd by virus, so they suffer for that,
         * and then are copied into a new collection that will substitute the current list creatures.
         */
        public void givePain(int time){
        if(time>= 8){
            HashSet<Creature> copyList = new HashSet<>();
            for(Creature creature : creatures){

                // check if the creature either heals or is dead
                if(Math.random()>0.2 && creature.isActive()){
                    // make them suffer
                    for(int i = 0 ; i<3 ; i++){
                        creature.incrementHunger();
                    }
                    copyList.add(creature);
                }
            }

            creatures = copyList;
        }
    }

    /**
     * Return a the collection storing all the infected creatures.
     * @return creatures
     */
    public HashSet<Creature> getInfectedCreatures(){
        return creatures;
    }
}
