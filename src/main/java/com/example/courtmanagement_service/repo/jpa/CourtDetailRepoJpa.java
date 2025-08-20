package com.example.courtmanagement_service.repo.jpa;


import com.example.courtmanagement_service.entity.BadmintonCourt;
import com.example.courtmanagement_service.entity.CourtDetail;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.beans.Transient;
import java.util.List;
import java.util.Optional;

public interface CourtDetailRepoJpa extends JpaRepository<CourtDetail, Integer> {

    long countAllByIdIn(List<Integer> ids);

    Optional<List<CourtDetail>> findAllByStatusIs(Integer status);

    Optional<CourtDetail> findByIdAndStatusIs(Integer id, Integer status);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM court_details WHERE court_id = :court_id", nativeQuery = true)
    int deleteByCourtId(@Param("court_id") Integer court_id);

    @Query(value = "SELECT court_id, COUNT(*) as total FROM court_details GROUP BY court_id", nativeQuery = true)
    List<Object[]> getTotalCourtDetailByCourtId();

    @Modifying
    @Transactional
    @Query(value = """
            UPDATE court_details
            SET status = :status
            WHERE court_id IN (:ids)
           """, nativeQuery = true)
    int updateStatusCourtDetailByCourtIdIn(@Param("ids") List<Integer> ids, @Param("status") int status);



}
