package ch.epfl.alpano;

import java.util.function.DoubleUnaryOperator;
/**
 * 
 * @author Bayrem Kaabachi (261340), Emine Ghariani (262850)
 *
 */
/*
 * Interface comportant des opérations mathématiques
 */
public interface Math2 {
    public static final double PI2 = 2*Math.PI;
    /**
     * @param x variable 
     * @returns x elevee au carre
     */
    public static double sq(double x){

        return x*x;

    }

    /**
     * @returns reste de la division entiere par defaut de x par y 
     */
    public static double floorMod(double x, double y){

        return x-y*(Math.floor(x/y));

    }

    /**
     * @returns demi sinus verse de x
     */
    public static double haversin(double x){

        return sq(Math.sin(x/2));


    }

    /**
     * @returns difference angulaire entre a1 et a2
     */
    public static double angularDistance(double a1, double a2){

        return (floorMod((a2-a1 + Math.PI), PI2) ) - Math.PI;


    }

    /**
     * @returns la valeur de f(x) obtenue par interpolation linéaire 
     */
    public static double lerp(double y0, double y1, double x){

        return y0+(y1-y0)*x;
    }

    /**
     *@returns la valeur de f(x,y) obtenue par interpolation bilinéaire
     */
    public static double bilerp(double z00, double z10, double z01, double z11, double x, double y){

        double point1 = lerp(z00,z10,x);
        double point2 = lerp(z01,z11,x);
        return lerp(point1,point2,y);


    }

    /**
     *@returns la borne inférieure du premier intervalle de taille dX contenant un zéro de la fonction f et compris entre minX et maxX ; si aucun zéro n'est trouvé, retourne Double.POSITIVE_INFINITY
     */
    public static double firstIntervalContainingRoot(DoubleUnaryOperator f, double minX, double maxX, double dX) {
        Preconditions.checkArgument(minX <= maxX && 0 < dX);
        
        double borneInf = minX;
        double borneSup = minX+dX;

        while (borneSup < maxX) {
            if (f.applyAsDouble(borneInf) * f.applyAsDouble(borneSup) <= 0 && f.applyAsDouble(borneInf) + dX <= maxX)
                return borneInf;
            borneInf += dX;
            borneSup += dX;
        }
        
        if (f.applyAsDouble(borneInf) * f.applyAsDouble(maxX) <= 0 && f.applyAsDouble(borneInf) + dX <= maxX)
            return borneInf;

        return Double.POSITIVE_INFINITY;
    

    }

    /**
     * @returns retourne sa borne inférieure  d'un intervalle compris entre x1 et x2, de taille inférieure ou égale à epsilon et contenant un zéro de f
     */
    public static double improveRoot(DoubleUnaryOperator f, double x1,
            double x2, double epsilon) {
        Preconditions.checkArgument(f.applyAsDouble(x1) * f.applyAsDouble(x2) <= 0);

        double borneInf = x1;
        double borneSup = x2;


        while (borneSup - borneInf > epsilon) {
            double middle = (borneInf + borneSup) / 2;
            if (f.applyAsDouble(middle) * f.applyAsDouble(borneInf) <= 0) 
                borneSup = middle;
            else 
                borneInf = middle;

        }
        return borneInf;

    }



}
