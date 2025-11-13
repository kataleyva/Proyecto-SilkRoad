
package test;
import silkroad.SilkRoad;
import silkroad.SilkRoadContest;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de pruebas para SilkRoadContest.
 * Incluye:
 *  - Pruebas unitarias del método solve().
 *  - Pruebas de aceptación del funcionamiento completo.
 * 
 * IMPORTANTE: Todas las pruebas usan assertEquals con valores EXACTOS calculados,
 * no comparaciones con >= o <=, para detectar errores en la implementación.
 */
public class SilkRoadContestTest {
    // SE REALIZAN PRUEBAS UNITARIAS PARA VER EL COMPORTAMIENTO DE SOLVE EN DIFERENTES SITUACIONES
    // Y PRUEBAS DE ACEPTACION DE SIMULATE PARA VER EL COMPORTAMIENTO DE LA CLASE DE FORMA VISUAL
    /**
     * Verifica que solve() devuelva un array vacío si la entrada es null.
     */
    @Test
    public void solveLRshouldReturnEmptyWithNullImput() {
        int[] resultado = SilkRoadContest.solve(null);
        assertEquals(0, resultado.length);
    }
    
    /**
     * Verifica que solve() maneje un array vacío correctamente.
     */
    @Test
    public void solveLRshouldReturnArrayEmptyWithEmptyArray() {
        int[] resultado = SilkRoadContest.solve(new int[0][]);
        assertEquals(0, resultado.length);
    }
    
    /**
     * Verifica que solve() retorne 0 cuando solo hay un robot sin tiendas.
     */
    @Test
    public void shouldReturnZeroWithOnlyRobots() {
        int[][] dias = {
            {1, 5}
        };
        int[] resultado = SilkRoadContest.solve(dias);
        assertEquals(1, resultado.length);
        assertEquals(0, resultado[0]);
    }
    
    /**
     * Verifica que solve() retorne 0 cuando solo hay una tienda sin robots.
     */
    @Test
    public void shouldReturnZeroWithOnlyStores() {
        int[][] dias = {
            {2, 10, 100}
        };
        int[] resultado = SilkRoadContest.solve(dias);
        assertEquals(1, resultado.length);
        assertEquals(0, resultado[0]);
    }
    
    /**
     * Caso con distancia pequeña.
     * Robot en pos 0, Tienda en pos 10 con 50 tenges
     * Cálculo: distancia = |10-0| = 10
     * Ganancia = 50 - 10 = 40 tenges EXACTOS
     */
    @Test
    public void shouldRetorn40Tenges() {
        int[][] dias = {
            {1, 0},      // Día 1: Robot en posición 0
            {2, 10, 50}  // Día 2: Tienda en posición 10 con 50 tenges
        };
        int[] resultado = SilkRoadContest.solve(dias);
        
        assertEquals(2, resultado.length);
        assertEquals(0, resultado[0]);
        assertEquals(40, resultado[1]);
    }
    
    /**
     * Caso donde NO conviene moverse (ganancia negativa).
     * Robot en pos 0, Tienda en pos 20 con 10 tenges
     * Cálculo: distancia = |20-0| = 20
     * Ganancia potencial = 10 - 20 = -10 (NEGATIVO)
     * Resultado esperado = 0 (el robot no se mueve)
     */
    @Test
    public void solveDeberiaRetornar0CuandoDistanciaExcedeTenges() {
        int[][] dias = {
            {1, 0},       // Día 1: Robot en posición 0
            {2, 20, 10}   // Día 2: Tienda en posición 20 con 10 tenges
        };
        int[] resultado = SilkRoadContest.solve(dias);
        
        assertEquals(2, resultado.length);
        assertEquals(0, resultado[0]);
        assertEquals(0, resultado[1]);
    }
    
    /**
     * Caso justo en el límite (ganancia = 0).
     * Robot en pos 0, Tienda en pos 15 con 15 tenges
     * Cálculo: distancia = 15, ganancia = 15 - 15 = 0
     * El robot podría moverse o no (ambos dan 0)
     */
    @Test
    public void shouldRetornZeroWhenNoProfits() {
        int[][] dias = {
            {1, 0},       // Día 1: Robot en posición 0
            {2, 15, 15}   // Día 2: Tienda en posición 15 con 15 tenges
        };
        int[] resultado = SilkRoadContest.solve(dias);
        
        assertEquals(2, resultado.length);
        assertEquals(0, resultado[0]);
        assertEquals(0, resultado[1]);
    }
    
    /**
     * Verifica que solve() NO debería calcular ganancia cuando no hay robots.
     * Solo tiendas disponibles, sin robots para saquearlas.
     */
    @Test
    public void notShouldCalculateProfitsWithoutRobots() {
        int[][] dias = {
            {2, 10, 100},  // Solo Tienda1
            {2, 20, 50}    // Solo Tienda2
        };
        int[] resultado = SilkRoadContest.solve(dias);
        
        assertEquals(2, resultado.length);
        assertEquals(0, resultado[0]);
        assertEquals(0, resultado[1]);
    }
    
     /**
     * Verifica que solve() NO debería calcular ganancia cuando no hay tiendas.
     * Solo robots disponibles, sin tiendas para saquear.
     */
    @Test
    public void notShouldCalculateProfitsWithoutStores() {
        int[][] dias = {
            {1, 5},   // Solo Robot1
            {1, 10},  // Solo Robot2
            {1, 15}   // Solo Robot3
        };
        int[] resultado = SilkRoadContest.solve(dias);
        
        assertEquals(3, resultado.length);
        assertEquals(0, resultado[0]);
        assertEquals(0, resultado[1]);
        assertEquals(0, resultado[2]);
    }
    
    /**
     * Verifica que solve() NO debería asignar mal cuando hay más robots que tiendas.
     * Solo los robots más rentables deberían moverse.
     */
    @Test
    public void shouldBeMovedOnlyProfitablesRobots() {
        int[][] dias = {
            {1, 0},        // Robot1 en 0
            {1, 5},        // Robot2 en 5
            {1, 20},       // Robot3 en 20
            {2, 10, 100}   // Solo una tienda en 10 con 100 tenges
        };
        int[] resultado = SilkRoadContest.solve(dias);
        assertEquals(4, resultado.length);
        // Mejor opción: Robot2 (en 5) → Tienda (en 10): 100 - 5 = 95
        // Robot1 (en 0) → Tienda (en 10): 100 - 10 = 90
        // Robot3 (en 20) → Tienda (en 10): 100 - 10 = 90
        // Solo el robot más cercano (Robot2) debe ir
        assertEquals(95, resultado[3]);
    }
}
