package ch.epfl.alpano;

import java.util.Objects;
/**
 * 
 * @author Bayrem Kaabachi (261340), Emine Ghariani (262850)
 *
 */
public final class PanoramaParameters {

    private final GeoPoint observerPosition;
    private final int observerElevation;
    private final double centerAzimuth;
    private final double horizontalFieldOfView;
    private final int maxDistance;
    private final int width;
    private final int height;
    private final double delta;
    private double verticalFieldOfView;

    /**
     * 
     * @param observerPosition 
     * la position de l'observateur 
     * @param observerElevation 
     * l'altitude de l'observateur
     * @param centerAzimuth 
     * l'azimuth du centre du panorama
     * @param horizontalFieldOfView 
     * l'angle de vue horizontal
     * @param maxDistance 
     * distance maximale de visibilite
     * @param width largeur du panorama
     * @param height hauteur du panorama
     * @throws NullPointerException 
     * si la position de l'observateur est nulle
     * @throws IllegalArgumentException 
     * si l'azimut central n'est pas canonique, si l'angle de vue horizontal n'est pas compris entre 0 (exclu) et 2π
        (inclu) ou si la largeur, hauteur et distance maximales ne sont pas strictement positives
     */
    public PanoramaParameters(GeoPoint observerPosition, int observerElevation, double centerAzimuth, double horizontalFieldOfView, int maxDistance, int width, int height){

        Preconditions.checkArgument( (Azimuth.isCanonical(centerAzimuth))&&
                (horizontalFieldOfView>0 && horizontalFieldOfView<=Math2.PI2)&&(maxDistance>0 && width>0 && height>0) );


        this.observerPosition=Objects.requireNonNull(observerPosition);
        this.observerElevation=observerElevation;
        this.centerAzimuth=centerAzimuth;
        this.horizontalFieldOfView=horizontalFieldOfView;
        this.maxDistance=maxDistance;
        this.width=width;
        this.height=height;
        this.delta= this.horizontalFieldOfView/(this.width()-1);
        this.verticalFieldOfView = delta*(height()-1);
    }
    /**
     * Retourne la position de l'observateur
     * @return la position de l'observateur
     */
    public GeoPoint observerPosition() {
        return observerPosition;
    }
    /**
     * Retourne l'altitude de l'observateur
     * @return l'altitude de l'observateur
     */
    public int observerElevation() {
        return observerElevation;
    }
    /**
     * Retourne l'azimut du centre du panorama
     * @return l'azimut du centre du panorama
     */
    public double centerAzimuth() {
        return centerAzimuth;
    }
    /**
     * Retourne l'angle de vue horizontal
     * @return l'angle de vue horizontal
     */
    public double horizontalFieldOfView() {
        return horizontalFieldOfView;
    }
    /**
     * Retourne la distance maximale de visibilité
     * @return la distance maximale de visibilité
     */
    public int maxDistance() {
        return maxDistance;
    }
    /**
     * Retourne la largeur du panorama
     * @return la largeur du panorama
     */
    public int width() {
        return width;
    }
    /**
     * Retourne la hauteur du panorama
     * @return la hauteur du panorama
     */
    public int height() {
        return height;
    }
    /**
     * Retourne l'angle de vue vertical
     * @return l'angle de vue vertical
     */
    public double verticalFieldOfView(){
        return verticalFieldOfView;
    }
    /**
     * Retourne l'azimut canonique correspondant à l'index de pixel horizontal x
     * @param x Index du pixel horizontal
     * @throws IllegalArgumentException l'index du pixel horizontal est inférieur à zéro, ou supérieur à la largeur moins un
     * @return l'azimut canonique correspondant à l'index de pixel horizontal x
     */
    public double azimuthForX(double x){
        Preconditions.checkArgument(x>=0 && x<=this.width()-1);


        double originAzimuth= this.centerAzimuth()-delta*(this.width()-1)/2.0 ;
        return Azimuth.canonicalize(originAzimuth+ x*delta);
    }   


    /**
     * Retourne l'index de pixel horizontal correspondant à l'azimut donné
     * @param a Azimuth
     * @throws IllegalArgumentException si l'azimut n'appartient pas à la zone visible
     * @return l'azimut canonique correspondant à l'index de pixel horizontal x
     */
    public double xForAzimuth(double a){
        Preconditions.checkArgument(Math.abs(Math2.angularDistance(a, centerAzimuth))<= horizontalFieldOfView/2d);
        double angle = Math2.angularDistance(this.azimuthForX(0), a);
        if (angle < 0)
          angle += Math2.PI2;
        return angle / delta;
    }
    /**
     * Retourne l'élévation correspondant à l'index de pixel vertical y
     * @param y index de pixel vertical
     * @throws IllegalArgumentException si l'index de pixel vertical est inférieur à zéro, ou supérieur à la hauteur moins un
     * @return l'élévation correspondant à l'index de pixel vertical y
     */
    public double altitudeForY(double y){
        Preconditions.checkArgument(y>= 0 && y<= this.height()-1);


        return (-y+(this.height()-1)/2.0)* delta ;



    }

    /**
     * Retourne l'index de pixel vertical correspondant à l'élévation donnée
     * @param a elevation
     * @throws IllegalArgumentException si l'elevation n'appartient pas à la zone visible.
     * @return  l'index de pixel vertical correspondant à l'élévation donnée
     */
    public double   yForAltitude(double a){

        Preconditions.checkArgument(Math.abs(a)<=verticalFieldOfView/2);
        double angle = Math2.angularDistance(a, altitudeForY(0));
        if(angle<0)
            angle=angle+Math2.PI2;

        return angle/delta ;

    }
    /**
     * qui retourne vrai ssi l'index passé est un index de pixel valide
     * @param x Index du pixel horizontal
     * @param y Index du pixel vertical
     * @return vrai ssi l'index passé est un index de pixel valide
     */
    boolean isValidSampleIndex(int x, int y){
        return (x>=0) && (x<= width()- 1) && (y>=0) && (y<=height()-1);
    }
    /**
     * Retourne l'index linéaire du pixel d'index donné
     * @param x Index du pixel horizontal
     * @param y Index du pixel vertical
     * @return  l'index linéaire du pixel d'index donné
     */
    int linearSampleIndex(int x, int y){
        assert(isValidSampleIndex(x,y));
        return x+y*this.width();
    }















}
