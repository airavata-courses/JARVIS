package com.ads.assignment1.dbaccess.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ads.assignment1.dbaccess.entity.Place;

//place_master repository
@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {

}
