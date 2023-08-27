package ec.edu.espe.arquitectura.banquito.loan.dto;

import lombok.*;

import java.math.BigDecimal;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AmortizationSimulationRQ {
    private String type;
    private BigDecimal amount;
    private Integer repaymentInstallments;
}
