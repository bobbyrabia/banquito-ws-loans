package ec.edu.espe.arquitectura.banquito.loan.controller;

import ec.edu.espe.arquitectura.banquito.loan.dto.GuarantyRQ;
import ec.edu.espe.arquitectura.banquito.loan.dto.LoanRQ;
import ec.edu.espe.arquitectura.banquito.loan.dto.LoanRS;
import ec.edu.espe.arquitectura.banquito.loan.model.Guaranty;
import ec.edu.espe.arquitectura.banquito.loan.model.Loan;
import ec.edu.espe.arquitectura.banquito.loan.service.LoanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoanControllerTest {
    private Loan loan;

    @Mock
    private LoanService loanService;

    @InjectMocks
    private LoanController loanController;

    @BeforeEach
    void setUp() {
        this.loanController = new LoanController(this.loanService);
        this.loan = Loan.builder().id(1)
                .uuid("abcd1234")
                .accountId(123)
                .guarantyId(456)
                .branchId(789)
                .loanProductId("lp123")
                .accountHolderType("CLI")
                .creationDate(new Date())
                .approvedDate(new Date())
                .lastModifiedDate(new Date())
                .state("APP")
                .name("Sample Loan")
                .amount(BigDecimal.valueOf(10000.00))
                .principalDue(BigDecimal.valueOf(9000.00))
                .principalPaid(BigDecimal.valueOf(2000.00))
                .interestDue(BigDecimal.valueOf(1000.00))
                .interestPaid(BigDecimal.valueOf(500.00))
                .penalityDue(BigDecimal.ZERO)
                .penalityPaid(BigDecimal.ZERO)
                .repaymentPeriodCount(12)
                .repaymentPeriodUnit("MON")
                .repaymentInstallments(12)
                .interestRate(BigDecimal.valueOf(0.10))
                .build();
    }

    @Test
    public void testObtainByUuidExistingLoan() {
        LoanRS loanRS = LoanRS.builder().id(1)
                .uuid("abcd1234")
                .accountId(123)
                .guarantyId(456)
                .branchId(789)
                .loanProductId("lp123")
                .accountHolderType("CLI")
                .creationDate(new Date())
                .approvedDate(new Date())
                .lastModifiedDate(new Date())
                .state("APP")
                .name("Sample Loan")
                .amount(BigDecimal.valueOf(10000.00))
                .principalDue(BigDecimal.valueOf(9000.00))
                .principalPaid(BigDecimal.valueOf(2000.00))
                .interestDue(BigDecimal.valueOf(1000.00))
                .interestPaid(BigDecimal.valueOf(500.00))
                .penalityDue(BigDecimal.ZERO)
                .penalityPaid(BigDecimal.ZERO)
                .repaymentPeriodCount(12)
                .repaymentPeriodUnit("MON")
                .repaymentInstallments(12)
                .interestRate(BigDecimal.valueOf(0.10))
                .build();
        when(this.loanService.listById("abcd1234")).thenReturn(loanRS);

        ResponseEntity<LoanRS> responseEntity = loanController.obtainByUuid("abcd1234");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertSame(loanRS, responseEntity.getBody());
    }

    @Test
    public void testObtainByUuidNonExistingLoan() {
        when(loanService.listById("uuid456")).thenThrow(new RuntimeException());

        ResponseEntity<LoanRS> responseEntity = loanController.obtainByUuid("uuid456");

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }

    @Test
    public void testCreateLoanSuccess() {
        LoanRQ loanRQ = new LoanRQ();
        loanRQ.setLoanProductId("1");//cambiar UUID prestamo personal
        loanRQ.setAmount(BigDecimal.valueOf(5000));
        loanRQ.setRepaymentPeriodUnit("MON");
        loanRQ.setRepaymentPeriodCount(12);
        loanRQ.setBranchId(1);
        loanRQ.setAccountId(1);
        loanRQ.setName("Prestamo personal");
        when(this.loanService.createLoan(loanRQ)).thenReturn(this.loan);

        ResponseEntity<Loan> responseEntity = loanController.createLoan(loanRQ);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertSame(this.loan, responseEntity.getBody());
    }
    @Test
    public void testCreateLoanFailure() {
        LoanRQ loanRequest = new LoanRQ();
        when(loanService.createLoan(loanRequest)).thenThrow(new RuntimeException());

        ResponseEntity<Loan> responseEntity = loanController.createLoan(loanRequest);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }

    @Test
    public void testCreateGuarantySuccess() {
        GuarantyRQ guarantyRQ = new GuarantyRQ();
        guarantyRQ.setCode("123");
        guarantyRQ.setClientId("client1");

        Guaranty guarantySaved = Guaranty.builder().id(1).clientId("client1")
                .groupCompanyId(null).code("123").assetName(null).type("GUA")
                .state("ACT").build();

        when(this.loanService.createGuaranty(guarantyRQ)).thenReturn(guarantySaved);

        ResponseEntity<Guaranty> response = this.loanController.createGuaranty(guarantyRQ);

        verify(loanService, times(1)).createGuaranty(guarantyRQ);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(guarantySaved, response.getBody());
    }
    @Test
    public void testCreateGuarantyFailure() {
        GuarantyRQ guarantyRQ = new GuarantyRQ();
        guarantyRQ.setCode("123");
        guarantyRQ.setClientId("client1");

        when(loanService.createGuaranty(guarantyRQ)).thenThrow(new RuntimeException());

        ResponseEntity<Guaranty> response = loanController.createGuaranty(guarantyRQ);

        verify(loanService, times(1)).createGuaranty(guarantyRQ);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testAddGuarantyToLoanSuccess() {
        String uuid = "1";
        LoanRQ loanRQ = new LoanRQ();
        loanRQ.setLoanProductId("1");//cambiar UUID prestamo personal
        loanRQ.setAmount(BigDecimal.valueOf(5000));
        loanRQ.setRepaymentPeriodUnit("MON");
        loanRQ.setRepaymentPeriodCount(12);
        loanRQ.setBranchId(1);
        loanRQ.setAccountId(1);
        loanRQ.setName("Prestamo personal");

        ResponseEntity<String> response = loanController.addGuarantyToLoan(loanRQ, uuid);

        verify(loanService, times(1)).addGuarantyToLoan(loanRQ, uuid);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("ok", response.getBody());
    }


}

