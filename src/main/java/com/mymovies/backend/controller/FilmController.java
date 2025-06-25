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

    // GET /api/films/wishlist → Restituisce tutti i film in wishlist
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
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }




}
