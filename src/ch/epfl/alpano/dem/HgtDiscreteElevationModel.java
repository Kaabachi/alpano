package ch.epfl.alpano.dem;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel.MapMode;

import ch.epfl.alpano.Interval1D;
import ch.epfl.alpano.Interval2D;
import ch.epfl.alpano.Preconditions;
/**
 * 
 * @author Bayrem Kaabachi (261340), Emine Ghariani (262850)
 *
 */
public final class HgtDiscreteElevationModel implements DiscreteElevationModel {
    private final FileInputStream stream ;
    private  ShortBuffer buffer;
    private final String name ;
    
    private final int latitude;
    private final int longitude;
    private final Interval2D extent;
    private final static int file_length = 25934402;
    private final static int name_length = 11;
    /**
     * construit un MNT discret dont les échantillons proviennent du fichier HGT passé en argument
     * @param file Fichier HGT fournissant les echantillions
     * @throws IllegalArgumentException si le nom du fichier est invalide ou si sa longueur n'est pas celle attendue
     */
    public HgtDiscreteElevationModel(File file) {
        name=file.getName();
        boolean condition1 = name.length()== name_length;
        boolean condition2 = ((name.charAt(0)=='N')||(name.charAt(0)=='S'));
        try{   
            latitude=Integer.parseInt(name.substring(1,3));
            longitude=Integer.parseInt(name.substring(4,7));

        }
        catch(NumberFormatException e)
        {      
            throw new IllegalArgumentException();
        }
        boolean condition3 = ((name.charAt(3)=='W')||(name.charAt(3)=='E'));
        boolean condition4 = ((name.substring(7)).equals(".hgt"));
        boolean condition5 = file.length() == file_length;

        Preconditions.checkArgument(condition1&&condition2&&condition3&&condition4&&condition5);
        try{
            stream = new FileInputStream(file);
            buffer = stream.getChannel()
                    .map(MapMode.READ_ONLY, 0, file.length())
                    .asShortBuffer();
        }
        catch(IOException e)
        {
            throw new IllegalArgumentException();

        }

        
        int latitudeSigned = name.charAt(0) == 'N' ? latitude : -latitude;
        int y=  latitudeSigned * SAMPLES_PER_DEGREE;
        
        
        int longitudeSigned = name.charAt(3) == 'E' ? longitude : -longitude;
        int x =  longitudeSigned * SAMPLES_PER_DEGREE;
        
        extent = new Interval2D(new Interval1D(x, x + SAMPLES_PER_DEGREE),
                new Interval1D(y, y + SAMPLES_PER_DEGREE));

    }


    @Override
    public void close() throws Exception {

        stream.close();
        buffer=null;



    }


    @Override
    public Interval2D extent() {
        return extent;
        


    }


    @Override
    public double elevationSample(int x, int y) {
        int dy = extent().iY().includedTo()-y;
        int dx = x-extent().iX().includedFrom();
        int difference = extent().iX().size();
        Preconditions.checkArgument(extent().contains(x, y));


        return buffer.get(difference*(dy)+dx);

    }




}
