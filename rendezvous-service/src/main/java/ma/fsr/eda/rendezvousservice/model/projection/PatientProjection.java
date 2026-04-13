package ma.fsr.eda.rendezvousservice.model.projection;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "patient_projection")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientProjection {
    @Id
    private String patientId;
    private String nom;
    private String email;
}