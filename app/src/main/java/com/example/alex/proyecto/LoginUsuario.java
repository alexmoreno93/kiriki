package com.example.alex.proyecto;

import java.util.List;

public class LoginUsuario {


    /**
     * usuario : [{"IdUsuario":"1","Correo":"alex@alex.com","Password":"$2y$10$kOAhRbtTMH.1fv2fz5GWUOQ2e4YvrLU2KtMFHBcSlFudmQFmE4kui","Administrador":"1"}]
     * success : 1
     * message : Login realizado con exito
     */

    private int success;
    private String message;
    private List<UsuarioBean> usuario;

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

    public List<UsuarioBean> getUsuario() {
        return usuario;
    }

    public void setUsuario(List<UsuarioBean> usuario) {
        this.usuario = usuario;
    }

    public static class UsuarioBean {
        /**
         * IdUsuario : 1
         * Correo : alex@alex.com
         * Password : $2y$10$kOAhRbtTMH.1fv2fz5GWUOQ2e4YvrLU2KtMFHBcSlFudmQFmE4kui
         * Administrador : 1
         */

        private String IdUsuario;
        private String Correo;
        private String Password;
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

        public String getAdministrador() {
            return Administrador;
        }

        public void setAdministrador(String Administrador) {
            this.Administrador = Administrador;
        }
    }
}
