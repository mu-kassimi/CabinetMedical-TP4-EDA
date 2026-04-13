package ma.fsr.eda.rendezvousservice.model.projection;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "medecin_projection")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedecinProjection {
    @Id
    private String medecinId;
    private String nom;
    private String specialite;
}