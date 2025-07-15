// ProduitMapper.java
package tchos.gesprod.produit;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tchos.gesprod.category.Category;
import tchos.gesprod.category.CategoryRepository;

@Component
@RequiredArgsConstructor
public class ProduitMapper {

    private final CategoryRepository categoryRepository;

    public ProduitDTO toDTO(Produit produit) {
        return ProduitDTO.builder()
                .idProduit(produit.getIdProduit())
                .nomProduit(produit.getNomProduit())
                .prixProduit(produit.getPrixProduit())
                .dateExpiration(produit.getDateExpiration())
                .categoryId(produit.getCategory() != null ? produit.getCategory().getIdCategory() : null)
                .build();
    }

    public Produit toEntity(ProduitDTO produitDTO) {
        Category category = produitDTO.getCategoryId() != null
                ? categoryRepository.findById(produitDTO.getCategoryId()).orElse(null)
                : null;

        return Produit.builder()
                .idProduit(produitDTO.getIdProduit())
                .nomProduit(produitDTO.getNomProduit())
                .prixProduit(produitDTO.getPrixProduit())
                .dateExpiration(produitDTO.getDateExpiration())
                .category(category)
                .build();
    }
}
