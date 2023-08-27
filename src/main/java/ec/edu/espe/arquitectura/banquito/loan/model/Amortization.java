package ec.edu.espe.arquitectura.banquito.loan.model;

import java.math.BigDecimal;
import java.util.Date;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "AMORTIZATION")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Amortization {
    // Primary Key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AMORTIZATION_ID", nullable = false)
    private Integer id;

    // Foreign Keys
    @Column(name = "LOAN_ID", nullable = false)
    private Integer loanId;

    // Attributes
    @Column(name = "UUID", length = 36, nullable = false)
    private String uuid;

    @Column(name = "TYPE", length = 3, nullable = false)
    private String type;

    @Column(name = "QUOTA_NUM", nullable = false)
    private Integer quotaNum;

    @Column(name = "REMAINING_BALANCE", precision = 18, scale = 2, nullable = false)
    private BigDecimal remainingBalance;

    @Column(name = "QUOTA_CAPITAL", precision = 18, scale = 2, nullable = false)
    private BigDecimal quotaCapital;

    @Column(name = "QUOTA_INTEREST", precision = 18, scale = 2, nullable = false)
    private BigDecimal quotaInterest;

    @Column(name = "QUOTA_AMOUNT", precision = 18, scale = 2, nullable = false)
    private BigDecimal quotaAmount;

    @Column(name = "QUOTA_STATUS", precision = 18, scale = 2, nullable = false)
    private String quotaStatus;

    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    @Column(name = "CREATION_DATE", nullable = false)
    private Date creationDate;

    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    @Column(name = "LAST_MODIFIED_DATE")
    private Date lastModifiedDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DUE_DATE", nullable = false)
    private Date dueDate;

    @Version
    @Column(name = "VERSION", nullable = false)
    private Long version;



    // Relationships
    @ManyToOne
    @JoinColumn(name = "LOAN_ID", referencedColumnName = "LOAN_ID", insertable = false, updatable = false, nullable = false)
    private Loan loan;

    @PrePersist
    public void prePersist() {
        this.creationDate = new Date();
        this.uuid = java.util.UUID.randomUUID().toString();
    }

}
