package ch.epfl.alpano;

import java.util.Objects;

/**
 * 
 * @author Bayrem Kaabachi (261340) Emine Ghariani (262850)
 *
 */
public final class Interval1D {
    private final int includedFrom,includedTo;

    /**
     * construit l'intervalle d'entiers allant de includedFrom à includedTo
     * @param includedFrom entier borne inferieure de l'intervalle
     * @param includedTo entoer borne superieure de l'interval
     * @throws IllegalArgumentException si includedTo est strictement inférieur à includedFrom.
     */
    public Interval1D(int includedFrom, int includedTo){
        Preconditions.checkArgument(includedTo>=includedFrom);
        this.includedFrom=includedFrom;
        this.includedTo=includedTo;
    }
    /**
     * Retourne le plus petit element de l'intervalle   
     * @return le plus petit element de l'intervalle
     */

    public int includedFrom(){
        return includedFrom;
    }
    /**
     * Retourne le plus grand element de l'intervalle
     * @return le plus grand element de l'intervalle
     */
    public int includedTo(){
        return includedTo ;
    }
    /**
     * Retourne vrai ssi v appartient a l'intervalle
     * @param entier v  
     * @return vrai ssi v appartient a l'intervalle
     */
    public boolean contains(int v ){
        return ( (v >= includedFrom()) & (v <= includedTo() ));

    }
    /**
     * Retourne la taille de l'intervalle
     * @return la taille de l'intervalle
     */
    public int size(){
        return includedTo()-includedFrom()+1;
    }
    /**
     * Retourne la taille de l'intersection du récepteur this et de l'argument that
     * @param Interva1D that
     * @return la taille de l'intersection du récepteur this et de l'argument that
     */
    public int sizeOfIntersectionWith(Interval1D that){

        int upperBoundOfIntersection = Math.min(includedTo(), that.includedTo());
        int lowerBoundOfIntersection = Math.max(includedFrom(), that.includedFrom());

        return Math.max(0,upperBoundOfIntersection-lowerBoundOfIntersection +1);

    }
    /**
     * Retourne l'union englobante du récepteur this et de l'argument that
     * @param that Interval1D that
     * @return l'union englobante du récepteur this et de l'argument that
     */
    public Interval1D boundingUnion(Interval1D that){
        int minFrom = Math.min(includedFrom(), that.includedFrom());
        int maxTo = Math.max(includedTo(), that.includedTo());


        return new Interval1D(minFrom,maxTo) ;
    }
    /**
     * Retourne vrai ssi le récepteur this et l'argument that sont unionables
     * @param that Interval1D that
     * @return vrai ssi le récepteur this et l'argument that sont unionables
     */
    public boolean isUnionableWith(Interval1D that){
        return this.size()+that.size()-sizeOfIntersectionWith(that) == boundingUnion(that).size();

    }
    /**
     * Retourne l'union du récepteur this et de l'argument that
     * @param that Interval1D that
     * @throws IllegalArgumentException  si le recepteur et l'argument ne sont pas unionable
     * @return l'union du récepteur this et de l'argument that
     */
    public Interval1D union(Interval1D that){
        Preconditions.checkArgument(isUnionableWith(that));
        return boundingUnion(that);
    }

    @Override
    public boolean equals(Object thatO){
        if(thatO instanceof Interval1D) 
            return ((includedFrom == ((Interval1D) thatO).includedFrom()) &&(includedTo==((Interval1D) thatO).includedTo()));

        else 
            return false ;

    }

    @Override
    public int hashCode() {
        return Objects.hash(includedFrom(), includedTo());
    }

    @Override
    public String toString(){
        return  "["+includedFrom+".."+includedTo+"]" ;

    }



}
