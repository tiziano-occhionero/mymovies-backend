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

    @DeleteMapping("/{id}")
    public void deleteFilm(@PathVariable String id) {
        filmService.deleteFilmById(id);
    }
}
