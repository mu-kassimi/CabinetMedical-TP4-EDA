package ma.fsr.eda.billingservice.repository;

import ma.fsr.eda.billingservice.model.Facture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FactureRepository extends JpaRepository<Facture, String> {
}