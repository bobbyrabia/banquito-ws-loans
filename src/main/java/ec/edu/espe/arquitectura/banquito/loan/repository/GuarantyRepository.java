package ec.edu.espe.arquitectura.banquito.loan.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import ec.edu.espe.arquitectura.banquito.loan.model.Guaranty;


public interface GuarantyRepository extends JpaRepository<Guaranty, Integer> {

 Guaranty findByCode(String code);
}
