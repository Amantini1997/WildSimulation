import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Color;
import java.util.HashSet;

/**
 * A simple predator-prey simulator, based on a rectangular field
 * containing plants( wheat and poisonBerrys) and creatures( dragons, humans, sheeps, hedgehogs, white walkers from GoT).
 */
public class Simulator
{
    // Constants representing configuration information for the simulation.

    // The default width for the grid.
    private static final int DEFAULT_WIDTH = 120;
    // The default depth of the grid.
    private static final int DEFAULT_DEPTH = 80;
    // The probability that a white walker will be created in any given grid position.
    private static final double WHITEWALKER_CREATION_PROBABILITY = 0.02;
    // The probability that a human will be created in any given grid position.
    private static final double HUMAN_CREATION_PROBABILITY = 0.2;
    // The probability that a dragon will be created in any given grid position.
    private static final double DRAGON_CREATION_PROBABILITY = 0.005;
    // The probability that a sheep will be created in any given grid position.
    private static final double SHEEP_CREATION_PROBABILITY = 0.13;
    // The probability that a stem wheat will be created in any given grid position.
    private static final double WHEAT_CREATION_PROBABILITY = 0.3;
    // The probability that a poison berry bush will be created in any given grid position.
    private final static double POISON_BERRY_CREATION_PROBABILITY = 0.08;
    // The probability that a hedgehog will be created in any given grid position.
    private final static double HEDGEHOG_CREATION_PROBABILITY = 0.03;
    // The day starts at this hour.
    private static final int FIRST_HOUR = 8;

    // List of species in the field.
    private List<Species> species;
    // The current state of the field.
    private Field field;
    // The current step of the simulation.
    private int step;
    // A graphical view of the simulation.
    private SimulatorView view;
    // Day time.
    private int time;
    // it's the time the compiler has to wait before changing weather(that may be the same as the previous)
    private int waitTime;

    private Virus virus;
    private Weather weather;

    /**
     * Construct a simulation field with default size.
     */
    public Simulator()
    {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH);
        view.showStatus(weather.getName(), time, field);
    }

    /**
     * Create a simulation field with the given size.
     * @param depth Depth of the field. Must be greater than zero.
     * @param width Width of the field. Must be greater than zero.
     */
    public Simulator(int depth, int width)
    {
        if(width <= 0 || depth <= 0) {
            System.out.println("The dimensions must be greater than zero.");
            System.out.println("Using default values.");
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }

        virus = new Virus();
        time = FIRST_HOUR; // the simulation will start at that hour
        this.weather = Weather.getRandomWeather();
        this.waitTime = weather.getHours();
        species = new ArrayList<>();
        field = new Field(depth, width);

        // Create a view of the state of each location in the field.
        Color PURPLE = new Color(138, 43, 226); //creating colour Purple

        view = new SimulatorView(depth, width);
        view.setColor(Human.class, Color.PINK);
        view.setColor(WhiteWalker.class, Color.BLUE);
        view.setColor(Dragon.class, Color.BLACK);
        view.setColor(Sheep.class, Color.ORANGE);
        view.setColor(Wheat.class, Color.YELLOW);
        view.setColor(PoisonBerry.class, PURPLE);
        view.setColor(Hedgehog.class, Color.LIGHT_GRAY);

        // Setup a valid starting point.
        reset();
    }

    /**
     * Run the simulation from its current state for a reasonably long period,
     * (4000 steps).
     */
    public void runLongSimulation()
    {
        simulate(4000);
    }

    /**
     * Run the simulation from its current state for the given number of steps.
     * Stop before the given number of steps if it ceases to be viable.
     * @param numSteps The number of steps to run for.
     */
    public void simulate(int numSteps)
    {
        for(int step = 1; step <= numSteps && view.isViable(field); step++) {
            simulateOneStep();
            delay(20);   // change the delay here to either speed up or slow down the animation
        }
    }

    /**
     * Run the simulation from its current state for a single step.
     * Iterate over the whole field updating the state of each species.
     * Their movement depends on day time and wheater.
     * Creatures can be affected by viruses, and infect sorrounding ones.
     * Plants are immune by viruses.
     */
    public void simulateOneStep()
    {
        step++;
        time = ++time%24;
        if(waitTime-- == 0){
            weather = Weather.getRandomWeather();
            waitTime = weather.getHours();
        }

        // Provide space for newborn species.
        List<Species> newSpecies = new ArrayList<>();
        // Let all rabbits act.

        for(Iterator<Species> it = species.iterator(); it.hasNext(); ) {
            Species species = it.next();

            //during the night creatures sleep, but white walkers.
            if(!(species instanceof Creature && time<8) || species instanceof WhiteWalker){
                species.act(newSpecies,time,weather);
            }

            if(Math.random()>0.9 && species instanceof Creature){
                Creature creature = (Creature) species;
                virus.infect(creature);
            }

            if(! species.isActive()) {
                it.remove();
            }

            // virus spreads infecting ONE random creature adjacent to those already infected
            if(virus.getInfectedCreatures().contains(species)){
                Location loc = species.getLocation();
                Field field = species.getField();
                if(loc != null){
                    Location location = field.randomAdjacentLocation(loc); // getting a random adjacent location
                    Object obj = field.getObjectAt(location);
                    if(obj instanceof Creature){
                        Species speciesToInfect = (Species) obj; //getting the species belonging to that location
                        Creature creatureToInfect = (Creature) species;
                        virus.infect(creatureToInfect);
                    }
                }
            }
        }

        // virus acting.
        virus.givePain(time);

        // Add the newly born foxes and rabbits to the main lists.
        species.addAll(newSpecies);

        view.showStatus(weather.getName(), time, field);
    }

    /**
     * Reset the simulation to a starting position.
     */
    public void reset()
    {
        step = 0;
        species.clear();
        populate();

        // Show the starting state in the view.
        view.showStatus(weather.getName(), time, field);
    }

    /**
     * Randomly populate the field with creatures and plants.
     */
    private void populate()
    {
        Random rand = Randomizer.getRandom();
        field.clear();
        for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {
                if(rand.nextDouble() <= POISON_BERRY_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    PoisonBerry berry = new PoisonBerry(field, location);
                    species.add(berry);
                }else if(rand.nextDouble() <= WHITEWALKER_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    WhiteWalker whiteWalker = new WhiteWalker(true, field, location);
                    species.add(whiteWalker);
                }else if(rand.nextDouble() <= DRAGON_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Dragon dragon = new Dragon(true, field, location);
                    species.add(dragon);
                }else if(rand.nextDouble() <= SHEEP_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Sheep sheep = new Sheep(true, field, location);
                    species.add(sheep);
                }else if(rand.nextDouble() <= WHEAT_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Wheat wheat = new Wheat(field, location);
                    species.add(wheat);
                }else if(rand.nextDouble() <= HUMAN_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Human human = new Human(true, field, location);
                    species.add(human);
                }
                else if(rand.nextDouble() <= HEDGEHOG_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Hedgehog hedgehog = new Hedgehog(true, field, location);
                    species.add(hedgehog);
                }
                // else leave the location empty.
            }
        }
    }

    /**
     * Pause for a given time.
     * @param millisec  The time to pause for, in milliseconds
     */
    private void delay(int millisec)
    {
        try {
            Thread.sleep(millisec);
        }
        catch (InterruptedException ie) {
            // wake up
        }
    }

    public static void main(String args[]){
        Simulator simulator = new Simulator();
        simulator.runLongSimulation();
    }
}
