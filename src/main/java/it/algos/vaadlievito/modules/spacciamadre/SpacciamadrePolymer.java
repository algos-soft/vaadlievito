package it.algos.vaadlievito.modules.spacciamadre;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.dependency.*;
import com.vaadin.flow.component.page.*;
import com.vaadin.flow.component.polymertemplate.*;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.annotation.*;
import static it.algos.vaadlievito.application.VaadlievitoCost.*;
import org.springframework.beans.factory.config.*;
import org.springframework.context.annotation.Scope;

/**
 * Project vaadlievito
 * Created by Algos
 * User: gac
 * Date: dom, 26-apr-2020
 * Time: 17:40
 */
@Route(value = TAG_SPA_POLYMER)
@Tag("show-view")
@HtmlImport("src/views/spacciamadre/show-view.html")
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Viewport("width=device-width")
public class SpacciamadrePolymer extends PolymerTemplate<SpacciamadreModel> implements HasUrlParameter<String> {

    @Override
    public void setParameter(BeforeEvent beforeEvent, String s) {

    }

}// end of class
