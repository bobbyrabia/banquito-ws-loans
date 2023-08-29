package ec.edu.espe.arquitectura.banquito.loan.controller;

import ec.edu.espe.arquitectura.banquito.loan.dto.AmortizationRQ;
import ec.edu.espe.arquitectura.banquito.loan.dto.AmortizationRS;
import ec.edu.espe.arquitectura.banquito.loan.dto.AmortizationSimulationRQ;
import ec.edu.espe.arquitectura.banquito.loan.model.Amortization;
import ec.edu.espe.arquitectura.banquito.loan.service.AmortizationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/amortization")
@CrossOrigin({"*"})
public class AmortizationController {

    private final AmortizationService amortizationService;
    public AmortizationController(AmortizationService amortizationService) {
        this.amortizationService = amortizationService;
    }
    @PostMapping("/generate")
    public ResponseEntity<List<Amortization>> generateAmortization(@RequestBody AmortizationRQ amortizationRQ) {
        return ResponseEntity.ok(amortizationService.generateAmortization(amortizationRQ));
    }

    @GetMapping("/findByLoan/{uuid}")
    public ResponseEntity<List<AmortizationRS>> findByLoan(@PathVariable(name = "uuid") String uuid) {
        return ResponseEntity.ok(amortizationService.findByLoanUuid(uuid));
    }

    @GetMapping("/findByLoan-status/{uuid}/{quotaStatus}")
    public ResponseEntity<List<AmortizationRS>> findByLoanAndQuotaStatus(@PathVariable(name = "uuid") String uuid, @PathVariable(name = "quotaStatus") String quotaStatus) {
        return ResponseEntity.ok(amortizationService.findByLoanUuidAndQuotaStatus(uuid, quotaStatus));
    }
    @PostMapping("/simulate")
    public ResponseEntity<List<AmortizationRS>> simulateAmortization(@RequestBody AmortizationSimulationRQ amortizationRQ) {
        return ResponseEntity.ok(amortizationService.simulateAmortization(amortizationRQ));
    }

}
