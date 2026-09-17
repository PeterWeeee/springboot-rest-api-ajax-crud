package vn.iotstar.controllers.api;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.iotstar.entity.Category;
import vn.iotstar.entity.Product;
import vn.iotstar.model.Response;
import vn.iotstar.service.ICategoryService;
import vn.iotstar.service.IProductService;
import vn.iotstar.service.IStorageService;

@RestController
@RequestMapping(path = "/api/product")
public class ProductApiController {

    @Autowired
    private IProductService productService;

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IStorageService storageService;

    @GetMapping
    public ResponseEntity<?> getAllProduct() {
        return new ResponseEntity<Response>(
                new Response(true, "Thành công", productService.findAll()),
                HttpStatus.OK);
    }

    @GetMapping(path = "/page")
    public ResponseEntity<?> getProductPage(
            @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(name = "categoryId", required = false) Long categoryId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size,
            @RequestParam(name = "sortBy", defaultValue = "productId") String sortBy,
            @RequestParam(name = "sortDir", defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Product> productPage = productService.searchProduct(keyword, categoryId, pageable);

        return new ResponseEntity<Response>(
                new Response(true, "Thành công", productPage),
                HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") Long id) {
        Optional<Product> optProduct = productService.findById(id);
        if (optProduct.isPresent()) {
            return new ResponseEntity<Response>(
                    new Response(true, "Thành công", optProduct.get()),
                    HttpStatus.OK);
        }
        return new ResponseEntity<Response>(
                new Response(false, "Không tìm thấy sản phẩm", null),
                HttpStatus.NOT_FOUND);
    }

    @PostMapping(path = "/getProduct")
    public ResponseEntity<?> getProduct(@Validated @RequestParam("id") Long id) {
        Optional<Product> optProduct = productService.findById(id);
        if (optProduct.isPresent()) {
            return new ResponseEntity<Response>(
                    new Response(true, "Thành công", optProduct.get()),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<Response>(
                    new Response(false, "Không tìm thấy sản phẩm", null),
                    HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(path = "/addProduct")
    public ResponseEntity<?> addProduct(
            @Validated @RequestParam("productName") String productName,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            @Validated @RequestParam("unitPrice") Double unitPrice,
            @RequestParam(value = "discount", defaultValue = "0.0") Double discount,
            @RequestParam(value = "description", defaultValue = "") String description,
            @Validated @RequestParam("categoryId") Long categoryId,
            @RequestParam(value = "quantity", defaultValue = "0") Integer quantity,
            @RequestParam(value = "status", defaultValue = "1") Short status) {

        Optional<Product> optProduct = productService.findByProductName(productName.trim());
        if (optProduct.isPresent()) {
            return new ResponseEntity<Response>(
                    new Response(false, "Sản phẩm này đã tồn tại trong hệ thống", optProduct.get()),
                    HttpStatus.BAD_REQUEST);
        }

        Product product = new Product();
        product.setProductName(productName.trim());
        product.setUnitPrice(unitPrice);
        product.setDiscount(discount);
        product.setDescription(description);
        product.setQuantity(quantity);
        product.setStatus(status);
        product.setCreateDate(new Date());

        Optional<Category> optCategory = categoryService.findById(categoryId);
        if (optCategory.isPresent()) {
            product.setCategory(optCategory.get());
        } else {
            return new ResponseEntity<Response>(
                    new Response(false, "Không tìm thấy Category chỉ định", null),
                    HttpStatus.BAD_REQUEST);
        }

        if (imageFile != null && !imageFile.isEmpty()) {
            UUID uuid = UUID.randomUUID();
            String storeFilename = storageService.getSorageFilename(imageFile, uuid.toString());
            storageService.store(imageFile, storeFilename);
            product.setImages(storeFilename);
        }

        productService.save(product);

        return new ResponseEntity<Response>(
                new Response(true, "Thêm sản phẩm thành công", product),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/updateProduct", method = { RequestMethod.PUT, RequestMethod.POST })
    public ResponseEntity<?> updateProduct(
            @Validated @RequestParam("productId") Long productId,
            @Validated @RequestParam("productName") String productName,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            @Validated @RequestParam("unitPrice") Double unitPrice,
            @RequestParam(value = "discount", defaultValue = "0.0") Double discount,
            @RequestParam(value = "description", defaultValue = "") String description,
            @Validated @RequestParam("categoryId") Long categoryId,
            @RequestParam(value = "quantity", defaultValue = "0") Integer quantity,
            @RequestParam(value = "status", defaultValue = "1") Short status) {

        Optional<Product> optProduct = productService.findById(productId);
        if (optProduct.isEmpty()) {
            return new ResponseEntity<Response>(
                    new Response(false, "Không tìm thấy sản phẩm", null),
                    HttpStatus.BAD_REQUEST);
        }

        Product product = optProduct.get();
        product.setProductName(productName.trim());
        product.setUnitPrice(unitPrice);
        product.setDiscount(discount);
        product.setDescription(description);
        product.setQuantity(quantity);
        product.setStatus(status);

        Optional<Category> optCategory = categoryService.findById(categoryId);
        if (optCategory.isPresent()) {
            product.setCategory(optCategory.get());
        }

        if (imageFile != null && !imageFile.isEmpty()) {
            UUID uuid = UUID.randomUUID();
            String storeFilename = storageService.getSorageFilename(imageFile, uuid.toString());
            storageService.store(imageFile, storeFilename);
            product.setImages(storeFilename);
        }

        productService.save(product);

        return new ResponseEntity<Response>(
                new Response(true, "Cập nhật sản phẩm thành công", product),
                HttpStatus.OK);
    }

    @DeleteMapping(path = "/deleteProduct")
    public ResponseEntity<?> deleteProduct(@Validated @RequestParam("productId") Long productId) {
        Optional<Product> optProduct = productService.findById(productId);
        if (optProduct.isEmpty()) {
            return new ResponseEntity<Response>(
                    new Response(false, "Không tìm thấy sản phẩm", null),
                    HttpStatus.BAD_REQUEST);
        }

        productService.delete(optProduct.get());
        return new ResponseEntity<Response>(
                new Response(true, "Xóa sản phẩm thành công", optProduct.get()),
                HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteProductById(@PathVariable("id") Long id) {
        Optional<Product> optProduct = productService.findById(id);
        if (optProduct.isEmpty()) {
            return new ResponseEntity<Response>(
                    new Response(false, "Không tìm thấy sản phẩm", null),
                    HttpStatus.BAD_REQUEST);
        }

        productService.delete(optProduct.get());
        return new ResponseEntity<Response>(
                new Response(true, "Xóa sản phẩm thành công", optProduct.get()),
                HttpStatus.OK);
    }

    @GetMapping(path = { "/images/{filename:.+}", "/admin/products/images/{filename:.+}", "/admin/categories/images/{filename:.+}" })
    public ResponseEntity<Resource> serveFile(@PathVariable("filename") String filename) {
        try {
            Resource file = storageService.loadAsResource(filename);
            String contentType = Files.probeContentType(storageService.load(filename));
            if (contentType == null) {
                contentType = "image/jpeg";
            }
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFilename() + "\"")
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(file);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
