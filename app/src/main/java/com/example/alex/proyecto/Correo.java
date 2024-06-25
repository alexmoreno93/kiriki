package com.example.alex.proyecto;

import java.util.List;

public class Correo {

    /**
     * Usuario : [{"IdUsuario":"16","Correo":"alex@alex.com","Password":"$2y$10$n5CHaTOJ/wAD.BdU.fBTyOefK8KylDuO6FVA7h75EsG9GRQNS9ndC","Telefono":"662515687","Administrador":"1"}]
     * success : 1
     */

    private int success;
    private List<UsuarioBean> Usuario;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public List<UsuarioBean> getUsuario() {
        return Usuario;
    }

    public void setUsuario(List<UsuarioBean> Usuario) {
        this.Usuario = Usuario;
    }

    public static class UsuarioBean {
        /**
         * IdUsuario : 16
         * Correo : alex@alex.com
         * Password : $2y$10$n5CHaTOJ/wAD.BdU.fBTyOefK8KylDuO6FVA7h75EsG9GRQNS9ndC
         * Telefono : 662515687
         * Administrador : 1
         */

        private String IdUsuario;
        private String Correo;
        private String Password;
        private String Telefono;
        private String Administrador;

        public String getIdUsuario() {
            return IdUsuario;
        }

        public void setIdUsuario(String IdUsuario) {
            this.IdUsuario = IdUsuario;
        }

        public String getCorreo() {
            return Correo;
        }

        public void setCorreo(String Correo) {
            this.Correo = Correo;
        }

        public String getPassword() {
            return Password;
        }

        public void setPassword(String Password) {
            this.Password = Password;
        }

        public String getTelefono() {
            return Telefono;
        }

        public void setTelefono(String Telefono) {
            this.Telefono = Telefono;
        }

        public String getAdministrador() {
            return Administrador;
        }

        public void setAdministrador(String Administrador) {
            this.Administrador = Administrador;
        }
    }
}

