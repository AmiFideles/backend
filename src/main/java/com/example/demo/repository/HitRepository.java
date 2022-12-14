package com.example.demo.repository;

import com.example.demo.entity.Hit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HitRepository extends JpaRepository<Hit, Long> {
    List<Hit> findAllByUserId(Long id);
}
