package com.mindhub.homebanking.utils;

import java.util.Random;

public class CardUtils {

    public static String getCardNumber() {
        //Obtenemos los números aleatorios para el cvv y la tarjeta
        Random random = new Random();
        String numTarjeta = random.nextInt(9000) + 1000
                + "-" + (random.nextInt(9000) + 1000)
                + "-" + (random.nextInt(9000) + 1000)
                + "-" + (random.nextInt(9000) + 1000);
        return numTarjeta;
    }

    public static int getCvv() {
        Random random = new Random();
        int cvv = random.nextInt(900) + 100;
        return cvv;
    }


}
