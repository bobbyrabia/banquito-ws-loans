package ec.edu.espe.arquitectura.banquito.loan.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "GUARANTY")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Guaranty {
    // Primary Key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GUARANTY_ID", nullable = false)
    private Integer id;

    // Foreign Keys
    @Column(name = "CLIENT_ID", length = 50)
    private String clientId;

    @Column(name = "GROUP_COMPANY_ID", length = 50)
    private String groupCompanyId;

    // Attributes

    @Column(name = "CODE", length = 10, nullable = false)
    private String code;

    @Column(name = "ASSET_NAME", length = 100)
    private String assetName;

    @Column(name = "TYPE", length = 3, nullable = false)
    private String type;

    @Column(name = "STATE", length = 3, nullable = false)
    private String state;

    @Version
    @Column(name = "VERSION", nullable = false)
    private Long version;

    // Relationships
}
