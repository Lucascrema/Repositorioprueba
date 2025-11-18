package com.gestorcorreos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class RF08_LeidoNoLeidoTest {

    @Test
    void marcarComoLeidoYNoLeido() {
        EmailService svc = new EmailService(new InMemoryEmailStore());
        Email e = svc.crear("Asunto","Contenido", new Contacto("J","j@demo.com"), List.of(new Contacto("A","a@demo.com")));

        assertFalse(e.isLeido());
        svc.marcarLeido(e, true);
        assertTrue(e.isLeido());
        svc.marcarLeido(e, false);
        assertFalse(e.isLeido());
    }
}
