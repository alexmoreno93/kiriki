package com.example.alex.proyecto;

import java.util.List;

public class FallosPartida {

    /**
     * Fallos : ["2","1"]
     * success : 1
     * message : Fallos devueltos correctamente.
     */

    private int success;
    private String message;
    private List<String> Fallos;

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

    public List<String> getFallos() {
        return Fallos;
    }

    public void setFallos(List<String> Fallos) {
        this.Fallos = Fallos;
    }
}
