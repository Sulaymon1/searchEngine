package com.skyforce.dto;

import lombok.*;


/**
 * Created by Sulaymon on 12.03.2018.
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SearchDTO {
    private Long id;
    private String keyword;
    private String city;
    private Integer currentPage;
}
