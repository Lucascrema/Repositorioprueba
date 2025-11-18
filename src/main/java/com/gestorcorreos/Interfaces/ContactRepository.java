package com.gestorcorreos.Interfaces;

import java.util.List;
import java.util.Optional;

import com.gestorcorreos.Contacto;

public interface ContactRepository {
    void agregar(Contacto c);
    void editar(String emailClave, String nuevoNombre, String nuevoEmail);
    void eliminar(String email);
    Optional<Contacto> porEmail(String email);
    List<Contacto> listar();
    int cantidad();
}