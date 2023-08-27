package ec.edu.espe.arquitectura.banquito.loan.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

/**
 * DTO for {@link ec.edu.espe.arquitectura.banquito.loan.model.Repayment}
 */
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class RepaymentRS{
    private Integer id;
    private Integer loanId;
    private Integer amortizationId;
    private String state;
    private Date dueDate;
    private Date repaidDate;
    private BigDecimal principalDue;
    private BigDecimal principalPaid;
}