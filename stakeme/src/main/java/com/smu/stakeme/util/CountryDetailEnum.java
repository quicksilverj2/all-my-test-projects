package com.smu.stakeme.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by jitheshrajan on 12/6/18.
 */
public enum CountryDetailEnum {

    /** Application of the Singleton pattern using enum **/
    INSTANCE;


    private final Map<String, CountryDetail> countryDetailsHashMap = new HashMap<>();

    public void registerCountryDetail(CountryDetail countryDetail)
    {
        if (!countryDetailsHashMap.containsKey(countryDetail.getCountryCode()))
            countryDetailsHashMap.put(countryDetail.getCountryCode(), countryDetail);
    }

    public CountryDetail getCountryDetailForCode(String countryCode)
    {
        return this.countryDetailsHashMap.get(countryCode);
    }

    public Set<String> getRegisteredCountryCodes()
    {
        return this.countryDetailsHashMap.keySet();
    }

    public Collection<CountryDetail> getRegisteredCountryDetails(){
        return this.countryDetailsHashMap.values();
    }
}
