package com.mymovies.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Film {

    @Id
    private String id; // formato: tmdbId_formato_custodia

    private String titolo;
    private int anno;
    private String formato;    // es. dvd, bd, uhdbd
    private String custodia;   // es. standard, steelbook
    private String provenienza; // collezione o lista desideri

    // Costruttore vuoto obbligatorio per JPA
    public Film() {}

    public Film(String id, String titolo, int anno, String formato, String custodia, String provenienza) {
        this.id = id;
        this.titolo = titolo;
        this.anno = anno;
        this.formato = formato;
        this.custodia = custodia;
        this.provenienza = provenienza;
    }

    // Getters e Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitolo() { return titolo; }
    public void setTitolo(String titolo) { this.titolo = titolo; }

    public int getAnno() { return anno; }
    public void setAnno(int anno) { this.anno = anno; }

    public String getFormato() { return formato; }
    public void setFormato(String formato) { this.formato = formato; }

    public String getCustodia() { return custodia; }
    public void setCustodia(String custodia) { this.custodia = custodia; }

    public String getProvenienza() { return provenienza; }
    public void setProvenienza(String provenienza) { this.provenienza = provenienza; }
}
