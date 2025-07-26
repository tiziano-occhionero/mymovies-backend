package com.mymovies.backend.controller;

import com.mymovies.backend.model.Film;
import com.mymovies.backend.service.FilmService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/films")
@CrossOrigin(origins = "*") // utile per test futuri col frontend
public class FilmController {

	private final FilmService filmService;

	public FilmController(FilmService filmService) {
		this.filmService = filmService;
	}

	@GetMapping
	public List<Film> getAllFilms() {
		return filmService.getAllFilms();
	}

	@GetMapping("/collezione")
	public List<Film> getCollezione() {
		return filmService.getByProvenienza("collezione");
	}

	@GetMapping("/wishlist")
	public List<Film> getWishlist() {
		return filmService.getByProvenienza("wishlist");
	}

	@GetMapping("/{id}")
	public ResponseEntity<Film> getFilmById(@PathVariable String id) {
		Film film = filmService.getFilmById(id);
		if (film != null) {
			return ResponseEntity.ok(film);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping("/collezione")
	public Film aggiungiAllaCollezione(@RequestBody Film film) {
		film.setProvenienza("collezione"); // Forziamo che sia collezione
		return filmService.saveFilm(film);
	}

	@PostMapping("/wishlist")
	public Film aggiungiAllaWishlist(@RequestBody Film film) {
		film.setProvenienza("wishlist"); // Forziamo che sia wishlist
		return filmService.saveFilm(film);
	}

	@PostMapping
	public ResponseEntity<Film> aggiungiFilm(@Valid @RequestBody Film film) {
		return ResponseEntity.ok(filmService.saveFilm(film));
	}

	@DeleteMapping("/collezione/{id}")
	public ResponseEntity<Void> eliminaDaCollezione(@PathVariable String id) {
		filmService.deleteFilmById(id);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/wishlist/{id}")
	public ResponseEntity<Void> eliminaDaWishlist(@PathVariable String id) {
		filmService.deleteFilmById(id);
		return ResponseEntity.noContent().build();
	}

}
