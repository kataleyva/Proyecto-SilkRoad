package shapes;

import java.util.*;

public class Colour {
    private static HashSet<String> colorsUsed = new HashSet<>();
    private final Random random = new Random();
    
    public String chooseColorExcluding(String excludedColor) {
        String color;
        do {
            int r = random.nextInt(256);
            int g = random.nextInt(256);
            int b = random.nextInt(256);
            color = "rgb(" + r + "," + g + "," + b + ")";
        } while (colorsUsed.contains(color) || color.equals(excludedColor));
        
        colorsUsed.add(color);
        return color;
    }
    
    public String chooseColor() {
        return chooseColorExcluding(null);
    }

}
