package com.mindhub.homebanking.models;

import javax.persistence.*;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@Entity
@Getter
@Setter
public class ClientLoan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Setter(AccessLevel.NONE)
    private Long id;

    //Relación muchos es a uno con la entidad Client
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;

    //Relación muchos es a uno con la entidad Loan
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "loan_id")
    private Loan loan;
    private double amount;
    private int payments;

    public ClientLoan() {
    }

    public ClientLoan(Client client, Loan loan, Double amount, int payments) {
        this.amount = amount;
        this.payments = loan.getPayments().get(payments);
        this.client = client;
        this.loan = loan;
    }
}
