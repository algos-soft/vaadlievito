package it.algos.vaadlievito.modules.spacciamadre;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.modules.comune.ComuneService;
import it.algos.vaadflow.modules.provincia.ProvinciaService;
import it.algos.vaadflow.service.IAService;
import it.algos.vaadflow.ui.dialog.AViewDialog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import static it.algos.vaadflow.application.FlowCost.VUOTA;
import static it.algos.vaadlievito.application.VaadlievitoCost.TAG_SPA;

/**
 * Project vaadlievito
 * Created by Algos
 * User: gac
 * Date: gio, 09-apr-2020
 * Time: 18:32
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Qualifier(TAG_SPA)
@Slf4j
@AIScript(sovrascrivibile = false)
public class SpacciatoreDialog extends AViewDialog<Spacciamadre> {


    /**
     * Istanza unica di una classe (@Scope = 'singleton') di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo l'init() di questa classe o dopo un metodo @PostConstruct invocato da Spring <br>
     */
    @Autowired
    protected ProvinciaService provinciaService;


    /**
     * Istanza unica di una classe (@Scope = 'singleton') di servizio: <br>
     * Iniettata automaticamente dal Framework @Autowired (SpringBoot/Vaadin) <br>
     * Disponibile dopo l'init() di questa classe o dopo un metodo @PostConstruct invocato da Spring <br>
     */
    @Autowired
    protected ComuneService comuneService;


    //--casting del Service per usarlo localmente
    protected SpacciamadreService service;


    /**
     * Costruttore senza parametri <br>
     * Non usato. Serve solo per 'coprire' un piccolo bug di Idea <br>
     * Se manca, manda in rosso i parametri del costruttore usato <br>
     */
    public SpacciatoreDialog() {
    }// end of constructor


    /**
     * Costruttore con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * L'istanza DEVE essere creata con appContext.getBean(SpacciatoreDialog.class, service, entityClazz); <br>
     *
     * @param service     business class e layer di collegamento per la Repository
     * @param binderClass di tipo AEntity usata dal Binder dei Fields
     */
    public SpacciatoreDialog(IAService service, Class<? extends AEntity> binderClass) {
        super(service, binderClass);
        this.service = (SpacciamadreService) service;
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

        super.usaDeleteButton = false;
        super.usaSaveButton = false;
    }// end of method


    /**
     * Body placeholder per i campi, creati dopo open()
     * Normalmente colonna doppia <br>
     */
    protected Div creaFormLayout() {
        Div div;
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("5em", 1));

        formLayout.addClassName("no-padding");
        div = new Div(formLayout);
        div.addClassName("has-padding");

        return div;
    }// end of method

}// end of class
