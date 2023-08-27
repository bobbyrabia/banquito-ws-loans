package ec.edu.espe.arquitectura.banquito.loan.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@Getter
public class LoanProductRS {
    private String uniqueId;
    private List<ProductArrearsSettingDto> productArrearsSettingDtos;
    private LoanRequirementsDto loanRequirementsDto;
    private BigDecimal cappingPercentage;
    private String cappingMethod;
    private String interestType;
    private String name;
    private String description;
    private BigDecimal defaultLoanAmount;
    private BigDecimal minLoanAmount;
    private BigDecimal maxLoanAmount;
    private Integer defaultNumInstallments;
    private Integer minNumInstallments;
    private Integer maxNumInstallments;
    private Integer defaultGracePeriod;
    private Integer minGracePeriod;
    private Integer maxGracePeriod;
    private String gracePeriodType;
    private Integer defaultRepaymentPeriodCount;
    private String repaymentPeriodUnit;
    private BigDecimal defaultPenaltyRate;
    private BigDecimal minPenaltyRate;
    private BigDecimal maxPenaltyRate;
    private String interestCalculationMethod;
    private String loanPenaltyCalculationMethod;
    private Boolean valid;
}

