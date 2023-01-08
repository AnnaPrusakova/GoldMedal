package com.codecademy.goldmedal.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GoldMedal {
    @Id
    @GeneratedValue
    private Long id;
    private Integer year;
    private String city;
    private String season;
    private String name;
    private String country;
    private String gender;
    private String sport;
    private String discipline;
    private String event;
}
