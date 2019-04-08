package ch.epfl.alpano;

import java.util.Locale;
/**
 * 
 * @author Bayrem Kaabachi (261340) Emine Ghariani (262850)
 *
 */

public final class GeoPoint {

    private final double longitude;
    private final double latitude;

    /**
     * Construit le point de coordonnées données
     * @param longitude Longitude du GeoPoint
     * @param latitude Latitude du GeoPoint
     */
    public GeoPoint(double longitude, double latitude ){
        Preconditions.checkArgument((longitude <= Math.PI && longitude >= -Math.PI) && (latitude<=(Math.PI)/2 && latitude>=-(Math.PI/2)));

        this.latitude=latitude;
        this.longitude=longitude;

    }

    /**
     * Retourne la longitude du GeoPoint en radians
     * @return la longitude du GeoPoint en radians
     */
    public double longitude(){
        return longitude;
    }
    /**
     * retourne la latitude du GeoPoint en radians
     * @return la latitude du GeoPoint en radians
     */
    public double latitude(){
        return latitude;
    }
    /**
     * Retourne la distance entre le GeoPoint this et le GeoPoint that en metres.
     * @param that GeoPoint dont on veut calculer la distance par rapport au recepteur this. 
     * @return la distance entre le GeoPoint this et le GeoPoint that en metres.
     */
    public double distanceTo(GeoPoint that){
        return Distance.toMeters( 2 * Math.asin(Math.sqrt(Math2
                .haversin(this.latitude - that.latitude)
                + Math.cos(this.latitude) * Math.cos(that.latitude)
                * Math2.haversin(this.longitude - that.longitude))));
    }
    /**
     * Retourne l'Azimuth entre le GeoPoint this et le GeoPoint that en radians
     * @param that GeoPoint dont on veut calculer l'Azimuth par rapport au recepteur this.
     * @return l'Azimuth entre le GeoPoint this et le GeoPoint that en radians
     */
    public double azimuthTo(GeoPoint that){
        double num =(Math.sin(longitude-that.longitude())*Math.cos(that.latitude()));
        double denom = Math.cos(latitude)*Math.sin(that.latitude())-Math.sin(latitude)*Math.cos(that.latitude())*Math.cos(longitude-that.longitude());
        return Azimuth.fromMath(Azimuth.canonicalize(Math.atan2(num , denom)));
    }
    /**
     * Retourne les coordonnees du GeoPoint en degres
     * @return les coordonnees du GeoPoint en degres
     */
    @Override
    public String toString(){
        Locale locale = null;
        return String.format(locale, "(%.4f , %.4f)", Math.toDegrees(longitude), Math.toDegrees(latitude));


    }


}
