package tchos.gesprod.produit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProduitRepository extends JpaRepository<Produit, UUID> {
    boolean existsDistinctByNomProduit(String nomProduit);
    List<Produit> findByCategory(UUID categoryId);
}
