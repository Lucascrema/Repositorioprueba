package com.gestorcorreos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class RF04_BusquedaTextoLibreTest {

    @Test
    void buscarEnEntradaPorTextoLibre() {
        InMemoryEmailStore store = new InMemoryEmailStore();
        EmailService svc = new EmailService(store);

        Contacto ucp = new Contacto("Soporte UCP", "soporte@ucp.edu.ar");
        Contacto prof = new Contacto("Profesor", "jose@ucp.edu.ar");
        Contacto alumno = new Contacto("Alumno", "alumno@demo.com");

        Email e1 = svc.crear("Prueba UCP", "Estamos en Paradigmas 2", ucp, List.of(alumno));
        Email e2 = svc.crear("Recordatorio", "Examen parcial", prof, List.of(alumno));
        Email e3 = svc.crear("Hola", "Mensaje cualquiera", alumno, List.of(prof));

        svc.recibirEnEntrada(e1);
        svc.recibirEnEntrada(e2);
        svc.recibirEnEntrada(e3);

        // --- búsqueda por “UCP” ---
        var porUcp = svc.buscarEnEntradaPorTexto("UCP");
        assertEquals(3, porUcp.size()); // CORREGIDO (antes decía 2)
        assertTrue(porUcp.contains(e1));
        assertTrue(porUcp.contains(e2));
        assertTrue(porUcp.contains(e3)); // CORREGIDO: también matchea

        // --- búsqueda por alumno@demo.com ---
        var porAlumno = svc.buscarEnEntradaPorTexto("alumno@demo.com");
        assertEquals(3, porAlumno.size()); // CORREGIDO (antes decía 2)
        assertTrue(porAlumno.contains(e1));
        assertTrue(porAlumno.contains(e2));
        assertTrue(porAlumno.contains(e3));
    }
}