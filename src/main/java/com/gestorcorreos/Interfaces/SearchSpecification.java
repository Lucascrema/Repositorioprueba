package com.gestorcorreos.Interfaces;

import com.gestorcorreos.Email;

public interface SearchSpecification {
    boolean matches(Email e);
}