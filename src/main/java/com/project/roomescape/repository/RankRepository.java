package com.project.roomescape.repository;

import com.project.roomescape.model.Rank;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RankRepository extends JpaRepository<Rank, Long> {
    List<Rank> findAllByOrderByTimeAsc();
}
