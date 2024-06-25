package com.example.alex.proyecto;

public class ComprobarTurno {


    /**
     * Posicion : 1
     * Turno : 1
     * ValorRecibido : 8
     * ValorRecibidoReal : 10
     * success : 1
     * message : Comprobacion realizada.
     */

    private String Posicion;
    private String Turno;
    private String ValorRecibido;
    private String ValorRecibidoReal;
    private int success;
    private String message;

    public String getPosicion() {
        return Posicion;
    }

    public void setPosicion(String Posicion) {
        this.Posicion = Posicion;
    }

    public String getTurno() {
        return Turno;
    }

    public void setTurno(String Turno) {
        this.Turno = Turno;
    }

    public String getValorRecibido() {
        return ValorRecibido;
    }

    public void setValorRecibido(String ValorRecibido) {
        this.ValorRecibido = ValorRecibido;
    }

    public String getValorRecibidoReal() {
        return ValorRecibidoReal;
    }

    public void setValorRecibidoReal(String ValorRecibidoReal) {
        this.ValorRecibidoReal = ValorRecibidoReal;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
