package com.nickdenningart.fractal.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.nickdenningart.fractal.exception.AuthorizationException;

@Service
public class AuthorizationService {
    private final String key;

    public AuthorizationService(@Value("${x-api-key}") String key) {
        this.key = key;
    }

    public void checkKey(String key) throws AuthorizationException{
        if (! key.equals(this.key)) throw new AuthorizationException();
    }
}
