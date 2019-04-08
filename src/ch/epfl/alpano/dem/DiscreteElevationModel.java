package ch.epfl.alpano.dem;

import ch.epfl.alpano.Interval2D;
import ch.epfl.alpano.Preconditions;
/**
 * 
 * @author Bayrem Kaabachi (261340), Emine Ghariani (262850)
 *
 */
public interface DiscreteElevationModel extends AutoCloseable {

    public final static int SAMPLES_PER_DEGREE = 3600;
    public final static double SAMPLES_PER_RADIAN = (SAMPLES_PER_DEGREE*180) / Math.PI;
    /**
     * retourne l'index correspondant à l'angle donné, exprimé en radians
     * @param angle Angle auquel l'index correspond
     * @return retourne l'index correspondant à l'angle donné, exprimé en radians
     */
    public static double sampleIndex(double angle){
        return angle*SAMPLES_PER_RADIAN;
    }
    /**
     * Retourne l'étendue du MNT
     * @return retourne l'étendue du MNT
     */
    public Interval2D extent();
    /**
     * Retourne l'échantillon d'altitude à l'index donné, en mètres
     * @param x Index x
     * @param y Index y
     * @throws IllegalArgumentException si l'index ne fait pas partie de l'étendue du MNT.
     * @return l'étendue du MNT
     */
    public double elevationSample(int x, int y);
    /**
     * retourne un MNT discret représentant l'union du récepteur et de l'argument that
     * @param that MNT discret a unir 
     * @throws IllegalArgumentException si les étendues ne sont pas unionables.
     * @return un MNT discret représentant l'union du récepteur et de l'argument that
     */
    default DiscreteElevationModel union(DiscreteElevationModel that){
        Preconditions.checkArgument(this.extent().isUnionableWith(that.extent()));
        return new CompositeDiscreteElevationModel(this,that);

    }



}
