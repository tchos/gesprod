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
                .id(produit.getId())
                .nomProduit(produit.getNomProduit())
                .prixProduit(produit.getPrixProduit())
                .dateExpiration(produit.getDateExpiration())
                .categoryId(produit.getCategory() != null ? produit.getCategory().getId() : null)
                .build();
    }

    public Produit toEntity(ProduitDTO produitDTO) {
        Produit produit = new Produit();
        produit.setId(produitDTO.getId());
        produit.setNomProduit(produitDTO.getNomProduit());
        produit.setPrixProduit(produitDTO.getPrixProduit());
        produit.setDateExpiration(produitDTO.getDateExpiration());

        if (produitDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(produitDTO.getCategoryId())
                    .orElse(null);
            produit.setCategory(category);
        }

        return produit;
    }
}
