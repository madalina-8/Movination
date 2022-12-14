package com.movie.service.impl;

import com.movie.constants.MessageConstants;
import com.movie.exception.InvalidData;
import com.movie.exception.ObjectAlreadyExists;
import com.movie.exception.ObjectNotFound;
import com.movie.exception.ObjectNull;
import com.movie.model.Category;
import com.movie.model.Movie;
import com.movie.model.WatchlistItem;
import com.movie.repository.CategoryDAO;
import com.movie.repository.MovieDAO;
import com.movie.repository.WatchlistItemDAO;
import com.movie.service.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@RequiredArgsConstructor
@Setter
public class MovieServiceImpl implements MovieService {
    private CategoryDAO categoryDAO;
    private MovieDAO movieDAO;
    private WatchlistItemDAO watchlistItemDAO;

    @Override
    public void save(Movie movie) throws ObjectAlreadyExists, InvalidData {
        if (movieDAO.findMovieById(movie.getId()) != null)
            throw new ObjectAlreadyExists(MessageConstants.MOVIE_EXISTS);
        if (movie.getYear() != null && (movie.getYear() < 1800 || movie.getYear() > getCurrentYear()))
            throw new InvalidData(MessageConstants.INVALID_YEAR);
        if (movie.getMinutes() != null && movie.getMinutes() < 1)
            throw new InvalidData(MessageConstants.INVALID_DURATION);
        movieDAO.save(movie);
    }

    @Override
    public List<Movie> getAllMovies() {
        return movieDAO.findAll();
    }

    private Integer getCurrentYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    @Override
    public Movie getById(Long id) {
        return movieDAO.findMovieById(id);
    }

    @Override
    public List<Movie> getMovieByCategoryName(String categoryName) throws ObjectNotFound {
        Category category = categoryDAO.findCategoryByName(categoryName);
        if (category == null)
            throw new ObjectNotFound(MessageConstants.CATEGORY_NOT_FOUND);
        return category.getMovies();
    }

    @Override
    public void addCategoriesToMovie(Long idMovie, List<String> categoryNames) throws ObjectNotFound, ObjectAlreadyExists {
        Movie movie = movieDAO.findMovieById(idMovie);
        if (movie == null)
            throw new ObjectNotFound(MessageConstants.MOVIE_NOT_FOUND);
        List<Category> categories = new ArrayList<>();
        for (String categoryName : categoryNames) {
            Category category = categoryDAO.findCategoryByName(categoryName);
            if (category == null)
                throw new ObjectNotFound(MessageConstants.CATEGORY_NOT_FOUND);
            if (movie.getCategories().contains(category))
                throw new ObjectAlreadyExists(MessageConstants.CATEGORY_EXISTS_IN_MOVIE);
            categories.add(category);
        }
        movie.setCategories(categories);
        movieDAO.save(movie);
        addMovieToCategories(movie, categories);
    }

    @Override
    public Movie getMovieByItemId(Long id) throws ObjectNotFound {
        WatchlistItem watchlistItem = watchlistItemDAO.findItemById(id);
        if (watchlistItem == null)
            throw new ObjectNotFound(MessageConstants.ITEM_NOT_FOUND);
        return watchlistItem.getMovie();
    }

    private void addMovieToCategories(Movie movie, List<Category> categories) {
        if (categories.size() > 0 && movie != null) {
            for (Category category : categories) {
                List<Movie> moviesOfCategory = category.getMovies();
                moviesOfCategory.add(movie);
                category.setMovies(moviesOfCategory);
                categoryDAO.save(category);
            }
        }
    }

    @Override
    public List<Movie> getMovieBySearch(String search) throws ObjectNotFound {
        List<Movie> movies = movieDAO.findMoviesByNameContains(search);
        if (movies == null) {
            throw new ObjectNotFound(MessageConstants.MOVIE_NOT_FOUND);
        }
        return movies;
    }

    @Override
    public void delete(Long idMovie) throws ObjectNotFound {
        Movie movie = movieDAO.findMovieById(idMovie);
        if (movie == null)
            throw new ObjectNotFound(MessageConstants.MOVIE_NOT_FOUND);
        List<Category> categories = movie.getCategories();
        List<WatchlistItem> watchlistItems = movie.getWatchlistItems();
        for (Category category : categories) {
            List<Movie> movies = category.getMovies();
            movies.remove(movie);
            category.setMovies(movies);
            categoryDAO.save(category);
        }
        watchlistItemDAO.deleteAll(watchlistItems);
        movieDAO.delete(movie);
    }

    @Override
    public void update(Long id, String name, String description, Long minutes, String picture, Integer year) throws InvalidData, ObjectNotFound, ObjectNull {
        if (id == null || name == null || description == null || minutes == null || picture == null || year == null)
            throw new ObjectNull(MessageConstants.FIELD_CANNOT_BE_EMPTY);
        Movie movie = movieDAO.findMovieById(id);
        if (movie == null) throw new ObjectNotFound(MessageConstants.MOVIE_NOT_FOUND);
        if (year != null && (year < 1800 || year > getCurrentYear()))
            throw new InvalidData(MessageConstants.INVALID_YEAR);
        if (minutes != null && minutes < 1)
            throw new InvalidData(MessageConstants.INVALID_DURATION);
        movieDAO.update(id, name, description, minutes, picture, year);
    }

    public void setCategories(Movie movie, List<Category> categories) {
        movie.setCategories(categories);
    }

    @Override
    public void deleteCategories(Movie movie, List<Category> categories) {
        if (!categories.isEmpty()) {
            for (Category category : categories) {
                List<Movie> movies = category.getMovies();
                if (movies.contains(movie)) {
                    movies.remove(movie);
                    category.setMovies(movies);
                }
                categoryDAO.save(category);
            }
            if (movie.getCategories() != null) {
                movie.setCategories(null);
                movieDAO.save(movie);
            }
        }
    }
}
