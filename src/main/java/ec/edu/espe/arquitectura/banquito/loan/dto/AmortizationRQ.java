package ec.edu.espe.arquitectura.banquito.loan.dto;

import lombok.*;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AmortizationRQ {
    private Integer loanId;
    private String type;
}
