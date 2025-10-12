import java.util.ArrayList;
import java.util.List;


public class Posicion {
    public static int[][] generateSpiral(int size) {
        List<int[]> spiral = new ArrayList<>();
        int x = 0, y = 0;
        spiral.add(new int[]{x, y});
        
        int[] dx = {1, 0, -1, 0};  // derecha, abajo, izquierda, arriba
        int[] dy = {0, 1, 0, -1};
        int direction = 0;
        int steps = 1;
    
        while (spiral.size() < size) {
            for (int i = 0; i < 2 && spiral.size() < size; i++) {
                for (int j = 0; j < steps && spiral.size() < size; j++) {
                    x += dx[direction];
                    y += dy[direction];
                    spiral.add(new int[]{x, y});
                }
                direction = (direction + 1) % 4;
            }
            steps++;
        }
    
        return spiral.toArray(new int[spiral.size()][]);
    }
}