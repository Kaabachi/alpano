package ch.epfl.alpano;

import java.util.Objects;
import java.util.function.DoubleUnaryOperator;

import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.ElevationProfile;
/**
 * 
 * @author Bayrem Kaabachi (261340), Emine Ghariani (262850)
 *
 */
public final class PanoramaComputer {
    private final static double k = 0.13;
    private final static int FIRST_INTERVAL_DELTA = 64;
    private final static int IMPROVE_ROOT_DELTA = 4;
    private final ContinuousElevationModel dem;
    private static final double RAY_TO_GROUND_CONSTANT = (1 - k) / (2 * Distance.EARTH_RADIUS);

    /**
     * Construit un calculateur de panorama obtenant les données du MNT continu passé en argument
     * @param dem MNT continu
     * @throws NullPointerException si le MNT continu passe en argument est nul
     */
    public PanoramaComputer(ContinuousElevationModel dem) {
        this.dem=(Objects.requireNonNull(dem));


    }
    /**
     * calcule et retourne le panorama spécifié par les paramètres
     * @param parameters parametres du panorama a calculer
     * @return le panorama spécifié par les paramètres
     */

    public Panorama computePanorama(PanoramaParameters parameters) {
        Panorama.Builder builder = new Panorama.Builder(parameters);
        ElevationProfile profile;
        GeoPoint position;
        double altitude, horizontalDistance;
    
        for (int x = 0; x < parameters.width(); ++x) {
            horizontalDistance = 0;
            profile = new ElevationProfile(dem, parameters.observerPosition(), parameters.azimuthForX(x),
                    parameters.maxDistance());
            for (int y = parameters.height() - 1; y >= 0; --y) {
                altitude = parameters.altitudeForY(y);
                DoubleUnaryOperator f = rayToGroundDistance(profile, parameters.observerElevation(), Math.tan(altitude));

                horizontalDistance = Math2.firstIntervalContainingRoot(f, horizontalDistance, parameters.maxDistance(),
                        FIRST_INTERVAL_DELTA);

                if (horizontalDistance == Float.POSITIVE_INFINITY)
                    break;
                if(horizontalDistance+FIRST_INTERVAL_DELTA<parameters.maxDistance())

                    horizontalDistance = (float) Math2.improveRoot(f, horizontalDistance, horizontalDistance + FIRST_INTERVAL_DELTA,
                            IMPROVE_ROOT_DELTA);

                builder.setDistanceAt(x, y, (float) (horizontalDistance / Math.cos(altitude)));
                position = profile.positionAt(horizontalDistance);
                builder.setLongitudeAt(x, y, (float) position.longitude())
                .setLatitudeAt(x, y, (float) position.latitude())
                .setElevationAt(x, y, (float) dem.elevationAt(position))
                .setSlopeAt(x, y, (float) dem.slopeAt(position));
            }
        }
        
        return builder.build();
    }


    /**
     * Retourne la fonction donnant la distance entre un rayon d'altitude initiale ray0 et de pente de raySlope, et le profil altimétrique profile
     * @param profile le profil altimetrique
     * @param ray0 rayon d'altitude initiale
     * @param raySlope pente
     * @return la fonction donnant la distance entre un rayon d'altitude initiale ray0 et de pente de raySlope, et le profil altimétrique profile
     */
    static public DoubleUnaryOperator rayToGroundDistance(ElevationProfile profile, double ray0, double raySlope) {
        

        return x -> ray0 + x * raySlope - profile.elevationAt(x) + Math2.sq(x) * RAY_TO_GROUND_CONSTANT;
    }




}

