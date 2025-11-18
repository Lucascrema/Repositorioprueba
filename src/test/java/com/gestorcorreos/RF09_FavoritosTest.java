package com.gestorcorreos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

import com.gestorcorreos.Interfaces.SearchSpecification;

class RF09_FavoritosTest {

    @Test
    void marcarFavoritoYBuscarFavoritosEnEntrada() {
        InMemoryEmailStore store = new InMemoryEmailStore();
        EmailService svc = new EmailService(store);

        Contacto r = new Contacto("R","r@demo.com");
        Contacto a = new Contacto("A","a@demo.com");
        Contacto b = new Contacto("B","b@demo.com");

        Email e1 = svc.crear("UCP", "Info", r, List.of(a));
        Email e2 = svc.crear("General", "Mensaje", r, List.of(b));

        svc.recibirEnEntrada(e1);
        svc.recibirEnEntrada(e2);

        // marcar e1 como favorito
        svc.marcarFavorito(e1, true);
        assertTrue(e1.isFavorito());
        assertFalse(e2.isFavorito());

        // buscar FAVORITOS en ENTRADA (usando Specification)
        SearchSpecification soloFavoritos = Email::isFavorito;
        var favoritos = svc.buscarEnEntrada(soloFavoritos);

        assertEquals(1, favoritos.size());
        assertSame(e1, favoritos.get(0));
    }
}
