package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Email;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.EmailRepository;
import com.mindhub.homebanking.utils.AccountUtils;
import com.mindhub.homebanking.utils.ClientUtils;
import com.mindhub.homebanking.utils.EncryptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ClientService {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    EmailService emailService;
    @Value("${emailUsername}")
    private String userName;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmailRepository emailRepository;

    public ResponseEntity<Object> register(String firstName, String lastName, String email, String password, String rut, String referredCode) {
        //Validaciones de parametros repetidos y vacios
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);

        }
        if (clientRepository.findByRut(rut) != null) {
            return new ResponseEntity<>("El rut ya ha sido registrado anteriomente", HttpStatus.FORBIDDEN);
        }

        if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,}$")) {
            return new ResponseEntity<>("La contraseña debe contener numeros, mayusculas y minusculas", HttpStatus.FORBIDDEN);
        }
        if (rut.length() < 9) {
            return new ResponseEntity<>("el rut no es valido, debe ingresarlo sin puntos y con guion (1234567-8)", HttpStatus.FORBIDDEN);
        }
        if (emailRepository.findByEmail(email) != null) {
            return new ResponseEntity<>("El email ya existe, ingrese uno diferente", HttpStatus.FORBIDDEN);
        }

        //VALIDAMOS RUT
        boolean vr = ClientUtils.validarRut(rut);
        if (!vr) {
            return new ResponseEntity<>("el rut no es valido, debe ingresarlo sin puntos y con guion (1234567-8)", HttpStatus.FORBIDDEN);
        }

        String nameSt = firstName.substring(0, 2).toUpperCase() + lastName.substring(0, 2).toUpperCase();
        String providedCode = (referredCode.isEmpty()) ? "CODE79797" : clientRepository.findByReferredCode(referredCode).getReferredCode();

        double conditionalAmount = providedCode != "CODE79797" ? 5000d : 0d;

        //Agregamos el cliente y correo nuevo a sus repositorios
        Client client = clientRepository.save(new Client(firstName, lastName, passwordEncoder.encode(password), rut, ClientUtils.generateReferredCode(nameSt)));
        emailRepository.save(new Email(email, client));

        //Al crear el cliente nuevo se le asigna una cuenta con saldo 0
        accountRepository.save(new Account("VIN-" + AccountUtils.numCuenta(), LocalDate.now(), conditionalAmount, client));
        String validate = EncryptionUtils.encrypt(email);
        //Enviamos un email para validar
        emailService.send(userName, email, "Verificación de correo", "http://localhost:8080/api/validate/" + validate);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    public ClientDTO getClient(Authentication authentication) {
        Client client = clientRepository.findByRut(authentication.getName());
        return new ClientDTO(client);
    }

    public ResponseEntity<Object> validateEmail(String email) {
        String emailClient = EncryptionUtils.decrypt(email);
        Email validate = emailRepository.findByEmail(emailClient);
        validate.setEmailStatus(true);
        emailRepository.save(validate);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    public ResponseEntity<Object> updateEmail(Authentication authentication, String email) {
        Client client = clientRepository.findByRut(authentication.getName());
        Email updateEmail = emailRepository.findByClientId(client.getId());
        if (emailRepository.findAll().stream().findAny().equals(email)) {
            return new ResponseEntity<>("Email ya existe", HttpStatus.FORBIDDEN);
        }
        updateEmail.setEmail(email);
        updateEmail.setEmailStatus(false);
        emailRepository.save(updateEmail);
        String validate = EncryptionUtils.encrypt(email);
        //Enviamos un email para validar
        emailService.send(userName, email, "Verificación de correo", "http://localhost:8080/api/validate/" + validate);
        return new ResponseEntity<>("Email actualizado con exito", HttpStatus.ACCEPTED);
    }


}
