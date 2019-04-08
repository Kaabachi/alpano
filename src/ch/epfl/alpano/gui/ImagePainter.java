package ch.epfl.alpano.gui;

import javafx.scene.paint.Color;
/**
 * 
 * @author Bayrem Kaabachi (261340), Emine Ghariani (262850)
 *
 */
@FunctionalInterface
public interface ImagePainter {
    /**
     * prend en arguments les coordonnées x et y d'un point et retourne la couleur de l'image en ce point.


     * @param x int coordonee x
     * @param y int coordonnee y
     * @return la couleur de l'image en ce point.
     */
    public Color colorAt(int x,int y);
    
    /**
     * prend en arguments 4 peintres de canaux correspondant à la teinte, la saturation, la luminosité et l'opacité, et retournant le peintre d'image correspondant,
     * @param teinte ChannelPainter tinte
     * @param saturation ChannelPainter saturation
     * @param luminosite ChannelPainter luminosite
     * @param opacite ChannelPainter opacite
     * @return le peintre d'image correspondant au arguments
     */
    public static ImagePainter hsb(ChannelPainter teinte,ChannelPainter saturation, ChannelPainter luminosite, ChannelPainter opacite){
       
        return (x,y)-> Color.hsb(teinte.valueAt(x, y), saturation.valueAt(x, y), luminosite.valueAt(x, y), opacite.valueAt(x, y));

       
    }
    /**
     *une méthode nommée p.ex. gray, prenant en arguments 2 peintres de canaux correspondant à la teinte de gris et à l'opacité, et retournant le peintre d'image correspondant.
     * @param teinte ChannelPainter teinte
     * @param opacite CannelPainter opacite
     * @return le peintre d'image correspondant au argument 
     */
    public static ImagePainter gray(ChannelPainter teinte, ChannelPainter opacite){
        return (x,y) -> {
            return Color.gray(teinte.valueAt(x, y), opacite.valueAt(x, y));
        }
    ;
    }
    

}
