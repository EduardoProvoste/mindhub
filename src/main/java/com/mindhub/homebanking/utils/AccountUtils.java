package com.mindhub.homebanking.utils;

import java.util.Random;

public class AccountUtils {
    public static int numCuenta() {
        //Declaramos nuestra variable random para generar los números de las cuentas
        Random random = new Random();
        int numCuenta = random.nextInt(10000000) + 1;
        return numCuenta;
    }

}
