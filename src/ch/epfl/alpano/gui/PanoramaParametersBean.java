package ch.epfl.alpano.gui;


import java.util.EnumMap;

import java.util.Map;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
/**
 * 
 * @author Bayrem Kaabachi (261340), Emine Ghariani (262850)
 *
 */
public final class PanoramaParametersBean{

    private final ObjectProperty<PanoramaUserParameters> parametersProperty;
    private final Map<UserParameter,ObjectProperty<Integer>> map ;

    /**
     * Construit un bean de parametres a partir des parametres utilisateur
     * parametre.
     * 
     * @param parametre
     */
    public PanoramaParametersBean(PanoramaUserParameters param) {
        this.parametersProperty=new SimpleObjectProperty<>(param);
        map = new EnumMap<>(UserParameter.class) ;
    
        map.put(UserParameter.OBSERVER_ELEVATION, new SimpleObjectProperty<>(param.observerElevation()));
        map.put(UserParameter.OBSERVER_LONGITUDE, new SimpleObjectProperty<>(param.observerLongitude()));
        map.put(UserParameter.OBSERVER_LATITUDE, new SimpleObjectProperty<>(param.observerLatitude()));
        map.put(UserParameter.CENTER_AZIMUTH, new SimpleObjectProperty<>(param.centerAzimuth()));
        map.put(UserParameter.HORIZONTAL_FIELD_OF_VIEW, new SimpleObjectProperty<>(param.horizontalFieldOfView()));
        map.put(UserParameter.MAX_DISTANCE, new SimpleObjectProperty<>(param.maxDistance()));
        map.put(UserParameter.WIDTH, new SimpleObjectProperty<>(param.width()));
        map.put(UserParameter.HEIGHT, new SimpleObjectProperty<>(param.height()));
        map.put(UserParameter.SUPER_SAMPLING_EXPONENT, new SimpleObjectProperty<>(param.superSamplingExponent())); 
        map.forEach((k,v)-> v.addListener((b,o,n)-> Platform.runLater(this::synchronizeParameters)));
        


        
    }
    

    /**
     * @return la proprieté parametres.
     */ 
    public ReadOnlyObjectProperty<PanoramaUserParameters> parametersProperty(){
        return parametersProperty;
    }
    /**
     * @return la proprieté longitude.
     */
    public ObjectProperty<Integer> observerLongitudeProperty(){
        return map.get(UserParameter.OBSERVER_LONGITUDE);
        
    }
    /**
     * @return la proprieté latitude.
     */
    public ObjectProperty<Integer> observerLatitudeProperty(){
        return map.get(UserParameter.OBSERVER_LATITUDE);        
    }
    /**
     * @return la proprieté altitude .
     */
    public ObjectProperty<Integer> observerElevationProperty(){
        return map.get(UserParameter.OBSERVER_ELEVATION);  
    }
    /**
     * @return la proprieté azimuth central.
     */
    public ObjectProperty<Integer> centerAzimuthProperty(){
        return map.get(UserParameter.CENTER_AZIMUTH);    
        
    }
    /**
     * @return la proprieté angle de vue horizontal.
     */
    public ObjectProperty<Integer> horizontalFieldOfViewProperty(){
        return map.get(UserParameter.HORIZONTAL_FIELD_OF_VIEW);    
        
    }
    /**
     * @return la proprieté visibilité.
     */
    public ObjectProperty<Integer> maxDistanceProperty(){
        return map.get(UserParameter.MAX_DISTANCE);    
        
    }
    /**
     * @return la proprieté largeur.
     */
    public ObjectProperty<Integer> widthProperty(){
        return map.get(UserParameter.WIDTH);    
        
    }
    /**
     * @return la proprieté hauteur.
     */
    public ObjectProperty<Integer> heightProperty(){
        return map.get(UserParameter.HEIGHT);    
        
    }
    /**
     * @return la proprieté degres de surechantillonnage.
     */
    public ObjectProperty<Integer> superSamplingExponentProperty(){
        return map.get(UserParameter.SUPER_SAMPLING_EXPONENT);    
        
    }
    
    private void synchronizeParameters(){
      
        PanoramaUserParameters param = new PanoramaUserParameters(observerLongitudeProperty().get(),observerLatitudeProperty().get(),
                observerElevationProperty().get(),centerAzimuthProperty().get(),horizontalFieldOfViewProperty().get(),maxDistanceProperty().get(),
                widthProperty().get(),heightProperty().get(),superSamplingExponentProperty().get());
        
        
        parametersProperty.set(param);;
        observerLongitudeProperty().set(parametersProperty.get().observerLongitude());
        observerLatitudeProperty().set(parametersProperty.get().observerLatitude());
        observerElevationProperty().set(parametersProperty.get().observerElevation());
        centerAzimuthProperty().set(parametersProperty.get().centerAzimuth());
        horizontalFieldOfViewProperty().set(parametersProperty.get().horizontalFieldOfView());
        maxDistanceProperty().set(parametersProperty.get().maxDistance());
        widthProperty().set(parametersProperty.get().width());
        heightProperty().set(parametersProperty.get().height());
        superSamplingExponentProperty().set(parametersProperty.get().superSamplingExponent());
        
    }

}
