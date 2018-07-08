package com.skyforce.models;

import lombok.*;

import javax.persistence.*;

/**
 * Created by Sulaymon on 10.03.2018.
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    @Column(unique = true)
    private String categoryNameToLower;
}
