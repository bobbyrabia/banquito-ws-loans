package ec.edu.espe.arquitectura.banquito.loan.service;

import ec.edu.espe.arquitectura.banquito.loan.dto.GuarantyRQ;
import ec.edu.espe.arquitectura.banquito.loan.dto.LoanRQ;
import ec.edu.espe.arquitectura.banquito.loan.dto.LoanRS;
import ec.edu.espe.arquitectura.banquito.loan.model.Guaranty;
import ec.edu.espe.arquitectura.banquito.loan.model.Loan;
import ec.edu.espe.arquitectura.banquito.loan.repository.GuarantyRepository;
import ec.edu.espe.arquitectura.banquito.loan.repository.LoanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LoanServiceTest {

    private Loan loan;
    @InjectMocks
    private LoanService loanService;

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private GuarantyRepository guarantyRepository;

    @BeforeEach
    void setUp() {
        this.loanService = new LoanService(this.loanRepository, this.guarantyRepository);
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
    void testListById() {
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
        when(loanRepository.findByUuid("abcd1234")).thenReturn(this.loan);
        assertDoesNotThrow(() -> {
            this.loanService.listById("abcd1234");
        });
        assertThrows(RuntimeException.class, () -> {
            this.loanService.listById("abc");
        });
    }

    @Test
    public void testCreateLoanValidPersonalLoan() {
        LoanRQ loanRQ = new LoanRQ();
        loanRQ.setLoanProductId("1");//cambiar UUID prestamo personal
        loanRQ.setAmount(BigDecimal.valueOf(5000));
        loanRQ.setRepaymentPeriodUnit("MON");
        loanRQ.setRepaymentPeriodCount(12);
        loanRQ.setBranchId(1);
        loanRQ.setAccountId(1);
        loanRQ.setName("Prestamo personal");

        when(this.loanRepository.findByName(loanRQ.getName())).thenReturn(null);
        assertDoesNotThrow(() -> loanService.createLoan(loanRQ));
    }

    @Test
    public void testCreateLoanInvalidPersonalLoan() {
        LoanRQ loanRQ = new LoanRQ();
        loanRQ.setLoanProductId("1");//cambiar UUID prestamo personal
        loanRQ.setAmount(BigDecimal.valueOf(200));
        loanRQ.setRepaymentPeriodUnit("MON");
        loanRQ.setRepaymentPeriodCount(12);
        loanRQ.setBranchId(1);
        loanRQ.setAccountId(1);
        loanRQ.setName("Prestamo personal");
        loanRQ.setAccountHolderType("CLI");

        when(this.loanRepository.findByName(loanRQ.getName())).thenReturn(null);
        RuntimeException exceptionMontoMenor = assertThrows(RuntimeException.class, () -> {
            this.loanService.createLoan(loanRQ);
        });
        assertEquals("El monto no puede ser menor de $300 en este préstamo", exceptionMontoMenor.getMessage());
        loanRQ.setAmount(BigDecimal.valueOf(50000));
        RuntimeException exceptionMontoMayor = assertThrows(RuntimeException.class, () -> {
            this.loanService.createLoan(loanRQ);
        });
        assertEquals("El monto no puede exceder los $40.000 en este préstamo", exceptionMontoMayor.getMessage());
    }

    @Test
    public void testCreateLoanValidPersonalMortageLoan() {
        LoanRQ loanRQ = new LoanRQ();
        loanRQ.setLoanProductId("2");//cambiar UUID prestamo hipotecario personal
        loanRQ.setAmount(BigDecimal.valueOf(5000));
        loanRQ.setRepaymentPeriodUnit("YEA");
        loanRQ.setRepaymentPeriodCount(3);
        loanRQ.setBranchId(1);
        loanRQ.setAccountId(1);
        loanRQ.setName("Prestamo Hipotecario Personal");
        loanRQ.setAccountHolderType("CLI");

        when(this.loanRepository.findByName(loanRQ.getName())).thenReturn(null);
        assertDoesNotThrow(() -> loanService.createLoan(loanRQ));
    }

    @Test
    public void testCreateLoanInvalidPersonalMortageLoan() {
        LoanRQ loanRQ = new LoanRQ();
        loanRQ.setLoanProductId("2");//cambiar UUID prestamo hipotecario personal
        loanRQ.setAmount(BigDecimal.valueOf(2000));
        loanRQ.setRepaymentPeriodUnit("YEA");
        loanRQ.setRepaymentPeriodCount(3);
        loanRQ.setBranchId(1);
        loanRQ.setAccountId(1);
        loanRQ.setName("Prestamo hipotecario personal");
        loanRQ.setAccountHolderType("CLI");

        when(this.loanRepository.findByName(loanRQ.getName())).thenReturn(null);
        RuntimeException exceptionMontoMenor = assertThrows(RuntimeException.class, () -> {
            this.loanService.createLoan(loanRQ);
        });
        assertEquals("El monto no puede ser menor de $3.000 en este préstamo", exceptionMontoMenor.getMessage());
        loanRQ.setAccountHolderType("GRO");
        loanRQ.setAmount(BigDecimal.valueOf(3000));
        RuntimeException exceptionMontoMayor = assertThrows(RuntimeException.class, () -> {
            this.loanService.createLoan(loanRQ);
        });
        assertEquals("Este préstamo únicamente es para clientes naturales", exceptionMontoMayor.getMessage());
    }

    @Test
    public void testCreateLoanValidGroupMortageLoan() {
        LoanRQ loanRQ = new LoanRQ();
        loanRQ.setLoanProductId("3");//cambiar UUID prestamo hipotecario empresa
        loanRQ.setAmount(BigDecimal.valueOf(25000));
        loanRQ.setRepaymentPeriodUnit("YEA");
        loanRQ.setRepaymentPeriodCount(20);
        loanRQ.setBranchId(1);
        loanRQ.setAccountId(1);
        loanRQ.setName("Prestamo Hipotecario Empresa");
        loanRQ.setAccountHolderType("GRO");

        when(this.loanRepository.findByName(loanRQ.getName())).thenReturn(null);
        assertDoesNotThrow(() -> loanService.createLoan(loanRQ));
    }

    @Test
    public void testCreateLoanInvalidGroupMortageLoan() {
        LoanRQ loanRQ = new LoanRQ();
        loanRQ.setLoanProductId("3");//cambiar UUID prestamo hipotecario Empresa
        loanRQ.setAmount(BigDecimal.valueOf(20000));
        loanRQ.setRepaymentPeriodUnit("YEA");
        loanRQ.setRepaymentPeriodCount(20);
        loanRQ.setBranchId(1);
        loanRQ.setAccountId(1);
        loanRQ.setName("Prestamo hipotecario empresa");
        loanRQ.setAccountHolderType("GRO");

        when(this.loanRepository.findByName(loanRQ.getName())).thenReturn(null);
        RuntimeException exceptionMontoMenor = assertThrows(RuntimeException.class, () -> {
            this.loanService.createLoan(loanRQ);
        });
        assertEquals("El monto no puede ser menor de $25.000 en este préstamo", exceptionMontoMenor.getMessage());
        loanRQ.setAmount(BigDecimal.valueOf(25000));
        loanRQ.setAccountHolderType("CLI");
        RuntimeException exceptionCliente = assertThrows(RuntimeException.class, () -> {
            this.loanService.createLoan(loanRQ);
        });
        assertEquals("Este préstamo únicamente es para empresas o grupos", exceptionCliente.getMessage());
    }

    @Test
    public void testCreateLoanValidVehicleLoan() {
        LoanRQ loanRQ = new LoanRQ();
        loanRQ.setLoanProductId("4");//cambiar UUID prestamo vehicular
        loanRQ.setAmount(BigDecimal.valueOf(100000));
        loanRQ.setRepaymentPeriodUnit("YEA");
        loanRQ.setRepaymentPeriodCount(6);
        loanRQ.setBranchId(1);
        loanRQ.setAccountId(1);
        loanRQ.setName("Prestamo Vehicular");
        loanRQ.setAccountHolderType("CLI");

        when(this.loanRepository.findByName(loanRQ.getName())).thenReturn(null);
        assertDoesNotThrow(() -> loanService.createLoan(loanRQ));
    }

    @Test
    public void testCreateLoanInvalidVehicleLoan() {
        LoanRQ loanRQ = new LoanRQ();
        loanRQ.setLoanProductId("4");//cambiar UUID prestamo personal
        loanRQ.setAmount(BigDecimal.valueOf(2000));
        loanRQ.setRepaymentPeriodUnit("YEA");
        loanRQ.setRepaymentPeriodCount(6);
        loanRQ.setBranchId(1);
        loanRQ.setAccountId(1);
        loanRQ.setName("Prestamo vehicular");
        loanRQ.setAccountHolderType("CLI");

        when(this.loanRepository.findByName(loanRQ.getName())).thenReturn(null);
        RuntimeException exceptionMontoMenor = assertThrows(RuntimeException.class, () -> {
            this.loanService.createLoan(loanRQ);
        });
        assertEquals("El monto no puede ser menor de $3.000 en este préstamo", exceptionMontoMenor.getMessage());
        loanRQ.setAmount(BigDecimal.valueOf(160000));
        RuntimeException exceptionMontoMayor = assertThrows(RuntimeException.class, () -> {
            this.loanService.createLoan(loanRQ);
        });
        assertEquals("El monto no puede exceder los $150.000 en este préstamo", exceptionMontoMayor.getMessage());
    }

    @Test
    public void testCreateGuarantyNewGuarrantor() {
        GuarantyRQ guarantyRQ = new GuarantyRQ();
        guarantyRQ.setCode("123");
        guarantyRQ.setClientId("client1");

        Guaranty guarantySaved = Guaranty.builder().id(1).clientId("client1")
                .groupCompanyId(null).code("123").assetName(null).type("GUA")
                .state("ACT").build();

        when(guarantyRepository.findByCode("123")).thenReturn(null);
        when(guarantyRepository.save(any())).thenReturn(guarantySaved);

        Guaranty result = loanService.createGuaranty(guarantyRQ);
        assertEquals("ACT", result.getState());
        assertEquals("GUA", result.getType());
    }

    @Test
    public void testCreateGuarantyNewAsset() {
        GuarantyRQ guarantyRQ = new GuarantyRQ();
        guarantyRQ.setCode("123");
        guarantyRQ.setClientId("client1");

        Guaranty guarantySaved = Guaranty.builder().id(1).clientId(null)
                .groupCompanyId(null).code("123").assetName("Casa").type("ASS")
                .state("ACT").build();

        when(guarantyRepository.findByCode("123")).thenReturn(null);
        when(guarantyRepository.save(any())).thenReturn(guarantySaved);

        Guaranty result = loanService.createGuaranty(guarantyRQ);
        assertEquals("ACT", result.getState());
        assertEquals("ASS", result.getType());
        assertEquals("Casa", result.getAssetName());
    }

    @Test
    void testAddGuarantyToLoan(){
        LoanRQ loanRQ = new LoanRQ();
        loanRQ.setGuarantyId(1);

        Loan existingLoan = new Loan();
        Guaranty guaranty = new Guaranty();
        guaranty.setId(1);

        when(loanRepository.findByUuid(any())).thenReturn(existingLoan);
        when(guarantyRepository.findById(any())).thenReturn(Optional.of(guaranty));
        when(loanRepository.save(any())).thenReturn(existingLoan);

        Loan result = loanService.addGuarantyToLoan(loanRQ, "uuid");

        assertNotNull(result);
        assertEquals(1, result.getGuarantyId());
    }

}
