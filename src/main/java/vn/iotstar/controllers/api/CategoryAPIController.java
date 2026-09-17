package vn.iotstar.controllers.api;

import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.iotstar.entity.Category;
import vn.iotstar.model.Response;
import vn.iotstar.service.ICategoryService;
import vn.iotstar.service.IStorageService;

@RestController
@RequestMapping(path = "/api/category")
public class CategoryAPIController {

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IStorageService storageService;

    @GetMapping
    public ResponseEntity<?> getAllCategory() {
        return new ResponseEntity<Response>(
                new Response(true, "Thành công", categoryService.findAll()),
                HttpStatus.OK);
    }

    @GetMapping(path = "/page")
    public ResponseEntity<?> getCategoryPage(
            @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size,
            @RequestParam(name = "sortBy", defaultValue = "categoryId") String sortBy,
            @RequestParam(name = "sortDir", defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Category> categoryPage;
        if (keyword != null && !keyword.trim().isEmpty()) {
            categoryPage = categoryService.findByCategoryNameContaining(keyword.trim(), pageable);
        } else {
            categoryPage = categoryService.findAll(pageable);
        }

        return new ResponseEntity<Response>(
                new Response(true, "Thành công", categoryPage),
                HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable("id") Long id) {
        Optional<Category> optCategory = categoryService.findById(id);
        if (optCategory.isPresent()) {
            return new ResponseEntity<Response>(
                    new Response(true, "Thành công", optCategory.get()),
                    HttpStatus.OK);
        }
        return new ResponseEntity<Response>(
                new Response(false, "Không tìm thấy Category", null),
                HttpStatus.NOT_FOUND);
    }

    @PostMapping(path = "/getCategory")
    public ResponseEntity<?> getCategory(@Validated @RequestParam("id") Long id) {
        Optional<Category> optCategory = categoryService.findById(id);
        if (optCategory.isPresent()) {
            return new ResponseEntity<Response>(
                    new Response(true, "Thành công", optCategory.get()),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<Response>(
                    new Response(false, "Thất bại", null),
                    HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(path = "/addCategory")
    public ResponseEntity<?> addCategory(
            @Validated @RequestParam("categoryName") String categoryName,
            @RequestParam(value = "icon", required = false) MultipartFile icon,
            @RequestParam(value = "status", required = false, defaultValue = "1") Short status) {

        Optional<Category> optCategory = categoryService.findByCategoryName(categoryName.trim());
        if (optCategory.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response(false, "Category đã tồn tại trong hệ thống", optCategory.get()));
        }

        Category category = new Category();
        if (icon != null && !icon.isEmpty()) {
            UUID uuid = UUID.randomUUID();
            String uuString = uuid.toString();
            String storeFilename = storageService.getSorageFilename(icon, uuString);
            storageService.store(icon, storeFilename);
            category.setIcon(storeFilename);
        }

        category.setCategoryName(categoryName.trim());
        category.setStatus(status);
        categoryService.save(category);

        return new ResponseEntity<Response>(
                new Response(true, "Thêm Thành công", category),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/updateCategory", method = { RequestMethod.PUT, RequestMethod.POST })
    public ResponseEntity<?> updateCategory(
            @Validated @RequestParam("categoryId") Long categoryId,
            @Validated @RequestParam("categoryName") String categoryName,
            @RequestParam(value = "icon", required = false) MultipartFile icon,
            @RequestParam(value = "status", required = false) Short status) {

        Optional<Category> optCategory = categoryService.findById(categoryId);
        if (optCategory.isEmpty()) {
            return new ResponseEntity<Response>(
                    new Response(false, "Không tìm thấy Category", null),
                    HttpStatus.BAD_REQUEST);
        }

        Category existing = optCategory.get();
        if (icon != null && !icon.isEmpty()) {
            UUID uuid = UUID.randomUUID();
            String uuString = uuid.toString();
            String storeFilename = storageService.getSorageFilename(icon, uuString);
            storageService.store(icon, storeFilename);
            existing.setIcon(storeFilename);
        }

        existing.setCategoryName(categoryName.trim());
        if (status != null) {
            existing.setStatus(status);
        }
        categoryService.save(existing);

        return new ResponseEntity<Response>(
                new Response(true, "Cập nhật Thành công", existing),
                HttpStatus.OK);
    }

    @DeleteMapping(path = "/deleteCategory")
    public ResponseEntity<?> deleteCategory(@Validated @RequestParam("categoryId") Long categoryId) {
        Optional<Category> optCategory = categoryService.findById(categoryId);
        if (optCategory.isEmpty()) {
            return new ResponseEntity<Response>(
                    new Response(false, "Không tìm thấy Category", null),
                    HttpStatus.BAD_REQUEST);
        }

        categoryService.delete(optCategory.get());
        return new ResponseEntity<Response>(
                new Response(true, "Xóa Thành công", optCategory.get()),
                HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteCategoryById(@PathVariable("id") Long id) {
        Optional<Category> optCategory = categoryService.findById(id);
        if (optCategory.isEmpty()) {
            return new ResponseEntity<Response>(
                    new Response(false, "Không tìm thấy Category", null),
                    HttpStatus.BAD_REQUEST);
        }

        categoryService.delete(optCategory.get());
        return new ResponseEntity<Response>(
                new Response(true, "Xóa Thành công", optCategory.get()),
                HttpStatus.OK);
    }
}
