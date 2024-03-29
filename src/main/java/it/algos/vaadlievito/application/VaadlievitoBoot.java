package it.algos.vaadlievito.application;

import it.algos.vaadflow.annotation.*;
import static it.algos.vaadflow.application.FlowCost.*;
import it.algos.vaadflow.application.*;
import it.algos.vaadflow.backend.login.*;
import it.algos.vaadflow.boot.*;
import it.algos.vaadflow.modules.company.*;
import it.algos.vaadflow.modules.log.*;
import it.algos.vaadflow.modules.utente.*;
import it.algos.vaadlievito.modules.spacciamadre.*;
import lombok.extern.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.*;

import javax.annotation.*;
import java.time.*;
import java.util.*;

/**
 * Project vaadlievito
 * Created by Algos flowbase
 * User: gac
 * Date: ven, 8-mag-2018
 * <p>
 * Estende la classe ABoot per le regolazioni iniziali di questa applicazione <br>
 * Running logic after the Spring context has been initialized
 * The method onApplicationEvent() will be executed before the application is up and <br>
 * Aggiunge tutte le @Route (views) standard e specifiche di questa applicazione <br>
 * <p>
 * Annotated with @SpringComponent (obbligatorio) <br>
 * Annotated with @Scope (obbligatorio) <br>
 * Annotated with @AIScript (facoltativo) per controllare la ri-creazione di questo file nello script di algos <br>
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
@AIScript(sovrascrivibile = false)
public class VaadlievitoBoot extends ABoot {

    public final static boolean USA_SECURITY = false;

    public final static boolean USA_COMPANY = false;

    public final static String DEMO_COMPANY_CODE = "demo";

    private final static String PROJECT_NAME = "lievito";

    private final static double PROJECT_VERSION = 1.0;

    private final static LocalDate VERSION_DATE = LocalDate.of(2020, 4, 11);

    /**
     * La injection viene fatta da SpringBoot in automatico <br>
     */
    @Autowired
    protected LogService logger;

    /**
     * Iniettata dal costruttore <br>
     */
    private VaadlievitoVers vaadlievitoVers;


    /**
     * Costruttore @Autowired <br>
     *
     * @param wamVersBoot Log delle versioni, modifiche e patch installat
     */
    @Autowired
    public VaadlievitoBoot(VaadlievitoVers vaadlievitoVers) {
        super();
        this.vaadlievitoVers = vaadlievitoVers;
    }// end of Spring constructor


    /**
     * La injection viene fatta da Java/SpringBoot SOLO DOPO l'init() interno del costruttore dell'istanza <br>
     * Si usa un metodo @PostConstruct per avere disponibili tutte le istanze @Autowired <br>
     * <p>
     * Performing the initialization in a constructor is not suggested
     * as the state of the UI is not properly set up when the constructor is invoked.
     * <p>
     * Ci possono essere diversi metodi con @PostConstruct e firme diverse e funzionano tutti,
     * ma l'ordine con cui vengono chiamati NON è garantito
     * DEVE essere inserito nella sottoclasse e invocare (eventualmente) un metodo della superclasse.
     */
    @PostConstruct
    protected void postConstruct() {
        logger.crea("Pippoz");
    }// end of method


    /**
     * Inizializzazione delle versioni standard di vaadinflow <br>
     * Inizializzazione delle versioni del programma specifico <br>
     */
    protected void iniziaVersioni() {
        vaadlievitoVers.inizia();
    }// end of method


    /**
     * Riferimento alla sottoclasse specifica di ABoot per utilizzare il metodo sovrascritto resetPreferenze() <br>
     * Il metodo DEVE essere sovrascritto nella sottoclasse specifica <br>
     */
    protected void regolaRiferimenti() {
        preferenzaService.applicationBoot = this;
    }// end of method


    /**
     * Crea le preferenze standard <br>
     * Se non esistono, le crea <br>
     * Se esistono, NON modifica i valori esistenti <br>
     * Per un reset ai valori di default, c'è il metodo reset() chiamato da preferenzaService <br>
     * Il metodo può essere sovrascritto per creare le preferenze specifiche dell'applicazione <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    @Override
    protected int creaPreferenze() {
        int numPref = super.creaPreferenze();

        return numPref;
    }// end of method


    /**
     * Eventuali regolazione delle preferenze standard effettuata nella sottoclasse specifica <br>
     * Serve per modificare solo per l'applicazione specifica il valore standard della preferenza <br>
     * Eventuali modifiche delle preferenze specifiche (che peraltro possono essere modificate all'origine) <br>
     * Metodo che DEVE essere sovrascritto <br>
     */
    @Override
    protected void fixPreferenze() {
    }// end of method


    /**
     * Cancella e ricrea le preferenze standard <br>
     * Metodo invocato dal metodo reset() di preferenzeService per poter usufruire della sovrascrittura
     * nella sottoclasse specifica dell'applicazione <br>
     * Il metodo può essere sovrascitto per ricreare le preferenze specifiche dell'applicazione <br>
     * Le preferenze standard sono create dalla enumeration EAPreferenza <br>
     * Le preferenze specifiche possono essere create da una Enumeration specifica, oppure singolarmente <br>
     * Invocare PRIMA il metodo della superclasse <br>
     *
     * @return numero di preferenze creato
     */
    @Override
    public int resetPreferenze() {
        int numPref = super.resetPreferenze();

        return numPref;
    }// end of method


    /**
     * Regola alcune informazioni dell'applicazione
     */
    protected void regolaInfo() {
        /**
         * Nome identificativo dell'applicazione <br>
         * Usato (eventualmente) nella barra di informazioni a piè di pagina <br>
         */
        FlowVar.projectName = PROJECT_NAME;

        /**
         * Versione dell'applicazione <br>
         * Usato (eventualmente) nella barra di informazioni a piè di pagina <br>
         */
        FlowVar.projectVersion = PROJECT_VERSION;

        /**
         * Data della versione dell'applicazione <br>
         * Usato (eventualmente) nella barra di informazioni a piè di pagina <br>
         */
        FlowVar.versionDate = VERSION_DATE;

        /**
         * Controlla se l'applicazione usa il login oppure no <br>
         * Se si usa il login, occorre la classe SecurityConfiguration <br>
         * Se non si usa il login, occorre disabilitare l'Annotation @EnableWebSecurity di SecurityConfiguration <br>
         * Di defaul (per sicurezza) uguale a true <br>
         */
        FlowVar.usaSecurity = USA_SECURITY;

        /**
         * Controlla se l'applicazione è multi-company oppure no <br>
         * Di defaul (per sicurezza) uguale a true <br>
         * Deve essere regolato in xxxBoot.regolaInfo() sempre presente nella directory 'application' <br>
         */
        FlowVar.usaCompany = USA_COMPANY;


        /**
         * Derscrizione completa dell'applicazione <br>
         * Usato (eventualmente) nella barra di menu in testa pagina <br>
         * Usato (eventualmente) nella barra di informazioni a piè di pagina <br>
         */
        FlowVar.projectBanner = VUOTA;


        /**
         * Service da usare per recuperare dal mongoDB l'utenza loggata tramite 'username' che è unico <br>
         * Di default UtenteService oppure eventuale sottoclasse specializzata per applicazioni con accessi particolari <br>
         * Eventuale casting a carico del chiamante <br>
         * Deve essere regolata in xxxBoot.regolaInfo() sempre presente nella directory 'application' <br>
         */
        FlowVar.loginServiceClazz = UtenteService.class;

        /**
         * Classe da usare per gestire le informazioni dell'utenza loggata <br>
         * Di default ALogin oppure eventuale sottoclasse specializzata per applicazioni con accessi particolari <br>
         * Eventuale casting a carico del chiamante <br>
         * Deve essere regolata in xxxBoot.regolaInfo() sempre presente nella directory 'application' <br>
         */
        FlowVar.loginClazz = ALogin.class;

        /**
         * Service da usare per recuperare la lista delle Company (o sottoclassi) <br>
         * Di default CompanyService oppure eventuale sottoclasse specializzata per Company particolari <br>
         * Eventuale casting a carico del chiamante <br>
         * Deve essere regolata in xxxBoot.regolaInfo() sempre presente nella directory 'application' <br>
         */
        FlowVar.companyServiceClazz = CompanyService.class;

        /**
         * Path per recuperare dalle risorse un'immagine da inserire nella barra di menu di MainLayout14 <br>
         * Ogni applicazione può modificarla <br>
         * Deve essere regolata in xxxBoot.regolaInfo() sempre presente nella directory 'application' <br>
         */
        FlowVar.pathLogo = null;

        /**
         * Controlla se l'applicazione usa il Drawer laterale oppure no <br>
         * Di defaul uguale a true <br>
         * Deve essere regolato in xxxBoot.regolaInfo() sempre presente nella directory 'application' <br>
         */
        FlowVar.usaDrawer = false;

    }// end of method


    /**
     * Inizializzazione dei dati di alcune collections specifiche sul DB mongo
     */
    protected void iniziaDataProgettoSpecifico() {
    }// end of method


    /**
     * Questa classe viene invocata PRIMA della chiamata del browser
     * Se NON usa la security, le @Route vengono create solo qui
     * Se USA la security, le @Route vengono sovrascritte all'apertura del brose nella classe AUserDetailsService
     * <p>
     * Aggiunge le @Route (view) specifiche di questa applicazione
     * Le @Route vengono aggiunte ad una Lista statica mantenuta in BaseCost
     * Vengono aggiunte dopo quelle standard
     * Verranno lette da MainLayout la prima volta che il browser 'chiama' una view
     */
    protected void addRouteSpecifiche() {
        FlowVar.menuClazzList.add(SpacciamadreList.class);
        FlowVar.menuClazzList = new ArrayList<>();
    }// end of method


}// end of boot class