package ec.edu.espe.arquitectura.banquito.loan.dto;

import lombok.*;

import java.math.BigDecimal;

/**
 * DTO for {@link ec.edu.espe.arquitectura.banquito.loan.model.Repayment}
 */
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class RepaymentRQ{
    private String amortizationUuid;
    private Integer branchId;
    private Integer accountTransactionId;
    private BigDecimal amountToPay;
}