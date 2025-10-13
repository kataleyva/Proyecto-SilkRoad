public class Main {
        /**
     * Ejemplo con el Sample Input 1 del problema ICPC
     */
    public static void main(String[] args) {
        // Sample Input 1:
        // Expected Output: 0, 10, 35, 50, 50, 60
        
        int[][] sampleInput1 = {
            {1, 20},        // Día 1: Robot en 20
            {2, 15, 15},    // Día 2: Tienda en 15 con 15 tenges
            {2, 40, 50},    // Día 3: Tienda en 40 con 50 tenges
            {1, 50},        // Día 4: Robot en 50
            {2, 80, 20},    // Día 5: Tienda en 80 con 20 tenges
            {2, 70, 30}     // Día 6: Tienda en 70 con 30 tenges
        };
        
        System.out.println("  Expected Output: 0, 10, 35, 50, 50, 60");

        
        SilkRoadContest contest = new SilkRoadContest(sampleInput1);
        contest.simulate(sampleInput1, true);
    }
}