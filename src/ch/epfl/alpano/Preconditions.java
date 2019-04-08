package ch.epfl.alpano;
/**
 * 
 * @author Bayrem Kaabachi (261340), Emine Ghariani (262850)
 *
 */

public interface Preconditions {

    /**
     * lève l'exception IllegalArgumentException, sans message attaché, si l'argument b est faux, et ne fait rien sinon
     * @param b argument a verifier
     * @throws IllegalArgumentException si l'argument b est faux
     */
    public static void checkArgument (boolean b ){
        if(!b)
            throw new IllegalArgumentException();

    }

    /**
     * lève l'exception IllegalArgumentException avec le message donné attaché si l'argument b est faux, et ne fait rien sinon.
     * @param b argument a verifier
     * @param message Message a afficher si l'argument est faux
     * @throws IllegalArgumentException si l'argument b est faux
     */
    public static void checkArgument (boolean b, String message ){
        if(!b)
            throw new IllegalArgumentException("message");
    }

}
