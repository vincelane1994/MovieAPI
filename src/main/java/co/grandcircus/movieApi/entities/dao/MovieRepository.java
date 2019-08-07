package co.grandcircus.movieApi.entities.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import co.grandcircus.movieApi.entities.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long>{
	List<Movie> findByCategory(String category);
	List<Movie> findByTitleContainsIgnoreCase(String titleMatch);
}
