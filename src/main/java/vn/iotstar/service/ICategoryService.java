package vn.iotstar.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import vn.iotstar.entity.Category;

public interface ICategoryService {
    <S extends Category> S save(S entity);

    Optional<Category> findById(Long id);

    List<Category> findAll();

    Page<Category> findAll(Pageable pageable);

    List<Category> findAll(Sort sort);

    Optional<Category> findByCategoryName(String name);

    List<Category> findByCategoryNameContaining(String name);

    Page<Category> findByCategoryNameContaining(String name, Pageable pageable);

    void delete(Category entity);

    void deleteById(Long id);

    long count();
}
