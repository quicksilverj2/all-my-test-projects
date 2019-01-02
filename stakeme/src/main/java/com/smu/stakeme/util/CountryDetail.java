package com.smu.stakeme.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by jitheshrajan on 12/6/18.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CountryDetail {

    private String countryName;
    private String countryCode;
    private String countryNameShort;
}
