package com.gestorcorreos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class Destinatarios {
    private final List<Contacto> lista = new ArrayList<>();

    public void agregar(Contacto c) {

        if (c == null) throw new IllegalArgumentException("Destinatario nulo");
        if (!lista.contains(c)) lista.add(c);
        
    }

    public boolean esVacia() { return lista.isEmpty(); }

    public List<Contacto> ver() { return Collections.unmodifiableList(lista); }

    public boolean algunoCumple(Predicate<Contacto> p) {
        for (Contacto c : lista) if (p.test(c)) return true;
        return false;
    }
}
