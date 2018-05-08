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
public class SearchResultDTO {
    private String name;
    private String email;
    private String website;
    private String phoneStr;
    private String addressStr;
}
