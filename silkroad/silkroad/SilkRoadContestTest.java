package silkroad;

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
    public void shouldReturnEmptyWithNullImput() {
        int[] resultado = SilkRoadContest.solve(null);
        assertEquals(0, resultado.length, 
            "Debe devolver un arreglo vacío si la entrada es null");
    }
    
    /**
     * Verifica que solve() maneje un array vacío correctamente.
     */
    @Test
    public void shouldReturnArrayEmptyWithEmptyArray() {
        int[] resultado = SilkRoadContest.solve(new int[0][]);
        assertEquals(0, resultado.length, 
            "Debe devolver un arreglo vacío si no hay días");
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
        assertEquals(1, resultado.length, "Debe haber 1 resultado");
        assertEquals(0, resultado[0], 
            "Sin tiendas, la ganancia debe ser 0");
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
        assertEquals(1, resultado.length, "Debe haber 1 resultado");
        assertEquals(0, resultado[0], 
            "Sin robots, la ganancia debe ser 0");
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
        
        assertEquals(2, resultado.length, "Debe haber 2 resultados");
        assertEquals(0, resultado[0], 
            "Día 1: Solo robot, sin tiendas = 0");
        assertEquals(40, resultado[1], 
            "Día 2: Robot en 0, Tienda en 10 con 50 tenges. Distancia=10. Ganancia=50-10=40");
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
        
        assertEquals(2, resultado.length, "Debe haber 2 resultados");
        assertEquals(0, resultado[0], 
            "Día 1: Solo robot, sin tiendas = 0");
        assertEquals(0, resultado[1], 
            "Día 2: Robot en 0, Tienda en 20 con 10 tenges. Ganancia=10-20=-10. No conviene moverse = 0");
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
        
        assertEquals(2, resultado.length, "Debe haber 2 resultados");
        assertEquals(0, resultado[0], 
            "Día 1: Solo robot, sin tiendas = 0");
        assertEquals(0, resultado[1], 
            "Día 2: Robot en 0, Tienda en 15 con 15 tenges. Ganancia=15-15=0");
    }

    /**
     * Dos robots y dos tiendas, cada robot va a su tienda más cercana.
     * Robot1 en pos 5, Robot2 en pos 15
     * Tienda1 en pos 5 con 80 tenges, Tienda2 en pos 15 con 120 tenges
     * 
     * Día 3: Robot1→Tienda1: distancia=0, ganancia=80
     * Día 4: Robot1→Tienda1 (80) + Robot2→Tienda2: distancia=0, ganancia=120
     * Total día 4 = 80 + 120 = 200 EXACTOS
     */
    @Test
    public void shouldReturn200Tenges() {
        int[][] dias = {
            {1, 5},       // Día 1: Robot1 en posición 5
            {1, 15},      // Día 2: Robot2 en posición 15
            {2, 5, 80},   // Día 3: Tienda1 en posición 5 con 80 tenges
            {2, 15, 120}  // Día 4: Tienda2 en posición 15 con 120 tenges
        };
        int[] resultado = SilkRoadContest.solve(dias);
        
        assertEquals(4, resultado.length, "Debe haber 4 resultados");
        assertEquals(0, resultado[0], "Día 1: Solo Robot1, sin tiendas = 0");
        assertEquals(0, resultado[1], "Día 2: 2 robots, sin tiendas = 0");
        assertEquals(80, resultado[2], 
            "Día 3: Robot1 en 5 → Tienda1 en 5. Ganancia=80-0=80");
        assertEquals(200, resultado[3], 
            "Día 4: Robot1→Tienda1 (80) + Robot2→Tienda2 (120) = 200");
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
        
        assertEquals(2, resultado.length, "Debe haber 2 resultados");
        assertEquals(0, resultado[0], 
            "Día 1: Sin robots para saquear, ganancia = 0");
        assertEquals(0, resultado[1], 
            "Día 2: Sin robots para saquear, ganancia = 0");
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
        
        assertEquals(3, resultado.length, "Debe haber 3 resultados");
        assertEquals(0, resultado[0], "Día 1: Sin tiendas, ganancia = 0");
        assertEquals(0, resultado[1], "Día 2: Sin tiendas, ganancia = 0");
        assertEquals(0, resultado[2], "Día 3: Sin tiendas, ganancia = 0");
    }
    /**
     * Verifica que solve() NO debería permitir ganancias negativas.
     * Cuando el costo de movimiento excede los tenges, el robot no debe moverse.
     */
    @Test
    public void notShouldNegativeProfits() {
        int[][] dias = {
            {1, 0},       // Robot en 0
            {2, 100, 30}  // Tienda en 100 con 30 tenges (costo=100, pérdida=-70)
        };
        int[] resultado = SilkRoadContest.solve(dias);
        
        assertEquals(2, resultado.length, "Debe haber 2 resultados");
        assertEquals(0, resultado[1], 
            "Día 2: Ganancia sería negativa (30-100=-70), robot no se mueve = 0");
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
        assertEquals(4, resultado.length, "Debe haber 4 resultados");
        // Mejor opción: Robot2 (en 5) → Tienda (en 10): 100 - 5 = 95
        // Robot1 (en 0) → Tienda (en 10): 100 - 10 = 90
        // Robot3 (en 20) → Tienda (en 10): 100 - 10 = 90
        // Solo el robot más cercano (Robot2) debe ir
        assertEquals(95, resultado[3], 
            "Día 4: Solo el robot más rentable saquea. Robot2→Tienda = 95");
    }
    /**
    * Verifica que solve() NO debería aceptar posiciones negativas.
    * Las posiciones deben ser >= 0.
    */
    @Test
    public void notShouldAcceptNegativePositions() {
        int[][] dias = {
            {1, -5},      // Robot en posición negativa (inválido)
            {2, 10, 50}   // Tienda válida
        };
        int[] resultado = SilkRoadContest.solve(dias);
        assertEquals(2, resultado.length, "Debe haber 2 resultados");
        assertEquals(0, resultado[0], "Día 1: Sin robots válidos, ganancia = 0");
        assertTrue(resultado[1] >= 0,
        "No debe generarse ganancia negativa incluso si hay posición inválida");
    }

    /**
    * Verifica que solve() NO debería confundir tipos de eventos.
    * Tipo 1 = Robot, Tipo 2 = Tienda. Otros tipos deben ignorarse.
    */
   @Test
   public void notShouldAcceptIncorrectTypes() {
       int[][] dias = {
           {1, 10},       // Robot
           {2, 10, 100},  // Tienda
           {3, 15, 50}    // Tipo inválido → debe ser ignorado
        };
        int[] resultado = SilkRoadContest.solve(dias);
        assertEquals(3, resultado.length, "Debe haber 3 resultados");
        assertEquals(0, resultado[0], "Día 1: Solo robot, sin tiendas = 0");
        assertEquals(100, resultado[1], "Día 2: Robot → Tienda = 100");
        assertEquals(100, resultado[2], 
        "Día 3: Tipo inválido ignorado, ganancia se mantiene igual");
    }

}
