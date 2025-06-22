package com.mymovies.backend.repository;

import com.mymovies.backend.model.Film;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilmRepository extends JpaRepository<Film, String> {
	
	List<Film> findByProvenienza(String provenienza);

	
}
