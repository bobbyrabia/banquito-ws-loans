package ec.edu.espe.arquitectura.banquito.loan.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AmortizationRS {
    Integer id;
    String loanUuid;
    String uuid;
    String type;
    Integer quotaNum;
    Date dueDate;
    BigDecimal quotaCapital;
    BigDecimal quotaInterest;
    BigDecimal quotaAmount;
    BigDecimal remainingBalance;
    String quotaStatus;
}
