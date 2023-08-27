package ec.edu.espe.arquitectura.banquito.loan.dto;

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
public class GuarantyRQ {
    private String code;
    private String clientId;
    private String groupCompanyId;
    private String assetName;
    private String type;
    private String state;
}
