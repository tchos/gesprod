package tchos.gesprod.produit;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import tchos.gesprod.category.Category;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="produits")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "category")
public class Produit {
    @Id
    @GeneratedValue
    private UUID idProduit;

    @Column(nullable = false, unique = true)
    @Size(min = 3, max = 48, message = "{Size.produit.nomProduit}")
    private String nomProduit;

    @Column(nullable = false)
    @Min(value = 25, message = "{Min.produit.prixProduit}")
    @Max(value = 500000, message = "{Max.produit.prixProduit}")
    private int prixProduit;

    @Column(nullable = false)
    @FutureOrPresent(message = "{FutureOrPresent.produit.dateProduit}")
    private LocalDate dateExpiration;

    @ManyToOne
    @JoinColumn(name = "category")
    private Category category;
}
