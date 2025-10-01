package com.mymovies.backend.service;

import com.mymovies.backend.model.Film;
import com.mymovies.backend.model.CustomFilmIdSequence;
import com.mymovies.backend.repository.FilmRepository;
import com.mymovies.backend.repository.CustomFilmIdSequenceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FilmService {

    private final FilmRepository filmRepository;
    private final CustomFilmIdSequenceRepository sequenceRepository;

    public FilmService(FilmRepository filmRepository, CustomFilmIdSequenceRepository sequenceRepository) {
        this.filmRepository = filmRepository;
        this.sequenceRepository = sequenceRepository;
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

    @Transactional
    public synchronized String generateCustomFilmId() {
        CustomFilmIdSequence sequence = sequenceRepository.findById("film_sequence").orElse(null);
        if (sequence == null) {
            sequence = new CustomFilmIdSequence("film_sequence", 1L);
        }
        long nextId = sequence.getNextId();
        sequence.setNextId(nextId + 1);
        sequenceRepository.save(sequence);
        return "cstm_" + String.format("%04d", nextId);
    }

    @Transactional
    public Film addCustomFilm(Film film) {
        String customId = generateCustomFilmId();
        String finalId = customId + "_" + film.getFormato() + "_" + film.getCustodia();
        film.setId(finalId);
        film.setTmdbId(null); // Make sure tmdbId is null for custom films
        System.out.println("▶️ Salvataggio film custom: " + film);
        return filmRepository.save(film);
    }

    public void deleteFilmById(String id) {
        filmRepository.deleteById(id);
    }

}
