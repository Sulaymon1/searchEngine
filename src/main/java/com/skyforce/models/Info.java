package com.skyforce.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@Builder
@ToString
public class Info {
    private Boolean isCompleted;
    private int percent;
    private String keyword;
    private int totalCity;
    private int currentCityNum;
    private int isButtonStarted; // it will be 1 or 0 it means 1 -> started ; and 0 -> stopped
}
