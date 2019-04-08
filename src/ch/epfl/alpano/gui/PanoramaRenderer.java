package ch.epfl.alpano.gui;

import ch.epfl.alpano.Panorama;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
/**
 * 
 * @author Bayrem Kaabachi (261340), Emine Ghariani (262850)
 *
 */
public interface PanoramaRenderer {

    /**
     * permet d'obtenir l'image d'un panorama étant donnés ce panorama et un peintre d'image.
     * @param panorama Panorama 
     * @param painter ImagePainter
     * @return l'image d'un panorama étant donnés ce panorama et un peintre d'image.
     */
    public static Image renderPanorama(Panorama panorama,ImagePainter painter){
        int width = panorama.parameters().width();
        int height = panorama.parameters().height();
        WritableImage image=new WritableImage(width,height);
        for(int x=0;x<width;x++){
            for(int y=0;y<height;y++){
                image.getPixelWriter().setColor(x, y, painter.colorAt(x, y));
                
            }
        }
        return image;
        
        
    }
}
