package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ColorType;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CardController {
    @Autowired
    CardRepository cardRepository;
    @Autowired
    ClientRepository clientRepository;

    @Autowired
    private CardService cardService;

    //Se obtienen los datos del usuario autenticado y se crean tarjetas asociadas a su perfil
    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> createCards(Authentication authentication, @RequestParam CardType cardType, @RequestParam ColorType cardColor) {
        return cardService.createCards(authentication,cardType,cardColor);
    }

    @GetMapping("/clients/current/cards")
    public List<CardDTO> getCards(Authentication authentication) {
        Client client = clientRepository.findByRut(authentication.getName());
        return cardRepository.findByClient_id(client.getId()).stream().map(CardDTO::new).collect(Collectors.toList());
    }
}
