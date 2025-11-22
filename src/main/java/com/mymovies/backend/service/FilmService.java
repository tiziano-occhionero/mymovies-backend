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

    private String transformGoogleDriveUrl(String url) {
        if (url != null && url.contains("drive.google.com/file/d/")) {
            try {
                String fileId = url.split("/d/")[1].split("/")[0];
                return "https://drive.google.com/uc?export=view&id=" + fileId;
            } catch (Exception e) {
                // Log the error and return original URL if parsing fails
                System.err.println("Error parsing Google Drive URL: " + url + " - " + e.getMessage());
                return url;
            }
        }
        return url;
    }

    public Film saveFilm(Film film) {
        // Se il film è nuovo (senza ID), genera un ID custom
        if (film.getId() == null || film.getId().trim().isEmpty()) {
            String customId = generateCustomFilmId();
            String finalId = customId + "_" + film.getFormato() + "_" + film.getCustodia();
            film.setId(finalId);
            // Assicuriamoci che non ci sia un tmdbId per coerenza con i film custom
            film.setTmdbId(null);
        }

        film.setPosterUrl(transformGoogleDriveUrl(film.getPosterUrl()));
        System.out.println("▶️ Salvataggio film: " + film);
        return filmRepository.save(film);
    }

    public Film updateFilm(String id, Film filmDetails) {
        return filmRepository.findById(id).map(film -> {
            film.setTitolo(filmDetails.getTitolo());
            film.setAnno(filmDetails.getAnno());
            film.setFormato(filmDetails.getFormato());
            film.setCustodia(filmDetails.getCustodia());
            film.setProvenienza(filmDetails.getProvenienza());
            film.setTmdbId(filmDetails.getTmdbId());
            film.setPosterPath(filmDetails.getPosterPath());
            film.setPosterUrl(transformGoogleDriveUrl(filmDetails.getPosterUrl()));
            film.setVersioneSpeciale(filmDetails.isVersioneSpeciale());
            film.setNumeroDischi(filmDetails.getNumeroDischi());
            film.setNote(filmDetails.getNote());
            System.out.println("▶️ Aggiornamento film: " + film);
            return filmRepository.save(film);
        }).orElse(null);
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
        film.setPosterUrl(transformGoogleDriveUrl(film.getPosterUrl()));
        System.out.println("▶️ Salvataggio film custom: " + film);
        return filmRepository.save(film);
    }

    public void deleteFilmById(String id) {
        filmRepository.deleteById(id);
    }

}
