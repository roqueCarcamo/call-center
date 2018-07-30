package com.examen.callcenter.dispatcher;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.examen.callcenter.modelo.filtro.FiltroEmpleado;
import com.examen.callcenter.modelo.Llamada;
import com.examen.callcenter.modelo.Empleado;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Clase para despachar las llamadas a los empleados
 *
 * @author Rodolfo Cárcamo
 */
public class Dispatcher implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(Dispatcher.class);

    private ExecutorService executorService;
    private ConcurrentLinkedDeque<Empleado> listaEmpleados;
    private GeneradorLlamada generadorLlamada;
    private Boolean activo;
    public static ConcurrentLinkedDeque<Llamada> llamadasEntrantes = new ConcurrentLinkedDeque<Llamada>();
    private FiltroEmpleado filtroEmpleado;

    public Dispatcher(List<Empleado> empleados, Boolean estadoGeneradorLlamada) {
        this(empleados, new FiltroEmpleado(), estadoGeneradorLlamada);
    }

    /**
     * Método para despachar llamadas.
     *
     */
    public Dispatcher(List<Empleado> empleados, FiltroEmpleado filtroEmpleado, Boolean callGenerateState) {
        Validate.notNull(empleados);
        Validate.notNull(filtroEmpleado);
        Validate.notNull(callGenerateState);
        this.listaEmpleados = new ConcurrentLinkedDeque<Empleado>(empleados);
        this.filtroEmpleado = filtroEmpleado;
        this.generadorLlamada = new GeneradorLlamada(callGenerateState);
        this.executorService = Executors.newFixedThreadPool(empleados.size() + 1);
    }

    /**
     * Envía una nueva llamada entrante.
     *
     */
    public static synchronized void dispatchCall(Llamada llamada) {
        logger.info("Envío nueva llamada, con identificador : " + llamada.getFormatoIdentificador());
        llamadasEntrantes.add(llamada);
    }

    /**
     * Inicia el generador de llamadas y los subprocesos de empleado, lo que
     * permite ejecutar el método de ejecución del despachador
     *
     */
    public synchronized void start() {
        this.activo = true;
        this.executorService.execute(this.generadorLlamada);
        for (Empleado employee : this.listaEmpleados) {
            this.executorService.execute(employee);
        }
    }

    /**
     * Detiene inmediatamente el generador de llamadas y los hilos del empleado,
     * el método de ejecución del despachador
     *
     */
    public synchronized void stop() {
        this.activo = false;
        this.executorService.shutdown();
    }

    public synchronized Boolean getActivo() {
        return activo;
    }

    /**
     * Si las llamadas entrantes no están vacías, se busca un empleado
     * disponible para responder a la primera llamada. Las llamadas se pondrán
     * en cola hasta que haya algunos trabajadores disponibles.
     *
     */
    public void run() {
        while (getActivo()) {
            if (llamadasEntrantes.isEmpty()) {
                continue;
            } else {
                Empleado empleado = this.filtroEmpleado.buscarEmpleado(this.listaEmpleados);
                if (empleado == null) {
                    continue;
                }
                try {
                    Llamada llamada = llamadasEntrantes.pollFirst();
                    empleado.responder(llamada);
                } catch (Exception e) {
                    logger.error(e.toString());
                }
            }
        }
    }
}
