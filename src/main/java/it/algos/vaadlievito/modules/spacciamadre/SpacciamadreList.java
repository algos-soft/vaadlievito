package it.algos.vaadlievito.modules.spacciamadre;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.*;
import com.vaadin.flow.component.grid.*;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.*;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.annotation.*;
import it.algos.vaadflow.annotation.*;
import static it.algos.vaadflow.application.FlowCost.*;
import it.algos.vaadflow.backend.entity.*;
import it.algos.vaadflow.enumeration.*;
import it.algos.vaadflow.modules.provincia.*;
import it.algos.vaadflow.modules.regione.*;
import it.algos.vaadflow.modules.role.*;
import it.algos.vaadflow.service.*;
import it.algos.vaadflow.ui.*;
import it.algos.vaadflow.ui.fields.*;
import it.algos.vaadflow.ui.list.*;
import static it.algos.vaadlievito.application.VaadlievitoCost.*;
import lombok.extern.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.access.annotation.*;
import org.vaadin.klaudeta.*;

import java.util.*;

/**
 * Project vaadlievito <br>
 * Created by Algos <br>
 * User: Gac <br>
 * Fix date: 7-apr-2020 14.05.47 <br>
 * <p>
 * Estende la classe astratta AViewList per visualizzare la Grid <br>
 * Questa classe viene costruita partendo da @Route e NON dalla catena @Autowired di SpringBoot <br>
 * <p>
 * La classe viene divisa verticalmente in alcune classi astratte, per 'leggerla' meglio (era troppo grossa) <br>
 * Nell'ordine (dall'alto):
 * - 1 APropertyViewList (che estende la classe Vaadin VerticalLayout) per elencare tutte le property usate <br>
 * - 2 AViewList con la business logic principale <br>
 * - 3 APrefViewList per regolare le preferenze ed i flags <br>
 * - 4 ALayoutViewList per regolare il layout <br>
 * - 5 AGridViewList per gestire la Grid <br>
 * - 6 APaginatedGridViewList (opzionale) per gestire una Grid specializzata (add-on) che usa le Pagine <br>
 * L'utilizzo pratico per il programmatore è come se fosse una classe sola <br>
 * <p>
 * La sottoclasse concreta viene costruita partendo da @Route e NON dalla catena @Autowired di SpringBoot <br>
 * Le property di questa classe/sottoclasse vengono iniettate (@Autowired) automaticamente se: <br>
 * 1) vengono dichiarate nel costruttore @Autowired della sottoclasse concreta, oppure <br>
 * 2) la property è di una classe con @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) e viene richiamate
 * con AxxService.getInstance() <br>
 * 3) sono annotate @Autowired; sono disponibili SOLO DOPO @PostConstruct <br>
 * <p>
 * La sequenza di costruzione da parte di SpringBott/Vaadin/Route è:
 * 1) Costruttore esterno visibile (Vaadin/@Route constructor) (sempre presente anche implicitamente)
 * 2) init() interno del costruttore Java (sempre presente)
 * 3) @PostConstruct -> postConstruct() (facoltativo, sempre possibile)
 * 4) setParameter() (solo per questa view creata da @Route via browser)
 * 5) beforeEnter() (solo per questa view creata da @Route via browser)
 * Considerato che le sottoclassi concrete NON sono singleton e vengo ri-create ogni volta che dal menu (via @Router)
 * si invocano, è inutile (anche se possibile) usare un metodo @PostConstruct che è sempre un'appendici di init() del
 * costruttore.
 * Meglio spostare tutta la logica iniziale nel metodo beforeEnter() che è l'ultimo della catena di creazione <br>
 * <p>
 * Graficamente abbiamo in tutte (di solito) le XxxViewList:
 * 1) una barra di menu (obbligatorio) di tipo IAMenu
 * 2) un topPlaceholder (eventuale, presente di default) di tipo HorizontalLayout
 * - con o senza campo edit search, regolato da preferenza o da parametro
 * - con o senza bottone New, regolato da preferenza o da parametro
 * - con eventuali bottoni specifici, aggiuntivi o sostitutivi
 * 3) un alertPlaceholder di avviso (eventuale) con label o altro per informazioni; di norma per il developer
 * 4) un headerGridHolder della Grid (obbligatoria) con informazioni sugli elementi della lista
 * 5) una Grid (obbligatoria); alcune regolazioni da preferenza o da parametro (bottone Edit, ad esempio)
 * 6) un bottomPlacehorder della Grid (eventuale) con informazioni sugli elementi della lista; di norma delle somme
 * 7) un bottomPlacehorder (eventuale) con bottoni aggiuntivi
 * 8) un footer (obbligatorio) con informazioni generali
 * <p>
 * Le preferenze vengono (eventualmente) lette da mongo e (eventualmente) sovrascritte nella sottoclasse
 * <p>
 * Annotation @Route(value = "") per la vista iniziale - Ce ne può essere solo una per applicazione
 * ATTENZIONE: se rimangono due (o più) classi con @Route(value = ""), in fase di compilazione appare l'errore:
 * -'org.springframework.context.ApplicationContextException:
 * -Unable to start web server;
 * -nested exception is org.springframework.boot.web.server.WebServerException:
 * -Unable to start embedded Tomcat'
 * <p>
 * Usa l'interfaccia HasUrlParameter col metodo setParameter(BeforeEvent event, ...) per ricevere parametri opzionali
 * anche per chiamate che usano @Route <br>
 * Usa l'interfaccia BeforeEnterObserver col metodo beforeEnter()
 * invocato da @Route al termine dell'init() di questa classe e DOPO il metodo @PostConstruct <br>
 * <p>
 * Not annotated with @SpringView (sbagliato) perché usa la @Route di VaadinFlow <br>
 * Not annotated with @SpringComponent (sbagliato) perché usa la @Route di VaadinFlow <br>
 * Annotated with @UIScope (obbligatorio) <br>
 * Annotated with @Route (obbligatorio) per la selezione della vista. @Route(value = "") per la vista iniziale <br>
 * Annotated with @Qualifier (obbligatorio) per permettere a Spring di istanziare la sottoclasse specifica <br>
 * Annotated with @Slf4j (facoltativo) per i logs automatici <br>
 * Annotated with @Secured (facoltativo) per l'accesso con security a seconda del ruolo dell'utente loggato <br>
 * - 'developer' o 'admin' o 'user' <br>
 * Annotated with @AIScript (facoltativo Algos) per controllare la ri-creazione di questo file dal Wizard <br>
 * - la documentazione precedente a questo tag viene SEMPRE riscritta <br>
 * - se occorre preservare delle @Annotation con valori specifici, spostarle DOPO @AIScript <br>
 * Annotated with @AIView (facoltativo Algos) per il menu-name, l'icona-menu, la property-search e la visibilità <br>
 * Se serve una Grid paginata estende APaginatedGridViewList altrimenti AGridViewList <br>
 * Se si usa APaginatedGridViewList è obbligatorio creare la PaginatedGrid
 * 'tipizzata' con la entityClazz (Collection) specifica nel metodo creaGridPaginata() <br>
 */
@UIScope
@Route(value = VUOTA, layout = MainLayout14.class)
@Qualifier(TAG_SPA)
@Slf4j
@Secured("user")
@AIScript(sovrascrivibile = false)
@AIView(vaadflow = false, routeFormName = TAG_SPA_FORM, menuName = TAG_SPA, menuIcon = VaadinIcon.ASTERISK, searchProperty = "code", startListEmpty = true, roleTypeVisibility = EARoleType.developer)
public class SpacciamadreList extends AGridViewList {

    @Autowired
    RegioneService regioneService;

    @Autowired
    ProvinciaService provinciaService;

    AComboBox provinciaComboBox = new AComboBox();

    private boolean flagBottoniFiltro = true;


    /**
     * Costruttore @Autowired <br>
     * Questa classe viene costruita partendo da @Route e NON dalla catena @Autowired di SpringBoot <br>
     * Nella sottoclasse concreta si usa un @Qualifier(), per avere la sottoclasse specifica <br>
     * Nella sottoclasse concreta si usa una costante statica, per scrivere sempre uguali i riferimenti <br>
     * Passa alla superclasse il service iniettato qui da Vaadin/@Route <br>
     * Passa alla superclasse anche la entityClazz che viene definita qui (specifica di questo modulo) <br>
     *
     * @param service business class e layer di collegamento per la Repository
     */
    @Autowired
    public SpacciamadreList(@Qualifier(TAG_SPA) IAService service) {
        super(service, Spacciamadre.class);
    }// end of Vaadin/@Route constructor


    /**
     * Crea effettivamente il Component Grid <br>
     * <p>
     * Può essere Grid oppure PaginatedGrid <br>
     * DEVE essere sovrascritto nella sottoclasse con la PaginatedGrid specifica della Collection <br>
     * DEVE poi invocare il metodo della superclasse per le regolazioni base della PaginatedGrid <br>
     * Oppure queste possono essere fatte nella sottoclasse, se non sono standard <br>
     */
    @Override
    protected Grid creaGridComponent() {
        return new PaginatedGrid<Spacciamadre>();
    }// end of method


    /*
     * Preferenze specifiche di questa view <br>
     * <p>
     * Chiamato da AViewList.initView() e sviluppato nella sottoclasse APrefViewList <br>
     * Può essere sovrascritto, per modificare le preferenze standard <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void fixPreferenze() {
        super.fixPreferenze();

        if (flagBottoniFiltro) {
            super.searchType = EASearch.nonUsata;
            super.usaSecondTopPlaceholder = false;
        } else {
            super.searchType = EASearch.editField;
            super.usaSecondTopPlaceholder = true;
        }// end of if/else cycle

        super.usaButtonNew = false;
        super.usaHeaderGridHolder = false;
        super.usaBottoneEdit = false;
        super.usaBottomLayout = true;

        super.usaRouteFormView = true;
    }// end of method


    /**
     * Barra dei bottoni SOPRA la Grid inseriti in 'topPlaceholder' <br>
     * <p>
     * In fixPreferenze() si regola quali bottoni mostrare. Nell'ordine: <br>
     * 1) eventuale bottone per cancellare tutta la collezione <br>
     * 2) eventuale bottone di reset per ripristinare (se previsto in automatico) la collezione <br>
     * 3) eventuale bottone New, con testo regolato da preferenza o da parametro <br>
     * 4) eventuale bottone 'Cerca...' per aprire un DialogSearch oppure un campo EditSearch per la ricerca <br>
     * 5) eventuale bottone per annullare la ricerca e riselezionare tutta la collezione <br>
     * 6) eventuale combobox di selezione della company (se applicazione multiCompany) <br>
     * 7) eventuale combobox di selezione specifico <br>
     * 8) eventuali altri bottoni specifici <br>
     * <p>
     * I bottoni vengono creati SENZA listeners che vengono regolati nel metodo addListeners() <br>
     * Chiamato da AViewList.initView() e sviluppato nella sottoclasse ALayoutViewList <br>
     * Può essere sovrascritto, per aggiungere informazioni <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    @Override
    protected void creaTopLayout() {
        String lar = "15em";
        Label label = new Label("Cerca per...");
        super.creaTopLayout();

        if (buttonNew != null) {
            buttonNew.setText(VUOTA);
        }// end of if cycle

        if (searchField != null) {
            searchField.setWidth("11em");
        }// end of if cycle

        //--filtro provincia
        List items = provinciaService.findAll();
        provinciaComboBox.setItems(items);
        provinciaComboBox.setPlaceholder("Provincia");
        provinciaComboBox.setWidth(lar);
        provinciaComboBox.setClearButtonVisible(true);
        provinciaComboBox.addValueChangeListener(event -> sincroProvince(event));

        topPlaceholder.add(label);
        if (flagBottoniFiltro) {
            if (buttonNew != null) {
                buttonNew.getElement().setAttribute("style", "width: 2em");
            }// end of if cycle
            topPlaceholder.add(provinciaComboBox);
        } else {
            secondTopPlaceholder.add(provinciaComboBox);
        }// end of if/else cycle

    }// end of method


    protected void sincroProvince(HasValue.ValueChangeEvent eventCombo) {
        List items;
        Provincia provincia = (Provincia) eventCombo.getValue();

        if (provincia != null) {
            items = ((SpacciamadreService) service).findAllByProvincia(provincia);
        } else {
            items = ((SpacciamadreService) service).findAll();
        }// end of if/else cycle

        grid.setItems(items);
    }// end of method


//    /**
//     * Crea un (eventuale) Popup di selezione, filtro e ordinamento <br>
//     * DEVE essere sovrascritto, per regolare il contenuto (items) <br>
//     * Invocare PRIMA il metodo della superclasse <br>
//     */
//    protected void creaPopupFiltro() {
//        String lar = "15em";
//        super.creaPopupFiltro();
//
//        filtroComboBox.setPlaceholder("Provincia ...");
//        List<Provincia> items = provinciaService.findAll();
//        filtroComboBox.setItems(items);
//        filtroComboBox.setWidth(lar);
//        filtroComboBox.setClearButtonVisible(true);
//        filtroComboBox.addValueChangeListener(event -> sincroProvince(event));
//    }// end of method


    /**
     * Aggiunge in automatico le colonne previste in gridPropertyNamesList <br>
     * Se si usa una PaginatedGrid, il metodo DEVE essere sovrascritto nella classe APaginatedGridViewList <br>
     *
     * @param gridPropertyNamesList
     */
    @Override
    protected void addColumnsGrid(List<String> gridPropertyNamesList) {
        columnService.create(grid, entityClazz, "comune", searchProperty);

        grid.addComponentColumn((item -> {
            Button nomeButton = new Button(((Spacciamadre) item).nickname);
            if (usaRouteFormView) {
                nomeButton.addClickListener(event -> openForm((Spacciamadre) item));
            } else {
            }// end of if/else cycle
            return nomeButton;
        }));//end of lambda expressions

    }// end of method


    /**
     * Costruisce un (eventuale) layout con bottoni aggiuntivi <br>
     * Facoltativo (assente di default) <br>
     * Può essere sovrascritto, per aggiungere informazioni <br>
     * Invocare PRIMA il metodo della superclasse <br>
     */
    protected void creaGridBottomLayout() {
        super.creaGridBottomLayout();

        Label label = new Label("Nuovo spacciamadre:");
        Button buttonNew = new Button("New", new Icon("lumo", "plus"));
        buttonNew.getElement().setAttribute("title", "Crea una nuova entity");
        buttonNew.addClassName("view-toolbar__button");
        buttonNew.addClickListener(event -> openFormNew());

        bottomPlacehorder.add(label);
        bottomPlacehorder.add(buttonNew);
    }// end of method


    /**
     * Creazione ed apertura di una view di tipo Form per una nuova entity oppure per una esistente <br>
     * Il metodo DEVE essere sovrascritto e chiamare super.openForm(AEntity entityBean, String formRouteName) <br>
     */
    protected void openFormNew() {
        String formRouteName = annotation.getFormRouteName(SpacciamadreList.class);
        super.openFormNew(formRouteName);
    }// end of method


    /**
     * Creazione ed apertura di una view di tipo Form per una nuova entity oppure per una esistente <br>
     * Il metodo DEVE essere sovrascritto e chiamare super.openForm(AEntity entityBean, String formRouteName) <br>
     *
     * @param entityBean item corrente, null se nuova entity
     */
    protected void openForm(AEntity entityBean) {
        String formRouteName = annotation.getFormRouteName(SpacciamadreList.class);
        super.openForm(entityBean, formRouteName);
    }// end of method


}// end of class