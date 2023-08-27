package ec.edu.espe.arquitectura.banquito.loan.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@Getter
public class ProductArrearsSettingDto {
    private Integer defaultTolerancePeriod;
    private Integer minTolerancePeriod;
    private Integer maxTolerancePeriod;
    private Integer monthlyToleranceDay;
    
}
