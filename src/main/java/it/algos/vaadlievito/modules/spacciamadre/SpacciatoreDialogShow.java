package it.algos.vaadlievito.modules.spacciamadre;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.spring.annotation.SpringComponent;
import it.algos.vaadflow.annotation.AIScript;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EAOperation;
import it.algos.vaadflow.modules.comune.Comune;
import it.algos.vaadflow.modules.provincia.Provincia;
import it.algos.vaadflow.service.IAService;
import it.algos.vaadflow.ui.fields.AComboBox;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.List;

import static it.algos.vaadlievito.application.VaadlievitoCost.TAG_SPA;

/**
 * Project vaadlievito
 * Created by Algos
 * User: gac
 * Date: gio, 09-apr-2020
 * Time: 09:57
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Qualifier(TAG_SPA)
@Slf4j
@AIScript(sovrascrivibile = false)
public class SpacciatoreDialogShow extends SpacciatoreDialog {


    /**
     * Costruttore senza parametri <br>
     * Non usato. Serve solo per 'coprire' un piccolo bug di Idea <br>
     * Se manca, manda in rosso i parametri del costruttore usato <br>
     */
    public SpacciatoreDialogShow() {
    }// end of constructor


    /**
     * Costruttore con parametri <br>
     * Not annotated with @Autowired annotation, per creare l'istanza SOLO come SCOPE_PROTOTYPE <br>
     * L'istanza DEVE essere creata con appContext.getBean(SpacciatoreDialog.class, service, entityClazz); <br>
     *
     * @param service     business class e layer di collegamento per la Repository
     * @param binderClass di tipo AEntity usata dal Binder dei Fields
     */
    public SpacciatoreDialogShow(IAService service, Class<? extends AEntity> binderClass) {
        super(service, binderClass);
    }// end of constructor


    /**
     * Barra dei bottoni
     */
    protected Component creaBottomLayout() {
        Component comp = super.creaBottomLayout();

        cancelButton.getElement().setAttribute("theme", "primary");

        Button editButton = new Button("Edit");
        editButton.getElement().setAttribute("theme", "error");
        editButton.addClickListener(e -> openDialogEdit(currentItem));
        editButton.setIcon(new Icon(VaadinIcon.EDIT));
        bottomLayout.add(editButton);

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
        return service != null ? service.getFormPropertyNamesListShow(context) : null;
    }// end of method


    /**
     * Eventuali fields specifici aggiunti PRIMA di quelli automatici <br>
     * Sovrascritto nella sottoclasse <br>
     */
    @Override
    protected void creaFieldsBefore() {
        creaProvincia();
        creaComune();
    }// end of method


    protected AbstractField creaProvincia() {
        String propertyName = "provincia";
        AComboBox propertyField = new AComboBox(text.primaMaiuscola(propertyName));
        Provincia provincia = currentItem.provincia;
        List<Provincia> items = new ArrayList<>();
        items.add(provincia);
        propertyField.setItems(items);
        propertyField.setValue(provincia);
        propertyField.setClearButtonVisible(false);
        fieldMap.put(propertyName, propertyField);

        return propertyField;
    }// end of method


    protected AbstractField creaComune() {
        String propertyName = "comune";
        AComboBox propertyField = new AComboBox(text.primaMaiuscola(propertyName));
        Comune comune = currentItem.comune;
        List<Comune> items = new ArrayList<>();
        items.add(comune);
        propertyField.setItems(items);
        propertyField.setValue(comune);
        propertyField.setClearButtonVisible(false);
        fieldMap.put(propertyName, propertyField);

        return propertyField;
    }// end of method


    /**
     * Creazione ed apertura del dialogo per una nuova entity oppure per una esistente <br>
     * Il dialogo è PROTOTYPE e viene creato esclusivamente da appContext.getBean(... <br>
     * Nella creazione vengono regolati il service e la entityClazz di riferimento <br>
     * Contestualmente alla creazione, il dialogo viene aperto con l'item corrente (ricevuto come parametro) <br>
     * Se entityBean è null, nella superclasse AViewDialog viene modificato il flag a EAOperation.addNew <br>
     * Si passano al dialogo anche i metodi locali (di questa classe AViewList) <br>
     * come ritorno dalle azioni save e delete al click dei rispettivi bottoni <br>
     * Il metodo DEVE essere sovrascritto <br>
     *
     * @param entityBean item corrente, null se nuova entity
     */
    protected void openDialogEdit(AEntity entityBean) {
        appContext.getBean(SpacciatoreDialogEdit.class, service, binderClass).open(entityBean, EAOperation.edit, null, null);
        super.close();
    }// end of method


}// end of class