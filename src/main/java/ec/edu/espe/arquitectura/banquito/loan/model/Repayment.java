package ec.edu.espe.arquitectura.banquito.loan.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "REPAYMENT")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Repayment {
    // Primary Key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REPAYMENT_ID", nullable = false)
    private Integer id;

    // Foreign Keys
    @Column(name = "LOAN_ID", nullable = false)
    private Integer loanId;

    @Column(name = "AMORTIZATION_ID", nullable = false)
    private Integer amortizationId;

    @Column(name = "BRANCH_ID")
    private Integer branchId;

    @Column(name = "ACCOUNT_TRANSACTION_ID", nullable = false)
    private Integer accountTransactionId;

    // Attributes
    @Column(name = "UUID", length = 36, nullable = false)
    private String uuid;

    @Column(name = "STATE", length = 3, nullable = false)
    private String state;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DUE_DATE", nullable = false)
    private Date dueDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "REPAID_DATE",nullable = false)
    private Date repaidDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_PENALTY_APPLIED_DATE")
    private Date lastPenaltyAppliedDate;

    @Column(name = "PRINCIPAL_DUE", precision = 18, scale = 2, nullable = false)
    private BigDecimal principalDue;

    @Column(name = "PRINCIPAL_PAID", precision = 18, scale = 2, nullable = false)
    private BigDecimal principalPaid;

    @Column(name = "INTEREST_DUE", precision = 18, scale = 2, nullable = false)
    private BigDecimal interestDue;

    @Column(name = "INTEREST_PAID", precision = 18, scale = 2, nullable = false)
    private BigDecimal interestPaid;

    @Column(name = "PENALTY_DUE", precision = 18, scale = 2)
    private BigDecimal penaltyDue;

    @Column(name = "PENALTY_PAID", precision = 18, scale = 2)
    private BigDecimal penaltyPaid;

    @Version
    @Column(name = "VERSION", nullable = false)
    private Long version;

    // Relationships
    @ManyToOne
    @JoinColumn(name = "LOAN_ID", referencedColumnName = "LOAN_ID", insertable = false, updatable = false, nullable = false)
    private Loan loan;

    @ManyToOne
    @JoinColumn(name = "AMORTIZATION_ID", referencedColumnName = "AMORTIZATION_ID", insertable = false, updatable = false, nullable = false)
    private Amortization amortization;

    @PrePersist
    public void prePersist() {
        this.uuid = UUID.randomUUID().toString();
    }

}
