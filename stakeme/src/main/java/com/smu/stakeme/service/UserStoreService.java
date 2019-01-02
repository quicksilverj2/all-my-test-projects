package com.smu.stakeme.service;

import com.smu.stakeme.model.StakeUser;

import java.util.Collection;

/**
 * Created by jitheshrajan on 12/3/18.
 */
public interface UserStoreService {

    Collection<StakeUser> listUsers();

    <T extends StakeUser> boolean addUser(T StakeUser);

    Collection<StakeUser> searchUserBasedOnUserName(String username);

    Collection<StakeUser>  searchUsers(String genSearchString);

    Collection<StakeUser>  searchUserBasedOnName(String username);

}
