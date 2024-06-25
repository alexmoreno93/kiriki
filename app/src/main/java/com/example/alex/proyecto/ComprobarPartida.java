package com.example.alex.proyecto;

public class ComprobarPartida {

    /**
     * IdPartida : 11
     * Cantidad : 2
     * success : 1
     * message : Comprobacion realizada.
     */

    private String IdPartida;
    private String Cantidad;
    private int success;
    private String message;

    public String getIdPartida() {
        return IdPartida;
    }

    public void setIdPartida(String IdPartida) {
        this.IdPartida = IdPartida;
    }

    public String getCantidad() {
        return Cantidad;
    }

    public void setCantidad(String Cantidad) {
        this.Cantidad = Cantidad;
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
