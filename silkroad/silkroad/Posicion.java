package silkroad;

import java.util.ArrayList; 
import java.util.List; 
public class Posicion { 
    private static final int START_X = 80;
    private static final int START_Y = 80;
    private static final int STEP_SIZE = 50;
    public static int[][] generateSpiral(int size) { 
        List<int[]> spiral = new ArrayList<>(); 
        int x = START_X;
        int y = START_Y ;
        spiral.add(new int[]{x, y}); 
        int[] dx = {0, -80, 0, 80}; 
        int[] dy = {80, 0, -80, 0}; 
        int direction = 0; 
        int steps = 1; 
        
        while (spiral.size() < size) { 
            for (int i = 0; i < 2 && spiral.size() < size; i++) { 
                for (int j = 0; j < steps && spiral.size() < size; j++) {
                    x += dx[direction];
                    y += dy[direction];
                    //if(x<50) x = 50;
                    //if(x>650) x = 650;
                    //if(y<50) y = 50;
                    //if(y>650) y = 650;
                    spiral.add(new int[]{x, y});
                } 
                direction = (direction + 1) % 4; 
            } 
            steps++; 
        } 
        return spiral.toArray(new int[spiral.size()][]); 
    } 
}
