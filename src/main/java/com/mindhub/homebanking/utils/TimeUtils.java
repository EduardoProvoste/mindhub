package com.mindhub.homebanking.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URL;
import java.util.Scanner;

public class TimeUtils {
    public static String time() {
        URL url;
        try {
            // URL del servicio de tiempo
            url = new URL("http://worldtimeapi.org/api/timezone/America/Santiago");

            // Leer la respuesta del servicio
            Scanner scanner = new Scanner(url.openStream());
            StringBuilder response = new StringBuilder();
            while (scanner.hasNextLine()) {
                response.append(scanner.nextLine());
            }
            scanner.close();

            // Analizar la respuesta JSON
            JsonObject jsonObject = JsonParser.parseString(response.toString()).getAsJsonObject();
            String dateTime = jsonObject.get("datetime").getAsString();

            // Formatear la fecha en el formato deseado(dd-mm-yyyy)
            return dateTime.substring(8, 10) + "-" + dateTime.substring(5, 7) + "-" + dateTime.substring(0, 4);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
