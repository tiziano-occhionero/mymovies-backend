package com.mymovies.backend.controller;

import com.mymovies.backend.model.Film;
import com.mymovies.backend.service.FilmService;
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

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        return filmService.saveFilm(film);
    }
    
    @GetMapping("/collezione")
    public List<Film> getCollezione() {
        return filmService.getByProvenienza("collezione");
    }

    @GetMapping("/lista-desideri")
    public List<Film> getListaDesideri() {
        return filmService.getByProvenienza("lista");
    }

    @PutMapping("/{id}/provenienza")
    public Film aggiornaProvenienza(@PathVariable String id, @RequestBody String nuovaProvenienza) {
        return filmService.aggiornaProvenienza(id, nuovaProvenienza);
    }

    @DeleteMapping("/{id}")
    public void deleteFilm(@PathVariable String id) {
        filmService.deleteFilmById(id);
    }
}
