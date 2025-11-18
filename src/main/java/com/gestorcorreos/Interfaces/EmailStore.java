package com.gestorcorreos.Interfaces;

import com.gestorcorreos.Bandeja;
import com.gestorcorreos.Email;
import java.util.List;

public interface EmailStore {
    List<Email> bandeja(Bandeja tipo);
    void agregar(Bandeja tipo, Email e);
    boolean remover(Bandeja tipo, Email e);
}
