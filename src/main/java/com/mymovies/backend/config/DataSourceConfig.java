package com.mymovies.backend.config;

import javax.sql.DataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Configurazione programmatica del DataSource per l'ambiente di produzione su Railway.
 * Questa classe è attiva solo quando il profilo 'prod' è abilitato.
 * Si occupa di leggere la variabile d'ambiente DATABASE_URL fornita da Railway,
 * parsarla per estrarre le credenziali e l'URL JDBC, e costruire un DataSource.
 */
@Configuration
@Profile("prod") // Questa configurazione si applica solo quando il profilo 'prod' è attivo
public class DataSourceConfig {

    /**
     * Definisce e configura un bean DataSource utilizzando la variabile d'ambiente DATABASE_URL.
     *
     * @return Un DataSource configurato per la connessione al database PostgreSQL.
     * @throws URISyntaxException Se la DATABASE_URL non è un URI valido.
     */
    @Bean
    public DataSource dataSource() throws URISyntaxException {
        // Legge la variabile d'ambiente DATABASE_URL fornita da Railway
        URI dbUri = new URI(System.getenv("DATABASE_URL"));

        // Estrae username e password dalla parte userinfo dell'URI
        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];

        // Costruisce l'URL JDBC nel formato richiesto dal driver PostgreSQL
        // Sostituisce 'postgres://' con 'jdbc:postgresql://' e aggiunge host, porta e path
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

        // Utilizza DataSourceBuilder di Spring Boot per creare e configurare il DataSource
        return DataSourceBuilder.create()
                .url(dbUrl)
                .username(username)
                .password(password)
                .build();
    }
}

