package com.mindhub.homebanking.services;

import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.utils.ApiConnection;
import com.mindhub.homebanking.utils.TimeUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URL;

@Service
public class BotService {
    @Autowired
    private ClientRepository clientRepository;

    URL url;

    public double getValueUf() {
        double valor;
        try {
            url = new URL("https://mindicador.cl/api/uf" + "/" + TimeUtils.time());
            String cadena = ApiConnection.getHTTPInputStream(url);
            JSONObject obj = new JSONObject(cadena);
            JSONArray objArray = obj.getJSONArray("serie");
            JSONObject value = objArray.getJSONObject(0);
            valor = value.getDouble("valor");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return valor;
    }

    public double getValueDollar() {
        double valor;
        try {
            url = new URL("https://mindicador.cl/api/dolar" + "/" + TimeUtils.time());
            String cadena = ApiConnection.getHTTPInputStream(url);
            JSONObject obj = new JSONObject(cadena);
            JSONArray objArray = obj.getJSONArray("serie");
            JSONObject value = objArray.getJSONObject(0);
            valor = value.getDouble("valor");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return valor;
    }
}
