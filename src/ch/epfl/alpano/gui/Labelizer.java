package ch.epfl.alpano.gui;



import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.DoubleUnaryOperator;

import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.Math2;
import ch.epfl.alpano.PanoramaComputer;
import ch.epfl.alpano.PanoramaParameters;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.ElevationProfile;

import ch.epfl.alpano.summit.Summit;
import javafx.scene.Node;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
/**
 * 
 * @author Bayrem Kaabachi (261340), Emine Ghariani (262850)
 *
 */
public final class Labelizer {
    private final ContinuousElevationModel cem;
    private final List<Summit> summits;
    private final static int LABELSHIFT = 22;
    private final static int label_bounds = 20 ;
    private final static int DELTA = 64;
    private final static int TOLERANCE = 200;
    private final static int ROTATION_ANGLE=-60;
   
    /**
     * Construit un etiqueteur de panorama.
     * 
     * @param cem
     * @param summits
     */
    public Labelizer(ContinuousElevationModel cem, List<Summit> summits) {
        this.cem = cem;
        this.summits = summits;

    }
    /**
     * @param parameters
     * @return Retourne une liste de nœuds JavaFX représentant les étiquettes à
     *         attacher au panorama correspondant au parametres mis en argument.
     */
    public List<Node> labels(PanoramaParameters parameters) {
        

        ArrayList<VisibleSummit> visibleSummits = visibleSummits(parameters);


        ArrayList<VisibleSummit> labelableSummits = labelableSummits(
                visibleSummits, parameters);

        ArrayList<Node> nodes = new ArrayList<Node>();

        if(labelableSummits.size()!=0){
            int firstLabelY = labelableSummits.get(0).y() - LABELSHIFT;
        for (VisibleSummit s : labelableSummits) {
            int summitHorizontalIndex = s.x();
            int summitVerticalIndex = s.y();
            String st = s.summit().name() + " (" + s.summit().elevation() + " m)";
            Text summitName = new Text(st);
            summitName.getTransforms().addAll(new Translate(summitHorizontalIndex, firstLabelY),new Rotate(ROTATION_ANGLE, 0, 0));
            nodes.add(summitName);
            Line summitLine = new Line(summitHorizontalIndex,
                    summitVerticalIndex, summitHorizontalIndex,
                    firstLabelY + 2);
            nodes.add(summitLine);

        }
        }
        



        return nodes;

    }
    /**
     * @param summits
     * @param parameters
     * @return une liste des sommet qui peuvent etre étiquettés
     */
    ArrayList<VisibleSummit> labelableSummits(List<VisibleSummit> summits,
            PanoramaParameters parameters) {
        BitSet labelablePos = new BitSet(parameters.width());
        labelablePos.set(label_bounds, parameters.width() - label_bounds);
        ArrayList<VisibleSummit> labelableSummits = new ArrayList<VisibleSummit>();
        for (VisibleSummit s : summits) {
            int summitHorizontalIndex = s.x();
            if (labelablePos.nextClearBit(summitHorizontalIndex) > label_bounds-1
                    + summitHorizontalIndex) {
                
                labelableSummits.add(s);
                labelablePos.clear(summitHorizontalIndex,
                        summitHorizontalIndex + label_bounds);
            }

        }
        return labelableSummits;
    }



    /**
     * 
     * @param parameters parametres
     * @return un tableau de sommets visibles trié
     */
    ArrayList<VisibleSummit> visibleSummits(PanoramaParameters parameters) {
        ArrayList<VisibleSummit> visibleSummits = new ArrayList<VisibleSummit>();
        GeoPoint originPosition = parameters.observerPosition();

        for (Summit s : summits) {

            GeoPoint summitPosition = s.position();
            double summitAzimuth = originPosition.azimuthTo(summitPosition);
            double distance = originPosition.distanceTo(summitPosition);
            if (distance <= parameters.maxDistance()) {
                ElevationProfile profile = new ElevationProfile(cem,
                        originPosition,
                        summitAzimuth, parameters.maxDistance());
                DoubleUnaryOperator f = PanoramaComputer.rayToGroundDistance(
                        profile, parameters.observerElevation(), 0);

                double summitElevation = Math.atan2(-f.applyAsDouble(distance),
                        distance);

                //le sommet est dans l'angle de vue
                boolean condition1 = (Math.abs(Math2.angularDistance(
                        summitAzimuth,
                        parameters.centerAzimuth())) <= parameters
                        .horizontalFieldOfView() / 2.0)
                        && (summitElevation >= (parameters
                                .altitudeForY(parameters.height() - 1)))
                        && (summitElevation <= parameters.altitudeForY(0));

                DoubleUnaryOperator function1 = PanoramaComputer
                        .rayToGroundDistance(profile,
                                parameters.observerElevation(),
                                Math.tan(summitElevation));
                double intersection = Math2.firstIntervalContainingRoot(
                        function1, 0, distance , DELTA);

                //l'intersection du rayon avec le sommet est dans un interval restreint
                boolean condition2 = (intersection >= distance - TOLERANCE);
               
                if (condition1 && condition2 ) {       
                    int x = (int) Math.round(parameters.xForAzimuth(summitAzimuth));
                    int y = (int) Math
                            .round(parameters.yForAltitude(summitElevation));
                    if(y>=170){
                    visibleSummits.add(new VisibleSummit(s, x, y));
                    }
                }

            }

        }
        summitSort(visibleSummits, parameters);

        return visibleSummits;
    }


    /**
     * Tri le tableau de sommet visibles
     * @param summits sommets visibles
     * @param parameters parametres
     */
    private void summitSort(List<VisibleSummit> summits,
            PanoramaParameters parameters) {
        Comparator<VisibleSummit> c = (i1, i2) -> {
            int firstElementY = i1.y();
            int secondElementY = i2.y();
            if (firstElementY != secondElementY)
                return Integer.compare(firstElementY, secondElementY);
            else
                return Integer.compare(i2.summit().elevation(), i1.summit().elevation()); 

        };

        Collections.sort(summits, c);

    }

    /**
     * 
     * @author Bayrem Kaabachi, Emine Ghariani
     * Classe représentant un sommet visible(ayant une composante x et y )
     *
     */
    private static class VisibleSummit {
       
        private final Summit s;
        private final int x;
        private final int y;

        private VisibleSummit(Summit s,
                int x, int y) {
            this.s=s;
            this.x = x;
            this.y = y;
        }

        private Summit summit(){
            return s;
        }     

        private int x() {
            return x;
        }

        private int y() {
            return y;
        }

    }

}


