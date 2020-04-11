package it.algos.vaadlievito.modules.spacciatore;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.modules.comune.Comune;
import it.algos.vaadflow.modules.provincia.Provincia;
import it.algos.vaadflow.service.IAService;
import it.algos.vaadflow.ui.fields.AComboBox;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.util.List;

import static it.algos.vaadlievito.application.VaadlievitoCost.TAG_SPA;

/**
 * Project vaadlievito
 * Created by Algos
 * User: gac
 * Date: gio, 09-apr-2020
 * Time: 16:35
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Qualifier(TAG_SPA)
@Slf4j
@AIScript(sovrascrivibile = false)
public class SpacciatoreDialogNew extends SpacciatoreDialog {


    /**
     * Costruttore senza parametri <br>
     * Non usato. Serve solo per 'coprire' un piccolo bug di Idea <br>
     * Se manca, manda in rosso i parametri del costruttore usato <br>
     */
    public SpacciatoreDialogNew() {
    }// end of constructor


    /**
     * Costruttore con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * L'istanza DEVE essere creata con appContext.getBean(SpacciatoreDialog.class, service, entityClazz); <br>
     *
     * @param service     business class e layer di collegamento per la Repository
     * @param binderClass di tipo AEntity usata dal Binder dei Fields
     */
    public SpacciatoreDialogNew(IAService service, Class<? extends AEntity> binderClass) {
        super(service, binderClass);
    }// end of constructor


    /**
     * Preferenze standard e specifiche, eventualmente sovrascritte nella sottoclasse <br>
     * Può essere sovrascritto, per aggiungere e/o modificareinformazioni <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    protected void fixPreferenze() {
        super.fixPreferenze();

        super.cancelButton.setText("Annulla");
        super.usaSaveButton = true;
    }// end of method


    /**
     * Barra dei bottoni
     */
    protected Component creaBottomLayout() {
        Component comp = super.creaBottomLayout();
        saveButton.getElement().setAttribute("theme", "error");
        return comp;
    }// end of method


    /**
     * Costruisce nell'ordine una lista di nomi di properties <br>
     * La lista viene usata per la costruzione automatica dei campi e l'inserimento nel binder <br>
     * 1) Cerca nell'annotation @AIForm della Entity e usa quella lista (con o senza ID)
     * 2) Utilizza tutte le properties della Entity (properties della classe e superclasse)
     * 3) Sovrascrive la lista nella sottoclasse specifica di xxxService
     * Sovrasrivibile nella sottoclasse <br>
     * Se serve, modifica l'ordine della lista oppure esclude una property che non deve andare nel binder <br>
     */
    protected List<String> getPropertiesName() {
        return service != null ? service.getFormPropertyNamesListNew(context) : null;
    }// end of method


    /**
     * Eventuali regolazioni aggiuntive ai fields del binder DOPO aver associato i valori <br>
     * Sovrascritto nella sottoclasse
     */
    protected void addListeners() {
//        AComboBox fieldRegione = (AComboBox) getField("regione");
        AComboBox fieldProvincia = (AComboBox) getField("provincia");
//
//        fieldRegione.addValueChangeListener(e -> {
//            sincroProvince();
//        });// end of lambda expressions
        fieldProvincia.addValueChangeListener(e -> {
            sincroComuni();
        });// end of lambda expressions

//        sincroProvince();
    }// end of method


//    /**
//     * Chiamato da un listener di Regione <br>
//     */
//    protected void sincroProvince() {
//        AComboBox fieldRegione = (AComboBox) getField("regione");
//        AComboBox fieldProvincia = (AComboBox) getField("provincia");
//
//        Regione regione = (Regione) fieldRegione.getValue();
//        List<Provincia> province = provinciaService.findAllByRegione(regione);
//        if (fieldProvincia != null && province != null) {
//            fieldProvincia.setItems(province);
//        }// end of if cycle
//        fieldProvincia.setValue(currentItem.provincia);
//
//        sincroComuni();
//    }// end of method


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


}// end of class