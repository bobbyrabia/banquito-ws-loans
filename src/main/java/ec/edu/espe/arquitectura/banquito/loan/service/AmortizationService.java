package ec.edu.espe.arquitectura.banquito.loan.service;

import ec.edu.espe.arquitectura.banquito.loan.dto.AmortizationRQ;
import ec.edu.espe.arquitectura.banquito.loan.dto.AmortizationRS;
import ec.edu.espe.arquitectura.banquito.loan.model.Amortization;
import ec.edu.espe.arquitectura.banquito.loan.model.Loan;
import ec.edu.espe.arquitectura.banquito.loan.repository.AmortizationRepository;
import ec.edu.espe.arquitectura.banquito.loan.repository.LoanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class AmortizationService {
    private final AmortizationRepository amortizationRepository;
    private final LoanRepository loanRepository;


    public AmortizationService(AmortizationRepository amortizationRepository, LoanRepository loanRepository) {
        this.amortizationRepository = amortizationRepository;
        this.loanRepository = loanRepository;
    }

    @Transactional
    public List<Amortization> generateAmortization(AmortizationRQ amortizationRQ) {
        Loan loan = loanRepository.findById(amortizationRQ.getLoanId()).orElseThrow(() -> new RuntimeException("Loan not found"));

        if (amortizationRQ.getType().equals("FRA")){
            BigDecimal tasaInteresMensual =  BigDecimal.valueOf(loan.getInterestRate().doubleValue() / 12.0);
            BigDecimal cuotaMensual = calcularCuotaMensualFrances(
                    loan.getAmount(),
                    tasaInteresMensual,
                    loan.getRepaymentInstallments());

            List<Amortization> amortizationList = new ArrayList<>();
            BigDecimal remainingBalance = loan.getAmount();

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(loan.getApprovedDate());
            // Fixed day of month
//        int month = calendar.get(Calendar.MONTH) + 1;
//        int year = calendar.get(Calendar.YEAR);
//        int dayOfMonth = loan.getFixedDaysOfMonth();
//        calendar.set(year, month, dayOfMonth);
            for (int i = 0; i < loan.getRepaymentInstallments(); i++) {
                BigDecimal quotaInterest = remainingBalance.multiply(tasaInteresMensual);
                BigDecimal quotaCapital = cuotaMensual.subtract(quotaInterest);
                remainingBalance = remainingBalance.subtract(quotaCapital);
                Amortization amortization = Amortization.builder()
                        .type(amortizationRQ.getType())
                        .loanId(loan.getId())
                        .quotaNum(i + 1)
                        .quotaCapital(quotaCapital)
                        .quotaInterest(quotaInterest)
                        .quotaAmount(cuotaMensual)
                        .remainingBalance(remainingBalance)
                        .dueDate(calendar.getTime())
                        .build();
                if (i == 0) {
                    amortization.setQuotaStatus("CUR");
                } else {
                    amortization.setQuotaStatus("PEN");
                }
                calendar.add(Calendar.MONTH, 1);
                amortizationList.add(amortization);
            }
            return amortizationRepository.saveAll(amortizationList);
        } else if (amortizationRQ.getType().equals("ALE")){
            BigDecimal tasaInteresMensual =  BigDecimal.valueOf(loan.getInterestRate().doubleValue() / 12.0);
            List<Amortization> amortizationList = new ArrayList<>();
            BigDecimal remainingBalance = loan.getAmount();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(loan.getApprovedDate());
            for (int i = 0; i < loan.getRepaymentInstallments(); i++) {
                BigDecimal quotaInterest = remainingBalance.multiply(BigDecimal.ONE.add(tasaInteresMensual)).subtract(remainingBalance);
//                BigDecimal quotaCapital = loan.getAmount().divide(BigDecimal.valueOf(loan.getRepaymentInstallments()));
                BigDecimal quotaCapital = calcularCuotaAleman(loan.getAmount().doubleValue(),loan.getRepaymentInstallments());
                BigDecimal quotaAmount = quotaCapital.add(quotaInterest);
                remainingBalance = remainingBalance.subtract(quotaCapital);
                Amortization amortization = Amortization.builder()
                        .type(amortizationRQ.getType())
                        .loanId(loan.getId())
                        .quotaNum(i + 1)
                        .quotaCapital(quotaCapital)
                        .quotaInterest(quotaInterest)
                        .quotaAmount(quotaAmount)
                        .remainingBalance(remainingBalance)
                        .dueDate(calendar.getTime())
                        .build();
                calendar.add(Calendar.MONTH, 1);
                if (i == 0) {
                    amortization.setQuotaStatus("CUR");
                } else {
                    amortization.setQuotaStatus("PEN");
                }
                amortizationList.add(amortization);
            }
            return amortizationRepository.saveAll(amortizationList);
        }else {
            throw new RuntimeException("Type not found");
        }
    }

    private BigDecimal calcularCuotaAleman(double loanAmount, Integer repaymentInstallments) {
        double quota = loanAmount / repaymentInstallments;
        return BigDecimal.valueOf(quota);
    }

    private BigDecimal calcularCuotaMensualFrances(BigDecimal amount, BigDecimal tasaInteresMensual, Integer repaymentInstallments) {
        double factor = Math.pow(1 + tasaInteresMensual.doubleValue(), repaymentInstallments);
        double quota = amount.doubleValue() * tasaInteresMensual.doubleValue() * factor / (factor - 1);
        return BigDecimal.valueOf(quota);
    }

    public List<AmortizationRS> findByLoanUuid(String uuid) {
        List<Amortization> amortizationList = getByLoanUuid(uuid);
        return toAmortizationRSList(amortizationList);
    }

    public List<AmortizationRS> findByLoanUuidAndQuotaStatus(String uuid, String quotaStatus) {
        List<Amortization> amortizationList = amortizationRepository.findByLoan_Uuid_AndQuotaStatus(uuid, quotaStatus);
        if (amortizationList.isEmpty()) {
            throw new RuntimeException("Amortization not found");
        }
        return toAmortizationRSList(amortizationList);
    }

    private List<Amortization> getByLoanUuid(String uuid) {
        List<Amortization> amortizationList = amortizationRepository.findByLoan_Uuid(uuid);
        if (amortizationList.isEmpty()) {
            throw new RuntimeException("Amortization not found");
        }
        return amortizationList;
    }

    private List<AmortizationRS> toAmortizationRSList(List<Amortization> amortizationList) {
        List<AmortizationRS> amortizationRSList = new ArrayList<>();
        for (Amortization amortization : amortizationList) {
            AmortizationRS amortizationRS = new AmortizationRS();
            amortizationRS.setId(amortization.getId());
            amortizationRS.setLoanUuid(amortization.getLoan().getUuid());
            amortizationRS.setUuid(amortization.getUuid());
            amortizationRS.setType(amortization.getType());
            amortizationRS.setQuotaNum(amortization.getQuotaNum());
            amortizationRS.setDueDate(amortization.getDueDate());
            amortizationRS.setQuotaCapital(amortization.getQuotaCapital());
            amortizationRS.setQuotaInterest(amortization.getQuotaInterest());
            amortizationRS.setQuotaAmount(amortization.getQuotaAmount());
            amortizationRS.setRemainingBalance(amortization.getRemainingBalance());
            amortizationRS.setQuotaStatus(amortization.getQuotaStatus());
            amortizationRSList.add(amortizationRS);
        }
        return amortizationRSList;
    }

}
