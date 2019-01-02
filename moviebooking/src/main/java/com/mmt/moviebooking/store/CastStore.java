package com.mmt.moviebooking.store;

import com.mmt.moviebooking.model.Category;
import com.mmt.moviebooking.model.cast.Actor;
import com.mmt.moviebooking.model.cast.CastMember;
import com.mmt.moviebooking.model.cast.Director;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by jitheshrajan on 11/17/18.
 */
public enum CastStore {

    INSTANCE;

    private Map<String, Actor> availableActors = new HashMap<>();
    private Map<String, Director> availableDirectors = new HashMap<>();

    public void registerCastMember(CastMember castMember, String name)
    {

        if(castMember instanceof Actor){
            if(!availableActors.containsKey(name)){
                availableActors.put(name,(Actor)castMember);
            }
        } else if(castMember instanceof Director){
            if(!availableDirectors.containsKey(name)){
                availableDirectors.put(name,(Director) castMember);
            }
        }

    }

    public void registerCastMember(CastMember castMember)
    {
        registerCastMember(castMember, castMember.getName());
    }

    public Actor getActor(String name)
    {
        return this.availableActors.get(name);
    }
    public Director getDirector(String name)
    {
        return this.availableDirectors.get(name);
    }


    public Set<String> getDefinedActors()
    {
        return this.availableActors.keySet();
    }
    public Set<String> getDefinedDirectors()
    {
        return this.availableDirectors.keySet();
    }
}
