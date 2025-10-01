package com.mymovies.backend.repository;

import com.mymovies.backend.model.CustomFilmIdSequence;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomFilmIdSequenceRepository extends JpaRepository<CustomFilmIdSequence, String> {
}
