import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

/**
 * Clase Colour
 * Genera colores al azar que tienen los robots y las tiendas.
 */
public class Colour {
    private static List<String> availableColors;
    private static final List<String> colorDefault = Arrays.asList("black", "blue", "green", "magenta", "red", "yellow");
    private final Random random = new Random();

    public Colour() {
        if (availableColors == null) {
            availableColors = new ArrayList<>(colorDefault);
        }
    }

    public void returnColor(String color) {
        if (!availableColors.contains(color)) {
            availableColors.add(color);
        }
    }
    
    public String chooseColor() {
        if (availableColors.isEmpty()) {
            availableColors = new ArrayList<>(colorDefault);
        }

        int index = random.nextInt(availableColors.size());
        String chosen = availableColors.remove(index);

        return chosen;
    }
    
    public String chooseColorExcluding(String excludedColor) {
        if (availableColors.isEmpty()) {
            availableColors = new ArrayList<>(colorDefault);
        }
        
        List<String> validColors = new ArrayList<>();
        for (String c : availableColors) {
            if (!c.equals(excludedColor)) {
                validColors.add(c);
            }
        }
        
        if (validColors.isEmpty()) {
            availableColors = new ArrayList<>(colorDefault);
            for (String c : availableColors) {
                if (!c.equals(excludedColor)) {
                    validColors.add(c);
                }
            }
        }
        
        int index = random.nextInt(validColors.size());
        String chosen = validColors.get(index);
        availableColors.remove(chosen);
        return chosen;
    }
}

