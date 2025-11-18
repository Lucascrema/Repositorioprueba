package com.gestorcorreos;

import com.gestorcorreos.Filtros.Filtro;
import com.gestorcorreos.Interfaces.ContactRepository;
import com.gestorcorreos.Interfaces.EmailStore;
import com.gestorcorreos.Interfaces.SearchSpecification;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class RF10_ExtrasCoberturaTest {

    // =========================
    //  Filtro (clase funcional)
    // =========================

    @Test
    void filtroMatchesDebeUsarElCriterio() {
        Contacto remitente = new Contacto("Juan", "juan@demo.com");
        Email e = new Email("Hola mundo", "contenido", remitente);

        Filtro filtro = Filtro.asuntoContiene("Hola");
        assertTrue(filtro.matches(e));

        Filtro filtroNo = Filtro.asuntoContiene("XYZ");
        assertFalse(filtroNo.matches(e));
    }

    @Test
    void filtroConstructorDebeValidarNombreYCriterio() {
        assertThrows(IllegalArgumentException.class,
                () -> new Filtro("", email -> true));

        assertThrows(IllegalArgumentException.class,
                () -> new Filtro("Filtro sin criterio", null));
    }

    @Test
    void filtroNormalizarDominioRamasDistintas() {
        Contacto c1 = new Contacto("A", "a@ucp.edu.ar");
        Contacto c2 = new Contacto("B", "b@gmail.com");

        Email e1 = new Email("x", "y", c1);
        Email e2 = new Email("x", "y", c2);

        // dominio sin @  -> agrega @
        Filtro f1 = Filtro.remitenteDominio("ucp.edu.ar");
        assertTrue(f1.matches(e1));
        assertFalse(f1.matches(e2));

        // dominio con @ -> lo deja igual
        Filtro f2 = Filtro.remitenteDominio("@gmail.com");
        assertTrue(f2.matches(e2));
        assertFalse(f2.matches(e1));

        // dominio nulo / vacío -> termina en "" => siempre true
        Filtro f3 = Filtro.remitenteDominio(null);
        assertTrue(f3.matches(e1));
        assertTrue(f3.matches(e2));
    }

    // =========================
    //  Contacto
    // =========================

    @Test
    void contactoEqualsHashCodeYToString() {
        Contacto c1 = new Contacto("Ana", "ana@demo.com");
        Contacto c2 = new Contacto("Ana Maria", "ana@demo.com"); // mismo mail
        Contacto c3 = new Contacto("Ana", "otra@demo.com");

        assertEquals(c1, c2);          // mismo email
        assertNotEquals(c1, c3);       // distinto email
        assertNotEquals(c1, "otra cosa");

        // hashCode consistente con equals
        assertEquals(c1.hashCode(), c2.hashCode());

        String texto = c1.toString();
        assertTrue(texto.contains("Ana"));
        assertTrue(texto.contains("ana@demo.com"));
    }

    @Test
    void contactoSettersValidanDatos() {
        Contacto c = new Contacto("Juan", "juan@demo.com");

        c.setNombre("Juan Carlos");
        assertEquals("Juan Carlos", c.getNombre());

        c.setEmail("nuevo@demo.com");
        assertEquals("nuevo@demo.com", c.getEmail());

        assertThrows(IllegalArgumentException.class, () -> c.setNombre("   "));
        assertThrows(IllegalArgumentException.class, () -> c.setEmail("sin-arroba"));
        assertThrows(IllegalArgumentException.class, () -> c.setEmail("   "));
    }

    // =========================
    //  InMemoryContactRepository
    // =========================

    @Test
    void contactRepositoryListarYPorEmailRamas() {
        ContactRepository repo = new InMemoryContactRepository();

        Contacto a = new Contacto("Ana", "ana@demo.com");
        Contacto b = new Contacto("Bob", "bob@demo.com");
        repo.agregar(a);
        repo.agregar(b);

        // listar()
        List<Contacto> todos = repo.listar();
        assertEquals(2, todos.size());
        assertTrue(todos.contains(a));
        assertTrue(todos.contains(b));

        // porEmail existente
        Optional<Contacto> ana = repo.porEmail("ana@demo.com");
        assertTrue(ana.isPresent());

        // porEmail inexistente
        assertTrue(repo.porEmail("noexiste@demo.com").isEmpty());

        // porEmail null
        assertTrue(repo.porEmail(null).isEmpty());
    }

    @Test
    void contactRepositoryEditarYEliminarRamasExtra() {
        ContactRepository repo = new InMemoryContactRepository();
        Contacto c = new Contacto("Pepe", "pepe@demo.com");
        repo.agregar(c);

        // editar solo nombre
        repo.editar("pepe@demo.com", "Jose", null);
        assertEquals("Jose", repo.porEmail("pepe@demo.com").get().getNombre());

        // editar solo email
        repo.editar("pepe@demo.com", null, "jose@demo.com");
        assertTrue(repo.porEmail("pepe@demo.com").isEmpty());
        assertTrue(repo.porEmail("jose@demo.com").isPresent());

        // eliminar inexistente (no debe explotar)
        repo.eliminar("noexiste@demo.com");
        assertEquals(1, repo.cantidad());

        // editar contacto inexistente -> excepción
        assertThrows(Exception.class,
                () -> repo.editar("inexistente@demo.com", "X", "Y"));
    }

    @Test
    void contactRepositoryAgregarMismoEmailSobrescribe() {
        ContactRepository repo = new InMemoryContactRepository();

        Contacto c1 = new Contacto("Nombre1", "igual@demo.com");
        Contacto c2 = new Contacto("Nombre2", "igual@demo.com");

        repo.agregar(c1);
        repo.agregar(c2); // mismo email, reemplaza

        assertEquals(1, repo.cantidad());
        assertEquals("Nombre2", repo.porEmail("igual@demo.com").get().getNombre());
    }

    // =========================
    //  Email
    // =========================

    @Test
    void emailSettersYToString() {
        Contacto remitente = new Contacto("Rem", "rem@demo.com");
        Email e = new Email("Asunto", "Contenido", remitente);

        // setAsunto / setContenido con null -> string vacío
        e.setAsunto(null);
        e.setContenido(null);
        assertEquals("", e.getAsunto());
        assertEquals("", e.getContenido());

        // setRemitente válido
        Contacto nuevo = new Contacto("Nuevo", "nuevo@demo.com");
        e.setRemitente(nuevo);
        assertEquals(nuevo, e.getRemitente());

        // setRemitente null -> error
        assertThrows(IllegalArgumentException.class, () -> e.setRemitente(null));

        // toString al menos no rompe y contiene datos básicos
        String s = e.toString();
        assertTrue(s.contains("Email"));
    }

    // =========================
    //  Destinatarios
    // =========================

    @Test
    void destinatariosEsVaciaYNoDuplica() {
        Destinatarios dest = new Destinatarios();
        assertTrue(dest.esVacia());

        Contacto a = new Contacto("Ana", "ana@demo.com");
        dest.agregar(a);
        assertFalse(dest.esVacia());
        assertEquals(1, dest.ver().size());

        // agregar duplicado no aumenta tamaño
        dest.agregar(a);
        assertEquals(1, dest.ver().size());

        // agregar null -> excepción
        assertThrows(IllegalArgumentException.class, () -> dest.agregar(null));
    }

    // =========================
    //  EmailService - ramas de búsqueda
    // =========================

    @Test
    void emailServiceBuscarEnEntradaPorTextoDebeManejarNullYBlanco() {
        EmailStore store = new InMemoryEmailStore();
        EmailService svc = new EmailService(store);

        // null -> lista vacía
        assertTrue(svc.buscarEnEntradaPorTexto(null).isEmpty());

        // blanco -> lista vacía
        assertTrue(svc.buscarEnEntradaPorTexto("   ").isEmpty());
    }

    @Test
    void emailServiceBuscarEnEntradaConSpecificationVacia() {
        EmailStore store = new InMemoryEmailStore();
        EmailService svc = new EmailService(store);

        Contacto c = new Contacto("A", "a@demo.com");
        Email e1 = svc.crear("x", "y", c, List.of());
        svc.recibirEnEntrada(e1);

        // specification que nunca matchea
        SearchSpecification specFalse = email -> false;
        assertTrue(svc.buscarEnEntrada(specFalse).isEmpty());

        // specification que siempre matchea
        SearchSpecification specTrue = email -> true;
        assertEquals(1, svc.buscarEnEntrada(specTrue).size());
    }
}
