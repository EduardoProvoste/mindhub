package com.mindhub.homebanking.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String password;
    private String rut;
    private String referredCode;

    @OneToOne(mappedBy = "client")
    private Email email;

    @OneToMany(mappedBy="client", fetch=FetchType.EAGER)
    Set<Account> accounts = new HashSet<>();

    @OneToMany(mappedBy = "client", fetch=FetchType.EAGER)
    Set<ClientLoan> clientLoans = new HashSet<>();

    @OneToMany(mappedBy="client", fetch=FetchType.EAGER)
    Set<Card> cards = new HashSet<>();


    public Client(String firstName, String lastName, String password, String rut, String referredCode) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.rut = rut;
        this.referredCode = referredCode;
    }

    public Client() {
    }

}
