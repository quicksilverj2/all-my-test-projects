package com.smu.stakeme.store;

import com.smu.stakeme.model.StakeUser;
import com.smu.stakeme.util.StakeAppHelper;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by jitheshrajan on 11/29/18.
 */
public class UserStore {

    private ReadWriteLock userStoreReadWriteLock = new ReentrantReadWriteLock();

    private static final Object lock = new Object();

    private static volatile UserStore instance;

    public static UserStore getInstance() {
        UserStore r = instance;
        if (r == null) {
            synchronized (lock) {
                r = instance;
                if (r == null) {
                    instance = r = new UserStore();
                }
            }
        }
        return r;
    }

    private Map<String, Set<StakeUser>> stakeUserNameCollection = null;

    private Map<String, Set<StakeUser>> stakeUserFirstNameCollection = null;

    private Map<String, Set<StakeUser>> stakeUserLastNameCollection = null;

    private TernarySearchTree<StakeUser> stakeUserTree = null;

    private UserStore() {
        stakeUserNameCollection = new HashMap<>();
        stakeUserFirstNameCollection = new HashMap<>();
        stakeUserLastNameCollection = new HashMap<>();
        stakeUserTree = new TernarySearchTree();
    }




    public  <T extends StakeUser>  boolean addUser(T stakeUser) throws Exception {

        // confirm if username already exists just before adding also!

        System.out.println(stakeUser.getFirstName() + " is being inserted");

        stakeUserTree.searchAndPrint(stakeUser.getUsername());

        userStoreReadWriteLock.readLock().lock();
        if(stakeUserTree.search(stakeUser.getUsername())){
            userStoreReadWriteLock.readLock().unlock();
            System.out.println("User name already claimed : "+ stakeUser.getUsername());
            // return here or thrown exception!

            if(stakeUserNameCollection.get(stakeUser.getUsername()) != null){
                throw new Exception ("Claimed User");
            }

            // TODO give list of suggested usernames!

            return false;
        }else{
            userStoreReadWriteLock.readLock().unlock();


            stakeUser.setId(StakeAppHelper.generateUniqueId());

            userStoreReadWriteLock.writeLock().lock();
            stakeUserTree.insert(stakeUser.getUsername(), stakeUser);
            stakeUserTree.insert(stakeUser.getFirstName(), stakeUser);
            stakeUserTree.insert(stakeUser.getLastName(), stakeUser);
            userStoreReadWriteLock.writeLock().unlock();

            return insertToUserCollection(stakeUser);

        }

    }

    private <T extends StakeUser> boolean insertToUserCollection(T stakeUser) {

        userStoreReadWriteLock.writeLock().lock();
        putIfAbsent(stakeUser.getFirstName(), stakeUser, stakeUserFirstNameCollection);
        userStoreReadWriteLock.writeLock().unlock();

        userStoreReadWriteLock.writeLock().lock();
        putIfAbsent(stakeUser.getLastName(), stakeUser, stakeUserLastNameCollection);
        userStoreReadWriteLock.writeLock().unlock();

        userStoreReadWriteLock.writeLock().lock();
        putIfAbsent(stakeUser.getUsername(), stakeUser, stakeUserNameCollection);
        userStoreReadWriteLock.writeLock().unlock();

        return true;

    }

    private <T extends StakeUser> boolean putIfAbsent(String key, T stakeUser, Map <String, Set<T>> stakeCollection){

        if(stakeCollection.get(key)!=null){
            stakeCollection.get(key).add(stakeUser);
        }else{
            Set<StakeUser> userSet = new HashSet<>();
            userSet.add(stakeUser);
            stakeCollection.put(key, (Set<T>) userSet);
//            stakeCollection.get(key).add(stakeUser);
        }

        return true;
    }


    public Collection<StakeUser> searchUsersNew(String searchString){
        searchString = searchString.trim();

        userStoreReadWriteLock.readLock().lock();
        List<StakeUser> searchResults = stakeUserTree.searchAndReturnObjects(searchString);
        userStoreReadWriteLock.readLock().unlock();

        return searchResults;
    }


    public Collection<StakeUser> searchUsers(String searchString){

        searchString = searchString.trim();

        userStoreReadWriteLock.readLock().lock();
        List<String> searchResults = stakeUserTree.searchAndReturn(searchString);
        userStoreReadWriteLock.readLock().unlock();

        Set<StakeUser> responseList = new HashSet<>();

        searchResults.stream().forEach(s -> {
            System.out.println("Matched User String : "+s);
            Set<StakeUser> stakeUsers = stakeUserNameCollection.get(s);
            if(stakeUsers != null){
                responseList.addAll(stakeUsers);
            }
//            stakeUsers = null;
            stakeUsers = stakeUserLastNameCollection.get(s);
            if(stakeUsers != null){
                responseList.addAll(stakeUsers);
            }
//            stakeUsers = null;
            stakeUsers = stakeUserFirstNameCollection.get(s);
            if(stakeUsers != null){
                responseList.addAll(stakeUsers);
            }

        });

        return responseList;
    }


    public Collection<StakeUser> listAllUsers(){

        Set<StakeUser> returnSet = new HashSet<>();

        userStoreReadWriteLock.readLock().lock();

//        stakeUserLastNameCollection.values().stream().forEach(stakeUsers -> returnSet.addAll(stakeUsers));
//        stakeUserFirstNameCollection.values().stream().forEach(stakeUsers -> returnSet.addAll(stakeUsers));
//        stakeUserNameCollection.values().iterator().forEachRemaining(stakeUsers -> returnSet.addAll(stakeUsers));

        for(Set<StakeUser> stakeUsers : stakeUserNameCollection.values()){
            for(StakeUser s : stakeUsers){
                System.out.println(s.getFirstName() + " " + s.getUsername()+" "+s.getId());
                returnSet.add(s);
            }
            System.out.println(" ");
//            returnSet.addAll(stakeUsers);
        }

        for(StakeUser s : returnSet){
            System.out.println(s.getFirstName() + " " + s.getUsername()+" "+s.getId());
        }

        userStoreReadWriteLock.readLock().unlock();

        return returnSet;

    }


    public Collection<StakeUser> searchUsers(String nameString, boolean isUserName, boolean isFirstName, boolean isLastName){

        nameString = nameString.trim();

        userStoreReadWriteLock.readLock().lock();
        List<String> searchResults = stakeUserTree.searchAndReturn(nameString);
        userStoreReadWriteLock.readLock().unlock();

        Set<StakeUser> responseList = new HashSet<>();

        searchResults.stream().forEach(s -> {
            System.out.println("Matched User String : "+s);
            Set<StakeUser> stakeUsers = null;
            if(isUserName){
                stakeUsers = stakeUserNameCollection.get(s);
                if(stakeUsers != null){
                    responseList.addAll(stakeUsers);
                }
            }else if(isFirstName){
                stakeUsers = stakeUserFirstNameCollection.get(s);
                if(stakeUsers != null){
                    responseList.addAll(stakeUsers);
                }
            }else if(isLastName){
                stakeUsers = stakeUserLastNameCollection.get(s);
                if(stakeUsers != null){
                    responseList.addAll(stakeUsers);
                }
            } else{
                System.out.println("Search string not found in any of the maps!");
            }
        });

        return responseList;

    }




}
