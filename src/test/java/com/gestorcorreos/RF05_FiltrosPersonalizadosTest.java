package com.gestorcorreos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

import com.gestorcorreos.Filtros.Filtro;

class RF05_FiltrosPersonalizadosTest {

    @Test
    void filtrosSimplesYCombinados() {
        InMemoryEmailStore store = new InMemoryEmailStore();
        EmailService svc = new EmailService(store);

        Contacto u1 = new Contacto("Admin UCP", "admin@ucp.edu.ar");
        Contacto u2 = new Contacto("RRHH UCP", "rrhh@ucp.edu.ar");
        Contacto otro = new Contacto("Google", "no-reply@gmail.com");
        Contacto me = new Contacto("Yo", "yo@demo.com");

        Email e1 = svc.crear("Importante: Inscripción", "Fechas UCP", u1, List.of(me));
        Email e2 = svc.crear("Aviso", "UCP informa novedades", u2, List.of(me));
        Email e3 = svc.crear("Promo", "Ofertas semanales", otro, List.of(me));

        // trabajamos sobre una lista (filtros no "contienen" correos, filtran dinámicamente)
        List<Email> universo = List.of(e1, e2, e3);

        Filtro deUcp = Filtro.remitenteDominio("ucp.edu.ar");
        Filtro asuntoImportante = Filtro.asuntoContiene("Importante");
        Filtro contenidoOfertas = Filtro.contenidoContiene("ofertas");

        var soloUcp = deUcp.aplicar(universo);
        assertEquals(2, soloUcp.size());

        var ucpEImportante = deUcp.and(asuntoImportante).aplicar(universo);
        assertEquals(1, ucpEImportante.size());
        assertTrue(ucpEImportante.contains(e1));

        var noUcp = deUcp.not().aplicar(universo);
        assertEquals(1, noUcp.size());
        assertTrue(noUcp.contains(e3));

        var ucpOOfertas = deUcp.or(contenidoOfertas).aplicar(universo);
        assertEquals(3, ucpOOfertas.size()); // e1,e2 (ucp) + e3 (ofertas)
    }

    @Test
    void filtroPorDominioDeDestinatario() {
        Contacto me = new Contacto("Yo", "yo@demo.com");
        Contacto ana = new Contacto("Ana", "ana@ucp.edu.ar");
        Contacto bob = new Contacto("Bob", "bob@otro.com");

        Email e1 = new Email("Hola UCP", "Texto", me); e1.agregarDestinatario(ana);
        Email e2 = new Email("Hola Otro", "Texto", me); e2.agregarDestinatario(bob);

        var filtroDestUcp = Filtro.destinatarioDominio("ucp.edu.ar");

        var res = filtroDestUcp.aplicar(List.of(e1, e2));
        assertEquals(1, res.size());
        assertTrue(res.contains(e1));
    }
}
