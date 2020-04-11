package it.algos.vaadlievito.modules.spacciatore;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.modules.comune.Comune;
import it.algos.vaadflow.modules.comune.ComuneService;
import it.algos.vaadflow.modules.provincia.Provincia;
import it.algos.vaadflow.modules.provincia.ProvinciaService;
import it.algos.vaadflow.modules.regione.Regione;
import it.algos.vaadflow.service.IAService;
import it.algos.vaadflow.ui.dialog.AViewDialog;
import it.algos.vaadflow.ui.fields.AComboBox;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.util.List;

import static it.algos.vaadflow.application.FlowCost.VUOTA;
import static it.algos.vaadlievito.application.VaadlievitoCost.TAG_SPA;

/**
 * Project vaadlievito <br>
 * Created by Algos
 * User: Gac
 * Fix date: 7-apr-2020 14.05.47 <br>
 * <p>
 * Estende la classe astratta AViewDialog per visualizzare i fields <br>
 * Necessario per la tipizzazione del binder <br>
 * Costruita (nella List) con appContext.getBean(SpacciatoreDialog.class, service, entityClazz);
 * <p>
 * Not annotated with @SpringView (sbagliato) perché usa la @Route di VaadinFlow <br>
 * Annotated with @SpringComponent (obbligatorio) <br>
 * Annotated with @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE) (obbligatorio) <br>
 * Annotated with @Qualifier (obbligatorio) per permettere a Spring di istanziare la classe specifica <br>
 * Annotated with @Slf4j (facoltativo) per i logs automatici <br>
 * Annotated with @AIScript (facoltativo Algos) per controllare la ri-creazione di questo file dal Wizard <br>
 * - la documentazione precedente a questo tag viene SEMPRE riscritta <br>
 * - se occorre preservare delle @Annotation con valori specifici, spostarle DOPO @AIScript <br>
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Qualifier(TAG_SPA)
@Slf4j
@AIScript(sovrascrivibile = false)
public class SpacciatoreDialogOld extends AViewDialog<Spacciatore> {


    @Autowired
    ProvinciaService provinciaService;

    @Autowired
    ComuneService comuneService;


    /**
     * Costruttore senza parametri <br>
     * Non usato. Serve solo per 'coprire' un piccolo bug di Idea <br>
     * Se manca, manda in rosso i parametri del costruttore usato <br>
     */
    public SpacciatoreDialogOld() {
    }// end of constructor


    /**
     * Costruttore con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * L'istanza DEVE essere creata con appContext.getBean(SpacciatoreDialog.class, service, entityClazz); <br>
     *
     * @param service     business class e layer di collegamento per la Repository
     * @param binderClass di tipo AEntity usata dal Binder dei Fields
     */
    public SpacciatoreDialogOld(IAService service, Class<? extends AEntity> binderClass) {
        super(service, binderClass);
    }// end of constructor


    /**
     * Preferenze standard e specifiche, eventualmente sovrascritte nella sottoclasse <br>
     * Può essere sovrascritto, per aggiungere e/o modificareinformazioni <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    protected void fixPreferenze() {
        super.fixPreferenze();
        super.usaCancelButton = true;
        super.cancelButton.setText(VUOTA);
        super.usaFormDueColonne = false;
    }// end of method


    /**
     * Eventuali regolazioni aggiuntive ai fields del binder DOPO aver associato i valori <br>
     * Sovrascritto nella sottoclasse
     */
    protected void addListeners() {
        AComboBox fieldRegione = (AComboBox) getField("regione");
        AComboBox fieldProvincia = (AComboBox) getField("provincia");

        fieldRegione.addValueChangeListener(e -> {
            sincroProvince();
        });// end of lambda expressions
        fieldProvincia.addValueChangeListener(e -> {
            sincroComuni();
        });// end of lambda expressions

        sincroProvince();
    }// end of method


    /**
     * Chiamato da un listener di Regione <br>
     */
    protected void sincroProvince() {
        AComboBox fieldRegione = (AComboBox) getField("regione");
        AComboBox fieldProvincia = (AComboBox) getField("provincia");

        Regione regione = (Regione) fieldRegione.getValue();
        List<Provincia> province = provinciaService.findAllByRegione(regione);
        if (fieldProvincia != null && province != null) {
            fieldProvincia.setItems(province);
        }// end of if cycle
        fieldProvincia.setValue(currentItem.provincia);

        sincroComuni();
    }// end of method


    /**
     * Chiamato da un listener di Provincia <br>
     */
    protected void sincroComuni() {
        AComboBox fieldProvincia = (AComboBox) getField("provincia");
        AComboBox fieldComune = (AComboBox) getField("comune");

        Provincia provincia = (Provincia) fieldProvincia.getValue();
        List<Comune> comuni = comuneService.findAllByProvincia(provincia);
        if (fieldComune != null && comuni != null) {
            fieldComune.setItems(comuni);
        }// end of if cycle
        fieldComune.setValue(currentItem.comune);

    }// end of method


    /**
     * Barra dei bottoni
     */
    protected Component creaBottomLayout() {
        Component comp = super.creaBottomLayout();
        saveButton.getElement().setAttribute("theme", "secondary");
        return comp;
    }// end of method

}// end of class