package ch.epfl.alpano;
/**
 * 
 * @author Bayrem Kaabachi (261340) Emine Ghariani (262850)
 *
 */
/*
 * Interface permettant de faire des opérations sur les distances
 */
public interface Distance {
    public static final double EARTH_RADIUS =  6371000;

    /**
     * convertit une distance à la surface de la Terre exprimée en mètres en l'angle correspondant, en radians.
     * @param distanceInMeters distance à la surface de la terre 
     * @returns une distance à la surface de la Terre exprimée en mètres en l'angle correspondant, en radians
     */
    public static double toRadians(double distanceInMeters){

        return (distanceInMeters/EARTH_RADIUS);


    }

    /**
     * convertit un angle en radians en la distance correspondant à la surface de la Terre, en mètres.
     * @param distanceInRadians distance en radians
     * @returns un angle en radians en la distance correspondant à la surface de la Terre, en mètres.
     */
    public static double toMeters(double distanceInRadians){

        return ( distanceInRadians* EARTH_RADIUS);

    }

}
