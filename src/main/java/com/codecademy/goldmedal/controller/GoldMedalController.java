package com.codecademy.goldmedal.controller;

import com.codecademy.goldmedal.model.*;
import com.codecademy.goldmedal.repository.CountryRepository;
import com.codecademy.goldmedal.repository.GoldMedalRepository;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.apache.commons.text.WordUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/countries")
@AllArgsConstructor
public class GoldMedalController {
  private final GoldMedalRepository goldMedalRepository;
  private final CountryRepository countryRepository;

  @GetMapping
  public CountriesResponse getCountries(
      @RequestParam String sort_by, @RequestParam String ascending) {
    var ascendingOrder = ascending.equalsIgnoreCase("y");
    return new CountriesResponse(getCountrySummaries(sort_by.toLowerCase(), ascendingOrder));
  }

  @GetMapping("/{country}")
  public CountryDetailsResponse getCountryDetails(@PathVariable String country) {
    String countryName = WordUtils.capitalizeFully(country);
    return getCountryDetailsResponse(countryName);
  }

  @GetMapping("/{country}/medals")
  public CountryMedalsListResponse getCountryMedalsList(
      @PathVariable String country, @RequestParam String sort_by, @RequestParam String ascending) {
    String countryName = WordUtils.capitalizeFully(country);
    var ascendingOrder = ascending.equalsIgnoreCase("y");
    return getCountryMedalsListResponse(countryName, sort_by.toLowerCase(), ascendingOrder);
  }

  private CountryMedalsListResponse getCountryMedalsListResponse(
      String countryName, String sortBy, boolean ascendingOrder) {
    List<GoldMedal> medalsList;
    switch (sortBy) {
      case "year":
        medalsList =
            ascendingOrder
                ? goldMedalRepository.getByCountryOrderByYearAsc(countryName)
                : goldMedalRepository.getByCountryOrderByYearDesc(countryName);
        break;
      case "season":
        medalsList =
            ascendingOrder
                ? goldMedalRepository.getByCountryOrderBySeasonAsc(countryName)
                : goldMedalRepository.getByCountryOrderBySeasonDesc(countryName);
        break;
      case "city":
        medalsList =
            ascendingOrder
                ? goldMedalRepository.getByCountryOrderByCityAsc(countryName)
                : goldMedalRepository.getByCountryOrderByCityDesc(countryName);
        break;
      case "name":
        medalsList =
            ascendingOrder
                ? goldMedalRepository.getByCountryOrderByNameAsc(countryName)
                : goldMedalRepository.getByCountryOrderByNameDesc(countryName);
        break;
      case "event":
        medalsList =
            ascendingOrder
                ? goldMedalRepository.getByCountryOrderByEventAsc(countryName)
                : goldMedalRepository.getByCountryOrderByEventDesc(countryName);
        break;
      default:
        medalsList = new ArrayList<>();
        break;
    }

    return new CountryMedalsListResponse(medalsList);
  }

  private CountryDetailsResponse getCountryDetailsResponse(String countryName) {
    Optional<Country> countryOptional = countryRepository.findByName(countryName);
    if (countryOptional.isEmpty()) {
      return new CountryDetailsResponse(countryName);
    }

    Country country = countryOptional.get();
    Integer goldMedalCount = goldMedalRepository.countByCountry(countryName);

    List<GoldMedal> summerWins =
        goldMedalRepository.getByCountryAndSeasonOrderByYearAsc(countryName, "Summer");
    Integer numberSummerWins = summerWins.size() > 0 ? summerWins.size() : null;
    Integer totalSummerEvents = goldMedalRepository.countBySeason("Summer");
    Float percentageTotalSummerWins =
        totalSummerEvents != 0 && numberSummerWins != null
            ? (float) summerWins.size() / totalSummerEvents
            : null;
    Integer yearFirstSummerWin = summerWins.size() > 0 ? summerWins.get(0).getYear() : null;

    List<GoldMedal> winterWins =
        goldMedalRepository.getByCountryAndSeasonOrderByYearAsc(countryName, "Winter");
    Integer numberWinterWins = winterWins.size() > 0 ? winterWins.size() : null;
    Integer totalWinterEvents = goldMedalRepository.countBySeason("Winter");
    Float percentageTotalWinterWins =
        totalWinterEvents != 0 && numberWinterWins != null
            ? (float) winterWins.size() / totalWinterEvents
            : null;
    Integer yearFirstWinterWin = winterWins.size() > 0 ? winterWins.get(0).getYear() : null;

    Integer numberEventsWonByFemaleAthletes =
        goldMedalRepository.countByCountryAndGender(countryName, "Women");
    Integer numberEventsWonByMaleAthletes =
        goldMedalRepository.countByCountryAndGender(countryName, "Men");

    return new CountryDetailsResponse(
        countryName,
        country.getGdp(),
        country.getPopulation(),
        goldMedalCount,
        numberSummerWins,
        percentageTotalSummerWins,
        yearFirstSummerWin,
        numberWinterWins,
        percentageTotalWinterWins,
        yearFirstWinterWin,
        numberEventsWonByFemaleAthletes,
        numberEventsWonByMaleAthletes);
  }

  private List<CountrySummary> getCountrySummaries(String sortBy, boolean ascendingOrder) {
    List<Country> countries;
    switch (sortBy) {
      case "name":
        countries =
            ascendingOrder
                ? countryRepository.findAllByOrderByNameAsc()
                : countryRepository.findAllByOrderByNameDesc();
        break;
      case "gdp":
        countries =
            ascendingOrder
                ? countryRepository.findAllByOrderByGdpAsc()
                : countryRepository.findAllByOrderByGdpDesc();
        break;
      case "population":
        countries =
            ascendingOrder
                ? countryRepository.findAllByOrderByPopulationAsc()
                : countryRepository.findAllByOrderByPopulationDesc();
        break;
      case "medals":
      default:
        countries = countryRepository.findAllByOrderByNameAsc();
        break;
    }

    List<CountrySummary> countrySummaries = getCountrySummariesWithMedalCount(countries);

    if (sortBy.equalsIgnoreCase("medals")) {
      countrySummaries = sortByMedalCount(countrySummaries, ascendingOrder);
    }

    return countrySummaries;
  }

  private List<CountrySummary> sortByMedalCount(
      List<CountrySummary> countrySummaries, boolean ascendingOrder) {
    return countrySummaries.stream()
        .sorted(
            (t1, t2) ->
                ascendingOrder ? t1.getMedals() - t2.getMedals() : t2.getMedals() - t1.getMedals())
        .collect(Collectors.toList());
  }

  private List<CountrySummary> getCountrySummariesWithMedalCount(List<Country> countries) {
    List<CountrySummary> countrySummaries = new ArrayList<>();
    for (Country country : countries) {
      Integer goldMedalCount = goldMedalRepository.countByCountry(country.getName());
      countrySummaries.add(new CountrySummary(country, goldMedalCount));
    }
    return countrySummaries;
  }
}
