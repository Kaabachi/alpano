package ch.epfl.alpano.gui;


import static ch.epfl.alpano.Azimuth.toOctantString;
import static java.lang.String.format;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.Locale;

import ch.epfl.alpano.dem.HgtDiscreteElevationModel;
import ch.epfl.alpano.summit.GazetteerParser;

import javafx.application.Application;
import javafx.beans.binding.Bindings;

import javafx.collections.FXCollections;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import ch.epfl.alpano.Panorama;

import ch.epfl.alpano.dem.*;
/**
 * 
 * @author Bayrem Kaabachi (261340), Emine Ghariani (262850)
 *
 */
public final class Alpano extends Application {

        public File N45E006 = new File("N45E006.hgt");
        public File N45E007 = new File("N45E007.hgt");
        public File N45E008 = new File("N45E008.hgt");
        public File N45E009 = new File("N45E009.hgt");
        public File N46E006 = new File("N46E006.hgt");
        public File N46E007 = new File("N46E007.hgt");
        public File N46E008 = new File("N46E008.hgt");
        public File N46E009 = new File("N46E009.hgt");
        public File summits = new File("alps.txt");
        public PanoramaParametersBean parametersBean = new PanoramaParametersBean(PredefinedPanoramas.ALPES_DU_JURA);
        public PanoramaComputerBean computerBean;
        /** charge un PanoramaComputerBean a partir de l'union des DEM 
         * @throws IOException 
         */
        private void loadComputer() throws IOException {
            DiscreteElevationModel hgt1 = new HgtDiscreteElevationModel(N45E006);
            DiscreteElevationModel hgt2 = new HgtDiscreteElevationModel(N45E007);
            DiscreteElevationModel hgt3 = new HgtDiscreteElevationModel(N45E008);
            DiscreteElevationModel hgt4 = new HgtDiscreteElevationModel(N45E009);
            DiscreteElevationModel hgt5 = new HgtDiscreteElevationModel(N46E006);
            DiscreteElevationModel hgt6 = new HgtDiscreteElevationModel(N46E007);
            DiscreteElevationModel hgt7 = new HgtDiscreteElevationModel(N46E008);
            DiscreteElevationModel hgt8 = new HgtDiscreteElevationModel(N46E009);

            DiscreteElevationModel hgtFinal = hgt1.union(hgt2).union(hgt3).union(hgt4).union((hgt5).union(hgt6).union(hgt7).union(hgt8));
            ContinuousElevationModel cDem = new ContinuousElevationModel(hgtFinal);


            computerBean = new PanoramaComputerBean(cDem, GazetteerParser.readSummitsFrom(summits));
        }
        
        /**
         * cree un ImageView. Ce composant se charge de l'affichage de l'image et de son éventuel redimensionnement.
         * @return
         */
        private ImageView createPanoView() {
            ImageView panoView = new ImageView(computerBean.getImage());

            panoView.fitWidthProperty().bind(parametersBean.widthProperty());
            panoView.imageProperty().bind(computerBean.imageProperty());
            panoView.setSmooth(true);
            panoView.setPreserveRatio(true);

            panoView.setOnMouseClicked(x -> {
                int exponent = parametersBean.superSamplingExponentProperty().getValue();
                int iX = (int) ( (x.getX())*Math.pow(2, exponent)); int iY = (int) ( x.getY()*Math.pow(2, exponent));
                
                Panorama panorama = computerBean.getPanorama();
                double longitude = Math.toDegrees(panorama.longitudeAt(iX,iY));
                double latitude = Math.toDegrees(panorama.latitudeAt(iX, iY));
                String qy = "mlat="+latitude+"&mlon="+longitude;  
                String fg = "map=15/"+latitude+"/"+longitude;  
                try{
                    URI osmURI =
                            new URI("http", "www.openstreetmap.org", "/", qy, fg);
                    java.awt.Desktop.getDesktop().browse(osmURI);
                }
                catch(URISyntaxException e)
                {
                    throw new Error(e);
                }
                catch(IOException e1)
                {
                    throw new Error(e1);
                }
            });
            
            
            return panoView;
        }

        /**
         * creates and returns the text area of panoView
         * @param panoView : ImageView
         * @return returns the text area field1 of panoView
         */
        private TextArea createArea(ImageView panoView) {
            TextArea field1 = new TextArea();
            panoView.setOnMouseMoved(x -> { 
                int exponent = parametersBean.superSamplingExponentProperty().getValue();


                int iX = (int) ( (x.getX())*Math.pow(2, exponent)); int iY = (int) ( x.getY()*Math.pow(2, exponent));
              



                Panorama panorama = computerBean.getPanorama();
                double longitude = Math.toDegrees(panorama.longitudeAt(iX,iY));
                double latitude = Math.toDegrees(panorama.latitudeAt(iX, iY));
                double distance = panorama.distanceAt(iX, iY);
                double altitude = panorama.elevationAt(iX, iY);
                double azimuth = parametersBean.parametersProperty().get().panoramaParameters().azimuthForX(x.getX());
                double elevation = parametersBean.parametersProperty().get().panoramaParameters().altitudeForY(x.getY());
                Locale l = null;
                StringBuilder sBuilder = new StringBuilder();

                sBuilder.append(format(l, "Position: %.4f %S %.4f %S \n", latitude,latitude >= 0 ? "°N":"°S", longitude, longitude >= 0 ? "°E":"°O" ))
                .append(format(l, "Distance : %.1f km\n", distance / 1000))
                .append(format(l, "Altitude : %.0f m\n", altitude))
                .append(format(l, "Azimuth : %.1f° (%s)\t", Math.toDegrees(azimuth), toOctantString(azimuth, "N", "E", "S", "W")))
                .append(format(l, "Elévation : %.1f°",
                        Math.toDegrees(elevation)));


                field1.setText(sBuilder.toString()  );




            }
                    );


            field1.setEditable(false);
            field1.setPrefRowCount(2);
            GridPane.setRowSpan(field1, 3);

            return field1;
        }
        /**
         * cree un paneau labelsPane de type Pane dont les fils sont Les étiquettes du panorama, produites par la classe Labelizer
         * @return Pane : labelsPane
         */
        private Pane createLabelsPane() {
            Pane labelsPane = new Pane();

            labelsPane.prefWidthProperty().bind(parametersBean.widthProperty());
            labelsPane.prefHeightProperty().bind(parametersBean.heightProperty());
            Bindings.bindContent(labelsPane.getChildren(),computerBean.getLabels());
            labelsPane.setMouseTransparent(true);

            return labelsPane;
        }

        /**
         * La notice de mise à jour est un panneau placé au-dessus de celui montrant l'image étiquetée du panorama, et qui n'est visible que lorsque les paramètres figurant en bas de l'interface ne correspondent pas à ceux de l'image affichée en haut. Ce panneau, partiellement transparent, affiche en gros caractères le texte « Les paramètres du panorama ont changé. Cliquez ici pour mettre le dessin à jour. »
         * @return StackPane : notice de mise a jour
         */
        private StackPane createUpdateNotice() {
            Text updateText = new Text("Les paramètres du panorama ont changé.\nVeuillez cliquer ici pour mettre le dessin à jour.");
            StackPane updateNotice = new StackPane(updateText);  
            updateText.setFont(new Font(40));
            updateText.setTextAlignment(TextAlignment.CENTER);
            updateNotice.setOnMouseClicked((event)->{
                computerBean.setParameters(parametersBean.parametersProperty().get());});
            updateNotice.visibleProperty().bind(computerBean.parametersProperty().isNotEqualTo(parametersBean.parametersProperty()));
           
            updateNotice.setBackground(new Background(new BackgroundFill(Color.color(1, 1, 1,0.9), CornerRadii.EMPTY, Insets.EMPTY)));


            return updateNotice;
        }


        /**
         * creates a parameters grid
         * @param paramsGrid
         * @param parametersBean
         * 
         */
        private void createParamsGrid(GridPane paramsGrid,PanoramaParametersBean parametersBean){
            TextField latitudeField = new TextField();
            Label latitudeLabel = new Label("Latitude(°)");
            latitudeField.setAlignment(Pos.CENTER_RIGHT);
            latitudeField.setPrefColumnCount(7);
            StringConverter<Integer> stringConverter_4 = new FixedPointStringConverter(4);
            TextFormatter<Integer> formatter =
                    new TextFormatter<>(stringConverter_4);
            latitudeField.setTextFormatter(formatter);

            formatter.valueProperty().bindBidirectional(parametersBean.observerLatitudeProperty());



            TextField azimuthField = new TextField();
            Label azimuthLabel = new Label("Azimut(°)");
            azimuthField.setAlignment(Pos.CENTER_RIGHT);
            azimuthField.setPrefColumnCount(3);
            StringConverter<Integer> stringConverter_0 = new FixedPointStringConverter(0);
            TextFormatter<Integer> formatterAzimuth =
                    new TextFormatter<>(stringConverter_0);
            azimuthField.setTextFormatter(formatterAzimuth);
            formatterAzimuth.valueProperty().bindBidirectional(parametersBean.centerAzimuthProperty());


            TextField widthField = new TextField();
            Label widthLabel = new Label("Largeur(px)");
            widthField.setAlignment(Pos.CENTER_RIGHT);
            widthField.setPrefColumnCount(7);
     
            TextFormatter<Integer> formatterWidth =
                    new TextFormatter<>(stringConverter_0);
            widthField.setTextFormatter(formatterWidth);
            formatterWidth.valueProperty().bindBidirectional(parametersBean.widthProperty());

            TextField longitudeField = new TextField();
            Label longitudeLabel = new Label("Longitude(°)");
            longitudeField.setAlignment(Pos.CENTER_RIGHT);
            longitudeField.setPrefColumnCount(7);
            TextFormatter<Integer> formatterLongitude =
                    new TextFormatter<>(stringConverter_4);
            longitudeField.setTextFormatter(formatterLongitude);
            formatterLongitude.valueProperty().bindBidirectional(parametersBean.observerLongitudeProperty());

            TextField fovField = new TextField();
            Label fovLabel = new Label("Angle de vue (°)");
            fovField.setAlignment(Pos.CENTER_RIGHT);
            fovField.setPrefColumnCount(3);
            TextFormatter<Integer> formatterFOV =
                    new TextFormatter<>(stringConverter_0);
            fovField.setTextFormatter(formatterFOV);
            formatterFOV.valueProperty().bindBidirectional(parametersBean.horizontalFieldOfViewProperty());


            TextField heightField = new TextField();
            Label heightLabel = new Label("Hauteur(px)");
            heightField.setAlignment(Pos.CENTER_RIGHT);
            heightField.setPrefColumnCount(4);
            TextFormatter<Integer> formatterHeight =
                    new TextFormatter<>(stringConverter_0);
            heightField.setTextFormatter(formatterHeight);
            formatterHeight.valueProperty().bindBidirectional(parametersBean.heightProperty());

            TextField altitudeField = new TextField();
            Label altitudeLabel = new Label("Altitude(m)");
            altitudeField.setAlignment(Pos.CENTER_RIGHT);
            altitudeField.setPrefColumnCount(4);
            TextFormatter<Integer> formatterAltitude =
                    new TextFormatter<>(stringConverter_0);
            altitudeField.setTextFormatter(formatterAltitude);
            formatterAltitude.valueProperty().bindBidirectional(parametersBean.observerElevationProperty());

            TextField maxDistanceField = new TextField();
            Label maxDistanceLabel = new Label("Visibilité(km)");
            maxDistanceField.setAlignment(Pos.CENTER_RIGHT);
            maxDistanceField.setPrefColumnCount(3);
            TextFormatter<Integer> formatter1 =
                    new TextFormatter<>(stringConverter_0);
            maxDistanceField.setTextFormatter(formatter1);
            formatter1.valueProperty().bindBidirectional(parametersBean.maxDistanceProperty());


            ChoiceBox<Integer> exponentBox = new ChoiceBox<>((FXCollections.observableArrayList(0, 1, 2)));

            Label exponentLabel = new Label("Suréchantillonage");
            StringConverter<Integer> stringConverterExponent = new LabeledListStringConverter("non","x2","x4");
            exponentBox.setConverter(stringConverterExponent);
            exponentBox.valueProperty().bindBidirectional(parametersBean.superSamplingExponentProperty());


            TextArea field1 = new TextArea();
            field1.setEditable(false);
            field1.setPrefRowCount(2);
            GridPane.setRowSpan(field1, 3);






            paramsGrid.setAlignment(Pos.CENTER);
            paramsGrid.setHgap(10);
            paramsGrid.setVgap(3);
            paramsGrid.setPadding(new Insets(7, 5, 5, 5));


            paramsGrid.addRow(0,latitudeLabel,latitudeField,longitudeLabel,longitudeField,altitudeLabel,altitudeField );
            paramsGrid.addRow(1,azimuthLabel,azimuthField,fovLabel,fovField,maxDistanceLabel,maxDistanceField );
            paramsGrid.addRow(2,widthLabel,widthField,heightLabel,heightField,exponentLabel,exponentBox );





        }


        
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        
        loadComputer();
        
        ImageView panoView = createPanoView();
        Pane labelsPane = createLabelsPane();
        
        StackPane panoGroup= new StackPane(panoView,labelsPane); 
        ScrollPane panoScrollPane = new ScrollPane(panoGroup);
        StackPane updateNotice = createUpdateNotice();
        StackPane panoPane = new StackPane(panoScrollPane,updateNotice);
        GridPane paramsGrid = new GridPane();
        createParamsGrid(paramsGrid,parametersBean);
        TextArea field1 = createArea(panoView);
        paramsGrid.addColumn(6, field1);
        BorderPane root = new BorderPane(panoPane,null,null,paramsGrid,null);
        Scene scene = new Scene(root);
        primaryStage.setTitle("Alpano");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

 }