package com.codecademy.goldmedal.repository;

import com.codecademy.goldmedal.model.GoldMedal;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoldMedalRepository extends CrudRepository<GoldMedal, Integer> {
  List<GoldMedal> getByCountryOrderByYearAsc(String countryName);
  List<GoldMedal> getByCountryOrderByYearDesc(String countryName);
  List<GoldMedal> getByCountryOrderBySeasonAsc(String countryName);
  List<GoldMedal> getByCountryOrderBySeasonDesc(String countryName);
  List<GoldMedal> getByCountryOrderByCityAsc(String countryName);
  List<GoldMedal> getByCountryOrderByCityDesc(String countryName);
  List<GoldMedal> getByCountryOrderByNameAsc(String countryName);
  List<GoldMedal> getByCountryOrderByNameDesc(String countryName);
  List<GoldMedal> getByCountryOrderByEventAsc(String countryName);
  List<GoldMedal> getByCountryOrderByEventDesc(String countryName);
  Integer countByCountry(String countryName);
  List<GoldMedal> getByCountryAndSeasonOrderByYearAsc(String countryName, String season);
  Integer countBySeason(String season);
  Integer countByCountryAndGender(String countryName, String gender);
}
