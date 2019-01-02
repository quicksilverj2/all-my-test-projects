package com.smu.stakeme.model;

import lombok.ToString;

/**
 * Created by jitheshrajan on 11/19/18.
 */
@ToString
public class StakeClient extends StakeUser implements StakeClientAction{

    public StakeClient(String id, String username, String firstName, String lastName, String countryCode, String phoneNumber, String email) {
        super(id, username, firstName, lastName, countryCode, phoneNumber, email);
    }
    @Override
    public Object buyStakeInPack(Object pack, Object stakeSize) {
        return null;
    }

    @Override
    public boolean followPack(Object pack) {
        return false;
    }
}
