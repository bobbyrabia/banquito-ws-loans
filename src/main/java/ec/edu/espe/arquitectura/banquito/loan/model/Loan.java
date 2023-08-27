package ec.edu.espe.arquitectura.banquito.loan.model;

import java.math.BigDecimal;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "LOAN")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Loan {
    // Primary Key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LOAN_ID", nullable = false)
    private Integer id;

    @Column(name = "UUID", nullable = false)
    private String uuid;

    // Foreign Keys
    @Column(name = "ACCOUNT_ID")
    private Integer accountId;

    @Column(name = "GUARANTY_ID")
    private Integer guarantyId;

    @Column(name = "BRANCH_ID")
    private Integer branchId;

    @Column(name = "LOAN_PRODUCT_ID", nullable = false)
    private String loanProductId;

    // Attributes
    @Column(name = "ACCOUNT_HOLDER_TYPE", length = 3, nullable = false)
    private String accountHolderType;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATION_DATE", nullable = false)
    private Date creationDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "APPROVED_DATE")
    private Date approvedDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_MODIFIED_DATE", nullable = false)
    private Date lastModifiedDate;

    @Column(name = "STATE", length = 3, nullable = false)
    private String state;

    @Column(name = "NAME", length = 100, nullable = false)
    private String name;

    @Column(name = "AMOUNT", precision = 18, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(name = "PRINCIPAL_DUE", precision = 18, scale = 2, nullable = false)
    private BigDecimal principalDue;

    @Column(name = "PRINCIPAL_PAID", precision = 18, scale = 2, nullable = false)
    private BigDecimal principalPaid;

    @Column(name = "INTEREST_DUE", precision = 18, scale = 2, nullable = false)
    private BigDecimal interestDue;

    @Column(name = "INTEREST_PAID", precision = 18, scale = 2, nullable = false)
    private BigDecimal interestPaid;

    @Column(name = "PENALTY_DUE", precision = 18, scale = 2)
    private BigDecimal penalityDue;

    @Column(name = "PENALTY_PAID", precision = 18, scale = 2)
    private BigDecimal penalityPaid;

    @Column(name = "REPAYMENT_PERIOD_COUNT", nullable = false)
    private Integer repaymentPeriodCount;

    @Column(name = "REPAYMENT_PERIOD_UNIT", length = 3, nullable = false)
    private String repaymentPeriodUnit;

    @Column(name = "REPAYMENT_INSTALLMENTS", nullable = false)
    private Integer repaymentInstallments;

    @Column(name = "INTEREST_RATE", precision = 3, scale = 2, nullable = false)
    private BigDecimal interestRate;

    @Version
    @Column(name = "VERSION", nullable = false)
    private Long version;

    @ManyToOne
    @JoinColumn(name = "GUARANTY_ID", referencedColumnName = "GUARANTY_ID", insertable = false, updatable = false)
    private Guaranty guaranty;

}
