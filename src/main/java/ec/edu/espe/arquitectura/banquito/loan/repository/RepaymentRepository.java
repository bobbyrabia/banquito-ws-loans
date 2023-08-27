package ec.edu.espe.arquitectura.banquito.loan.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ec.edu.espe.arquitectura.banquito.loan.model.Repayment;

import java.util.List;


public interface RepaymentRepository extends JpaRepository<Repayment, Integer> {

    List<Repayment> findByLoan_Uuid(String uuid);

}
