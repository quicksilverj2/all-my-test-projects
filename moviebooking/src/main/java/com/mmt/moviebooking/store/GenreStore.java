package com.mmt.moviebooking.store;

import com.mmt.moviebooking.model.Genre;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by jitheshrajan on 11/17/18.
 */
public enum GenreStore {

    INSTANCE;

    private Map<String, Genre> availableGenres = new HashMap<>();

    public void registerGenre(Genre genre, String name)
    {
        if (!availableGenres.containsKey(name))
            availableGenres.put(name, genre);
    }

    public void registerGenre(Genre genre)
    {
        registerGenre(genre, genre.getName());
    }

    public Genre getGenre(String symbol)
    {
        return this.availableGenres.get(symbol);
    }

    public Set<String> getDefinedGenres()
    {
        return this.availableGenres.keySet();
    }
}
