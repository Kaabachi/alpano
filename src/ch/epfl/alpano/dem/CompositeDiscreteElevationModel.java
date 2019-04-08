package ch.epfl.alpano.dem;
import static java.util.Objects.requireNonNull;


import ch.epfl.alpano.Interval2D;
import ch.epfl.alpano.Preconditions;
/**
 * 
 * @author Bayrem Kaabachi (261340), Emine Ghariani (262850)
 *
 */
final class CompositeDiscreteElevationModel implements DiscreteElevationModel{

    private final DiscreteElevationModel dem1 ;
    private final DiscreteElevationModel dem2 ;
    private final Interval2D extent;
    

    /**
     * Retourne un MNT discret représentant l'union des MNT donnés
     * @param dem1 1er MNT discret a unir
     * @param dem2 2eme MNT discret a unir
     * @throws NullPointerException si l'un des MNT donnes en argument est nul
     */
    CompositeDiscreteElevationModel(DiscreteElevationModel dem1, DiscreteElevationModel dem2)  {
        Preconditions.checkArgument(dem1.extent().isUnionableWith(dem2.extent()));
        this.dem1=requireNonNull(dem1);
        this.dem2=requireNonNull(dem2);
        extent = dem1.extent().union(dem2.extent());



    }

    @Override
    public void close() throws Exception { 

        dem1.close();
        dem2.close();

    }

    @Override
    public Interval2D extent() {
        return extent;
    }

    @Override
    public double elevationSample(int x, int y) {
        Preconditions.checkArgument(extent().contains(x, y));
        if(dem1.extent().contains(x, y)){
            return dem1.elevationSample(x, y);
        }
        else {
            return dem2.elevationSample(x, y);
        }


    }

}
