package com.codecademy.goldmedal.repository;

import com.codecademy.goldmedal.model.Country;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends CrudRepository<Country, Integer> {
  Optional<Country> findByName(String name);
  List<Country> findAllByOrderByNameAsc();
  List<Country> findAllByOrderByNameDesc();
  List<Country> findAllByOrderByGdpAsc();
  List<Country> findAllByOrderByGdpDesc();
  List<Country> findAllByOrderByPopulationAsc();
  List<Country> findAllByOrderByPopulationDesc();
}
