package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.ClientLoan;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ClientLoanDTO {
    @Setter(AccessLevel.NONE)
    private Long id;
    private Long loanId;
    private String name;
    private Double amount;
    private int payments;

    public ClientLoanDTO(ClientLoan clientLoan) {
        this.id = clientLoan.getId();
        this.loanId = clientLoan.getLoan().getId();
        this.name = clientLoan.getLoan().getName();
        this.amount = clientLoan.getAmount();
        this.payments = clientLoan.getPayments();
    }
}
