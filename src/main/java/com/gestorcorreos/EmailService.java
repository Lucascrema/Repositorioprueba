package com.gestorcorreos;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.gestorcorreos.Interfaces.EmailStore;
import com.gestorcorreos.Interfaces.SearchSpecification;

public class EmailService {

    private final EmailStore store;

    public EmailService(EmailStore store) { this.store = store; }

    // RF-01
    public Email crear(String asunto, String contenido, Contacto remitente, List<Contacto> destinatarios) {
        Email e = new Email(asunto, contenido, remitente);
        if (destinatarios != null) for (Contacto c : destinatarios) e.agregarDestinatario(c);
        return e;
    }

    // RF-02
    public void enviar(Email e) {
        if (e.getDestinatarios().esVacia())
            throw new IllegalStateException("El email debe tener al menos un destinatario");
        store.agregar(Bandeja.ENVIADOS, e);
    }

    // RF-06
    public void mover(Bandeja origen, Bandeja destino, Email e) {
        if (store.remover(origen, e)) store.agregar(destino, e);
    }

    public void restaurarDeEliminados(Email e) { mover(Bandeja.ELIMINADOS, Bandeja.ENTRADA, e); }

    // RF-07
    public void guardarBorrador(Email e) { store.agregar(Bandeja.BORRADORES, e); }

    // RF-08 / RF-09
    public void marcarLeido(Email e, boolean v)    { e.marcarLeido(v); }
    public void marcarFavorito(Email e, boolean v) { e.marcarFavorito(v); }

    // RF-04 — Búsqueda OOP mediante Specification (puede ser un Filtro)
    public List<Email> buscarEnEntrada(SearchSpecification spec) {
        List<Email> entrada = store.bandeja(Bandeja.ENTRADA);
        List<Email> result = new ArrayList<>();
        for (Email e : entrada) if (spec.matches(e)) result.add(e);
        return result;
    }

    // Utilidades para demo/tests
    public void recibirEnEntrada(Email e) { store.agregar(Bandeja.ENTRADA, e); }

    public List<Email> ver(Bandeja b) { return store.bandeja(b); }

    // Búsqueda por texto libre convertida a una Specification simple (sin streams aquí)
    public List<Email> buscarEnEntradaPorTexto(String texto) {
        if (texto == null || texto.isBlank()) return List.of();
        final String q = texto.toLowerCase(Locale.ROOT);
        return buscarEnEntrada(email -> 
            email.getAsunto().toLowerCase().contains(q)
         || email.getContenido().toLowerCase().contains(q)
         || email.getRemitente().getNombre().toLowerCase().contains(q)
         || email.getRemitente().getEmail().toLowerCase().contains(q)
         || email.getDestinatarios().algunoCumple(c ->
                c.getNombre().toLowerCase().contains(q) || c.getEmail().toLowerCase().contains(q))
        );
    }
}
