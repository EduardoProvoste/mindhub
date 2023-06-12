package com.mindhub.homebanking.dtos;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class LoanApplicationDTO {
    private Long loanId;
    private double amount;
    private int payments;
    private String toAccountNumber;

    public LoanApplicationDTO() {
        this.loanId = this.getLoanId();
        this.amount = this.getAmount();
        this.payments = this.getPayments();
        this.toAccountNumber = this.getToAccountNumber();
    }
}
