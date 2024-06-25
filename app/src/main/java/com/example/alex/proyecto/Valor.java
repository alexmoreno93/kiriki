package com.example.alex.proyecto;

public class Valor {
    private int prioridad;
    private String valor;

    public Valor(int prioridad, String valor) {
        this.prioridad = prioridad;
        this.valor = valor;
    }
    public int getPrioridad() {
        return prioridad;
    }
    public String getValor() {
        return valor;
    }
    @Override
    public String toString() {
        return valor;
    }
}