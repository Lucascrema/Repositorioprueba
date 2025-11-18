package com.gestorcorreos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.gestorcorreos.Interfaces.ContactRepository;

class RF03_ContactosTest {

    @Test
    void crearEditarEliminarContactos() {
        ContactRepository repo = new InMemoryContactRepository();

        Contacto a = new Contacto("Ana", "ana@demo.com");
        repo.agregar(a);

        assertEquals(1, repo.cantidad());
        assertTrue(repo.porEmail("ana@demo.com").isPresent());

        // editar nombre y email (cambia la clave)
        repo.editar("ana@demo.com", "Ana Maria", "ana.m@demo.com");
        assertTrue(repo.porEmail("ana@demo.com").isEmpty());
        assertEquals("Ana Maria", repo.porEmail("ana.m@demo.com").get().getNombre());

        // eliminar
        repo.eliminar("ana.m@demo.com");
        assertEquals(0, repo.cantidad());
    }
}