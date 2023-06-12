package com.mindhub.homebanking.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Email {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private boolean emailStatus;

    @OneToOne()
    @JoinColumn(name = "client_id")
    private Client client;

    public Email(String email, Client client) {
        this.email = email;
        this.client = client;
    }

    public Email() {
    }
}
