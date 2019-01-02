package com.smu.stakeme.service;

import com.smu.stakeme.model.StakeUser;
import com.smu.stakeme.store.UserStore;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

/**
 * Created by jitheshrajan on 12/3/18.
 *
 * This user service impl uses the local cache store to add and delete users!
 *
 */


public class UserStoreServiceImpl implements UserStoreService {

    private final UserStore userStore;

    @Autowired
    public UserStoreServiceImpl() {
        this.userStore = UserStore.getInstance();
    }


    @Override
    public Collection<StakeUser> listUsers() {
        return userStore.listAllUsers();
    }

    @Override
    public <T extends StakeUser> boolean addUser(T stakeUser) {
       try{
           return userStore.addUser(stakeUser);
       }catch (Exception e){
           e.printStackTrace();
           return false;
       }
    }

    @Override
    public Collection<StakeUser> searchUserBasedOnUserName(String username) {

        return userStore.searchUsers(username, true, false, false);
    }

    @Override
    public Collection<StakeUser> searchUsers(String genSearchString) {
        return userStore.searchUsersNew(genSearchString);
    }

    @Override
    public Collection<StakeUser> searchUserBasedOnName(String username) {


        Collection<StakeUser> firstNameResponse = null;
        String[] nameSplit = username.split(" ");
        if(nameSplit.length == 2){

            firstNameResponse =   userStore.searchUsers(nameSplit[0], false, true, false);
            firstNameResponse.addAll(userStore.searchUsers(nameSplit[1], false, false, true));


        }else if(nameSplit.length > 2){
            // divide and conquer approach here!
            // search based on both the split strings and then
            // we will have to do a second round pattern matching!



        }else{
            firstNameResponse =   userStore.searchUsers(username, false, true, false);
            firstNameResponse.addAll(userStore.searchUsers(username, false, false, true));

        }


        return firstNameResponse;
    }
}
