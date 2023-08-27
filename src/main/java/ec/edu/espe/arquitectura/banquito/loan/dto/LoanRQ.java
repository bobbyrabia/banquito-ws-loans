package ec.edu.espe.arquitectura.banquito.loan.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class LoanRQ {
    private Integer accountId;
    private Integer guarantyId;
    private Integer branchId;
    private String loanProductId;
    private String accountHolderType;
    //private Date creationDate;
    //private Date approvedDate;
    //private Date lastModifiedDate;
    private String state;
    private String name;
    private BigDecimal amount;
    //private BigDecimal principalDue;
    private BigDecimal principalPaid;
    //private BigDecimal interestDue;
    private BigDecimal interestPaid;
    //private BigDecimal penalityDue;
    private BigDecimal penalityPaid;
    private Integer repaymentPeriodCount; 
    private String repaymentPeriodUnit; 
    //private Integer repaymentInstallments; 
    //private BigDecimal interestRate; 
    
}
