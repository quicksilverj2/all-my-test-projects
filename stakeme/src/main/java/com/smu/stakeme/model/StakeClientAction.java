package com.smu.stakeme.model;

/**
 * Created by jitheshrajan on 11/19/18.
 */
public interface StakeClientAction {

    // TODO modify this method to take in pack object!
    Object buyStakeInPack(Object pack, Object stakeSize);

    /*
    * This action will add the pack into the client's watchlist,
    * it will be available as a part of the clients dashboard.
    * */
    boolean followPack(Object pack);

}
