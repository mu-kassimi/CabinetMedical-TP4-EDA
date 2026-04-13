package ma.fsr.eda.rendezvousservice.repository;

import ma.fsr.eda.rendezvousservice.model.RendezVous;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RendezVousRepository extends JpaRepository<RendezVous, String> {
}