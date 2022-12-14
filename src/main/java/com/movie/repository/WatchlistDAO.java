package com.movie.repository;

import com.movie.model.Watchlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WatchlistDAO extends JpaRepository<Watchlist, Long>, CrudRepository<Watchlist, Long> {
    Watchlist findWatchlistById(Long id);
}
