package com.gestorcorreos;

import java.util.Objects;

public class Email {
    private String asunto;
    private String contenido;
    private Contacto remitente;
    private final Destinatarios destinatarios = new Destinatarios();
    private boolean leido;
    private boolean favorito;

    public Email(String asunto, String contenido, Contacto remitente) {
        this.asunto = Objects.requireNonNullElse(asunto, "");
        this.contenido = Objects.requireNonNullElse(contenido, "");
        setRemitente(remitente);
    }

    public String getAsunto() { return asunto; }
    public String getContenido() { return contenido; }
    public Contacto getRemitente() { return remitente; }
    public Destinatarios getDestinatarios() { return destinatarios; }
    public boolean isLeido() { return leido; }
    public boolean isFavorito() { return favorito; }

    public void setAsunto(String asunto)   { this.asunto = (asunto == null ? "" : asunto); }
    public void setContenido(String texto) { this.contenido = (texto == null ? "" : texto); }

    public void setRemitente(Contacto remitente) {
        if (remitente == null) throw new IllegalArgumentException("Remitente obligatorio");
        this.remitente = remitente;
    }

    public void agregarDestinatario(Contacto c) { destinatarios.agregar(c); }

    public void marcarLeido(boolean v)    { this.leido = v; }
    public void marcarFavorito(boolean v) { this.favorito = v; }

    @Override public String toString() {
        return "Email{asunto='" + asunto + "', de=" + remitente + ", para=" + destinatarios.ver() + "}";
    }
}
