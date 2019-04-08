package ch.epfl.alpano.gui;


import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

import ch.epfl.alpano.Azimuth;
import ch.epfl.alpano.GeoPoint;

import ch.epfl.alpano.PanoramaParameters;
/**
 * 
 * @author Bayrem Kaabachi (261340), Emine Ghariani (262850)
 *
 */
public final class PanoramaUserParameters {
    private final Map<UserParameter, Integer > parametersMap;
    private final static int MAX_ANGLE = 170 ;
    private static final int MAXD_COEFFICIENT=1000;
    private static final double ANGLE_COEFFICIENT=10000.0;
    /**
     * Construit un objet PanoramaUserParameters a partir d'une table
     * associative associant a chaque UserParameter une valeur Integer.
     * 
     * @param parameters
     */
    public PanoramaUserParameters(Map<UserParameter, Integer> parametersMap) {
        


        int hauteur =  Math.min(UserParameter.HEIGHT.sanitize(parametersMap.get(UserParameter.HEIGHT)),
                ((MAX_ANGLE*(parametersMap.get(UserParameter.WIDTH)-1)/((parametersMap.get(UserParameter.HORIZONTAL_FIELD_OF_VIEW))))));


        for(Map.Entry<UserParameter, Integer> parameter : parametersMap.entrySet()){
            UserParameter key = parameter.getKey();
            if(key.equals(UserParameter.HEIGHT)){
                parametersMap.put(key, hauteur);
            }
            else{
                parametersMap.put(key,key.sanitize(parametersMap.get(parameter.getKey())));
            }
        }



        this.parametersMap= Collections.unmodifiableMap(parametersMap);


    }

     private static EnumMap<UserParameter,Integer> createParametersMap(int OBSERVER_LONGITUDE,int OBSERVER_LATITUDE,int OBSERVER_ELEVATION,
            int CENTER_AZIMUTH,int HORIZONTAL_FIELD_OF_VIEW,int MAX_DISTANCE,int WIDTH,int HEIGHT,int SUPER_SAMPLING_EXPONENT){

        EnumMap<UserParameter, Integer> parametersMap =
                new EnumMap<>(UserParameter.class);


        parametersMap.put(UserParameter.OBSERVER_LONGITUDE, OBSERVER_LONGITUDE);
        parametersMap.put(UserParameter.OBSERVER_LATITUDE, OBSERVER_LATITUDE);
        parametersMap.put(UserParameter.OBSERVER_ELEVATION, OBSERVER_ELEVATION);
        parametersMap.put(UserParameter.CENTER_AZIMUTH, CENTER_AZIMUTH);
        parametersMap.put(UserParameter.HORIZONTAL_FIELD_OF_VIEW, HORIZONTAL_FIELD_OF_VIEW);
        parametersMap.put(UserParameter.MAX_DISTANCE, MAX_DISTANCE);
        parametersMap.put(UserParameter.WIDTH, WIDTH);
        parametersMap.put(UserParameter.HEIGHT, HEIGHT);
        parametersMap.put(UserParameter.SUPER_SAMPLING_EXPONENT, SUPER_SAMPLING_EXPONENT);

        return parametersMap;



    }



    public PanoramaUserParameters(int OBSERVER_LONGITUDE,int OBSERVER_LATITUDE,int OBSERVER_ELEVATION,
            int CENTER_AZIMUTH,int HORIZONTAL_FIELD_OF_VIEW,int MAX_DISTANCE,int WIDTH,int HEIGHT,int SUPER_SAMPLING_EXPONENT){
        this( createParametersMap( OBSERVER_LONGITUDE, OBSERVER_LATITUDE, OBSERVER_ELEVATION,
                CENTER_AZIMUTH, HORIZONTAL_FIELD_OF_VIEW, MAX_DISTANCE, WIDTH, HEIGHT, SUPER_SAMPLING_EXPONENT));

    }
    /**
     * @param parameter
     * @return La valeur du paramètre passé en argument.
     */
    public int get( UserParameter parameter){
        return parametersMap.get(parameter);

    }
    /**
     * @return La valeur du parametre longitude.
     */
    public int observerLongitude(){
        return parametersMap.get(UserParameter.OBSERVER_LONGITUDE);
    }
    /**
     * @return La valeur du parametre latitude.
     */
    public int observerLatitude(){
        return parametersMap.get(UserParameter.OBSERVER_LATITUDE);
    }
    /**
     * @return La valeur du parametre altitude.
     */
    public int observerElevation(){
        return parametersMap.get(UserParameter.OBSERVER_ELEVATION);
    }
    /**
     * @return La valeur du parametre azimuth central.
     */
    public int centerAzimuth(){
        return parametersMap.get(UserParameter.CENTER_AZIMUTH);

    }
    /**
     * @return La valeur du parametre angle de vue horizontal.
     */
    public int horizontalFieldOfView(){
        return parametersMap.get(UserParameter.HORIZONTAL_FIELD_OF_VIEW);
    }
    /**
     * @return La valeur du parametre visibilité.
     */
    public int maxDistance(){
        return parametersMap.get(UserParameter.MAX_DISTANCE);
    }

    public int width(){
        return parametersMap.get(UserParameter.WIDTH);
    }
    /**
     * @return La valeur du parametre largeur.
     */
    public int height(){
        return parametersMap.get(UserParameter.HEIGHT);
    }
    /**
     * @return La valeur du parametre hauteur .
     */
    public int superSamplingExponent(){
        return parametersMap.get(UserParameter.SUPER_SAMPLING_EXPONENT);
    }
    /**
     * @return La valeur du parametre degres de surechantillonnage.
     */
    public PanoramaParameters panoramaParameters(){
        

        GeoPoint position = new GeoPoint(Math.toRadians(observerLongitude()/ANGLE_COEFFICIENT),Math.toRadians(observerLatitude()/ANGLE_COEFFICIENT));
        int sampledWidth = (int)Math.pow(2, superSamplingExponent())*width();
        int sampledHeight=(int)Math.pow(2, superSamplingExponent())*height();
        return new PanoramaParameters(position,observerElevation(),Azimuth.canonicalize(Math.toRadians(centerAzimuth())),Math.toRadians(horizontalFieldOfView()),maxDistance()*MAXD_COEFFICIENT,sampledWidth,sampledHeight);


    }

    /**
     * @return Les parametres du panorama tel qu'il sera affiché.
     */
    public PanoramaParameters panoramaDisplayParameters(){
        GeoPoint position = new GeoPoint(Math.toRadians(observerLongitude()/ANGLE_COEFFICIENT),Math.toRadians(observerLatitude()/ANGLE_COEFFICIENT));
        return new PanoramaParameters(position,observerElevation(),
                Azimuth.canonicalize(Math.toRadians(centerAzimuth())),Math.toRadians(horizontalFieldOfView()),maxDistance()*MAXD_COEFFICIENT,width(),height());

    }

    @Override
    public boolean equals(Object that){
        return(that instanceof PanoramaUserParameters)&& parametersMap.equals(((PanoramaUserParameters)that).parametersMap);
        


    }

    @Override
    public int hashCode(){
        return Objects.hash(this.parametersMap);
    }



}
