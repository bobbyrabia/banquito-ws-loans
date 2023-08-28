package ec.edu.espe.arquitectura.banquito.loan.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import ec.edu.espe.arquitectura.banquito.loan.dto.GuarantyRQ;
import ec.edu.espe.arquitectura.banquito.loan.dto.LoanProductRS;
import ec.edu.espe.arquitectura.banquito.loan.dto.LoanRQ;
import ec.edu.espe.arquitectura.banquito.loan.dto.LoanRS;
import ec.edu.espe.arquitectura.banquito.loan.model.Guaranty;
import ec.edu.espe.arquitectura.banquito.loan.model.Loan;
import ec.edu.espe.arquitectura.banquito.loan.service.LoanService;

import java.util.List;

@RestController
@RequestMapping("/api/v2/loans")
@CrossOrigin
public class LoanController {
    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<LoanRS> obtainByUuid(
            @PathVariable(name = "uuid") String uuid) {
        try {
            LoanRS loan = this.loanService.listById(uuid);
            return ResponseEntity.ok(loan);
        } catch (RuntimeException rte) {
            return ResponseEntity.notFound().build();
        }

    }

    @PostMapping
    public ResponseEntity<Loan> createLoan(@RequestBody LoanRQ loan) {
        try {
            Loan loanRS = this.loanService.createLoan(loan);
            return ResponseEntity.ok(loanRS);
        } catch (RuntimeException rte) {
            return ResponseEntity.badRequest().build();

        }
    }

        @PostMapping("/guaranty")
    public ResponseEntity<Guaranty> createGuaranty(@RequestBody GuarantyRQ guaranty) {
        try {
            Guaranty guarantyRS = this.loanService.createGuaranty(guaranty);
            return ResponseEntity.ok(guarantyRS);
        } catch (RuntimeException rte) {
            return ResponseEntity.badRequest().build();

        }
    }

    @PutMapping("/addGuaranty/{uuid}")
    public ResponseEntity<String> addGuarantyToLoan(@RequestBody LoanRQ loan,
            @PathVariable(name = "uuid") String uuid) {
        try {
            // Loan loanRS = this.loanService.addGuarantyToLoan(loan);
            this.loanService.addGuarantyToLoan(loan, uuid);
            return ResponseEntity.ok("ok");
        } catch (RuntimeException rte) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(rte.getMessage());

        }
    }

    @GetMapping("/getLoansByAccount/{accountId}")
    public ResponseEntity<List<LoanRS>> obtainLoansByAccount(@PathVariable(name = "accountId") Integer accountId) {
        try {
            return ResponseEntity.ok( this.loanService.listByAccountId(accountId));
        } catch (RuntimeException rte) {
            return ResponseEntity.notFound().build();
        }
    }
}
