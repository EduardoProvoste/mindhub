package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface AccountRepository extends JpaRepository<Account, Long> {
    //Query para obtener las cuentas del cliente autenticado
    List<Account> findByClient_id(Long id);

    //Obtener los cuenta por el número de cuenta
    Account findByNumber(String number);
}
