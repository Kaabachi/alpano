package ch.epfl.alpano;
import java.util.Arrays;
import java.util.Objects;

/**
 * 
 * @author Bayrem Kaabachi (261340), Emine Ghariani (262850)
 *
 */
public final class Panorama {
    private final PanoramaParameters parameters;
    private final float[] distance,longitude,latitude,altitude,pente;

    /**
     * Construit un panorama
     * @param parameters Parametres du panorama
     * @param distance tableau echantillions de distances
     * @param longitude tableau echantillions de longitudes
     * @param latitude tableau echantillions de latitudes
     * @param altitude tableau echantillions d'altitude
     * @param pente tableau echantillions de pentes
     */
    private  Panorama(PanoramaParameters parameters,float[] distance,float[] longitude,float[] latitude,float[] altitude,float[] pente) {
        this.parameters=parameters;
        this.distance=distance;
        this.longitude=longitude;
        this.latitude=latitude;
        this.altitude=altitude;
        this.pente=pente;

    }

    /**
     * Vérifie si les échantillons sont valides
     * 
     * @param x échantillion x 
     * 
     * @param y échantillion y
     * 
     * @throws IndexOutOfBoundsException lorsque x et y ne sont pas des échantillons valides
     */
    private void checkIndexOutOfBounds(int x,int y ){
        if(!parameters.isValidSampleIndex(x, y)){
            throw new IndexOutOfBoundsException();
        }

    }
    /**
     * Retourne les prameteres du panorama
     * @return les parametres du panorama
     */
    public PanoramaParameters parameters(){
        return parameters;
    }
    /**
     * Retourne la distance pour le point de coordonnées données
     * @param x echantillion x
     * @param y echantillion y
     * @throws IndexOutOfBoundsException si les coordonnées du point passées sont hors des bornes du panorama.
     * @return la distance
     */
    public float distanceAt(int x, int y){
        checkIndexOutOfBounds(x,y);
        return distance[parameters.linearSampleIndex(x, y)];
    }
    /**
     * Retourne la longitude pour le point de coordonnées données
     * @param x echantillion x
     * @param y echantillion y
     * @throws IndexOutOfBoundsException si les coordonnées du point passées sont hors des bornes du panorama.
     * @return la longitude a un index donne
     */
    public float longitudeAt(int x, int y ){
        checkIndexOutOfBounds(x,y);
        return longitude[parameters.linearSampleIndex(x, y)];

    }
    /**
     * Retourne la latitude pour le point de coordonnées données
     * @param x echantillion x
     * @param y echantillion y
     * @throws IndexOutOfBoundsException si les coordonnées du point passées sont hors des bornes du panorama.
     * @return la latitude a un index donne
     */
    public float latitudeAt(int x, int y ){
        checkIndexOutOfBounds(x,y);
        return latitude[parameters.linearSampleIndex(x, y)];

    }
    /**
     * Retourne l'altitude pour le point de coordonnées données
     * @param x echantillion x
     * @param y echantillion y
     * @throws IndexOutOfBoundsException si les coordonnées du point passées sont hors des bornes du panorama.
     * @return l'altitude a un index donne
     */
    public float elevationAt(int x, int y ){
        checkIndexOutOfBounds(x,y);
        return altitude[parameters.linearSampleIndex(x, y)];

    }
    /**
     * Retourne la pente pour le point de coordonnées données     
     * @param x echantillion x
     * @param y echantillion y
     * @throws IndexOutOfBoundsException si les coordonnées du point passées sont hors des bornes du panorama.
     * @return la pente a un index donne
     */
    public float slopeAt(int x, int y ){
        checkIndexOutOfBounds(x,y);
        return pente[parameters.linearSampleIndex(x, y)];

    }
    /**
     * Retourne la distance pour le point de coordonnées données, ou la valeur par défaut d si les coordonnées sont hors des bornes du panorama.
     * @param x echantillion x
     * @param y echantillion y
     * @param d valeur par defaut d si les coordonnees sont hors des bornes du panorama
     * @return la distance pour le point de coordonnées données, ou la valeur par défaut d si les coordonnées sont hors des bornes du panorama.
     */
    public float distanceAt(int x, int y,float d ){
        if(!parameters.isValidSampleIndex(x, y))
            return d;
        return distance[parameters.linearSampleIndex(x, y)];

    }   


    public final static class  Builder{
        private final PanoramaParameters parameters;
        private float[] distance,longitude,latitude,altitude,pente;
        private boolean state=false;


        /**
         * construit un bâtisseur de panorama dont les paramètres sont ceux donnés
         * @param parameters parametres du panorama
         */
        public Builder(PanoramaParameters parameters){


            this.parameters=Objects.requireNonNull(parameters);
            int taille=parameters.height()*parameters.width();
            distance = new float[taille];
            longitude = new float[taille];
            latitude = new float[taille];
            altitude = new float[taille];
            pente = new float[taille];
            Arrays.fill(distance, Float.POSITIVE_INFINITY);





        }
        /**
         * Vérifie si les échantillons sont valides
         * 
         * @param x échantillion x 
         * 
         * @param y échantillion y
         * 
         * @throws IndexOutOfBoundsException lorsque x et y ne sont pas des échantillons valides
         */
        private void checkIndexOutOfBounds(int x,int y ){
            if(!parameters.isValidSampleIndex(x, y)){
                throw new IndexOutOfBoundsException();
            }

        }

        /**
         * Verifie si le batisseur a déja été appelé
         * 
         * @param b condition à vérifier 
         * 
         * @throws IllegalStateException lorsque la condition est vraie, cad le batisseur a deja ete appelée
         */
        private void checkState(boolean b ){
            if(b)
                throw new IllegalStateException();
        }
        /**
         * Definit la disance pour le point de coordonnées données et retounre le batisseur lui meme
         * @param x echantillion x
         * @param y echantillion y
         * @param distance Distance a definir
         * @throws IndexOutOfBoundsException si les coordonnées du point passées (l'index x, y) sont invalides
         * @return le batisseur lui meme
         */
        public Builder setDistanceAt(int x, int y, float distance){
            checkIndexOutOfBounds(x,y);
            checkState(state);
            this.distance[parameters.linearSampleIndex(x, y)]=distance;
            return this;


        }
        /**
         * Definit la longitude pour le point de coordonnées données et retounre le batisseur lui meme
         * @param x echantillion x
         * @param y echantillion y
         * @param longitude Longitude a definir
         * @throws IndexOutOfBoundsException si les coordonnées du point passées (l'index x, y) sont invalides
         * @throws IllegalStateException si la méthode build a déjà été appelée une fois sur ce bâtisseur
         * @return le batisseur lui meme
         */
        public Builder setLongitudeAt(int x, int y, float longitude){
            checkIndexOutOfBounds(x,y);
            checkState(state);
            this.longitude[parameters.linearSampleIndex(x, y)]=longitude;
            return this;

        }
        /**
         * Definit la latitude pour le point de coordonnées données et retounre le batisseur lui meme
         * @param x echantillion x
         * @param y echantillion y
         * @param latitude Latitude a definir
         * @throws IndexOutOfBoundsException si les coordonnées du point passées (l'index x, y) sont invalides
         * @throws IllegalStateException si la méthode build a déjà été appelée une fois sur ce bâtisseur
         * @return le batisseur lui meme
         */
        public Builder setLatitudeAt(int x, int y, float latitude){
            checkIndexOutOfBounds(x,y);
            checkState(state);
            this.latitude[parameters.linearSampleIndex(x, y)]=latitude;
            return this;

        }
        /**
         * Definit l'altitude pour le point de coordonnées données et retounre le batisseur lui meme
         * @param x echantillion x
         * @param y echantillion y
         * @param elevation Altitude a definir
         * @throws IndexOutOfBoundsException si les coordonnées du point passées (l'index x, y) sont invalides
         * @throws IllegalStateException si la méthode build a déjà été appelée une fois sur ce bâtisseur
         * @return le batisseur lui meme
         */
        public Builder setElevationAt(int x, int y, float elevation){
            checkIndexOutOfBounds(x,y);
            checkState(state);
            this.altitude[parameters.linearSampleIndex(x, y)]=elevation;
            return this;


        }
        /**
         * Definit la pente pour le point de coordonnées données et retounre le batisseur lui meme
         * @param x echantillion x
         * @param y echantillion y
         * @param slope Pente a definir
         * @throws IndexOutOfBoundsException si les coordonnées du point passées (l'index x, y) sont invalides
         * @throws IllegalStateException si la méthode build a déjà été appelée une fois sur ce bâtisseur
         * @return le batisseur lui meme
         */
        public Builder setSlopeAt(int x, int y, float slope){
            checkIndexOutOfBounds(x,y);
            checkState(state);
            this.pente[parameters.linearSampleIndex(x, y)]=slope;
            return this;

        }
        /**
         * Construit et retourne le panorama
         * @throws IllegalStateException si la methode a deja ete appelee une fois
         * @return le panorama
         */
        public Panorama build(){
            checkState(state);
            state=true;
            Panorama panorama = new Panorama(parameters,distance,longitude,latitude,altitude,pente);
            this.distance=null;
            this.longitude=null;
            this.latitude=null;
            this.altitude=null;
            this.pente=null;
            return panorama;
            


        }

    }



}



