package com.example.courtmanagement_service.repo.jpa;

import com.example.courtmanagement_service.entity.BadmintonCourt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BadmintonCourtRepoJpa extends JpaRepository<BadmintonCourt, Integer> {
    Optional<List<BadmintonCourt>> findAllByIdIn(List<Integer> ids);

    @Query(value = """
            SELECT bc.id
            FROM badminton_courts bc
            WHERE NOT EXISTS(
                SELECT 1 FROM court_details cd
                         WHERE cd.court_id = bc.id
            )
            """, nativeQuery = true)
    List<Integer> getCourtIdNotExistInCourtDetail();
}
