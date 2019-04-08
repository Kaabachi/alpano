package ch.epfl.alpano.gui;


import java.util.List;

import ch.epfl.alpano.Panorama;
import ch.epfl.alpano.PanoramaComputer;

import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.summit.Summit;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.image.Image;
/**
 * 
 * @author Bayrem Kaabachi (261340), Emine Ghariani (262850)
 *
 */
public final class PanoramaComputerBean {
    private final ObjectProperty<Panorama> panoramaProperty;
    private final ObjectProperty<Image> imageProperty;
    private final ObjectProperty<PanoramaUserParameters> parametersProperty;
    private final ObservableList<Node> labels;
    private final ObservableList<Node> unmodifiableLabels;
    private final static int n_100000 = 100000;
    private final static int n_200000 = 200000;


    public PanoramaComputerBean(ContinuousElevationModel cem, List<Summit> summits ){
        Labelizer labelizer= new Labelizer(cem,summits);

        parametersProperty= new SimpleObjectProperty<>();
        imageProperty=new SimpleObjectProperty<>();
        labels = FXCollections.observableArrayList();
        //unmodifiableLabels a été crée afin d'éviter l'interaction avec le garbage collector et les références faibles de JavaFX
        unmodifiableLabels=  FXCollections.unmodifiableObservableList(labels);
        panoramaProperty=new SimpleObjectProperty<>();


        this.parametersProperty.addListener((b,o,n)-> {  
            labels.setAll(labelizer.labels(getParameters().panoramaDisplayParameters()));
            
            Panorama panoramaJdid=new PanoramaComputer(cem)
                    .computePanorama(getParameters().panoramaParameters());  
            ImagePainter l = createPainter(panoramaJdid);
            this.imageProperty.set(PanoramaRenderer.renderPanorama(panoramaJdid, l));
            panoramaProperty.set(panoramaJdid);
        });

    }



    public ObjectProperty<PanoramaUserParameters> parametersProperty(){
        return parametersProperty;
    }

    public PanoramaUserParameters getParameters(){
        return parametersProperty().get();

    }
    public void setParameters(PanoramaUserParameters newParameters){
        parametersProperty().set(newParameters);

    }
    public ReadOnlyObjectProperty<Panorama> panoramaProperty(){
        return panoramaProperty;

    }
    public Panorama getPanorama(){
        return panoramaProperty().get();
    }
    public ReadOnlyObjectProperty<Image> imageProperty(){
        return imageProperty;

    }
    public Image getImage(){
        return imageProperty().get();

    }



    public ObservableList<Node> getLabels(){

        return unmodifiableLabels;

    }


    private ImagePainter createPainter(Panorama panorama){
        ChannelPainter distance = panorama::distanceAt;
        ChannelPainter pente = panorama::slopeAt;
        ChannelPainter h =
                distance
                .div(n_100000)
                .cycling()
                .mul(360);
        ChannelPainter s =
                distance
                .div(n_200000)
                .clamped()
                .inverted();
        ChannelPainter b =
                pente
                .mul(2)
                .div((float)Math.PI)
                .inverted()
                .mul((float)0.7)
                .add((float)0.3);
        ChannelPainter opacity =
                distance.map(d -> d == Float.POSITIVE_INFINITY ? 0 : 1);

        return ImagePainter.hsb(h, s,b,opacity);

    }

}
