package it.algos.vaadlievito.modules.spacciamadre;

import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.modules.provincia.Provincia;
import it.algos.vaadflow.modules.regione.Regione;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

import static it.algos.vaadlievito.application.VaadlievitoCost.TAG_SPA;

/**
 * Project vaadlievito <br>
 * Created by Algos <br>
 * User: Gac <br>
 * Fix date: 7-apr-2020 14.05.47 <br>
 * <br>
 * Estende la l'interaccia MongoRepository col casting alla Entity relativa di questa repository <br>
 * <br>
 * Annotated with @SpringComponent (obbligatorio) <br>
 * Annotated with @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) (obbligatorio) <br>
 * Annotated with @Qualifier (obbligatorio) per permettere a Spring di istanziare la classe specifica <br>
 * Annotated with @AIScript (facoltativo Algos) per controllare la ri-creazione di questo file dal Wizard <br>
 * - la documentazione precedente a questo tag viene SEMPRE riscritta <br>
 * - se occorre preservare delle @Annotation con valori specifici, spostarle DOPO @AIScript <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Qualifier(TAG_SPA)
@AIScript(sovrascrivibile = false)
public interface SpacciamadreRepository extends MongoRepository<Spacciamadre, String> {

    public Spacciamadre findByNickname(String nickname);

    public List<Spacciamadre> findAllByOrderByNicknameAsc();

    public List<Spacciamadre> findAllByOrderByComuneAsc();

    public List<Spacciamadre> findAllByRegioneOrderByNicknameAsc(Regione regione);

    public List<Spacciamadre> findAllByRegioneOrderByComuneAsc(Regione regione);

    public List<Spacciamadre> findAllByProvinciaOrderByNicknameAsc(Provincia provincia);

    public List<Spacciamadre> findAllByProvinciaOrderByComuneAsc(Provincia provincia);

}// end of interface