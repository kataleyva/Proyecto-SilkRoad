public class Main {
    public static void main(String[] args) {
        /*
         * Cada fila representa un día:
         * Formato: [n, s1, t1, s2, t2, ..., sn, tn, r1, r2, ...]
         *
         * Ejemplo:
         * Día 1: 3 tiendas y 2 robots
         * Día 2: 2 tiendas nuevas y 1 robot nuevo
         * Día 3: 1 tienda y 1 robot adicional
         */

        int[][] days = {
            // Día 1: 3 tiendas y 2 robots
            {3, 2, 40, 5, 80, 8, 30, 0, 3},
            // Día 2: 2 tiendas nuevas (10, 50) y 1 robot nuevo (6)
            {2, 10, 100, 12, 60, 6},
            // Día 3: 1 tienda nueva y un robot adicional
            {1, 15, 120, 9}
        };

        // Crear el concurso y ejecutar la simulación
        SilkRoadContest contest = new SilkRoadContest(days);
        contest.simulate(days);
    }
}
