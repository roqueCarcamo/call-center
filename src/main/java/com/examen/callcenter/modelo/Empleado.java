package com.examen.callcenter.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;
import com.examen.callcenter.dispatcher.Dispatcher;
import com.examen.callcenter.enumerador.EstadoEmpleado;
import com.examen.callcenter.enumerador.TipoEmpleado;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Modelo de empleado
 *
 * @author Rodolfo Cárcamo
 */
public class Empleado implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(Empleado.class);

    public String nombre;
    private TipoEmpleado tipoEmpleado;
    private EstadoEmpleado estadoEmpleado;
    private Llamada llamada;
    private ConcurrentLinkedDeque<Llamada> llamadasAtendidas;

    /**
     * Crea un nuevo empleado.
     *
     * @param nombre Nombre de empleado
     * @param tipoEmpleado Tipo de empleado
     *
     *
     */
    public Empleado(String nombre, TipoEmpleado tipoEmpleado) {
        Validate.notNull(tipoEmpleado);
        Validate.notNull(nombre);
        this.nombre = nombre;
        this.tipoEmpleado = tipoEmpleado;
        this.estadoEmpleado = EstadoEmpleado.DISPONIBLE;
        llamadasAtendidas = new ConcurrentLinkedDeque<Llamada>();
    }

    public String getEmpleadoNombre() {
        return nombre;
    }

    public TipoEmpleado getTipoEmpleado() {
        return tipoEmpleado;
    }

    public synchronized EstadoEmpleado getEstadoEmpleado() {
        return estadoEmpleado;
    }

    private synchronized void setEstadoEmpleado(EstadoEmpleado estadoEmpleado) {
        logger.debug(
                getTipoEmpleado().toLowerCase() + ": " + getEmpleadoNombre() + " cambia su estado a " + estadoEmpleado);
        this.estadoEmpleado = estadoEmpleado;
    }

    public synchronized List<Llamada> getAttendedCalls() {
        return new ArrayList<>(llamadasAtendidas);
    }

    /**
     *
     * Asignar estado ocupado al empleado que contesta la llamada.
     *
     */
    public synchronized void responder(Llamada llamada) {
        try {
            this.llamada = llamada;
            this.setEstadoEmpleado(EstadoEmpleado.OCUPADO);
            logger.info(getTipoEmpleado().toUpperCase() + ": " + getEmpleadoNombre() + " deja en cola la llamada "
                    + llamada.getFormatoIdentificador());
            logger.info(Dispatcher.llamadasEntrantes.size() + " llamadas en espera");
        } catch (NoSuchElementException nee) {
            logger.debug("Sin llamadas en espera");
        }
    }

    /**
     *
     * Método para crear empleado de tipo Operador
     *
     * @param nombre
     * @return Empleado
     */
    public static Empleado crearOperador(String nombre) {
        return new Empleado(nombre, TipoEmpleado.OPERADOR);
    }

    /**
     *
     * Método para crear empleado de tipo Supervisor
     *
     * @param nombre
     * @return Empleado
     */
    public static Empleado crearSupervisor(String nombre) {
        return new Empleado(nombre, TipoEmpleado.SUPERVISOR);
    }

    /**
     *
     * Método para crear empleado de tipo Director
     *
     * @param nombre
     * @return Empleado
     */
    public static Empleado crearDirector(String nombre) {
        return new Empleado(nombre, TipoEmpleado.DIRECTOR);
    }

    /**
     *
     * Si la cola de llamadas entrantes no está vacía, entonces cambia su estado
     * de DISPONIBLE a OCUPADO, y toma la llamada y cuando termina cambie su
     * estado de OCUPADO a DISPONIBLE.
     */
    public void run() {
        logger.info(getTipoEmpleado().toUpperCase() + ": " + getEmpleadoNombre() + " inicio a trabajar ");
        while (true) {
            if (getEstadoEmpleado() == EstadoEmpleado.OCUPADO) {
                logger.info(getTipoEmpleado().toUpperCase() + ": " + getEmpleadoNombre() + " inicio a responder la llamada " + this.llamada.getFormatoIdentificador());
                try {
                    TimeUnit.SECONDS.sleep(this.llamada.getDuracionSegundos());
                } catch (InterruptedException ie) {
                    Dispatcher.llamadasEntrantes.addFirst(this.llamada);
                    logger.debug("Hubo un error en la llamada, se agregará a la lista pendiente para ser atendido por el siguiente operador disponible.");
                } finally {
                    this.setEstadoEmpleado(EstadoEmpleado.DISPONIBLE);
                    logger.info(getTipoEmpleado().toUpperCase() + ": " + getEmpleadoNombre() + " termina la llamada " + this.llamada.getFormatoIdentificador() + " con una duración de " + this.llamada.getDuracionSegundos() + " segundos");
                }
                this.llamadasAtendidas.add(llamada);
            }
        }
    }
}
