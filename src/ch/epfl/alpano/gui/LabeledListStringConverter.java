package ch.epfl.alpano.gui;

import java.util.ArrayList;
import java.util.List;

import javafx.util.StringConverter;
/**
 * 
 * @author Bayrem Kaabachi (261340), Emine Ghariani (262850)
 *
 */
public final class LabeledListStringConverter extends StringConverter<Integer> {
    private final List<String> arguments ;
    
    /**
     * Construit un convertisseur de chaine LabeledListStringConverter. La
     * conversion se fait en fonction d'une liste de chaînes passées au
     * constructeur sous la forme d'un nombre variable d'arguments : une chaîne
     * égale à celle à la position n de cette liste est convertie en l'entier n,
     * et vice versa.
     * 
     * @param args
     */
    LabeledListStringConverter(String ... strings  ){
        arguments= new ArrayList<>();
        for (String s : strings){
            arguments.add(s);
        }
        
    }

    @Override
    public String toString(Integer object) {
        if(object == null)
            return "";
        if (object>=arguments.size()) 
            throw new IllegalArgumentException() ;
        
        return arguments.get(object);
    }

    @Override
    public Integer fromString(String string) {
        if (!arguments.contains(string))
            throw new IllegalArgumentException() ;
            
        
        return arguments.indexOf(string);
    }

}
