package ec.edu.espe.arquitectura.banquito.loan.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ec.edu.espe.arquitectura.banquito.loan.model.Loan;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Integer> {

    Loan findByName(String name);

    Loan findByUuid(String uuid);
    List<Loan> findByAccountId(Integer accountId);

}
