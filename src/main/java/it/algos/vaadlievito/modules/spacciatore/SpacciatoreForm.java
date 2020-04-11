package it.algos.vaadlievito.modules.spacciatore;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import it.algos.vaadflow.enumeration.EAOperation;
import it.algos.vaadflow.modules.comune.Comune;
import it.algos.vaadflow.modules.comune.ComuneService;
import it.algos.vaadflow.modules.provincia.Provincia;
import it.algos.vaadflow.modules.provincia.ProvinciaService;
import it.algos.vaadflow.modules.regione.Regione;
import it.algos.vaadflow.modules.regione.RegioneService;
import it.algos.vaadflow.service.IAService;
import it.algos.vaadflow.ui.fields.AComboBox;
import it.algos.vaadflow.ui.form.AFieldsViewForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.util.HashMap;
import java.util.List;

import static it.algos.vaadflow.application.FlowCost.*;
import static it.algos.vaadlievito.application.VaadlievitoCost.TAG_SPA;
import static it.algos.vaadlievito.application.VaadlievitoCost.TAG_SPA_FORM;

/**
 * Project vaadlievito
 * Created by Algos
 * User: gac
 * Date: sab, 11-apr-2020
 * Time: 06:35
 */
@Route(value = TAG_SPA_FORM)
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SpacciatoreForm extends AFieldsViewForm {

    private final static String REGIONE_NAME = "regione";

    private final static String PROVINCIA_NAME = "provincia";

    private final static String COMUNE_NAME = "comune";

    /**
     * Istanza unica di una classe (@Scope = 'singleton') di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected RegioneService regioneService;

    /**
     * Istanza unica di una classe (@Scope = 'singleton') di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected ProvinciaService provinciaService;

    /**
     * Istanza unica di una classe (@Scope = 'singleton') di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo il metodo beforeEnter() invocato da @Route al termine dell'init() di questa classe <br>
     * Disponibile dopo un metodo @PostConstruct invocato da Spring al termine dell'init() di questa classe <br>
     */
    @Autowired
    protected ComuneService comuneService;

    //--casting del Service per usarlo localmente
    //--casting subito dopo aver invocato il costruttore della superclasse
    private SpacciatoreService service;

    //--casting di entityBean per usarlo localmente
    //--casting in fixPreferenze dopo aver recuperato la entityBean nel metodo APrefViewForm.fixParameters()
    private Spacciatore entityBean;


    /**
     * Costruttore @Autowired <br>
     * Questa classe viene costruita partendo da @Route e NON dalla catena @Autowired di SpringBoot <br>
     * Nella sottoclasse concreta si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Nella sottoclasse concreta si usa una costante statica, per scrivere sempre uguali i riferimenti <br>
     * Passa nella superclasse anche la entityClazz che viene definita qui (specifica di questo mopdulo) <br>
     *
     * @param service business class e layer di collegamento per la Repository
     */
    public SpacciatoreForm(@Qualifier(TAG_SPA) IAService service) {
        super(service, Spacciatore.class);
        this.service = (SpacciatoreService) service;
    }// end of Vaadin/@Route constructor


    /**
     * Preferenze standard <br>
     * Può essere sovrascritto, per aggiungere informazioni <br>
     * Invocare PRIMA il metodo della superclasse <br>
     * Le preferenze vengono (eventualmente) lette da mongo e (eventualmente) sovrascritte nella sottoclasse <br>
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();

        //--casting di entityBean per usarlo localmente
        entityBean = (Spacciatore) super.entityBean;

        super.usaFormDueColonne = false;
        super.minWidthForm = "5em";
    }// end of method


    /**
     * Aggiunge alla mappa (ordinata) eventuali fields specifici PRIMA di quelli automatici <br>
     * Sovrascritto nella sottoclasse <br>
     */
    @Override
    protected void creaFieldsBefore() {
        switch (operationForm) {
            case addNew:
                creaRegioneNew();
                creaProvinciaNew();
                creaComuneNew();
                break;
            case edit:
            case editNoDelete:
            case editDaLink:
                creaRegioneEdit();
                creaProvinciaEdit();
                creaComuneEdit();
                break;
            case showOnly:
            default:
                logger.warn("Switch - caso non definito");
                break;
        } // end of switch statement
    }// end of method


    protected AbstractField creaRegioneNew() {
        String propertyName = REGIONE_NAME;
        AComboBox fieldRegione = new AComboBox(text.primaMaiuscola(propertyName));
        List<Regione> items = regioneService.findAll();
        fieldRegione.setItems(items);
        fieldRegione.setClearButtonVisible(false);
        fieldRegione.addValueChangeListener(e -> {
            sincroProvince();
        });// end of lambda expressions
        fieldMap.put(propertyName, fieldRegione);

        return fieldRegione;
    }// end of method


    protected AbstractField creaProvinciaNew() {
        String propertyName = PROVINCIA_NAME;
        AComboBox fieldProvincia = new AComboBox(text.primaMaiuscola(propertyName));
        fieldProvincia.addValueChangeListener(e -> {
            sincroComuni();
        });// end of lambda expressions
        fieldProvincia.setClearButtonVisible(false);
        fieldMap.put(propertyName, fieldProvincia);

        return fieldProvincia;
    }// end of method


    protected AbstractField creaComuneNew() {
        String propertyName = COMUNE_NAME;
        AComboBox propertyField = new AComboBox(text.primaMaiuscola(propertyName));
        propertyField.setClearButtonVisible(false);
        fieldMap.put(propertyName, propertyField);

        return propertyField;
    }// end of method


    protected AbstractField creaRegioneEdit() {
        String propertyName = REGIONE_NAME;
        AComboBox fieldRegione = new AComboBox(text.primaMaiuscola(propertyName));
        List<Regione> items = regioneService.findAll();
        fieldRegione.setItems(items);
        fieldRegione.setValue(entityBean.regione);
        fieldRegione.setClearButtonVisible(false);
        fieldRegione.addValueChangeListener(e -> {
            sincroProvince();
        });// end of lambda expressions
        fieldMap.put(propertyName, fieldRegione);

        return fieldRegione;
    }// end of method


    protected AbstractField creaProvinciaEdit() {
        String propertyName = PROVINCIA_NAME;
        AComboBox fieldProvincia = new AComboBox(text.primaMaiuscola(propertyName));
        List<Provincia> items = provinciaService.findAllByRegione(entityBean.regione);
        fieldProvincia.setItems(items);
        fieldProvincia.setValue(entityBean.provincia);
        fieldProvincia.setClearButtonVisible(false);
        fieldProvincia.addValueChangeListener(e -> {
            sincroComuni();
        });// end of lambda expressions
        fieldMap.put(propertyName, fieldProvincia);

        return fieldProvincia;
    }// end of method


    protected AbstractField creaComuneEdit() {
        String propertyName = COMUNE_NAME;
        AComboBox fieldComune = new AComboBox(text.primaMaiuscola(propertyName));
        List<Comune> items = comuneService.findAllByProvincia(entityBean.provincia);
        fieldComune.setItems(items);
        fieldComune.setValue(entityBean.comune);
        fieldComune.setClearButtonVisible(false);
        fieldMap.put(propertyName, fieldComune);

        return fieldComune;
    }// end of method


    /**
     * Chiamato dal listener di Regione <br>
     */
    protected void sincroProvince() {
        AComboBox fieldRegione = (AComboBox) getField(REGIONE_NAME);
        AComboBox fieldProvincia = (AComboBox) getField(PROVINCIA_NAME);

        Regione regione = (Regione) fieldRegione.getValue();
        List<Provincia> province = provinciaService.findAllByRegione(regione);
        if (fieldProvincia != null && province != null) {
            fieldProvincia.setItems(province);
        }// end of if cycle
    }// end of method


    /**
     * Chiamato dal listener di Provincia <br>
     */
    protected void sincroComuni() {
        AComboBox fieldProvincia = (AComboBox) getField(PROVINCIA_NAME);
        AComboBox fieldComune = (AComboBox) getField(COMUNE_NAME);

        Provincia provincia = (Provincia) fieldProvincia.getValue();
        List<Comune> comuni = comuneService.findAllByProvincia(provincia);
        if (fieldComune != null && comuni != null) {
            fieldComune.setItems(comuni);
        }// end of if cycle
    }// end of method


    /**
     * Regola in scrittura eventuali valori NON associati al binder. Dalla UI al DB <br>
     * Sovrascritto nella sottoclasse <br>
     */
    protected boolean writeSpecificFields() {
        boolean valido = true;

        AComboBox fieldRegione = (AComboBox) getField(REGIONE_NAME);
        AComboBox fieldProvincia = (AComboBox) getField(PROVINCIA_NAME);
        AComboBox fieldComune = (AComboBox) getField(COMUNE_NAME);

        if (fieldRegione != null) {
            entityBean.regione = (Regione) fieldRegione.getValue();
            if (entityBean.regione == null) {
                valido = false;
                Notification.show("La regione è indispensabile", 3000, Notification.Position.MIDDLE);
            }// end of if cycle
        }// end of if cycle

        if (fieldProvincia != null) {
            entityBean.provincia = (Provincia) fieldProvincia.getValue();
            if (entityBean.provincia == null) {
                valido = false;
                Notification.show("La provincia è indispensabile", 3000, Notification.Position.MIDDLE);
            }// end of if cycle
        }// end of if cycle

        if (fieldComune != null) {
            entityBean.comune = (Comune) fieldComune.getValue();
            if (entityBean.comune == null) {
                valido = false;
                Notification.show("Il comune è indispensabile", 3000, Notification.Position.MIDDLE);
            }// end of if cycle
        }// end of if cycle

        if (text.isEmpty(entityBean.cellulare) && text.isEmpty(entityBean.email)) {
            Notification.show("Devi inserire un cellulare oppure una eMail", 3000, Notification.Position.MIDDLE);
            valido = false;
        }// end of if cycle

        return valido;
    }// end of method


    /**
     * Costruisce eventuali fields specifici (costruiti non come standard type)
     * Aggiunge i fields specifici al binder (facoltativo, alcuni fields non funzionano col binder)
     * Se i fields non sono associati al binder, DEVONO comparire in readSpecificFields()
     * Aggiunge i fields specifici alla fieldMap (obbligatorio)
     * Sovrascritto nella sottoclasse <br>
     */
    protected void addSpecificAlgosFields() {
        VerticalLayout layout = new VerticalLayout();

        if (operationForm == EAOperation.showOnly) {
            bodyPlaceHolder.removeAll();
            if (entityBean.regione != null) {
                layout.add(new Label("Regione: " + entityBean.regione.nome));
            } else {
                if (entityBean.provincia != null) {
                    layout.add(new Label("Regione: " + entityBean.provincia.regione.nome));
                }// end of if cycle
            }// end of if/else cycle
            if (entityBean.provincia != null) {
                layout.add(new Label("Provincia: " + entityBean.provincia.nome));
            }// end of if cycle
            if (entityBean.comune != null) {
                layout.add(new Label("Comune: " + entityBean.comune.nome));
            }// end of if cycle

            String localita = entityBean.localita != null ? entityBean.localita : VUOTA;
            layout.add(new Label("Località: " + localita));

            String riferimento = entityBean.nickname != null ? entityBean.nickname : VUOTA;
            layout.add(new Label("Riferimento: " + riferimento));

            if (entityBean.solida) {
                HorizontalLayout oriz = new HorizontalLayout();
                oriz.add(new Label("Pasta madre solida"));
                oriz.add(new Checkbox(entityBean.solida));
                layout.add(oriz);
            } else {
                layout.add(new Label("Non disponibile pasta madre solida"));
            }// end of if/else cycle
            if (entityBean.liquida) {
                HorizontalLayout oriz = new HorizontalLayout();
                oriz.add(new Label("Pasta madre liquida"));
                oriz.add(new Checkbox(entityBean.liquida));
                layout.add(oriz);
            } else {
                layout.add(new Label("Non disponibile pasta madre liquida"));
            }// end of if/else cycle

            String cellulare = entityBean.cellulare != null ? entityBean.cellulare : VUOTA;
            layout.add(new Label("Cellulare: " + cellulare));

            String email = entityBean.email != null ? entityBean.email : VUOTA;
            layout.add(new Label("eMail: " + email));

            String contatto = entityBean.contatto != null ? entityBean.contatto : VUOTA;
            layout.add(new Label("Note: " + contatto));

            bodyPlaceHolder.add(layout);
        }// end of if cycle

        if (operationForm == EAOperation.addNew || operationForm == EAOperation.edit || operationForm == EAOperation.editNoDelete) {
            bodyPlaceHolder.add(text.getLabelDev("Premendo il bottone 'Save' si è consapevoli che tutti questi dati diventano pubblici"));
        }// end of if cycle

    }// end of method


    /**
     * Azione proveniente dal click sul bottone Edit
     * Apre una View in Edit mode <br>
     */
    protected void modifica() {
        HashMap mappa = new HashMap();
        mappa.put(KEY_MAPPA_FORM_TYPE, EAOperation.edit.name());
        mappa.put(KEY_MAPPA_ENTITY_BEAN, entityBean.id);
        final QueryParameters query = routeService.getQuery(mappa);
        getUI().ifPresent(ui -> ui.navigate(TAG_SPA_FORM, query));
    }// end of method

}// end of class
