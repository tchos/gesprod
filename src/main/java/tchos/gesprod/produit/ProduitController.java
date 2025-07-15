package tchos.gesprod.produit;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@Tag(name = "Produits", description = "API pour la gestion des produits")
public class ProduitController {

    private final ProduitService produitService;

    /*
        ResponseEntity.ok() indique explicitement :
        - Que la requête a réussi (code 200)
        - Que le corps contient la ressource demandée
     */

    // Liste de tous les produits
    @GetMapping
    @Operation(summary = "Liste de tous les produits")
    public ResponseEntity<List<ProduitDTO>> getAllProduits() {
       return ResponseEntity.ok(produitService.getAllProduits());
    }

    // Recupérer un produitDTO à partir de son ID
    @GetMapping("/{id}")
    @Operation(summary = "Obtenir un produit par son ID")
    public ResponseEntity<ProduitDTO> getProduitDTOById(@PathVariable UUID id) {
        return ResponseEntity.ok(produitService.getProduitDTOById(id));
    }

    // Créer un nouveau produit
    @PostMapping
    @Operation(summary = "Créer un nouveau produit")
    public ResponseEntity<ProduitDTO> addProduit(@Valid @RequestBody ProduitDTO produitDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(produitService.createProduit(produitDTO));
    }

    // Mettre à jour produit
    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un produit existant")
    public ResponseEntity<ProduitDTO> updateProduit(@PathVariable UUID id, @Valid @RequestBody ProduitDTO produitDTO) {
        return ResponseEntity.ok(produitService.updateProduit(id, produitDTO));
    }

    // Supprimer un produit connaissant son ID
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un produit existant")
    public ResponseEntity<Void> deleteProduit(@PathVariable UUID id) {
        produitService.deleteProduit(id);
        return ResponseEntity.noContent().build();
    }

    // Liste des produits périmés
    @GetMapping("/perimes")
    @Operation(summary = "Obtenir la liste des produits périmés")
    public List<ProduitDTO> getProduitsPerimes() {
        return produitService.getProduitsPerimes();
    }

    // Liste des produits par catégorie
    @GetMapping("/list/categorie/{id}")
    @Operation(summary = "Liste des produits par catégorie")
    public List<ProduitDTO> getProduitsByCategory(@PathVariable UUID id) {
        return produitService.getProduitsByCategory(id);
    }
}
