package com.gestorcorreos.Filtros;

import com.gestorcorreos.Email;
import com.gestorcorreos.Interfaces.SearchSpecification;
import com.gestorcorreos.Contacto;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Filtros implements SearchSpecification {
    private final String nombre;
    private final Predicate<Email> criterio;

    public Filtros(String nombre, Predicate<Email> criterio) {
        if (nombre == null || nombre.isBlank()) throw new IllegalArgumentException("Nombre de filtro vacío");
        if (criterio == null) throw new IllegalArgumentException("Criterio nulo");
        this.nombre = nombre;
        this.criterio = criterio;
    }

    public String getNombre() { return nombre; }

    @Override public boolean matches(Email e) { return criterio.test(e); }

    public List<Email> aplicar(List<Email> emails) {
        return emails.stream().filter(criterio).collect(Collectors.toList());
    }

    public Filtros and(Filtros otro) { return new Filtros(this.nombre + " AND " + otro.nombre, this.criterio.and(otro.criterio)); }
    public Filtros or(Filtros otro)  { return new Filtros(this.nombre + " OR " + otro.nombre,  this.criterio.or(otro.criterio)); }
    public Filtros not()            { return new Filtros("NOT " + this.nombre, this.criterio.negate()); }

    // Fábricas típicas:
    public static Filtros asuntoContiene(String texto) {
        String t = texto == null ? "" : texto.toLowerCase();
        return new Filtros("Asunto~'" + texto + "'", e -> e.getAsunto().toLowerCase().contains(t));
    }
    public static Filtros contenidoContiene(String texto) {
        String t = texto == null ? "" : texto.toLowerCase();
        return new Filtros("Contenido~'" + texto + "'", e -> e.getContenido().toLowerCase().contains(t));
    }
    public static Filtros remitenteDominio(String dominio) {
        String d = normalizarDominio(dominio);
        return new Filtros("Remitente@" + d, e -> e.getRemitente().getEmail().toLowerCase().endsWith(d));
    }
    public static Filtros destinatarioDominio(String dominio) {
        String d = normalizarDominio(dominio);
        return new Filtros("Para@" + d, e -> e.getDestinatarios().algunoCumple(c -> c.getEmail().toLowerCase().endsWith(d)));
    }

    private static String normalizarDominio(String dominio) {
        if (dominio == null || dominio.isBlank()) return "";
        String d = dominio.toLowerCase();
        return d.startsWith("@") ? d : "@" + d;
        }
}
