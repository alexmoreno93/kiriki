package com.example.alex.proyecto;

public class BorradoUsuario {

    /**
     * success : 0
     * message : No existe un usuario con ese email
     */

    private int success;
    private String message;

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
