package com.mymovies.backend.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

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

	@Column(nullable = true)
	private Integer tmdbId;

	@Column(nullable = true)
	private String posterPath;

	private String posterUrl;

	// Nuovi campi
	@Column(updatable = false)
	private LocalDateTime dataInserimento;

	private Integer numeroDischi;

	@Column(length = 2048)
	private String note;

	// Costruttore vuoto obbligatorio per JPA
	public Film() {
	}

	public Film(String id, String titolo, int anno, String formato, String custodia, String provenienza,
			Integer tmdbId, String posterPath, String posterUrl, Integer numeroDischi,
			String note) {
		this.id = id;
		this.titolo = titolo;
		this.anno = anno;
		this.formato = formato;
		this.custodia = custodia;
		this.provenienza = provenienza;
		this.tmdbId = tmdbId;
		this.posterPath = posterPath;
		this.posterUrl = posterUrl;
		this.numeroDischi = numeroDischi;
		this.note = note;
	}

	@PrePersist
	protected void onCreate() {
		this.dataInserimento = LocalDateTime.now();
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

	// Getters e Setters per i nuovi campi
	public LocalDateTime getDataInserimento() {
		return dataInserimento;
	}

	public void setDataInserimento(LocalDateTime dataInserimento) {
		this.dataInserimento = dataInserimento;
	}

	public Integer getNumeroDischi() {
		return numeroDischi;
	}

	public void setNumeroDischi(Integer numeroDischi) {
		this.numeroDischi = numeroDischi;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Override
	public String toString() {
		return "Film [id=" + id + ", titolo=" + titolo + ", anno=" + anno + ", formato=" + formato + ", custodia="
				+ custodia + ", provenienza=" + provenienza + ", tmdbId=" + tmdbId + ", posterPath=" + posterPath
				+ ", posterUrl=" + posterUrl + ", dataInserimento=" + dataInserimento + ", numeroDischi=" + numeroDischi + ", note=" + note + "]";
	}
}
