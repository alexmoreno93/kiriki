package com.example.alex.proyecto;

import java.util.List;

public class Partida {

    /**
     * Partida : [{"IdPartida":"1","IdUsuario":"1","Posicion":"1","Turno":"1"},{"IdPartida":"1","IdUsuario":"5","Posicion":"2","Turno":"0"}]
     * success : 1
     */

    private int success;
    private List<PartidaBean> Partida;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public List<PartidaBean> getPartida() {
        return Partida;
    }

    public void setPartida(List<PartidaBean> Partida) {
        this.Partida = Partida;
    }

    public static class PartidaBean {
        /**
         * IdPartida : 1
         * IdUsuario : 1
         * Posicion : 1
         * Turno : 1
         */

        private String IdPartida;
        private String IdUsuario;
        private String Posicion;
        private String Turno;

        public String getIdPartida() {
            return IdPartida;
        }

        public void setIdPartida(String IdPartida) {
            this.IdPartida = IdPartida;
        }

        public String getIdUsuario() {
            return IdUsuario;
        }

        public void setIdUsuario(String IdUsuario) {
            this.IdUsuario = IdUsuario;
        }

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
    }
}
