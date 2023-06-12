package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class AccountService {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AccountRepository accountRepository;

    public ResponseEntity<Object> createAccount(Authentication authentication) {
        //Obtenemos los datos del cliente autenticado
        Client client = clientRepository.findByRut(authentication.getName());
        //Obtenemos el total de cuentas asociadas al cliente y si es mayor a 3 no deja agregar más
        if (client.getAccounts().stream().count() < 3) {
            //Se agrega la cuenta al cliente autenticado
            accountRepository.save(new Account("VIN" + AccountUtils.numCuenta() + 1, LocalDate.now(), 0, client));
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            //Si supera el máximo de cuentas devuelve un error
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}
