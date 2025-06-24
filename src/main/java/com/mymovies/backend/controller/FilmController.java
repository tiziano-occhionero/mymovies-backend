package com.mymovies.backend.controller;

import com.mymovies.backend.model.Film;
import com.mymovies.backend.service.FilmService;
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

    @GetMapping("/lista-desideri")
    public List<Film> getListaDesideri() {
        return filmService.getByProvenienza("wishlist");  // <- correzione qui
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
    
    @PostMapping
    public Film addFilm(@RequestBody Film film) {
    	return filmService.saveFilm(film);
    }
    
    @PostMapping("/wishlist")
    public Film aggiungiAllaWishlist(@RequestBody Film film) {
        film.setProvenienza("wishlist"); // Forziamo che sia wishlist
        return filmService.saveFilm(film);
    }

    
    @PutMapping("/{id}/provenienza")
    public Film aggiornaProvenienza(@PathVariable String id, @RequestBody String nuovaProvenienza) {
        return filmService.aggiornaProvenienza(id, nuovaProvenienza);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminaFilm(@PathVariable String id) {
        boolean rimosso = filmService.rimuoviFilm(id);
        if (rimosso) {
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.notFound().build();  // 404 Not Found
        }
    }

}
