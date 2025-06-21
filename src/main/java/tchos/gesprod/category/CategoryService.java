package tchos.gesprod.category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    // Liste des catégories
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // Recupere une catégorie par son ID
    public Category getCategoryByID(UUID id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categorie non existente !"));
    }

    // Créer une nouvelle catégorie
    public Category createCategory(Category category) {
        // Verifie si la catégorie existe déjà
        if(categoryRepository.existsDistinctByNomCategory(category.getNomCategory())) {
            throw new IllegalArgumentException("Cette catégorie existe déjà !");
        }
        return categoryRepository.save(category);
    }

    /* Mettre à jour les informations sur une
        catégorie (existingCategory) déja existante */
    public Category updateCategory(UUID id, Category updatedCategory) {
        return categoryRepository.findById(id).map(
                existingCategory->{
                    existingCategory.setNomCategory(updatedCategory.getNomCategory());
                    return categoryRepository.save(existingCategory);
                }
        ).orElseThrow(() -> new IllegalArgumentException("Categorie non existente !"));
    }

    /* Supprimer une catégorie existante en BD */
    public void deleteCategory(UUID id) {
        // Si la catégorie à supprimer n'existe pas il faut afficher un message
        if(!categoryRepository.existsById(id)) {
            throw new IllegalArgumentException("Categorie non existente !");
        }
        categoryRepository.deleteById(id);
    }
}
