package com.skyforce.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.*;

import javax.persistence.*;
import java.util.List;

/**
 * Date 07.04.2018
 *
 * @author Hursanov Sulaymon
 * @version v1.0
 **/
@Setter
@Getter
@Entity
@Table(name = "datatoparse")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DataParser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Category category;

    @Column(columnDefinition = "boolean default FALSE")
    private Boolean onlySelectedCities = false;

    private Integer currentCityNumber = 1;

    @Column(columnDefinition = "boolean default FALSE")
    private Boolean isParsingCurrent = false;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY)
    private List<City> cities;

    @Column(columnDefinition = "int default 0")
    private Integer size = 0;

    @Column(columnDefinition = "boolean default FALSE")
    private Boolean isCompleted = false;
}
