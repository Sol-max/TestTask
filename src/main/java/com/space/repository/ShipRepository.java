package com.space.repository;

import com.space.model.Ship;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ShipRepository extends JpaRepository<Ship,Long> , JpaSpecificationExecutor<Ship>
{
    //@Query("select s from Ship s")
    //List<Ship> findAllbyPage(Specification<Ship> scec, Pageable page);


    //@Query("select count(s) from Ship s")
    //long countAllbyPage(Pageable page);

}
