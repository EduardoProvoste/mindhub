package com.mindhub.homebanking.utils;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Loan;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class EmailUtils {
    public static String generateTransferEmail(Client client, String fromAccount, String toAccount, String description, double amount) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM uuuu HH:mm:ss");
        DecimalFormat decimalFormat = new DecimalFormat("###,###.##");

        String text = "Estimad@ " + client.getFirstName() + " " + client.getLastName() +
                ": le informamos que con fecha " + LocalDateTime.now().format(formatter) +
                " se realizó una transferencia con los siguientes detalles: \n\n" +
                "Cuenta de origen: " + fromAccount + "\n" +
                "Cuenta de destino: " + toAccount + "\n" +
                "Descripción: " + description + "\n" +
                "Monto: $" + decimalFormat.format(amount) + "\n" +
                "\n" +
                "Banco EWAY le recuerda que: \n" +
                "Nunca le pediremos claves o datos personales. \n" +
                "Nunca le llamaremos pidiendo información.\n" +
                "Los emails de Banco EWAY no contienen link con acceso a tu banca en línea.";

        return text;
    }

    public static String generateLoanEmail(Client client, Loan loan, LoanApplicationDTO loanApp) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM uuuu HH:mm:ss");
        DecimalFormat format = new DecimalFormat("###,###.##");

        String text = "Estimad@ " + client.getFirstName() + " " + client.getLastName() +
                ": le informamos que con fecha " +  LocalDateTime.now().format(formatter) +
                " se realizó una abono a su cuenta con los siguientes detalles: \n\n" +
                "Tipo de prestamo:   " + loan.getName() + "\n" +
                "Cuenta de destino:  " + loanApp.getToAccountNumber() +"\n" +
                "Monto:              " + format.format(loanApp.getAmount()) + "\n" +
                "Cantidad de cuotas: " + loanApp.getPayments() + "\n" +
                "Monto a pagar:      " + format.format(loanApp.getAmount()*1.2) + "\n" +

                "\n" +
                "Banco EWAY le recuerda que: \n" +
                "Nunca le pediremos claves o datos personales. \n" +
                "Nunca le llamaremos pidiendo información.\n" +
                "Los emails de Banco EWAY no contienen link con acceso a tu banca en linea.";

        return text;
    }


}
