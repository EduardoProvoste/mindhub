package com.mindhub.homebanking.utils;

import java.util.HashMap;
import java.util.Map;

public class ReferredCodeCounterUtils {
        private Map<String, Integer> contador;

        public ReferredCodeCounterUtils() {
            contador = new HashMap<>();
        }

        public void incrementarContador(String valor) {
            if (contador.containsKey(valor)) {
                int veces = contador.get(valor);
                contador.put(valor, veces + 1);
            } else {
                contador.put(valor, 1);
            }
        }

        public int obtenerContador(String valor) {
            return contador.getOrDefault(valor, 0);
        }
    }

