package ec.edu.espe.arquitectura.banquito.loan.service;

import ec.edu.espe.arquitectura.banquito.loan.dto.AmortizationRQ;
import ec.edu.espe.arquitectura.banquito.loan.dto.AmortizationRS;
import ec.edu.espe.arquitectura.banquito.loan.dto.AmortizationSimulationRQ;
import ec.edu.espe.arquitectura.banquito.loan.model.Amortization;
import ec.edu.espe.arquitectura.banquito.loan.model.Loan;
import ec.edu.espe.arquitectura.banquito.loan.repository.AmortizationRepository;
import ec.edu.espe.arquitectura.banquito.loan.repository.LoanRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AmortizationServiceTest {

    @InjectMocks
    private AmortizationService underTest;
    @Mock
    private AmortizationRepository amortizationRepository;
    @Mock
    private LoanRepository loanRepository;

    @Test
    void simulateAmortizationFrenchTest() {
        //given
        AmortizationSimulationRQ amortizationRQ = AmortizationSimulationRQ.builder()
                .type("FRA")
                .amount(BigDecimal.valueOf(10000))
                .repaymentInstallments(10)
                .build();
        //when
        List<AmortizationRS> actual = underTest.simulateAmortization(amortizationRQ);
        //then
        assertEquals(amortizationRQ.getRepaymentInstallments(), actual.size());
    }

    @Test
    void simulateAmortizationGermanTest() {
        //given
        AmortizationSimulationRQ amortizationRQ = AmortizationSimulationRQ.builder()
                .type("ALE")
                .amount(BigDecimal.valueOf(10000))
                .repaymentInstallments(10)
                .build();
        //when
        List<AmortizationRS> actual = underTest.simulateAmortization(amortizationRQ);
        //then
        assertEquals(amortizationRQ.getRepaymentInstallments(), actual.size());
    }

    @Test
    void generateAmortizationThrowExceptionWhenLoanNotFound() {
        //given
        Loan loan = getLoan(UUID.randomUUID().toString());
        AmortizationRQ amortizationRQ = AmortizationRQ.builder()
                .loanId(loan.getId())
                .type("FRA")
                .build();
        given(loanRepository.findById(loan.getId())).willReturn(Optional.empty());
        //when
        //then
        assertThatThrownBy(() -> underTest.generateAmortization(amortizationRQ))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Loan not found");
    }

    @Test
    void generateAmortizationFrenchTest() {
        //given
        Loan loan = getLoan(UUID.randomUUID().toString());
        loan.setRepaymentInstallments(2);
        AmortizationRQ amortizationRQ = AmortizationRQ.builder()
                .loanId(loan.getId())
                .type("FRA")
                .build();
        Amortization amortization = getFirstQuota(loan);
        Amortization amortization1 = getSecondQuota(loan);
        List<Amortization> amortizationList = List.of(amortization, amortization1);
        given(loanRepository.findById(loan.getId())).willReturn(Optional.of(loan));
        //when
        given(amortizationRepository.saveAll(any())).willReturn(amortizationList);
        List<Amortization> amortizationResponse = underTest.generateAmortization(amortizationRQ);
        //then
        assertEquals(loan.getRepaymentInstallments(), amortizationResponse.size());
    }

    @Test
    void generateAmortizationGermanTest() {
        //given
        Loan loan = getLoan(UUID.randomUUID().toString());
        loan.setRepaymentInstallments(2);
        AmortizationRQ amortizationRQ = AmortizationRQ.builder()
                .loanId(loan.getId())
                .type("ALE")
                .build();
        Amortization amortization = getFirstQuota(loan);
        Amortization amortization1 = getSecondQuota(loan);
        List<Amortization> amortizationList = List.of(amortization, amortization1);
        given(loanRepository.findById(loan.getId())).willReturn(Optional.of(loan));
        //when
        given(amortizationRepository.saveAll(any())).willReturn(amortizationList);
        List<Amortization> amortizationResponse = underTest.generateAmortization(amortizationRQ);
        //then
        assertEquals(loan.getRepaymentInstallments(), amortizationResponse.size());
    }

    @Test
    void generateAmortizationThrowExceptionTypeNotFound() {
        //given
        Loan loan = getLoan(UUID.randomUUID().toString());
        loan.setRepaymentInstallments(2);
        AmortizationRQ amortizationRQ = AmortizationRQ.builder()
                .loanId(loan.getId())
                .type("ITA")
                .build();
        given(loanRepository.findById(loan.getId())).willReturn(Optional.of(loan));
        //when
        //then
        assertThatThrownBy(() -> underTest.generateAmortization(amortizationRQ))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Type not found");

    }

    @Test
    void findByLoanUuidAmortizationTest() {
        //given
        String loanUUID = UUID.randomUUID().toString();
        Loan loan = getLoan(loanUUID);
        Amortization amortization = getFirstQuota(loan);
        Amortization amortization1 = getSecondQuota(loan);
        List<Amortization> amortizationList = List.of(amortization, amortization1);
        given(amortizationRepository.findByLoan_Uuid(loanUUID)).willReturn(amortizationList);
        //when
        List<AmortizationRS> actual = underTest.findByLoanUuid(loanUUID);
        //then
        assertEquals(amortizationList.size(), actual.size());
    }

    @Test
    void findByLoanUuidAmortizationTestTrowException() {
        //given
        String loanUUID = UUID.randomUUID().toString();
        List<Amortization> amortizationList = new ArrayList<>();
        given(amortizationRepository.findByLoan_Uuid(loanUUID)).willReturn(amortizationList);
        //when
        //then
        assertThatThrownBy(() -> underTest.findByLoanUuid(loanUUID))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Amortization not found");
    }
    @Test
    void findByLoanUuidAndQuotaStatusAmortizationTest() {
        //given
        String loanUUID = UUID.randomUUID().toString();
        Loan loan = getLoan(loanUUID);
        Amortization amortization = getFirstQuota(loan);
        Amortization amortization1 = getSecondQuota(loan);
        List<Amortization> amortizationList = List.of(amortization, amortization1);
        given(amortizationRepository.findByLoan_Uuid_AndQuotaStatus(loanUUID, "PEN")).willReturn(amortizationList);
        //when
        List<AmortizationRS> actual = underTest.findByLoanUuidAndQuotaStatus(loanUUID, "PEN");
        //then
        assertEquals(amortizationList.size(), actual.size());
    }

    @Test
    void findByLoanUuidAndQuotaStatusAmortizationTestThrowException() {
        //given
        String loanUUID = UUID.randomUUID().toString();
        List<Amortization> amortizationList = new ArrayList<>();
        given(amortizationRepository.findByLoan_Uuid_AndQuotaStatus(loanUUID, "PEN")).willReturn(amortizationList);
        //when
        //then
        assertThatThrownBy(() -> underTest.findByLoanUuidAndQuotaStatus(loanUUID, "PEN"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Amortization not found");
    }

    private Loan getLoan(String loanUUID) {
        return Loan.builder()
                .id(1)
                .uuid(loanUUID)
                .amount(BigDecimal.valueOf(10000))
                .interestRate(BigDecimal.valueOf(0.1))
                .repaymentInstallments(10)
                .approvedDate(new Date())
                .build();
    }
    private Amortization getFirstQuota(Loan loan) {
        return Amortization.builder()
                .id(1)
                .loanId(1)
                .uuid(UUID.randomUUID().toString())
                .type("FRA")
                .quotaNum(0)
                .remainingBalance(BigDecimal.valueOf(4000))
                .quotaCapital(BigDecimal.valueOf(1000))
                .quotaInterest(BigDecimal.valueOf(100))
                .quotaAmount(BigDecimal.valueOf(1100))
                .remainingBalance(BigDecimal.valueOf(1000))
                .creationDate(new Date())
                .dueDate(new Date())
                .quotaStatus("PEN")
                .version(1L)
                .loan(loan)
                .build();
    }
    private Amortization getSecondQuota(Loan loan) {
        return Amortization.builder()
                .id(2)
                .loanId(1)
                .uuid(UUID.randomUUID().toString())
                .type("FRA")
                .quotaNum(1)
                .remainingBalance(BigDecimal.valueOf(3000))
                .quotaCapital(BigDecimal.valueOf(1000))
                .quotaInterest(BigDecimal.valueOf(100))
                .quotaAmount(BigDecimal.valueOf(1100))
                .remainingBalance(BigDecimal.valueOf(1000))
                .creationDate(new Date())
                .dueDate(new Date())
                .quotaStatus("PEN")
                .version(1L)
                .loan(loan)
                .build();
    }
}