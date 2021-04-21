package it.algos.vaadlievito.application;

import it.algos.vaadflow.annotation.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;

/**
 * Project vaadlievito
 * Created by Algos
 * User: gac
 * Date: ven, 8-mag-2018
 * <p>
 * Completa la classe BaseCost con le costanti statiche specifiche di questa applicazione <br>
 * <p>
 * Not annotated with @SpringComponent (inutile) <br>
 * Not annotated with @Scope (inutile) <br>
 * Annotated with @AIScript (facoltativo) per controllare la ri-creazione di questo file nello script di algos <br>
 */
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@AIScript(sovrascrivibile = false)
public class VaadlievitoCost {

    public final static String TAG_SPA = "spacciamadre";

    public final static String TAG_SPA_LIST = "spacciamadreList";

    public final static String TAG_SPA_FORM = "spacciamadreForm";

    public final static String TAG_SPA_POLYMER = "spacciamadrePolymer";

}// end of static class