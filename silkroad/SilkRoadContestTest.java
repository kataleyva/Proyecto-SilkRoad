import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

/**
 * Clase de pruebas para SilkRoadContest.
 * Incluye:
 *  - Pruebas unitarias del método solve().
 *  - Pruebas de aceptación del funcionamiento completo.
 */
public class SilkRoadContestTest {
    //Pruebas de SOLVE

    /**
     * Verifica que solve() devuelva un array vacío si la entrada es null.
     */
    @Test
    public void testSolveConEntradaNula() {
        SilkRoadContest contest = new SilkRoadContest(new int[][] {{1, 2}});
        int[] resultado = contest.solve(null);
        assertEquals(0, resultado.length, "Debe devolver un arreglo vacío si la entrada es null");
    }

    /**
     * Verifica que solve() maneje un array vacío correctamente.
     */
    @Test
    public void testSolveConArrayVacio() {
        SilkRoadContest contest = new SilkRoadContest(new int[][] {{1, 2}});
        int[] resultado = contest.solve(new int[0][]);
        assertEquals(0, resultado.length, "Debe devolver un arreglo vacío si no hay días");
    }

    /**
     * Caso básico con un robot y una tienda en la misma posición.
     * Esperamos que la ganancia sea igual al dinero de la tienda.
     */
    @Test
    public void testSolveRobotYTiendajuntos() {
        int[][] dias = {
            {1, 5},     
            {2, 5, 100} 
        };
        SilkRoadContest contest = new SilkRoadContest(dias);
        int[] resultado = contest.solve(dias);

        assertEquals(2, resultado.length);
        assertTrue(resultado[1] >= 100, 
            "El robot y la tienda están en la misma posición, la ganancia debería ser al menos 100");
    }

    /**
     * Caso con una tienda lejos del robot.
     * Debe existir un costo de distancia.
     */
    @Test
    public void testSolveRobotYTiendalejos() {
        int[][] dias = {
            {1, 0},     
            {2, 10, 50}
        };
        SilkRoadContest contest = new SilkRoadContest(dias);
        int[] resultado = contest.solve(dias);

        assertEquals(2, resultado.length);
        assertTrue(resultado[1] < 50, 
            "La ganancia debe ser menor que 50 por el costo de distancia");
    }
    //Pruebas de Simulate
    //Pruebas de aceptacion

}
