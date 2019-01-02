package com.smu.stakeme.model;

import lombok.ToString;

/**
 * Created by jitheshrajan on 11/19/18.
 */
@ToString
public class StakePlayer extends StakeUser implements StakeClientAction, StakePlayerAction {


    public StakePlayer(String id, String username, String firstName, String lastName, String countryCode, String phoneNumber, String email) {
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

    @Override
    public Object addPacks(Object pack) {
        return null;
    }
}
