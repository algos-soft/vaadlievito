package it.algos.vaadlievito.application;

import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.boot.AVers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * Log delle versioni, modifiche e patch installate
 * Executed on container startup
 * Setup non-UI logic here
 * <p>
 * Classe eseguita solo quando l'applicazione viene caricata/parte nel server (Tomcat od altri) <br>
 * Eseguita quindi ad ogni avvio/riavvio del server e NON ad ogni sessione <br>
 * È OBBLIGATORIO aggiungere questa classe nei listeners del file web.WEB-INF.web.xml
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
@AIScript(sovrascrivibile = false)
public class VaadlievitoVers extends AVers {


    private final static String CODE_PROJECT = "V";


    /**
     * This method is called prior to the servlet context being initialized (when the Web application is deployed).
     * You can initialize servlet context related data here.
     * <p>
     * Tutte le aggiunte, modifiche e patch vengono inserite con una versione <br>
     * L'ordine di inserimento è FONDAMENTALE
     */
    public int inizia() {
        int k = super.inizia();
        codeProject = CODE_PROJECT;

        //--creata una nuova preferenza
        if (installa(++k)) {
            creaPrefDate("pippoz", "Ultimo download del modulo nazionalità");
        }// fine del blocco if

        return k;
    }// end of method


}// end of bootstrap class