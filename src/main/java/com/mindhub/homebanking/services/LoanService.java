package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import com.mindhub.homebanking.utils.EmailUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class LoanService {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientLoanRepository clientLoanRepository;
    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private EmailService emailService;

    public ResponseEntity<Object> addLoan(Authentication authentication, LoanApplicationDTO loanApp) {
        //Obtenemos el cliente autenticado
        Client client = clientRepository.findByRut(authentication.getName());
        //Obtenemos el préstamo
        long loanId = loanApp.getLoanId().longValue();
        Loan loan = loanRepository.findById(loanId);
        //Obtenemos la cuenta para depositar el préstamo
        Account toAccountNumber = accountRepository.findByNumber(loanApp.getToAccountNumber());
        //realizar validaciones para distintos casos
        //Validación para campos vacios
        if (loanApp.getLoanId() == null || loanApp.getPayments() <= 0 || loanApp.getAmount() <= 0 || loanApp.getToAccountNumber().isEmpty()) {
            return new ResponseEntity<>("Completar todos los campos", HttpStatus.FORBIDDEN);
        }
        //Valida que la cuenta de destino exista
        if (toAccountNumber == null) {
            return new ResponseEntity<>("La cuenta de destino no existe", HttpStatus.FORBIDDEN);
        }
        //Validar que la cuenta de origen pertenece al cliente autenticado
        if (!client.getAccounts().contains(toAccountNumber)) {
            return new ResponseEntity<>("La cuenta de origen no pertenece al cliente", HttpStatus.FORBIDDEN);
        }
        //Valida que el préstamo exista
        if (loan == null) {
            return new ResponseEntity<>("El préstamo no existe", HttpStatus.FORBIDDEN);
        }
        //Valida que el monto solicitado no supere el monto máximo
        if (loanApp.getAmount() > loan.getMaxAmount()) {
            return new ResponseEntity<>("El monto supera al máximo permitido", HttpStatus.FORBIDDEN);
        }
        //Valida que la cantidad de cuotas este en el rango permitido
        if (!loan.getPayments().contains(loanApp.getPayments())) {

            return new ResponseEntity<>("cantidad de cuotas no permitida", HttpStatus.FORBIDDEN);
        }
        /*Obtenemos las cuotas del préstamo, primero inicializamos la variable payments, despues recorremos el listado y finalmente
        seteamos el nuevo valor de payments para recorrer la lista
         */
        int payments = 0;
        if (loan.getPayments().contains(loanApp.getPayments())) {
            for (int i = 0; i < loan.getPayments().size(); i++) {
                if (loan.getPayments().get(i) == loanApp.getPayments()) {
                    payments = i;
                    break;
                }
            }
        }
        //Agregamos al repositorio el nuevo préstamo
        clientLoanRepository.save(new ClientLoan(client, loan, loanApp.getAmount() + (loanApp.getAmount() * 0.2), payments));
        //Realizamos la transaccion
        transactionRepository.save(new Transaction(TransactionType.CREDIT, loanApp.getAmount(), loan.getName() + " loan approved", LocalDate.now(), toAccountNumber));
        /*if (!client.getEmail().isEmailStatus()) {
            toAccountNumber.setBalance(toAccountNumber.getBalance() + loanApp.getAmount());
            return new ResponseEntity<>("Para el envio de correo por favor validar su correo\n\n Solicitud de préstamo exitosa", HttpStatus.CREATED);
        } else {//Envio de correo por solicitud de préstamo
            sendLoanEmail(client, loan, loanApp);
            //Le agregamos a la cuenta el monto solicitado
            toAccountNumber.setBalance(toAccountNumber.getBalance() + loanApp.getAmount());
            return new ResponseEntity<>("Solicitud de préstamo exitosa", HttpStatus.CREATED);
        }
         */
        if (!client.getEmail().isEmailStatus()) {
            toAccountNumber.setBalance(toAccountNumber.getBalance() + loanApp.getAmount());
            return new ResponseEntity<>("Para el envío de correo, por favor valida tu correo\n\nSolicitud de préstamo exitosa", HttpStatus.CREATED);
        } else {
            sendLoanEmail(client, loan, loanApp);
            toAccountNumber.setBalance(toAccountNumber.getBalance() + loanApp.getAmount());
            return new ResponseEntity<>("Solicitud de préstamo exitosa", HttpStatus.CREATED);
        }
    }

    public void sendLoanEmail(Client client, Loan loan, LoanApplicationDTO loanApp) {
        EmailUtils templateGenerator = new EmailUtils();
        String emailContent = templateGenerator.generateLoanEmail(client, loan, loanApp);
        //Envío de correo
        String from = "eway-bank@sense-it.cl";
        String to = client.getEmail().getEmail().toString();
        String subject = "Aviso de préstamo recibido";
        String text = emailContent;
        emailService.send(from, to, subject, text);
    }
}