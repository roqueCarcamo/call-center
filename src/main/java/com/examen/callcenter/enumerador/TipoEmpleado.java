package com.examen.callcenter.enumerador;

/**
 * Tipos de empleados
 *
 * @author Rodolfo Cárcamo
 */
public enum TipoEmpleado {

    OPERADOR(3), SUPERVISOR(2), DIRECTOR(1);

    private int nivel;

    TipoEmpleado(int nivel) {
        this.nivel = nivel;
    }

    public int getNivel() {
        return nivel;
    }

    public String toLowerCase() {
        return toString().toLowerCase();
    }

    public String toUpperCase() {
        return toString().toUpperCase();
    }

}
