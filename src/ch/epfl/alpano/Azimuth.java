package ch.epfl.alpano;
/**
 * 
 * @author Bayrem Kaabachi (261340) Emine Ghariani (262850)
 *
 */
/*
 * Interface permettant de faire des opérations sur un Azimuth
 */
public interface Azimuth  {

    /**
     * retourne vrai ssi son argument est un azimut « canonique »
     * @param azimuth Azimuth a verifier
     * @return  vrai ssi son argument est un azimut « canonique »
     */
    public static boolean isCanonical(double azimuth){



        return (azimuth>=0)&&(azimuth<Math2.PI2);

    }

    /**
     * retourne l'azimut canonique équivalent à celui passé en argument, c-à-d compris dans l'intervalle [0;2π[
     * @param azimuth Azimuth a transformer
     * @return l'azimut canonique équivalent à celui passé en argument, c-à-d compris dans l'intervalle [0;2π[
     */
    public static double canonicalize(double azimuth){
        return Math2.floorMod(azimuth, Math2.PI2);

    }

    /**
     * transforme un azimut en angle mathématique, c-à-d exprimé dans le sens antihoraire
     * @param azimuth Azimuth a transformer
     * @return  un azimut en angle mathématique, c-à-d exprimé dans le sens antihoraire
     * @throws IllegalArgumentException si son argument n'est pas un azimut canonique
     */
    public static double toMath(double azimuth)throws IllegalArgumentException{
        Preconditions.checkArgument(isCanonical(azimuth));
        return canonicalize(Math2.PI2-azimuth);

    }

    /**
     * transforme un angle mathématique en azimut, c-à-d exprimé dans le sens horaire,
     * @param angle Angle a transformer 
     * @return un angle mathématique en azimut, c-à-d exprimé dans le sens horaire,
     * @throws IllegalArgumentException   si l'argument n'est pas compris dans l'intervalle [0;2π[
     */
    public static double fromMath(double angle)throws IllegalArgumentException{
        Preconditions.checkArgument(isCanonical(angle));
        return canonicalize(Math2.PI2-angle);



    }

    /**
     * retourne une chaîne correspondant à l'octant dans lequel se trouve l'azimut donné, formée en combinant les chaînes n, e, s et w correspondant aux quatre points cardinaux (resp. nord, est, sud et ouest)
     * @param azimuth Azimuth contenu dans l'octant
     * @param n chaine correspondant au nord
     * @param e chaine correspondant a l'est
     * @param s chaine correspondant au sud
     * @param w chaine correspondant a l'ouest
     * @return une chaîne correspondant à l'octant dans lequel se trouve l'azimut donné
     * @throws IllegalArgumentException si l'azimut donnée n'est pas canonique
     */
    public static String toOctantString(double azimuth, String n, String e, String s, String w)throws IllegalArgumentException{
        Preconditions.checkArgument(isCanonical(azimuth));


        if(( Math.PI/8 >= azimuth)||(azimuth > 15*Math.PI/8))
            return n;

        if(( Math.PI/8 <= azimuth)&&(azimuth < 3*Math.PI/8))
            return n+e;

        if(( 3*Math.PI/8 <= azimuth)&&(azimuth < 5*Math.PI/8))
            return e;

        if(( 5*Math.PI/8 <= azimuth)&&(azimuth < 7*Math.PI/8))
            return s+e;

        if(( 7*Math.PI/8 <= azimuth)&&(azimuth < 9*Math.PI/8))
            return s;

        if(( 9*Math.PI/8 <= azimuth)&&(azimuth < 11*Math.PI/8))
            return s+w;

        if(( 11*Math.PI/8 <= azimuth)&&(azimuth < 13*Math.PI/8))
            return w;

        if(( 13*Math.PI/8 <= azimuth)&&(azimuth < 15*Math.PI/8))
            return n+w;


        throw new Error();


    }

}


