package com.example.courtmanagement_service.repo.jpa;

import com.example.courtmanagement_service.entity.BadmintonCourt;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BadmintonCourtRepoJpa extends JpaRepository<BadmintonCourt, Integer>,
        JpaSpecificationExecutor<BadmintonCourt> {

    Optional<List<BadmintonCourt>> findAllByStatusIs(Integer status);

    Optional<List<BadmintonCourt>> findAllByIdIn(List<Integer> ids);

    Optional<BadmintonCourt> findByIdAndStatusIs(Integer id, Integer status);
    long countAllByIdIn(List<Integer> ids);

    @Query(value = """
            SELECT bc.id
            FROM badminton_courts bc
            WHERE NOT EXISTS(
                SELECT 1 FROM court_details cd
                         WHERE cd.court_id = bc.id
            )
            """, nativeQuery = true)
    List<Integer> getCourtIdNotExistInCourtDetail();


    @Modifying
    @Transactional
    @Query(value = """
        UPDATE badminton_courts
        SET status = :status
        WHERE id IN (:ids)
        """, nativeQuery = true)
    int updateStatusBadmintonCourtByIdIn(@Param("ids") List<Integer> ids, @Param("status") Integer status);

    @Query(value = """
            with info_badminton as (select bc.opening_time, bc.closing_time, bc.id
                                    from badminton.badminton_courts bc
                                    where id IN (:ids))
            select cd.court_id, cd.court_number, ib.opening_time, ib.closing_time
            from badminton.court_details cd
                     join info_badminton ib on cd.court_id = ib.id
            order by cd.court_id;
            """, nativeQuery = true)
    List<Object[]> getInfoTimeCourtDetailByListCourtId(@Param("ids") List<Integer> ids);
}
