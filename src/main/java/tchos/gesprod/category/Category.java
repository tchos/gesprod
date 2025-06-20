package tchos.gesprod.category;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import tchos.gesprod.produit.Produit;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "categories")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "produits")
public class Category {

    @Id
    @GeneratedValue
    private UUID idCategory;

    @Column(nullable = false, unique = true)
    @Size(min = 3, max = 48, message = "{Size.category.nomCategory}")
    private String nomCategory;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Produit> produits;

}
