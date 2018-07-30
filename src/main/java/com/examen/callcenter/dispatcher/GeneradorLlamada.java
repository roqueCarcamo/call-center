package com.examen.callcenter.dispatcher;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.examen.callcenter.modelo.Llamada;

/**
 * Clase para generar las llamadas a los empleados
 *
 * @author Rodolfo Cárcamo
 */
public class GeneradorLlamada implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(GeneradorLlamada.class);

    private static final int MIN_DURACION_LLAMADA = 5;
    private static final int MAX_DURACION_LLAMADA = 10;
    private static final int CANTIDAD_MIN_LLAMADA = 10;
    private static final int CANTIDAD_MAX_LLAMADA = 16;
    private Boolean activo;

    public GeneradorLlamada(Boolean estadoGenerarLLamada) {
        Validate.notNull(estadoGenerarLLamada);
        this.activo = estadoGenerarLLamada;
    }

    public synchronized Boolean getActivo() {
        return activo;
    }

    /**
     * Método para detener el generador de llamadas y los hilos de los empleados.
     * 
     */
    public synchronized void stop() {
        this.activo = false;
    }

    /**
     * Si el estado del generador de llamadas es activo y la lista de llamadas entrantes está vacía, 
     * se generara una cantidad entre 10 y 16 llamadas. La duración de una llamada puede tomar entre 5 y 10 segundos.
     */
    @Override
    public void run() {
        if (getActivo()) {
            logger.info("Inio de generador de llamada.");
        }
        while (getActivo()) {
            if (Dispatcher.llamadasEntrantes.isEmpty()) {
                int totalCallGenerator = ThreadLocalRandom.current().nextInt(CANTIDAD_MIN_LLAMADA, CANTIDAD_MAX_LLAMADA);
                List<Llamada> llamadas = Llamada.contruirListaAleatoriaLlamadas(totalCallGenerator, MIN_DURACION_LLAMADA, MAX_DURACION_LLAMADA);
                logger.info("Se generaron nuevas llamadas y serán enviadas por el centro de llamadas.");
                llamadas.forEach(llamada -> {
                    Dispatcher.dispatchCall(llamada);
                });
            }
        }
    }
}
