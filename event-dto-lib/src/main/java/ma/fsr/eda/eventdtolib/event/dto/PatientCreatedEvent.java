package ma.fsr.eda.eventdtolib.event.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientCreatedEvent {
    private String eventId;
    private String patientId;
    private String nom;
    private String email;
    private LocalDateTime dateCreation;
}
