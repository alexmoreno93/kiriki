package com.example.alex.proyecto;

import java.util.List;

public class Usuario {


    /**
     * Usuario : [{"IdUsuario":"1","Correo":"juan@juan.com","Password":"$2y$10$SCM1iyj7ri4hsHes4zYRnOV.e0E1dsVO2lsn/RJMxhLB7kbRUTZxK","Administrador":"1"},{"IdUsuario":"2","Correo":"alex@alex.com","Password":"$2y$10$3O.2o.JkIyuD52oVjqcv/OtkuQzwIITsCYo5RdHbLZpu9gOOP4A32","Administrador":"1"},{"IdUsuario":"3","Correo":"pepe@pepe.com","Password":"$2y$10$WKgSXMNx2qZoFYw/xymQ6OG/6joFR2e7okpSA4IF1MdGqj57DRz/m","Administrador":"1"},{"IdUsuario":"5","Correo":"pepe@pp.com","Password":"$2y$10$adFbaONrs9u8Gv0vb2uA1e2rRgsEIiQnrtULwB2elJmANFJvesa0K","Administrador":"1"},{"IdUsuario":"6","Correo":"prueba@prueba.com","Password":"$2y$10$6Mmo.yZG9p2F.9YjcPTdauGyNSw308ZvwFIidK992CsDw6/YuTegC","Administrador":"0"},{"IdUsuario":"7","Correo":"paco@paco.com","Password":"$2y$10$GblJ14XBGYJBd5WuasLebOaAbKjuPnSp21/HDDiO3PCf7ieUzkrDa","Administrador":"0"},{"IdUsuario":"9","Correo":"vakalatm@gmail.com","Password":"$2y$10$tm6S5w4Po9BrNwhdBDjFH.9qAeMErIREU93u0YVw5QIzMZ74ryGLq","Administrador":"0"}]
     * success : 1
     * message : Devueltos los usuarios correctamente.
     */

    private int success;
    private String message;
    private List<UsuarioBean> Usuario;

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
        return Usuario;
    }

    public void setUsuario(List<UsuarioBean> Usuario) {
        this.Usuario = Usuario;
    }

    public static class UsuarioBean {
        /**
         * IdUsuario : 1
         * Correo : juan@juan.com
         * Password : $2y$10$SCM1iyj7ri4hsHes4zYRnOV.e0E1dsVO2lsn/RJMxhLB7kbRUTZxK
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
