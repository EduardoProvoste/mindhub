package com.mindhub.homebanking.utils;


public class IndexValidar {
    public static boolean guion(String inputEmail) {

        boolean estado = false;
        for (int i = 0; i < inputEmail.length(); i++) {
            if (inputEmail.charAt(i) == '-') {
                estado = true;
            }
        }
        return estado;
    }
}
