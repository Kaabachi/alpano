package ch.epfl.alpano.summit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.alpano.GeoPoint;

/**
 * 
 * @author Bayrem Kaabachi (261340), Emine Ghariani (262850)
 *
 */
public class GazetteerParser {


    private GazetteerParser() {}
    /**
     * Retourne un tableau dynamique non modifiable contenant les sommets lus depuis le fichier file
     * @param file fichier duquel les sommets sont lus
     * @return un tableau dynamique non modifiable contenant les sommets lus depuis le fichier file
     * @throws IOException en cas d'erreur d'entrée/sortie ou si une ligne du fichier n'obéit pas au format décrit plus haut.
     */
    public static List<Summit> readSummitsFrom(File file) throws IOException{


        ArrayList<Summit> modifiableSummit = new ArrayList<Summit>();

        try(  BufferedReader buffer = new BufferedReader(new InputStreamReader(new FileInputStream(file))) ){

            String s;

            while( buffer.read() != -1){
                s=buffer.readLine();
                if(s==null)
                    throw new IOException();
                GeoPoint p = new GeoPoint(Angle(s.substring(0,9)),Angle(s.substring(9,18)));
                modifiableSummit.add(new Summit(Sommet(s),p,Integer.parseInt(s.substring(19,24).trim())));
                
            }
        } 
        catch(StringIndexOutOfBoundsException | NumberFormatException e){
            throw new IOException();
        }
        
        List<Summit> summits =  Collections.unmodifiableList(modifiableSummit);
        return summits;
    }

    /**
     * Retourne le sommet correspondant a la ligne
     * @param line Ligne
     * @return le sommet correspondant a la ligne
     */
    private static String Sommet(String line){

        return line.substring(35);
    }
    /**
     * Retourne l'angle en radians correspondant a la chaine 
     * @param angle chaine contenant un angle exprimé en degrés, minutes et secondes
     * @return l'angle en radians correspondant a la chaine 
     */
    private static double Angle(String angle){
        String[]hms=angle.split(":");

        return Math.toRadians(Integer.parseInt(hms[0].trim()) + Integer.parseInt(hms[1].trim())/60.0 + Integer.parseInt(hms[2].trim())/3600.0);
    }
}
