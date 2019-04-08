package ch.epfl.alpano.gui;
/**
 * 
 * @author Bayrem Kaabachi (261340), Emine Ghariani (262850)
 *
 */
public enum UserParameter {
    OBSERVER_LONGITUDE(60000,120000),
    OBSERVER_LATITUDE(450000,480000),
    OBSERVER_ELEVATION(300,10000),
    CENTER_AZIMUTH(0,359),
    HORIZONTAL_FIELD_OF_VIEW(1,360),
    MAX_DISTANCE(10,600),
    WIDTH(30,16000),
    HEIGHT(10,4000),
    SUPER_SAMPLING_EXPONENT(0,2);
    
    private int valMin;
    private int valMax;

    private UserParameter(int valMin, int valMax){    
        this.valMin=valMin;
        this.valMax=valMax;
    };
    /**
     * 
     * @param valeur valeur à ajuster
     * @return une valeur comprise entre valMax ET valMin
     */
    public int sanitize(int valeur){
        if(valeur > valMax)
            return valMax;
        if(valeur< valMin)
            return valMin;
        return valeur;
         
        
    }
    
   
}
