package ec.edu.espe.arquitectura.banquito.loan.service;

import ec.edu.espe.arquitectura.banquito.loan.dto.RepaymentRQ;
import ec.edu.espe.arquitectura.banquito.loan.dto.RepaymentRS;
import ec.edu.espe.arquitectura.banquito.loan.model.Amortization;
import ec.edu.espe.arquitectura.banquito.loan.model.Loan;
import ec.edu.espe.arquitectura.banquito.loan.model.Repayment;
import ec.edu.espe.arquitectura.banquito.loan.repository.AmortizationRepository;
import ec.edu.espe.arquitectura.banquito.loan.repository.LoanRepository;
import ec.edu.espe.arquitectura.banquito.loan.repository.RepaymentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RepaymentService {
    private final RepaymentRepository repaymentRepository;
    private final LoanRepository loanRepository;
    private final AmortizationRepository amortizationRepository;


    public RepaymentService(RepaymentRepository repaymentRepository, LoanRepository loanRepository, AmortizationRepository amortizationRepository) {
        this.repaymentRepository = repaymentRepository;
        this.loanRepository = loanRepository;
        this.amortizationRepository = amortizationRepository;
    }

    @Transactional
    public Repayment doPayment(RepaymentRQ repaymentRQ){
        Amortization amortizationTmp = amortizationRepository.findByUuid(repaymentRQ.getAmortizationUuid())
                .orElseThrow(() -> new RuntimeException("Amortization no encontrada"));
        Loan loan = loanRepository.findById(amortizationTmp.getLoan().getId())
                .orElseThrow(() -> new RuntimeException("Prestamo no encontrado"));
        if (amortizationTmp.getQuotaStatus().equals("PAI")) {
            throw new RuntimeException("Cuota ya pagada");
        }
        Repayment repayment = Repayment.builder().build();
        if(amortizationTmp.getQuotaStatus().equals("CUR")){
            if (amortizationTmp.getQuotaAmount().compareTo(repaymentRQ.getAmountToPay()) == 0) {
                amortizationTmp.setQuotaStatus("PAI");
                amortizationRepository.save(amortizationTmp);
                repayment.setLoanId(loan.getId());
                repayment.setAmortizationId(amortizationTmp.getId());
                repayment.setBranchId(repaymentRQ.getBranchId());
                repayment.setAccountTransactionId(repaymentRQ.getAccountTransactionId());
                repayment.setState("PAI");
                repayment.setDueDate(amortizationTmp.getDueDate());
                repayment.setRepaidDate(new Date());
                repayment.setPrincipalDue(amortizationTmp.getRemainingBalance());
                repayment.setPrincipalPaid(repaymentRQ.getAmountToPay());
                repayment.setInterestDue(amortizationTmp.getQuotaInterest());
                repayment.setInterestPaid(amortizationTmp.getQuotaInterest());
                loan.setPrincipalPaid(loan.getPrincipalPaid().add(amortizationTmp.getQuotaCapital()));
//                loan.setInterestPaid(loan.getInterestPaid().add(amortizationTmp.getQuotaInterest()));
                loanRepository.save(loan);
                Optional<Amortization> amortization = amortizationRepository.findByLoan_UuidAndQuotaNum(
                        amortizationTmp.getLoan().getUuid(),
                        amortizationTmp.getQuotaNum() + 1);
                if (amortization.isPresent()) {
                    amortization.get().setQuotaStatus("CUR");
                    amortizationRepository.save(amortization.get());
                }

            } else {
                throw new RuntimeException("El monto a pagar no es igual al monto de la cuota");
            }
        }
        return repaymentRepository.save(repayment);
    }

    public List<RepaymentRS> findByLoan_Uuid(String uuid){
        return toRepaymentList(repaymentRepository.findByLoan_Uuid(uuid));
    }

    private List<RepaymentRS> toRepaymentList(List<Repayment> repaymentList) {
        return  repaymentList.stream()
                .map(payment -> RepaymentRS.builder()
                        .id(payment.getId())
                        .loanId(payment.getLoanId())
                        .amortizationId(payment.getAmortizationId())
                        .state(payment.getState())
                        .dueDate(payment.getDueDate())
                        .repaidDate(payment.getRepaidDate())
                        .principalDue(payment.getPrincipalDue())
                        .principalPaid(payment.getPrincipalPaid())
                        .build())
                .collect(Collectors.toList());
    }
}
