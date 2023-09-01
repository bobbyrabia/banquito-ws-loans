package ec.edu.espe.arquitectura.banquito.loan.controller;

import ec.edu.espe.arquitectura.banquito.loan.dto.RepaymentRQ;
import ec.edu.espe.arquitectura.banquito.loan.dto.RepaymentRS;
import ec.edu.espe.arquitectura.banquito.loan.model.Repayment;
import ec.edu.espe.arquitectura.banquito.loan.service.RepaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/repayment")
public class RepaymentController {
    private final RepaymentService repaymentService;

    public RepaymentController(RepaymentService repaymentService) {
        this.repaymentService = repaymentService;
    }

    @PostMapping("/doPay")
    public ResponseEntity<Repayment> doPay(@RequestBody RepaymentRQ repaymentRQ) {
        return ResponseEntity.ok(repaymentService.doPayment(repaymentRQ));
    }

    @GetMapping("/getRepayment/{uuid}")
    ResponseEntity<List<RepaymentRS>> getRepayment(@PathVariable String uuid) {
        return ResponseEntity.ok(repaymentService.findByLoan_Uuid(uuid));
    }
}
