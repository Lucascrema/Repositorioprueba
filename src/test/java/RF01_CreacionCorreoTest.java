package com.gestorcorreos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class RF01_CreacionCorreoTest {

    @Test
    void crearCorreoConRemitenteYVariosDestinatarios() {
        EmailService svc = new EmailService(new InMemoryEmailStore());

        Contacto remitente = new Contacto("Juan", "juan@demo.com");
        Contacto ana = new Contacto("Ana", "ana@demo.com");
        Contacto nico = new Contacto("Nico", "nico@demo.com");

        Email e = svc.crear("Asunto X", "Contenido Y", remitente, List.of(ana, nico));

        assertEquals("Asunto X", e.getAsunto());
        assertEquals("Contenido Y", e.getContenido());
        assertEquals(remitente, e.getRemitente());
        assertEquals(2, e.getDestinatarios().ver().size());
        assertTrue(e.getDestinatarios().ver().contains(ana));
        assertTrue(e.getDestinatarios().ver().contains(nico));
    }

    @Test
    void alCrearContenidoNuloSeNormalizaAStringVacio() {
        EmailService svc = new EmailService(new InMemoryEmailStore());
        Email e = svc.crear("Hola", null, new Contacto("A","a@demo.com"), List.of());
        assertEquals("", e.getContenido());
    }
}