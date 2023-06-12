package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.utils.EmailUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDate;

@Service
public class TransactionService {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private EmailService emailService;

    public ResponseEntity<Object> addTransaction(Authentication authentication, String fromAccountNumber,
                                                 String toAccountNumber, double amount,
                                                 String description) {
        //Obtenemos el cliente autenticado
        Client client = clientRepository.findByRut(authentication.getName());
        //Obtenemos la cuenta de origen
        Account cuentaOrigen = accountRepository.findByNumber(fromAccountNumber);
        //Cuenta de destino
        Account cuentaDestino = accountRepository.findByNumber(toAccountNumber);

        //Validamos que ningun dato ingresado quede vacio
        if (fromAccountNumber.isEmpty() || toAccountNumber.isEmpty() || description.isEmpty()) {
            return new ResponseEntity<>("Faltan datos", HttpStatus.FORBIDDEN);
        }
        //Validar que el monto a transferir sea mayor a 0
        if (amount <= 0) {
            return new ResponseEntity<>("El monto debe ser mayor a 0", HttpStatus.FORBIDDEN);
        }
        //Validar que la cuenta de origen exista
        if (cuentaOrigen == null) {
            return new ResponseEntity<>("La cuenta de origen no existe", HttpStatus.FORBIDDEN);
        }
        //Validar que la cuenta de origen pertenece al cliente autenticado
        if (!client.getAccounts().contains(cuentaOrigen)) {
            return new ResponseEntity<>("La cuenta de origen no pertenece al cliente", HttpStatus.FORBIDDEN);
        }
        //Validar que la cuenta de destino exista
        if (cuentaDestino == null) {
            return new ResponseEntity<>("La cuenta de destino no existe", HttpStatus.FORBIDDEN);
        }
        //Validar que ambas cuentas no sean iguales
        if (cuentaOrigen == cuentaDestino) {
            return new ResponseEntity<>("Las cuentas no pueden ser iguales", HttpStatus.FORBIDDEN);
        }
        //Validar que el cliente tenga fondos
        if (cuentaOrigen.getBalance() < amount) {
            return new ResponseEntity<>("La cuenta no tiene fondos suficientes", HttpStatus.FORBIDDEN);
        }
        //Creamos la transferencia de la cuenta de origen a la de destino
        //Cuenta de origen
        transactionRepository.save(new Transaction(TransactionType.DEBIT, -amount, description, LocalDate.now(), cuentaOrigen));
        //Cuenta destino
        transactionRepository.save(new Transaction(TransactionType.CREDIT, amount, description, LocalDate.now(), cuentaDestino));
        if (!client.getEmail().isEmailStatus()) {
            cuentaOrigen.setBalance(cuentaOrigen.getBalance() - amount);
            //Cuenta destino
            cuentaDestino.setBalance(cuentaDestino.getBalance() + amount);
            return new ResponseEntity<>("Para el envio de correo por favor validar su correo\n\nTransferencia exitosa", HttpStatus.CREATED);
        } else {
            //Envio de correo por transferencia
            sendTransactionEmail(client, fromAccountNumber, toAccountNumber, description, amount);
            //cambiamos el valor de las cuentas para que se actualicen
            cuentaOrigen.setBalance(cuentaOrigen.getBalance() - amount);
            //Cuenta destino
            cuentaDestino.setBalance(cuentaDestino.getBalance() + amount);
            return new ResponseEntity<>("Transferencia exitosa", HttpStatus.CREATED);
        }
    }

    public void sendTransactionEmail(Client client, String fromAccount, String toAccount, String description, double amount) {
        EmailUtils templateGenerator = new EmailUtils();
        String emailContent = templateGenerator.generateTransferEmail(client, fromAccount, toAccount, description, amount);
        DecimalFormat formatea = new DecimalFormat("###,###");
        //Envío de correo
        String from = "eway-bank@sense-it.cl";
        String to = client.getEmail().getEmail().toString();
        String subject = "Aviso de transferencia de fondos";
        String text = emailContent;
        emailService.send(from, to, subject, text);
    }
}
