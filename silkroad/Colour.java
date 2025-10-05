import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

/**
 * Clase Colour
 * Genera colores al azar que tienen los robots y las tiendas.
 */
public class Colour {
    private List<String> colors;
    private static List<String> colorDefault;
    private final Random random = new Random();

    public Colour() {
        colorDefault = new ArrayList<>();
        colors = new ArrayList<>();
        putColors();
    }

    private void putColors() {
        colorDefault.add("black");
        colorDefault.add("blue");
        colorDefault.add("green");
        colorDefault.add("magenta");
        colorDefault.add("red");
        colorDefault.add("yellow");
    }

    public String chooseColor() {
        String colour = colorDefault.get(random.nextInt(colorDefault.size()));
    
        if (!colors.isEmpty()) {
            while (colour.equals(colors.get(colors.size() - 1))) {
                colour = colorDefault.get(random.nextInt(colorDefault.size()));
            }   
        }
    
        colors.add(colour);
       return colour.toLowerCase();
    }          
}
