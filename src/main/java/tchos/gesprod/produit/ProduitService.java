package tchos.gesprod.produit;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProduitService {

    private final ProduitRepository produitRepository;

    // Conversion de Produit en ProduitDTO
    public ProduitDTO mapToDTO(Produit produit) {
        return new ProduitDTO(
                produit.getIdProduit(),
                produit.getNomProduit(),
                produit.getPrixProduit(),
                isPerime(produit.getDateExpiration()),
                produit.getCategory().getNomCategory()
        );
    }

    private static Boolean isPerime(LocalDate dateExpiration) {
        return dateExpiration.isBefore(LocalDate.now());
    }

    // Liste de tous les produits
    public List<ProduitDTO> getAllProduits() {
        return produitRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Recupérer un produitDTO à partir de son id
    public ProduitDTO getProduitDTOById(UUID id) {
        Produit produit =  produitRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produit non existant !"));
        return mapToDTO(produit);
    }

    // Recupérer un produit à partir de son id
    public Produit getProduitById(UUID id) {
        Produit produit =  produitRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produit non existant !"));
        return produit;
    }

    // Enregistrer un nouveau produit
    public Produit createProduit(Produit produit) {
        // Verifie si la catégorie existe déjà
        if(produitRepository.existsDistinctByNomProduit(produit.getNomProduit())) {
            throw new IllegalArgumentException("Ce produit existe déjà en BD !");
        }
        return produitRepository.save(produit);
    }

    /* Mettre à jour les informations sur un
        produit (existingProduit) déja existant */
    public Produit updateProduit(UUID id, Produit updatedProduit) {
        return produitRepository.findById(id).map(
                existingProduit->{
                    existingProduit.setNomProduit(updatedProduit.getNomProduit());
                    existingProduit.setPrixProduit(updatedProduit.getPrixProduit());
                    existingProduit.setDateExpiration(updatedProduit.getDateExpiration());
                    return produitRepository.save(existingProduit);
                }
        ).orElseThrow(() -> new IllegalArgumentException("Produit non existant !"));
    }

    /* Supprimer un produit connaissant son ID */
    public void deleteProduit(UUID id) {
        if(!produitRepository.existsById(id)) {
            throw new IllegalArgumentException("Produit non existant !");
        }
        produitRepository.deleteById(id);
    }
}
