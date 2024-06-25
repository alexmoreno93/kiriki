package com.example.alex.proyecto;

import java.util.List;

public class CorreosPartida {

    /**
     * Correos : ["alex@alex.com","juan@juan.com"]
     * success : 1
     * message : Correos devueltos correctamente.
     */

    private int success;
    private String message;
    private List<String> Correos;

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

    public List<String> getCorreos() {
        return Correos;
    }

    public void setCorreos(List<String> Correos) {
        this.Correos = Correos;
    }
}
