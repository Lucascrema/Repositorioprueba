package com.gestorcorreos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class RF02_EnvioCorreosTest {

    @Test
    void alEnviarCorreoDebeIrABandejaEnviados() {
        InMemoryEmailStore store = new InMemoryEmailStore();
        EmailService svc = new EmailService(store);

        Contacto juan = new Contacto("Juan","juan@demo.com");
        Contacto ana  = new Contacto("Ana", "ana@demo.com");

        Email e = svc.crear("Prueba", "Texto", juan, List.of(ana));
        svc.enviar(e);

        assertEquals(1, store.bandeja(Bandeja.ENVIADOS).size());
        assertSame(e, store.bandeja(Bandeja.ENVIADOS).get(0));
    }

    @Test
    void enviarSinDestinatariosLanzaError() {
        InMemoryEmailStore store = new InMemoryEmailStore();
        EmailService svc = new EmailService(store);

        Email e = svc.crear("Asunto", "Texto", new Contacto("J","j@demo.com"), List.of());
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> svc.enviar(e));
        assertTrue(ex.getMessage().toLowerCase().contains("al menos un destinatario"));
    }
}
