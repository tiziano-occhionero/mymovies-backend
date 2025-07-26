package com.mymovies.backend.service;

import com.mymovies.backend.model.Film;
import com.mymovies.backend.repository.FilmRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FilmService {

	private final FilmRepository filmRepository;

	public FilmService(FilmRepository filmRepository) {
		this.filmRepository = filmRepository;
	}

	public List<Film> getAllFilms() {
		return filmRepository.findAll();
	}

	public List<Film> getByProvenienza(String provenienza) {
		return filmRepository.findByProvenienza(provenienza);
	}

	public Film getFilmById(String id) {
		return filmRepository.findById(id).orElse(null);
	}

	public Film saveFilm(Film film) {
		System.out.println("▶️ Salvataggio film: " + film);
		return filmRepository.save(film);
	}

	public void deleteFilmById(String id) {
		filmRepository.deleteById(id);
	}

}
