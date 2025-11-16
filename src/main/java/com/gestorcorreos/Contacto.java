package com.gestorcorreos;

import java.util.Objects;

public class Contacto {
    private String nombre;
    private String email;

    public Contacto(String nombre, String email) {
        setNombre(nombre);
        setEmail(email);
    }

    public String getNombre() { return nombre; }
    public String getEmail()  { return email; }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.isBlank())
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        this.nombre = nombre;
    }

    public void setEmail(String email) {
        if (email == null || email.isBlank() || !email.contains("@"))
            throw new IllegalArgumentException("Email inválido");
        this.email = email.toLowerCase();
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Contacto)) return false;
        return Objects.equals(email, ((Contacto) o).email);
    }

    @Override public int hashCode() { return Objects.hash(email); }

    @Override public String toString() { return nombre + " <" + email + ">"; }
}