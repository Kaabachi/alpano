package ch.epfl.alpano.dem;
import ch.epfl.alpano.Distance;
import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.Math2;


import ch.epfl.alpano.Azimuth;

import ch.epfl.alpano.Preconditions;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import static java.util.Objects.requireNonNull;


/**
 * 
 * @author Bayrem Kaabachi (261340), Emine Ghariani (262850)
 *
 */

public final class ElevationProfile {
    private final ContinuousElevationModel elevationModel ;
    private final GeoPoint origin;
    private final double azimuth;
    private final double  length;

    private final double[] tabLon ;
    private final double[] tabLat ;
    final static double SPACING = 4096;
    /**
     * construit un profil altimétrique basé sur le MNT donné et dont le tracé débute au point origin, suit le grand cercle dans la direction donnée par azimuth, et a une longueur de length mètres ; 
     * @param elevationModel MNT continu
     * @param origin GeoPoint dont le trace debute
     * @param azimuth Azimuth donnant la direction
     * @param length Longueur en metres
     * @throws IllegalArgumentException si l'azimuth n'est pas canonique, ou si la longueur n'est pas strictement positive
     * @throws NullPointerException si origin ou elevationModel sont nul
     */
    public ElevationProfile(ContinuousElevationModel elevationModel, GeoPoint origin, double azimuth, double length){

        Preconditions.checkArgument(Azimuth.isCanonical(azimuth) && length>0);


        this.elevationModel=requireNonNull(elevationModel);
        this.origin=requireNonNull(origin);
        this.azimuth=azimuth;
        this.length=length;

        int lengthOfArray=(int)Math.ceil(length/SPACING) +1;

        tabLon=new double[lengthOfArray];

        tabLat=new double[lengthOfArray];



        for(int i=0;i<lengthOfArray;i++){
            tabLat[i]=Math.asin(Math.sin(this.origin.latitude())* Math.cos(Distance.toRadians(SPACING * i))
                    + Math.cos(this.origin.latitude())
                    * Math.sin(Distance.toRadians(SPACING * i))
                    * Math.cos(Azimuth.toMath(this.azimuth)));


            tabLon[i]= ((this.origin.longitude()
                    - Math.asin(Math.sin(Azimuth.toMath(this.azimuth))
                            * Math.sin(Distance.toRadians(SPACING * i))
                            / Math.cos(tabLat[i]))
                    + Math.PI) % (Math2.PI2)) - Math.PI;

        }


    }
    /**
     * retourne l'altitude du terrain à la position donnée du profil
     * @param x Position donne du profil
     * @throws IllegalArgumentException si la position n'est pas dans les bornes du profil
     * @return l'altitude du terrain à la position donnée du profil
     */
    public GeoPoint positionAt(double x){
        Preconditions.checkArgument(x>=0 && x<=this.length);
        int id = (int)(x/SPACING) ;
        double longitude = Math2.lerp(tabLon[id],tabLon[id+1],x/SPACING - id) ;
        double latitude = Math2.lerp(tabLat[id], tabLat[id+1], (x/SPACING)-id) ;

        return new GeoPoint(longitude,latitude);
    }

    /**
     * retourne les coordonnées du point à la position donnée du profil
     * @param x Position donnee du profil
     * @throws IllegalArgumentException si cette position n'est pas dans les bornes du profil
     * @return les coordonnées du point à la position donnée du profil
     */
    public double elevationAt(double x){

       

        return this.elevationModel.elevationAt(positionAt(x));
    }
    /**
     * retourne la pente du terrain à la position donnée du profil
     * @param x Position donnee du profil
     * @throws IllegalArgumentException si la position n'est pas dans les bornes du profil.
     * @return la pente du terrain à la position donnée du profil
     */
    public double slopeAt(double x) {
        Preconditions.checkArgument(x>=0 && x<=this.length);
        return this.elevationModel.slopeAt(positionAt(x));
    }


}

