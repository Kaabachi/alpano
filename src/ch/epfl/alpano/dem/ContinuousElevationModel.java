package ch.epfl.alpano.dem;
import static java.util.Objects.requireNonNull;
import ch.epfl.alpano.Distance;
import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.Math2;

/**
 * 
 * @author Bayrem Kaabachi (261340), Emine Ghariani (262850)
 *
 */
public final class ContinuousElevationModel {

    private final DiscreteElevationModel dem;
    final static double  d =(Distance.toMeters(1/DiscreteElevationModel.SAMPLES_PER_RADIAN));
    /**
     * construit un MNT continu basé sur le MNT discret passé en argument
     * @param dem MNT discret sur lequel est contruit le MNT continu
     * @throws NullPointerException si le MNT discret passe en argument est  nul
     */
    public ContinuousElevationModel(DiscreteElevationModel dem) {
        this.dem=requireNonNull(dem);
    }
    /**
     * Retourne l'altitude au point donné, en mètres, obtenue par interpolation bilinéaire de l'extension du MNT discret passé au constructeur
     * @param p
     * @return l'altitude au point donné, en mètres, obtenue par interpolation bilinéaire de l'extension du MNT discret passé au constructeur
     */
    public double elevationAt(GeoPoint p){
        int x = (int)Math.floor(DiscreteElevationModel.sampleIndex(p.longitude()));
        int y =(int)Math.floor(DiscreteElevationModel.sampleIndex(p.latitude()));

        double point1= altitude(x,y);
        double point2= altitude(x+1,y);
        double point4= altitude(x+1,y+1);
        double point3= altitude(x,y+1);
        return Math2.bilerp(point1, point2, point3, point4, DiscreteElevationModel.sampleIndex(p.longitude())-x, DiscreteElevationModel.sampleIndex(p.latitude())-y);



    }
    /**
     * Retourne la pente du terrain au point donné, en radians
     * @param p GeoPoint
     * @return  la pente du terrain au point donné, en radians
     */
    public double slopeAt(GeoPoint p){
        int x = (int)Math.floor(DiscreteElevationModel.sampleIndex(p.longitude()));
        int y =(int)Math.floor(DiscreteElevationModel.sampleIndex(p.latitude()));


        double pente1=pente(x,y);
        double pente2=pente(x+1,y);
        double pente3=pente(x,y+1);
        double pente4=pente(x+1,y+1);
        return Math2.bilerp(pente1, pente2, pente3, pente4, DiscreteElevationModel.sampleIndex(p.longitude())-x, DiscreteElevationModel.sampleIndex(p.latitude())-y);




    }
    /**
     * retourne l'altitude d'un point de l'extension du DEM discret passé au constructeur
     * @param x index du point  
     * @param y index du point
     * @return l'altitude d'un point de l'extension du DEM discret passé au constructeur
     */
    private double altitude(int x,int y ){
        if(dem.extent().contains(x, y))
            return dem.elevationSample(x, y);

        else
            return 0;

    }
    /**
     * retourne la pente d'un point de l'extension du DEM discret passé au constructeur
     * @param x index du point
     * @param y index du point
     * @return la pente d'un point de l'extension du DEM discret passé au constructeur
     */
    private double pente(int x,int y){
        if(dem.extent().contains(x, y)){

            double point1= altitude(x,y);
            double point2= altitude(x+1,y);
            double point3= altitude(x,y+1);
            double deltaZA = point2-point1;
            double deltaZB = point3-point1;
            return Math.acos(d/Math.sqrt(Math2.sq(deltaZA)+Math2.sq(deltaZB)+Math2.sq(d)));
        }
        else
            return 0;
    }


}
