package com.gestorcorreos;

import com.gestorcorreos.Interfaces.ContactRepository;

import java.util.*;

public class InMemoryContactRepository implements ContactRepository {

    private final Map<String, Contacto> porEmail = new HashMap<>();

    @Override public void agregar(Contacto c) {
        if (c == null) throw new IllegalArgumentException("Contacto nulo");
        porEmail.put(c.getEmail(), c);
    }

    @Override public void editar(String emailClave, String nuevoNombre, String nuevoEmail) {
        Contacto actual = porEmail.get(emailClave == null ? "" : emailClave.toLowerCase());
        if (actual == null) throw new NoSuchElementException("No existe: " + emailClave);

        if (nuevoNombre != null && !nuevoNombre.isBlank()) actual.setNombre(nuevoNombre);
        if (nuevoEmail != null && !nuevoEmail.isBlank() && !nuevoEmail.equalsIgnoreCase(emailClave)) {
            porEmail.remove(emailClave.toLowerCase());
            actual.setEmail(nuevoEmail);
            porEmail.put(actual.getEmail(), actual);
        }
    }

    @Override public void eliminar(String email) {
        if (email != null) porEmail.remove(email.toLowerCase());
    }

    @Override public Optional<Contacto> porEmail(String email) {
        if (email == null) return Optional.empty();
        return Optional.ofNullable(porEmail.get(email.toLowerCase()));
    }

    @Override public List<Contacto> listar() { return new ArrayList<>(porEmail.values()); }

    @Override public int cantidad() { return porEmail.size(); }
}