package ch.epfl.alpano.gui;


import java.math.BigDecimal;	


import javafx.util.StringConverter;
/**
 * 
 * @author Bayrem Kaabachi (261340), Emine Ghariani (262850)
 *
 */
public final class FixedPointStringConverter extends StringConverter<Integer>{

    private final Integer decimals ;
    
    /**
     * Construit un convertisseur de chaine FixedPointStringConverter. La
     * conversion d'une chaîne en entier se fait en interprétant celle-ci comme
     * un nombre réel, en l'arrondissant à un nombre de décimales fixes et
     * spécifié au moment de la construction du convertisseur, puis en
     * «supprimant la virgule» afin d'obtenir un entier. La conversion d'un
     * entier en chaîne suit le chemin inverse.
     * 
     * @param n
     */
    public FixedPointStringConverter(Integer decimals){
        this.decimals=decimals;
    }
    
    @Override
    public Integer fromString(String string){
        BigDecimal b = new BigDecimal(string);
        
        b=b.setScale(decimals, BigDecimal.ROUND_HALF_EVEN).stripTrailingZeros().movePointRight(decimals);
       
        
        return b.intValue();
    }

    @Override
    public String toString(Integer number) {
        if(number==null)
            return "";
        else{
        BigDecimal b = new BigDecimal(number);
        b=b.movePointLeft(decimals);
        
        return b.toPlainString();
        }
    }

    
}