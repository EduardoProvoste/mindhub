package com.mindhub.homebanking.utils;

import java.util.Random;

public class ClientUtils {

    public static boolean validarRut(String rut) {
        boolean validacion = false;
        try {
            rut = rut.toUpperCase();
            rut = rut.replace(".", "");
            rut = rut.replace("-", "");
            int rutAux = Integer.parseInt(rut.substring(0, rut.length() - 1));

            char dv = rut.charAt(rut.length() - 1);

            int m = 0, s = 1;
            for (; rutAux != 0; rutAux /= 10) {
                s = (s + rutAux % 10 * (9 - m++ % 6)) % 11;
            }
            if (dv == (char) (s != 0 ? s + 47 : 75)) {
                validacion = true;
            }
        } catch (NumberFormatException e) {
        } catch (Exception e) {
        }
        return validacion;
    }

    public static String generateReferredCode(String nameStr) {
        Random random = new Random();
        int randomNumber = random.nextInt(9000) + 1000;
        return nameStr + randomNumber;
    }
}
