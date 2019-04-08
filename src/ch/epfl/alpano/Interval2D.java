package ch.epfl.alpano;

import java.util.Objects;

/**
 * 
 * @author Bayrem Kaabachi (261340) Emine Ghariani (262850)
 *
 */
public final class Interval2D {

    private final Interval1D iX , iY ;

    /**
     * construit le produit cartésien des intervalles iX et iY
     * @param iX intervalle 1D
     * @param iY intervalle 1D
     * @throws NullPointerException si l'un ou l'autre de ces intervalles est null
     */
    public Interval2D(Interval1D iX, Interval1D iY) {
        this.iX=Objects.requireNonNull(iX);;
        this.iY=Objects.requireNonNull(iY);



    }
    /**
     * retourne le premier intervalle du produit cartésien
     * @return le premier intervalle du produit cartésien
     */
    public Interval1D iX(){
        return iX;
    }
    /**
     * retourne le second intervalle du produit cartésien
     * @returnle second intervalle du produit cartésien
     */
    public Interval1D iY(){
        return iY;
    }
    /**
     * retourne vrai ssi l'intervalle contient la paire (x, y)
     * @param x l'entier x
     * @param y l'entier y
     * @return vrai ssi l'intervalle contient la paire (x, y)
     */
    public boolean contains(int x, int y ){
        return (iX.contains(x) && iY.contains(y) );

    }
    /**
     * Retourne la taille de l'intervalle
     * @return La taille de l'intervalle
     */
    public int size(){
        return iX.size()*iY.size();
    }
    /**
     * retourne la taille de l'intersection du récepteur this avec l'argument that
     * @param that Interval2D that 
     * @return la taille de l'intersection du récepteur this avec l'argument that
     */
    public int sizeOfIntersectionWith(Interval2D that){
        return iX.sizeOfIntersectionWith(that.iX())*iY.sizeOfIntersectionWith(that.iY());
    }
    /**
     * Retourne l'union englobante du récepteur this et de l'argument that
     * @param that Interval2D that
     * @return l'union englobante du récepteur this et de l'argument that
     */
    public Interval2D boundingUnion (Interval2D that){
        return new Interval2D(iX.boundingUnion(that.iX()),iY.boundingUnion(that.iY()));
    }

    /**
     * retourne vrai ssi le récepteur this et l'argument that sont unionables
     * @param that Inerval2D that
     * @return vrai ssi le récepteur this et l'argument that sont unionables
     */
    public boolean isUnionableWith(Interval2D that){
        return (this.size()+that.size()-this.sizeOfIntersectionWith(that)== this.boundingUnion(that).size());
    }
    /**
     * Retourne l'union du récepteur et de that
     * @param that Interval2D
     * @throws IllegalArgumentException si le recepteur et l'argument ne sont pas unionable
     * @return
     */
    public Interval2D union(Interval2D that){
        Preconditions.checkArgument(isUnionableWith(that));
        return boundingUnion(that);
    }

    @Override
    public boolean equals(Object that0){
        if(that0 instanceof Interval2D){
            return ((Interval2D)that0).iX() .equals(this.iX() ) && ((Interval2D)that0).iY() .equals(this.iY() ) ; 
        }
        else
            return false;

    }

    @Override
    public int hashCode(){
        return Objects.hash(iX,iY);
    }

    @Override
    public String toString(){
        return iX.toString()+"x"+iY.toString();
    }


}
