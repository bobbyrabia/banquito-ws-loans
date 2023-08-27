package ec.edu.espe.arquitectura.banquito.loan.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Getter
public class LoanRequirementsDto {
    private String name;
    private String description;
    private String type;
    private String documentation;
}
