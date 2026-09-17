package vn.iotstar.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import vn.iotstar.entity.Product;

public interface IProductService {
    <S extends Product> S save(S entity);

    Optional<Product> findById(Long id);

    List<Product> findAll();

    Page<Product> findAll(Pageable pageable);

    List<Product> findAll(Sort sort);

    Optional<Product> findByProductName(String name);

    List<Product> findByProductNameContaining(String name);

    Page<Product> findByProductNameContaining(String name, Pageable pageable);

    Page<Product> searchProduct(String name, Long categoryId, Pageable pageable);

    Optional<Product> findByCreateDate(Date createDate);

    void delete(Product entity);

    void deleteById(Long id);

    long count();
}
