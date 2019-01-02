package com.mmt.moviebooking.store;

import com.mmt.moviebooking.model.Category;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by jitheshrajan on 11/17/18.
 */
public enum CategoryStore {

    INSTANCE;

    private Map<String, Category> availableCategories = new HashMap<>();

    public void registerCategory(Category category, String name)
    {
        if (!availableCategories.containsKey(name))
            availableCategories.put(name, category);
    }

    public void registerCategory(Category category)
    {
        registerCategory(category, category.getName());
    }

    public Category getCategoryName(String name)
    {
        return this.availableCategories.get(name);
    }

    public Set<String> getDefinedCategories()
    {
        return this.availableCategories.keySet();
    }
}
