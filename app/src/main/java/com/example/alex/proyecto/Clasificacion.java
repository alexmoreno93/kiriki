package com.example.alex.proyecto;

import java.util.List;

public class Clasificacion {


    /**
     * Clasificacion : [{"IdUsuario":"1","Nombre":"juan","Turnos":"2","Fallos":"17","Participantes":"2"},{"IdUsuario":"11","Nombre":"juan","Turnos":"2","Fallos":"17","Participantes":"2"}]
     * success : 1
     */

    private int success;
    private List<ClasificacionBean> Clasificacion;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public List<ClasificacionBean> getClasificacion() {
        return Clasificacion;
    }

    public void setClasificacion(List<ClasificacionBean> Clasificacion) {
        this.Clasificacion = Clasificacion;
    }

    public static class ClasificacionBean {
        /**
         * IdUsuario : 1
         * Nombre : juan
         * Turnos : 2
         * Fallos : 17
         * Participantes : 2
         */

        private String IdUsuario;
        private String Nombre;
        private String Turnos;
        private String Fallos;
        private String Participantes;

        public String getIdUsuario() {
            return IdUsuario;
        }

        public void setIdUsuario(String IdUsuario) {
            this.IdUsuario = IdUsuario;
        }

        public String getNombre() {
            return Nombre;
        }

        public void setNombre(String Nombre) {
            this.Nombre = Nombre;
        }

        public String getTurnos() {
            return Turnos;
        }

        public void setTurnos(String Turnos) {
            this.Turnos = Turnos;
        }

        public String getFallos() {
            return Fallos;
        }

        public void setFallos(String Fallos) {
            this.Fallos = Fallos;
        }

        public String getParticipantes() {
            return Participantes;
        }

        public void setParticipantes(String Participantes) {
            this.Participantes = Participantes;
        }
    }
}
