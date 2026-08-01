package com.example.todolistapp;

public enum Rol {
    ADMIN("admin"),
    USER("user"),
    VIEWER("viewer");

    private final String valor;

    Rol(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }

    public static Rol fromString(String valor) {
        for (Rol rol : Rol.values()) {
            if (rol.valor.equals(valor)) {
                return rol;
            }
        }
        return USER; // Por defecto
    }
}
