package ch.epfl.alpano.summit;

import java.util.Objects;

import ch.epfl.alpano.GeoPoint;
/**
 * 
 * @author Bayrem Kaabachi (261340), Emine Ghariani (262850)
 *
 */
public final class Summit {
    private final GeoPoint position;
    private final int elevation;
    private final String name;

    /**
     * construit un sommet
     * @param name nom du sommet
     * @param position position du sommet
     * @param elevation altitude du sommet 
     * @throws NullPointerException si le nom ou la position sont nuls
     */
    public Summit(String name, GeoPoint position, int elevation) {
        this.name=Objects.requireNonNull(name);
        this.position=Objects.requireNonNull(position);
        this.elevation=elevation;
    }

    /**
     * retourne le nom du sommet
     * @return retourne le nom du sommet
     */
    public String name(){
        return name;
    }
    /**
     * retourne l'altitude du sommet
     * @return retourne l'altitude du sommet
     */
    public int elevation(){
        return elevation;
    }
    /**
     * retourne la position du sommet
     * @return retourne la position du sommet
     */
    public GeoPoint position(){
        return position;
    }

    @Override 
    public String toString(){
        return (name()+" "+position.toString()+" "+elevation());  
    }



}
