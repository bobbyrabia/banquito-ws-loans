package ec.edu.espe.arquitectura.banquito.loan.service;

import ec.edu.espe.arquitectura.banquito.loan.dto.RepaymentRQ;
import ec.edu.espe.arquitectura.banquito.loan.dto.RepaymentRS;
import ec.edu.espe.arquitectura.banquito.loan.model.Amortization;
import ec.edu.espe.arquitectura.banquito.loan.model.Loan;
import ec.edu.espe.arquitectura.banquito.loan.model.Repayment;
import ec.edu.espe.arquitectura.banquito.loan.repository.AmortizationRepository;
import ec.edu.espe.arquitectura.banquito.loan.repository.LoanRepository;
import ec.edu.espe.arquitectura.banquito.loan.repository.RepaymentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class RepaymentServiceTest {
    @InjectMocks
    private RepaymentService underTest;
    @Mock
    private LoanRepository loanRepository;
    @Mock
    private AmortizationRepository amortizationRepository;
    @Mock
    private RepaymentRepository repaymentRepository;

    @Test
    void doPayment() {
        //given
        String amortizationUUID = UUID.randomUUID().toString();
        RepaymentRQ repaymentRQ = RepaymentRQ.builder()
                .amortizationUuid(amortizationUUID)
                .amountToPay(BigDecimal.valueOf(2500))
                .accountTransactionId(1)
                .branchId(1)
                .build();
        String loanUUID = UUID.randomUUID().toString();
        Loan loan = getLoan(loanUUID);
        Amortization amortization = Amortization.builder()
                .id(1)
                .uuid(amortizationUUID)
                .loan(loan)
                .quotaNum(1)
                .quotaStatus("CUR")
                .quotaAmount(BigDecimal.valueOf(2500))
                .quotaInterest(BigDecimal.valueOf(250))
                .remainingBalance(BigDecimal.valueOf(2500))
                .quotaCapital(BigDecimal.valueOf(2500))
                .dueDate(new Date())
                .build();
        given(amortizationRepository.findByUuid(repaymentRQ.getAmortizationUuid())).willReturn(Optional.of(amortization));
        given(loanRepository.findById(loan.getId())).willReturn(Optional.of(loan));
        Repayment repayment = Repayment.builder()
                .id(1)
                .loanId(loan.getId())
                .amortizationId(amortization.getId())
                .branchId(repaymentRQ.getBranchId())
                .accountTransactionId(repaymentRQ.getAccountTransactionId())
                .uuid(UUID.randomUUID().toString())
                .state("PAI")
                .dueDate(amortization.getDueDate())
                .repaidDate(new Date())
                .principalDue(amortization.getRemainingBalance())
                .principalPaid(repaymentRQ.getAmountToPay())
                .interestDue(amortization.getQuotaInterest())
                .interestPaid(amortization.getQuotaInterest())
                .version(1L)
                .build();
        given(repaymentRepository.save(any())).willReturn(repayment);
        //when
        Repayment result = underTest.doPayment(repaymentRQ);
        //then
        assertEquals(1, result.getId());
        assertEquals(loan.getId(), result.getLoanId());
        assertEquals(amortization.getId(), result.getAmortizationId());
    }

    @Test
    void findByLoan_UuidRepaymentTest() {
        //given
        String uuid = UUID.randomUUID().toString();
        Repayment repayment = Repayment.builder()
                .id(1)
                .loanId(1)
                .amortizationId(1)
                .state("PAI")
                .dueDate(new Date())
                .repaidDate(new Date())
                .principalDue(BigDecimal.valueOf(2500))
                .principalPaid(BigDecimal.valueOf(250))
                .build();
        Repayment repayment1 = Repayment.builder()
                .id(2)
                .loanId(1)
                .amortizationId(2)
                .state("PAI")
                .dueDate(new Date())
                .repaidDate(new Date())
                .principalDue(BigDecimal.valueOf(2500))
                .principalPaid(BigDecimal.valueOf(250))
                .build();
        given(repaymentRepository.findByLoan_Uuid(uuid)).willReturn(List.of(repayment, repayment1));
        //when
        List<RepaymentRS> repaymentRSList = underTest.findByLoan_Uuid(uuid);
        //then
        assertEquals(2, repaymentRSList.size());
    }

    private Loan getLoan(String loanUUID) {
        return Loan.builder()
                .id(1)
                .uuid(loanUUID)
                .amount(BigDecimal.valueOf(10000))
                .interestRate(BigDecimal.valueOf(0.1))
                .repaymentInstallments(10)
                .approvedDate(new Date())
                .principalPaid(BigDecimal.valueOf(2500))
                .build();
    }
}