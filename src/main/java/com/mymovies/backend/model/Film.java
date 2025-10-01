package com.mymovies.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;

@Entity
public class Film {

	@Id
	private String id; // formato:

	@NotBlank(message = "Il titolo è obbligatorio")
	private String titolo;

	@Min(value = 1900, message = "L'anno deve essere maggiore di 1900")
	private int anno;

	@NotBlank(message = "Il formato è obbligatorio")
	private String formato; // es. dvd, bd, uhdbd

	@NotBlank(message = "La custodia è obbligatoria")
	private String custodia; // es. standard, steelbook

	@NotBlank(message = "La provenienza è obbligatoria")
	private String provenienza; // collezione o lista desideri

	private Integer tmdbId;

	private String posterPath;
	
	private String posterUrl;

	// Costruttore vuoto obbligatorio per JPA
	public Film() {
	}

	public Film(String id, String titolo, int anno, String formato, String custodia, String provenienza, Integer tmdbId,
			String posterPath, String posterUrl) {
		this.id = id;
		this.titolo = titolo;
		this.anno = anno;
		this.formato = formato;
		this.custodia = custodia;
		this.provenienza = provenienza;
		this.tmdbId = tmdbId;
		this.posterPath = posterPath;
		this.posterUrl = posterUrl;
	}

	// Getters e Setters
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public int getAnno() {
		return anno;
	}

	public void setAnno(int anno) {
		this.anno = anno;
	}

	public String getFormato() {
		return formato;
	}

	public void setFormato(String formato) {
		this.formato = formato;
	}

	public String getCustodia() {
		return custodia;
	}

	public void setCustodia(String custodia) {
		this.custodia = custodia;
	}

	public String getProvenienza() {
		return provenienza;
	}

	public void setProvenienza(String provenienza) {
		this.provenienza = provenienza;
	}

	public Integer getTmdbId() {
		return tmdbId;
	}

	public void setTmdbId(Integer tmdbId) {
		this.tmdbId = tmdbId;
	}

	public String getPosterPath() {
		return posterPath;
	}

	public void setPosterPath(String posterPath) {
		this.posterPath = posterPath;
	}

	public String getPosterUrl() {
		return posterUrl;
	}

	public void setPosterUrl(String posterUrl) {
		this.posterUrl = posterUrl;
	}

	@Override
	public String toString() {
	    return "Film{id='%s', titolo='%s', anno=%d, formato='%s', custodia='%s', provenienza='%s', tmdbId=%d, posterPath='%s', posterUrl='%s'}"
	            .formatted(id, titolo, anno, formato, custodia, provenienza, tmdbId, posterPath, posterUrl);
	}


}
