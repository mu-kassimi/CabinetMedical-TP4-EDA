package ma.fsr.eda.consultationservice.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "consultations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Consultation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    private String rendezVousId;
    private String patientId;
    private String medecinId;
    private LocalDateTime dateConsultation;
    private String statut; // CREATED, COMPLETED
}