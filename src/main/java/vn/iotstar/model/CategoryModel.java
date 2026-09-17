package vn.iotstar.model;

import org.springframework.web.multipart.MultipartFile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryModel {
    private Long categoryId;
    private String categoryName;
    private MultipartFile icon;
    private Short status = 1;
    private Boolean isEdit = false;
}
