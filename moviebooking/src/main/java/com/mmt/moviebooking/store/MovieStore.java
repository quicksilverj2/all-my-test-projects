package com.mmt.moviebooking.store;

import com.mmt.moviebooking.model.Category;
import com.mmt.moviebooking.model.Genre;
import com.mmt.moviebooking.model.Movie;
import com.mmt.moviebooking.model.cast.Actor;
import com.mmt.moviebooking.model.cast.CastMember;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import com.googlecode.concurrenttrees.radix.ConcurrentRadixTree;
import com.googlecode.concurrenttrees.radix.node.concrete.SmartArrayBasedNodeFactory;

/**
 * Created by jitheshrajan on 11/17/18.
 *
 * This will be a class for storing all the movies entirely. This is where the search will also work from!
 */
public class MovieStore {

    private static final Object lock = new Object();

    private AtomicLong movieId = new AtomicLong(1000000);

    private static volatile MovieStore instance;

    private ConcurrentRadixTree<MovieResults> movieConcurrentRadixTree;

    private ConcurrentRadixTree<MovieResults> actorConcurrentRadixTree;

    private ConcurrentRadixTree<MovieResults> directorConcurrentRadixTree;

    private HashMap<Genre, List<Movie>> genreListHashMap;

    private HashMap<Category, List<Movie>> categoryListHashMap;


    public static MovieStore getInstance() {
        MovieStore r = instance;
        if (null == r) {
            synchronized (lock) {
                r = instance;
                if (r == null) {
                    instance = r = new MovieStore();
                }
            }
        }
        return r;
    }

    private MovieStore() {
        movieConcurrentRadixTree = new ConcurrentRadixTree<>(new SmartArrayBasedNodeFactory());
        actorConcurrentRadixTree = new ConcurrentRadixTree<>(new SmartArrayBasedNodeFactory());
        directorConcurrentRadixTree = new ConcurrentRadixTree<>(new SmartArrayBasedNodeFactory());
        genreListHashMap = new HashMap<>();
    }

    public static boolean addMovie(String name, long releaseDate, List<CastMember> castMembers){

        Movie.MovieBuilder movieBuilder = new Movie.MovieBuilder();
        movieBuilder.setName(name).setReleaseDate(releaseDate).setCast(castMembers);

        Movie movie = movieBuilder.build();

        return addMovie(movie);
    }

    public static boolean addMovie(Movie movie) {

        System.out.println("Adding movie");

        getInstance().addSpaceSeparatedKeysToTree(movie.getName(), movie, getInstance().movieConcurrentRadixTree);

        movie.getCast()
                .stream()
                .forEach(castMember -> getInstance().addSpaceSeparatedKeysToTree(castMember.getName(),movie,
                        castMember instanceof Actor ?
                                getInstance().actorConcurrentRadixTree :
                                getInstance().directorConcurrentRadixTree));

        return true;
    }

    public static boolean addGenreToMovie(String genre, Movie movie){

        Movie.MovieUpdater.addGenre(movie, GenreStore.INSTANCE.getGenre(genre));
        getInstance().genreListHashMap.get(GenreStore.INSTANCE.getGenre(genre)).add(movie);

        return true;
    }

    public static boolean addCategoryToMovie(String category, Movie movie){

        Movie.MovieUpdater.addCategory(movie, CategoryStore.INSTANCE.getCategoryName(category));
        getInstance().categoryListHashMap.get(CategoryStore.INSTANCE.getCategoryName(category)).add(movie);

        return true;
    }


    public void addFullKeyToTree(String key, Movie movie, ConcurrentRadixTree<MovieResults> tree) {

        if (key == null || key.equalsIgnoreCase("")) {
            return;
        }

        key = key.toUpperCase(); // to make all keys uppercase and trimmed

		/*log.debug("key : " + key);*/

        MovieResults movies = null;
        if (tree.getValueForExactKey(key) == null) {
            movies = new MovieResults();
        } else {
            movies = tree.getValueForExactKey(key);
        }
        movies.add(movie);
        tree.put(key, movies);
    }

    public void addSpaceSeparatedKeysToTree(String key, Movie movie, ConcurrentRadixTree<MovieResults> tree) {

        System.out.println("Adding movie to tree  :"+key + " "+movie.getName());

        if (key == null || key.equalsIgnoreCase("")) {
            return;
        }

        key = key.trim();

        String[] splitKeys = key.split(" ");
        for (String splitKey : splitKeys) {
            addFullKeyToTree(splitKey, movie, tree);
        }
    }


    public MovieResults getSearchResultListMovie(String sStr) {

        if ("".equals(sStr)) {
            return new MovieResults();
        }

        System.out.println("Search String : " + sStr);

        sStr = sStr.trim();
        sStr = sStr.toUpperCase();

        MovieResults resultList = new MovieResults();

        String[] splitsStr = sStr.split("\\s");

        if (splitsStr.length > 1) {

            long maxLength = -1;
            String maxLengthWord = "";
            StringBuilder pattern = new StringBuilder("");
            for (String word : splitsStr) {
                if (word.length() > maxLength) {
                    maxLength = word.length();
                    maxLengthWord = word;
                }
                pattern.append(word).append(".* ");
            }

            String fullPattern = pattern.toString();
            fullPattern = fullPattern.substring(0, fullPattern.length() - 1); // to remove the last whitespace!

            System.out.println("maxLengthWord : " + maxLengthWord);
            Iterable<MovieResults> searchList = this.movieConcurrentRadixTree.getValuesForKeysStartingWith(maxLengthWord);

            // this needs to be tweaked if search has to support like 'Sultan salman'
            for (MovieResults eachResult : searchList) {
                //log.debug("Each Result Size for Longest Search Word : " + eachResult.size());
                for (Movie searchData : eachResult) {
                    System.out.println("Description from Tree : " + searchData.getName());

                    if (searchData.getName().matches(fullPattern) || searchData.getName().matches(".* " + fullPattern)) {
                        resultList.add(searchData);
                    }
                }
            }

        } else {

            Iterable<MovieResults> searchList = this.movieConcurrentRadixTree.getValuesForKeysStartingWith(sStr);

            for (MovieResults eachResult : searchList) {
                /*log.debug("Each Result Size : " + eachResult.size());
				for (EQSearchData debugSearchData : eachResult) {
					log.debug("symbol from Tree : " + debugSearchData.getsSymbol());
				}*/
                resultList.addAll(eachResult);
            }
        }

        System.out.println("Final Result List Size : " + resultList.size());

        return resultList;
    }


    public MovieResults getSearchResultListCastName(String sStr) {

        if ("".equals(sStr)) {
            return new MovieResults();
        }

        System.out.println("Search String : " + sStr);

        sStr = sStr.trim();
        sStr = sStr.toUpperCase();

        MovieResults resultList = new MovieResults();

        String[] splitsStr = sStr.split("\\s");

        if (splitsStr.length > 1) {

            long maxLength = -1;
            String maxLengthWord = "";
            StringBuilder pattern = new StringBuilder("");
            for (String word : splitsStr) {
                if (word.length() > maxLength) {
                    maxLength = word.length();
                    maxLengthWord = word;
                }
                pattern.append(word).append(".* ");
            }

            String fullPattern = pattern.toString();
            fullPattern = fullPattern.substring(0, fullPattern.length() - 1); // to remove the last whitespace!

            System.out.println("maxLengthWord : " + maxLengthWord);
            Iterable<MovieResults> searchList = this.actorConcurrentRadixTree.getValuesForKeysStartingWith(maxLengthWord);

            // this needs to be tweaked if search has to support like 'Sultan salman'
            for (MovieResults eachResult : searchList) {
                //log.debug("Each Result Size for Longest Search Word : " + eachResult.size());
                for (Movie searchData : eachResult) {
                    System.out.println("Description from Tree : " + searchData.getName());
                    for(CastMember castMember : searchData.getCast()){
                        if (castMember.getName().matches(fullPattern)
                                || searchData.getName().matches(".* " + fullPattern)) {
                            resultList.add(searchData);
                        }
                    }
                }
            }

        } else {

            Iterable<MovieResults> searchList = this.actorConcurrentRadixTree.getValuesForKeysStartingWith(sStr);

            for (MovieResults eachResult : searchList) {
                /*log.debug("Each Result Size : " + eachResult.size());
				for (EQSearchData debugSearchData : eachResult) {
					log.debug("symbol from Tree : " + debugSearchData.getsSymbol());
				}*/
                resultList.addAll(eachResult);
            }
        }

        System.out.println("Final Result List Size : " + resultList.size());

        return resultList;
    }



}
