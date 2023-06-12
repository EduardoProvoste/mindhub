package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ColorType;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.utils.CardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Service
public class CardService {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private CardRepository cardRepository;

    public ResponseEntity<Object> createCards(Authentication authentication,CardType cardType,ColorType cardColor){
        //Obtenemos los datos del cliente autenticado
        Client client = clientRepository.findByRut(authentication.getName());

        //Si el tipo de tarjeta a crear el cliente tiene menos de 3 por tipo se crea una nueva
        if (client.getCards().stream().filter(card -> card.getType() == cardType).count() < 3) {
            //Se crea la tarjeta
            cardRepository.save(new Card(client.getFirstName() + " " + client.getLastName(), cardType, cardColor, CardUtils.getCardNumber(), CardUtils.getCvv(), LocalDate.now(), LocalDate.now().plusYears(5), client));
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            //Si supera el máximo de tarjetas devuelve un error
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}
