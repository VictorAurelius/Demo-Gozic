package com.web.gozic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.web.gozic.entity.Clothes;
import java.util.List;

public interface ClothesRepository extends JpaRepository<Clothes, Long> {
    List<Clothes> findByType(String type);
}