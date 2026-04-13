package ma.fsr.eda.billingservice.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FactureFailedEvent {
    private String eventId;
    private String consultationId;
    private String reason;
    private LocalDateTime dateFailure;
}
