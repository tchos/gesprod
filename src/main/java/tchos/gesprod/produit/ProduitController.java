package tchos.gesprod.produit;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tchos.gesprod.category.CategoryService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/produits")
@RequiredArgsConstructor
public class ProduitController {

    private final ProduitService produitService;

    /*
        ResponseEntity.ok() indique explicitement :
        - Que la requête a réussi (code 200)
        - Que le corps contient la ressource demandée
     */

    // Liste de tous les produits
    @GetMapping
    public ResponseEntity<List<ProduitDTO>> getAllProduits() {
       return ResponseEntity.ok(produitService.getAllProduits());
    }

    // Recupérer un produitDTO à partir de son ID
    @GetMapping("/{id}")
    public ResponseEntity<ProduitDTO> getProduitDTOById(@PathVariable UUID id) {
        return ResponseEntity.ok(produitService.getProduitDTOById(id));
    }

    // Créer un nouveau produit
    @PostMapping
    public ResponseEntity<Produit> addProduit(@RequestBody Produit produit) {
        return ResponseEntity.status(HttpStatus.CREATED).body(produitService.createProduit(produit));
    }

    // Mettre à jour produit
    @PutMapping("/{id}")
    public ResponseEntity<Produit> updateProduit(@PathVariable UUID id, @RequestBody Produit produit) {
        return ResponseEntity.ok(produitService.updateProduit(id, produit));
    }

    // Supprimer un produit connaissant son ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduit(@PathVariable UUID id) {
        produitService.deleteProduit(id);
        return ResponseEntity.noContent().build();
    }
}
