package ma.fsr.eda.consultationservice.repository;

import ma.fsr.eda.consultationservice.model.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, String> {
}