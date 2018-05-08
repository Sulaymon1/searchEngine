package com.skyforce.models;

import lombok.*;

import javax.persistence.*;

/**
 * Created by Sulaymon on 12.03.2018.
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "data")
@ToString
public class Data {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String keyword;
    private String city;

    private String name;
    private String uri;
    private String email;
    private String website;
    private String phoneStr;
    private String addressStr;

    private String errors;
}
