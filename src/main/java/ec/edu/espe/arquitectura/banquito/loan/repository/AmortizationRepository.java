package ec.edu.espe.arquitectura.banquito.loan.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ec.edu.espe.arquitectura.banquito.loan.model.Amortization;

import java.util.List;
import java.util.Optional;

public interface AmortizationRepository extends JpaRepository<Amortization, Integer> {

    List<Amortization> findByLoan_Uuid(String uuid);
    List<Amortization> findByLoan_Uuid_AndQuotaStatus(String uuid, String quotaStatus);
    Optional<Amortization> findByUuid(String uuid);
    Optional<Amortization> findByLoan_UuidAndQuotaNum(String uuid, Integer quotaNum);

}
