package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Email;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.EmailRepository;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.utils.EncryptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ClientController {
    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private EmailRepository emailRepository;
    @Autowired
    private ClientService clientService;

    //Se devuelven todos los clientes
    @GetMapping("/clients")
    public List<ClientDTO> getClients() {
        return clientRepository.findAll().stream().map(ClientDTO::new).collect(Collectors.toList());
    }

    //Se busca un cliente especifico en base a su id
    @GetMapping("/clients/{id}")
    public ClientDTO getClientsById(@PathVariable Long id) {
        return clientRepository.findById(id).map(ClientDTO::new).orElse(null);
    }

    //Obtenemos el cliente autenticado
    @GetMapping("/clients/current")
    public ClientDTO getClient(Authentication authentication) {
        return clientService.getClient(authentication);
    }

    //Ruta para crear nuevos usuarios
    @Transactional
    @PostMapping("/clients")
    public ResponseEntity<Object> register(@RequestParam String firstName,
                                           @RequestParam String lastName,
                                           @RequestParam String email,
                                           @RequestParam String password,
                                           @RequestParam String rut,
                                           @RequestParam(required = false, defaultValue = "") String referredCode
    ) {
        return clientService.register(firstName, lastName, email, password, rut, referredCode);
    }

    @GetMapping("/validate/{email}")
    public ResponseEntity<Object> validateEmail(@PathVariable String email) {
        return clientService.validateEmail(email);
    }

    @PutMapping("/update/email")
    public ResponseEntity<Object> updateEmail(Authentication authentication, @RequestParam String email) {
        return clientService.updateEmail(authentication, email);
    }
}