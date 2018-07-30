package com.examen.callcenter.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.apache.commons.lang3.Validate;

/**
 * Modelo de llamada
 *
 * @author Rodolfo Cárcamo
 */
public class Llamada {

    private final Integer identificador;
    private Integer duracionSegundos;
    public static int total = 0;

    /**
     * Crea una llamada con duración en segundos e aumenta el total de llamadas
     * creadas.
     *
     * @param duracionSegundos la duración en segundos debe ser igual o mayor a
     * Cero
     *
     *
     */
    public Llamada(Integer duracionSegundos) {
        Validate.notNull(duracionSegundos);
        Validate.isTrue(duracionSegundos >= 0);
        total++;
        this.identificador = total;
        this.duracionSegundos = duracionSegundos;
    }

    public String getFormatoIdentificador() {
        return "#" + identificador;
    }

    public Integer getDuracionSegundos() {
        return duracionSegundos;
    }

    public void setDuracionSegundos(Integer duracionSegundos) {
        this.duracionSegundos = duracionSegundos;
    }

    /**
     * Crea una nueva lista de llamadas aleatorias
     *
     * @param tamañoLista cantidad de llamadas aleatorias que se crearán
     * @param duracionMinSegundos duración mínima en segundos de cada llamada
     * debe ser igual o mayor a cero
     * @param duracionMaxSegundos duración máxima en segundos de cada llamada
     * debe ser igual o mayor que duracionMinSegundos
     * @return Una nueva lista de llamadas aleatorias, cada una con un valor de
     * duración aleatorio entre duracionMinSegundos y duracionMaxSegundos
     */
    public static List<Llamada> contruirListaAleatoriaLlamadas(Integer tamañoLista, Integer duracionMinSegundos, Integer duracionMaxSegundos) {
        Validate.isTrue(tamañoLista >= 0);
        List<Llamada> listaLlamadas = new ArrayList<Llamada>();
        for (int i = 0; i < tamañoLista; i++) {
            listaLlamadas.add(construirLlamadaAleatoria(duracionMinSegundos, duracionMaxSegundos));
        }
        return listaLlamadas;
    }

    /**
     * Crea una nueva llamada aleatoria
     *
     * @param duracionMinSegundos la duración mínima en segundos debe ser igual
     * o mayor a cero
     * @param duracionMaxSegundos la duración máxima en segundos debe ser igual
     * o mas grande que duracionMinSegundos
     * @return Una nueva llamada aleatoria con un valor de duración aleatorio
     * entre duracionMinSegundos y duracionMaxSegundos
     */
    public static Llamada construirLlamadaAleatoria(Integer duracionMinSegundos, Integer duracionMaxSegundos) {
        Validate.isTrue(duracionMaxSegundos >= duracionMinSegundos && duracionMinSegundos >= 0);
        return new Llamada(ThreadLocalRandom.current().nextInt(duracionMinSegundos, duracionMaxSegundos + 1));
    }

}
