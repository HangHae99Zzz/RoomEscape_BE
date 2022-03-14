package com.project.roomescape.repository;

import com.project.roomescape.model.GameResource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameResourceRepository extends JpaRepository<GameResource, Long> {
    List<GameResource> findAllByType(String type);
}
