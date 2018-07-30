package com.examen.callcenter.modelo;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Clase de pruebas de llamada
 *
 * @author Rodolfo Cárcamo
 */
public class LlamadaTest {

    @Test(expected = NullPointerException.class)
    public void testCreacionLlamadaNull() {
        new Llamada(null);
    }

    @Test
    public void testLlamadasAleatoriaValida() {
        Integer min = 5;
        Integer max = 10;
        Llamada llamada = Llamada.construirLlamadaAleatoria(min, max);
        assertNotNull(llamada);
        assertTrue(min <= llamada.getDuracionSegundos());
        assertTrue(llamada.getDuracionSegundos() <= max);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLlamadaAleatoriaInvalida() {
        Llamada.construirLlamadaAleatoria(-1, 1);
    }

}
