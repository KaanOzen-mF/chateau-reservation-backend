package com.reservationsystem.reservation_backend.repo;

import com.reservationsystem.reservation_backend.entity.Chateau;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChateauRepo extends JpaRepository<Chateau, Long> {

    List<Chateau> findByThemeIgnoreCase(String theme);
}
