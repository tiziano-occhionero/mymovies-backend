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
    
    public Film aggiornaProvenienza(String id, String nuovaProvenienza) {
        Film film = filmRepository.findById(id).orElseThrow(() -> new RuntimeException("Film non trovato"));
        film.setProvenienza(nuovaProvenienza);
        return filmRepository.save(film);
    }

    public Film saveFilm(Film film) {
        return filmRepository.save(film);
    }

    public void deleteFilmById(String id) {
        filmRepository.deleteById(id);
    }
    
    public boolean rimuoviFilm(String id) {
        if (filmRepository.existsById(id)) {
            filmRepository.deleteById(id);
            return true;
        }
        return false;
    }

}
