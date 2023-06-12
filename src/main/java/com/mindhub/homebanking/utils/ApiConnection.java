package com.mindhub.homebanking.utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class ApiConnection {

    public static String getHTTPInputStream(URL url) throws Exception{

        HttpURLConnection connection;
            try {
                connection = (HttpURLConnection) url.openConnection();

                int resCode = connection.getResponseCode();
                if (resCode == HttpURLConnection.HTTP_OK) {
                    Scanner scanner = new Scanner(url.openStream());
                    StringBuilder response = new StringBuilder();
                    while (scanner.hasNextLine()) {
                        response.append(scanner.nextLine());
                    }
                    scanner.close();
                    return response.toString();
                }else if(resCode == HttpURLConnection.HTTP_MOVED_TEMP || resCode == HttpURLConnection.HTTP_MOVED_PERM){
                    String redirectedUrl = connection.getHeaderField("Location");
                    url = new URL(redirectedUrl);
                }
                else {
                    throw new MalformedURLException("No se puede conectar a la url ["+url.toString()+"] Code: "+resCode);
                   }
                } catch(IOException e){
                    throw new RuntimeException(e);
                }
        return null;
    }

}
