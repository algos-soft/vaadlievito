package it.algos.vaadlievito.modules.spacciamadre;

import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.application.AContext;
import it.algos.vaadflow.modules.comune.Comune;
import it.algos.vaadflow.modules.provincia.Provincia;
import it.algos.vaadflow.modules.regione.Regione;
import it.algos.vaadflow.service.AService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static it.algos.vaadflow.application.FlowCost.VUOTA;
import static it.algos.vaadlievito.application.VaadlievitoCost.TAG_SPA;

/**
 * Project vaadlievito <br>
 * Created by Algos <br>
 * User: Gac <br>
 * Fix date: 7-apr-2020 14.05.47 <br>
 * <br>
 * Business class. Layer di collegamento per la Repository. <br>
 * <br>
 * Annotated with @Service (obbligatorio, se si usa la catena @Autowired di SpringBoot) <br>
 * NOT annotated with @SpringComponent (inutile, esiste già @Service) <br>
 * Annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (obbligatorio) <br>
 * NOT annotated with @VaadinSessionScope (sbagliato, perché SpringBoot va in loop iniziale) <br>
 * Annotated with @Qualifier (obbligatorio) per permettere a Spring di istanziare la classe specifica <br>
 * Annotated with @@Slf4j (facoltativo) per i logs automatici <br>
 * Annotated with @AIScript (facoltativo Algos) per controllare la ri-creazione di questo file dal Wizard <br>
 * - la documentazione precedente a questo tag viene SEMPRE riscritta <br>
 * - se occorre preservare delle @Annotation con valori specifici, spostarle DOPO @AIScript <br>
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Qualifier(TAG_SPA)
@Slf4j
@AIScript(sovrascrivibile = false)
public class SpacciamadreService extends AService {


    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * La repository viene iniettata dal costruttore e passata al costruttore della superclasse, <br>
     * Spring costruisce una implementazione concreta dell'interfaccia MongoRepository (prevista dal @Qualifier) <br>
     * Qui si una una interfaccia locale (col casting nel costruttore) per usare i metodi specifici <br>
     */
    public SpacciamadreRepository repository;


    /**
     * Costruttore @Autowired <br>
     * Si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Si usa una costante statica, per essere sicuri di scrivere sempre uguali i riferimenti <br>
     * Regola il modello-dati specifico e lo passa al costruttore della superclasse <br>
     *
     * @param repository per la persistenza dei dati
     */
    @Autowired
    public SpacciamadreService(@Qualifier(TAG_SPA) MongoRepository repository) {
        super(repository);
        super.entityClass = Spacciamadre.class;
        this.repository = (SpacciamadreRepository) repository;
    }// end of Spring constructor


    /**
     * Ricerca di una entity (la crea se non la trova) <br>
     *
     * @param nickname di riferimento (obbligatorio ed unico)
     *
     * @return la entity trovata o appena creata
     */
    public Spacciamadre findOrCrea(String nickname) {
        Spacciamadre entity = findByKeyUnica(nickname);

        if (entity == null) {
            entity = crea((Regione) null, (Provincia) null, (Comune) null, nickname, VUOTA, VUOTA, VUOTA);
        }// end of if cycle

        return entity;
    }// end of method


    /**
     * Crea una entity e la registra <br>
     *
     * @param regione   di riferimento (obbligatorio)
     * @param provincia di riferimento (obbligatorio)
     * @param comune    di riferimento (obbligatorio)
     * @param nickname  di presentazione (obbligatorio)
     * @param cellulare di riferimento (facoltativo)
     * @param email     di riferimento (facoltativo)
     * @param contatto  di riferimento (facoltativo)
     *
     * @return la entity appena creata
     */
    public Spacciamadre crea(Regione regione, Provincia provincia, Comune comune, String nickname, String cellulare, String email, String contatto) {
        return (Spacciamadre) save(newEntity(regione, provincia, comune, nickname, cellulare, email, contatto));
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata
     * Eventuali regolazioni iniziali delle property
     * Senza properties per compatibilità con la superclasse
     *
     * @return la nuova entity appena creata (non salvata)
     */
    @Override
    public Spacciamadre newEntity() {
        return newEntity((Regione) null, (Provincia) null, (Comune) null, VUOTA, VUOTA, VUOTA, VUOTA);
    }// end of method


    /**
     * Creazione in memoria di una nuova entity che NON viene salvata <br>
     * Eventuali regolazioni iniziali delle property <br>
     * All properties <br>
     * Utilizza, eventualmente, la newEntity() della superclasse, per le property della superclasse <br>
     *
     * @param regione   di riferimento (obbligatorio)
     * @param provincia di riferimento (obbligatorio)
     * @param comune    di riferimento (obbligatorio)
     * @param nickname  di presentazione (obbligatorio)
     * @param cellulare di riferimento (facoltativo)
     * @param email     di riferimento (facoltativo)
     * @param contatto  di riferimento (facoltativo)
     *
     * @return la nuova entity appena creata (non salvata)
     */
    public Spacciamadre newEntity(Regione regione, Provincia provincia, Comune comune, String nickname, String cellulare, String email, String contatto) {
        Spacciamadre entity = null;

        entity = findByKeyUnica(nickname);
        if (entity != null) {
            return findByKeyUnica(nickname);
        }// end of if cycle

        entity = Spacciamadre.builderSpacciamadre()
                .regione(regione != null ? regione : (Regione) null)
                .provincia(provincia != null ? provincia : (Provincia) null)
                .comune(comune != null ? comune : (Comune) null)
                .nickname(text.isValid(nickname) ? nickname : VUOTA)
                .solida(false)
                .liquida(false)
                .cellulare(text.isValid(cellulare) ? cellulare : VUOTA)
                .email(text.isValid(email) ? email : VUOTA)
                .contatto(text.isValid(contatto) ? contatto : VUOTA)
                .build();

        return (Spacciamadre) creaIdKeySpecifica(entity);
    }// end of method


    /**
     * Recupera una istanza della Entity usando la query della property specifica (obbligatoria ed unica) <br>
     *
     * @param nickname di presentazione (obbligatorio)
     *
     * @return istanza della Entity, null se non trovata
     */
    public Spacciamadre findByKeyUnica(String nickname) {
        return repository.findByNickname(nickname);
    }// end of method


    /**
     * Returns all entities of the type <br>
     * <p>
     * Se esiste la property 'ordine', ordinate secondo questa property <br>
     * Altrimenti, se esiste la property 'code', ordinate secondo questa property <br>
     * Altrimenti, se esiste la property 'descrizione', ordinate secondo questa property <br>
     * Altrimenti, ordinate secondo il metodo sovrascritto nella sottoclasse concreta <br>
     * Altrimenti, ordinate in ordine di inserimento nel DB mongo <br>
     *
     * @return all ordered entities
     */
    @Override
    public List<Spacciamadre> findAll() {
        return repository.findAllByOrderByComuneAsc();
    }// end of method


    /**
     * Returns all entities of the type <br>
     *
     * @return all ordered entities
     */
    public List<Spacciamadre> findAllByRegione(Regione regione) {
        return (List) repository.findAllByRegioneOrderByComuneAsc(regione);
    }// end of method


    /**
     * Returns all entities of the type <br>
     *
     * @return all ordered entities
     */
    public List<Spacciamadre> findAllByProvincia(Provincia provincia) {
        return (List) repository.findAllByProvinciaOrderByComuneAsc(provincia);
    }// end of method


    /**
     * Costruisce una lista di nomi delle properties del Form, specializzata per una specifica operazione <br>
     * Sovrascritto nella sottoclasse concreta <br>
     *
     * @param context legato alla sessione
     *
     * @return lista di nomi di properties
     */
    public List<String> getFormPropertyNamesListNew(AContext context) {
        String[] matrix = {"localita", "nickname", "solida", "liquida", "cellulare", "email", "contatto"};
        return array.fromStr(matrix);
    }// end of method


    /**
     * Costruisce una lista di nomi delle properties del Form, specializzata per una specifica operazione <br>
     * Sovrascritto nella sottoclasse concreta <br>
     *
     * @param context legato alla sessione
     *
     * @return lista di nomi di properties
     */
    public List<String> getFormPropertyNamesListEdit(AContext context) {
        String[] matrix = {"localita", "nickname", "solida", "liquida", "cellulare", "email", "contatto"};
        return array.fromStr(matrix);
    }// end of method


    /**
     * Costruisce una lista di nomi delle properties del Form, specializzata per una specifica operazione <br>
     * Sovrascritto nella sottoclasse concreta <br>
     *
     * @param context legato alla sessione
     *
     * @return lista di nomi di properties
     */
    public List<String> getFormPropertyNamesListShow(AContext context) {
        String[] matrix = {"localita", "nickname", "solida", "liquida", "contatto"};
        return array.fromStr(matrix);
    }// end of method

}// end of class