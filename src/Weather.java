/**
 * 
 * Define possible weather conditions, appliable both at day and night.
 * 
 * @author Alessandro Amantini and Ido Benzvi
 * @version 2018.02.21
 */
public enum Weather
{    
    RAINY("Rainy", 2), WINDY("Windy", 3), CLEAR("Clear", 5);
    
    private int hours; // defines the number of hours each weather condition lasts
    private String name;
    
    /**
     * @param name
     * @param hours
     */
    Weather(String name, int hours)
    {
        this.name = name;
        this.hours = hours;
    }
    
    /**
     *  return the number of hours a weather condition leasts
     *  @return The hours lasting that weather condition
     */
    public int getHours(){
        return hours;
    }
    
    public String getName(){
        return name;
    }
    
    /**
     * returns a random weather.
     * @return A random weather.
     */
    public static Weather getRandomWeather(){
        double x = Math.random()*3;
        if(x<1){
            return RAINY;
        }else if(x<2){
            return WINDY;
        }
        return CLEAR;        
    }
}
