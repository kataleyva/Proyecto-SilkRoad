package shapes;

import java.util.*;

public class Colour {
    private static HashSet<String> colorsUsed = new HashSet<>();
    private final Random random = new Random();
    
    public String chooseColorExcluding(String excludedColor) {
        String color;
        int r;
        int g;
        int b;
        do {
            r = random.nextInt(256);
            g = random.nextInt(256);
            b = random.nextInt(256);
            color = "rgb(" + r + "," + g + "," + b + ")";
        } while (colorsUsed.contains(color) || color.equals(excludedColor) || !(r != 67 && g != 100 && b != 204) || !(r != 67 && g != 204 && b != 76)
        || !(r != 247 && g != 172 && b != 42) || !(r != 247 && g != 236 && b != 84) || !(r != 250 && g != 175 && b != 241));
        
        colorsUsed.add(color);
        return color;
    }
    
    public String changeColor(int r, int g, int b){
       String color = "rgb(" + r + "," + g + "," + b + ")";
       return color;
    }
    
    public String chooseColor() {
        return chooseColorExcluding(null);
    }

}
