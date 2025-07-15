package tchos.gesprod.category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryMapper {

    public CategoryDTO toDTO(Category category) {
        return CategoryDTO.builder()
                .idCategory(category.getIdCategory())
                .nomCategory(category.getNomCategory())
                .build();
    }

    public Category toEntity(CategoryDTO categoryDTO) {
        return Category.builder()
                .idCategory(categoryDTO.getIdCategory())
                .nomCategory(categoryDTO.getNomCategory())
                .build();
    }
}
