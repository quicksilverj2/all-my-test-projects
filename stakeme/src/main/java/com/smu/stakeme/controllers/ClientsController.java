package com.smu.stakeme.controllers;

import com.smu.stakeme.model.StakeUser;
import com.smu.stakeme.service.UserStoreService;
import com.smu.stakeme.service.UserStoreServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by jitheshrajan on 12/3/18.
 */
@RestController
public class ClientsController {

    UserStoreService userStoreService = new UserStoreServiceImpl();

    @GetMapping("/search")
    public List<StakeUser> searchUsers(
            @RequestParam("str") String name
    ){
        return  new ArrayList<>(userStoreService.searchUsers(name));
    }

    @GetMapping("/all")
    public Collection<StakeUser> allUsers(){
        return userStoreService.listUsers();
    }

    @PostMapping("/user")
    public <T extends StakeUser> boolean addUser(
            @RequestBody T stakeUser
    ){

        return userStoreService.addUser(stakeUser);

    }


}
