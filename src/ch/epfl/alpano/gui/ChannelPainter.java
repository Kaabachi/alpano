package ch.epfl.alpano.gui;

import java.util.function.DoubleUnaryOperator;

import ch.epfl.alpano.Panorama;
/**
 * 
 * @author Bayrem Kaabachi (261340), Emine Ghariani (262850)
 *
 */
@FunctionalInterface
public interface ChannelPainter {
    /**
     * prend en arguments les coordonnées entières x et y d'un point et retourne la valeur du canal en ce point, un nombre à virgule flottante de type float.
     * @param x int coordonne x
     * @param y int coordonne y
     * @return retourne la valeur du canal en x et y , un nombre à virgule flottante de type float.
     */

    public float valueAt(int x,int y);
    
    /**
     * étant donné un panorama, retourne un peintre de canal dont la valeur pour un point est la différence de distance entre le plus lointain des voisins et le point en question
     * @param p Panorama
     * @return un peintre de canal dont la valeur pour un point est la différence de distance entre le plus lointain des voisins et le point en question
     */
    public static ChannelPainter maxDistanceToNeighbors(Panorama p){


        ChannelPainter c =(x,y) -> {return Math.max(Math.max(p.distanceAt(x+1, y, 0), p.distanceAt(x, y+1, 0)),
                Math.max(p.distanceAt(x-1, y,0), p.distanceAt(x, y-1,0)))-p.distanceAt(x, y, 0);

       
        }; 

        return c;
    }
/**
 * permet d'ajouter la valeur produite par le peintre a la constante donnee en argument
 * @param number float: constatnte a ajouter
 * @return la valeur produite par le peintre ajoute a la constante donnee en argument
 */
    default ChannelPainter add(float number){
        ChannelPainter c = (x,y) -> valueAt(x,y)+number;
        return c;

    }
    /**
     * permet de soustraire la valeur produite par le peintre a la constante donnee en argument
     * @param number float: constatnte a soustraite
     * @return la valeur produite par le peintre soustrait de la constatnte
     */
    default ChannelPainter sub(float number){
        ChannelPainter c = (x,y) -> valueAt(x,y)-number;
        return c;

    }
    /**
     * permet de multiplier la valeur produite par le peintre a la constante donnee en argument
     * @param number float: constatnte a multiplier
     * @return la valeur produite par le peintre multiplie a la constante donnee en argument
     */
    default ChannelPainter mul(float number){
        ChannelPainter c = (x,y) -> valueAt(x,y)*number;
        return c;

    }
    /**
     * permet de diviser la valeur produite par le peintre sur la constante donnee en argument
     * @param number float: constatnte diviseur
     * @return la valeur produite par le peintre divise par la constante donnee en argument
     */
    default ChannelPainter div(float number){
        ChannelPainter c = (x,y) -> valueAt(x,y)/number;
        return c;

    }
    /**
     * permet d'appliquer à la valeur produite par le peintre un opérateur unaire (de type DoubleUnaryOperator) passé en argument à la méthode,
    @param u DoubleUnaryOperator
     * @return la valeur produite par le peintre appiquee a l'operateur unaire
     */
    default ChannelPainter map(DoubleUnaryOperator u){
        ChannelPainter c = (x,y) -> (float)u.applyAsDouble(valueAt(x,y));
        return c;

    }
    /**
     * permet d'appliquer à la valeur produite par le peintre un opérateur unaire (de type DoubleUnaryOperator) passé en argument à la méthode,

     * @return la valeur produite par le peintre a la constante donnee en argument
     */
    default ChannelPainter inverted(){
        ChannelPainter c = (x,y) -> (1-valueAt(x,y));
        return c;

    }

    /**
     * 
     * @return un peintre ayant comme résultat la valeur de cette fonction
     */
    default ChannelPainter clamped(){
        ChannelPainter c = (x,y) -> Math.max(0,Math.min(1,valueAt(x,y)));
        return c;

    }

    /**
     * 
     * @return un peintre ayant comme résultat la valeur de cette fonction
     */
    default ChannelPainter cycling(){
        ChannelPainter c = (x,y) -> valueAt(x,y)%1;
        return c;

    }

    /**
     * 
     * @return un peintre ayant comme résultat la valeur de cette fonction
     */
    default ChannelPainter renderZero(){
        return (x,y)-> 0;
    }

    /**
     * 
     * @return un peintre ayant comme résultat la valeur de cette fonction
     */
    default ChannelPainter renderOne(){
        return (x,y)-> 1;
    }








}