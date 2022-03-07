package com.project.roomescape.repository;

import com.project.roomescape.model.GameResource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameResourceRepository extends JpaRepository<GameResource, Long> {
}
