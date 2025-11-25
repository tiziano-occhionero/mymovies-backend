package com.mymovies.backend.controller;

import com.mymovies.backend.model.Film;
import com.mymovies.backend.service.FilmService;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/films")
@CrossOrigin(origins = "*") // utile per test futuri col frontend
public class FilmController {

    private static final Logger logger = LoggerFactory.getLogger(FilmController.class);

    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
        logger.info("FilmController initialized!");
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

    @PostMapping("/custom")
    public ResponseEntity<Film> addCustomFilm(@Valid @RequestBody Film film) {
        logger.info("addCustomFilm method called with film: {}", film);
        Film savedFilm = filmService.addCustomFilm(film);
        return new ResponseEntity<>(savedFilm, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Film> updateFilm(@PathVariable String id, @Valid @RequestBody Film filmDetails) {
        logger.info("Richiesta di aggiornamento ricevuta per l'ID: {}. Dettagli: {}", id, filmDetails);
        Film updatedFilm = filmService.updateFilm(id, filmDetails);
        if (updatedFilm != null) {
            return ResponseEntity.ok(updatedFilm);
        } else {
            logger.warn("Aggiornamento fallito: film non trovato con ID: {}", id);
            return ResponseEntity.notFound().build();
        }
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

    // ✅ NUOVO: DELETE generica usata dal frontend (es. /api/films/{id})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminaGenerica(@PathVariable String id) {
        filmService.deleteFilmById(id);
        return ResponseEntity.noContent().build(); // 204
    }
}
