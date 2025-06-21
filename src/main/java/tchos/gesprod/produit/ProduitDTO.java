package tchos.gesprod.produit;

import java.util.UUID;

public record ProduitDTO(
        UUID id,
        String nom,
        int prix,
        Boolean perimed,
        String category
) {
}
