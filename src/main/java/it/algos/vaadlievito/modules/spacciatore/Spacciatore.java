package it.algos.vaadlievito.modules.spacciatore;

import it.algos.vaadflow.annotation.*;
import it.algos.vaadflow.backend.entity.AEntity;
import it.algos.vaadflow.enumeration.EACompanyRequired;
import it.algos.vaadflow.enumeration.EAFieldType;
import it.algos.vaadflow.modules.comune.Comune;
import it.algos.vaadflow.modules.comune.ComuneService;
import it.algos.vaadflow.modules.provincia.Provincia;
import it.algos.vaadflow.modules.provincia.ProvinciaService;
import it.algos.vaadflow.modules.regione.Regione;
import it.algos.vaadflow.modules.regione.RegioneService;
import lombok.*;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Project vaadlievito <br>
 * Created by Algos <br>
 * User: Gac <br>
 * Fix date: 7-apr-2020 14.05.47 <br>
 * <p>
 * Estende la entity astratta AEntity che contiene la key property ObjectId <br>
 * <p>
 * Not annotated with @SpringComponent (inutile).  <br>
 * Not annotated with @Scope (inutile). Le istanze 'prototype' vengono generate da xxxService.newEntity() <br>
 * Not annotated with @Qualifier (inutile) <br>
 * Annotated with @Entity (facoltativo) per specificare che si tratta di una collection (DB Mongo) <br>
 * Annotated with @Document (facoltativo) per avere un nome della collection (DB Mongo) diverso dal nome della Entity <br>
 * Annotated with @TypeAlias (facoltativo) to replace the fully qualified class name with a different value. <br>
 * Annotated with @Data (Lombok) for automatic use of Getter and Setter <br>
 * Annotated with @NoArgsConstructor (Lombok) for JavaBean specifications <br>
 * Annotated with @AllArgsConstructor (Lombok) per usare il costruttore completo nel Service <br>
 * Annotated with @Builder (Lombok) con un metodo specifico, per usare quello standard nella (eventuale) sottoclasse <br>
 * - lets you automatically produce the code required to have your class be instantiable with code such as:
 * - Person.builder().name("Adam Savage").city("San Francisco").build(); <br>
 * Annotated with @EqualsAndHashCode (Lombok) per l'uguaglianza di due istanze della classe <br>
 * Annotated with @AIScript (facoltativo Algos) per controllare la ri-creazione di questo file dal Wizard <br>
 * - la documentazione precedente a questo tag viene SEMPRE riscritta <br>
 * - se occorre preservare delle @Annotation con valori specifici, spostarle DOPO @AIScript <br>
 * Annotated with @AIEntity (facoltativo Algos) per alcuni parametri generali del modulo <br>
 * Annotated with @AIList (facoltativo Algos) per le colonne automatiche della Grid nella lista <br>
 * Annotated with @AIForm (facoltativo Algos) per i fields automatici nel dialogo del Form <br>
 * <p>
 * Inserisce SEMPRE la versione di serializzazione <br>
 * Le singole property sono pubbliche in modo da poterne leggere il valore tramite 'reflection' <br>
 * Le singole property sono annotate con @AIField (obbligatorio Algos) per il tipo di fields nel dialogo del Form <br>
 * Le singole property sono annotate con @AIColumn (facoltativo Algos) per il tipo di Column nella Grid <br>
 * Le singole property sono annotate con @Field("xxx") (facoltativo)
 * -which gives a name to the key to be used to store the field inside the document.
 * -The property name (i.e. 'descrizione') would be used as the field key if this annotation was not included.
 * -Remember that field keys are repeated for every document so using a smaller key name will reduce the required space.
 * -va usato SOLO per 'collection' molto grandi (per evitare confusione sul nome della property da usare).
 * Le property non primitive, di default sono EMBEDDED con un riferimento statico
 * (EAFieldType.link e XxxPresenter.class)
 * Le singole property possono essere annotate con @DBRef per un riferimento DINAMICO (not embedded)
 * (EAFieldType.combo e XXService.class, con inserimento automatico nel ViewDialog)
 * Una (e una sola) property deve avere @AIColumn(flexGrow = true) per fissare la larghezza della Grid <br>
 */
@Entity
@Document(collection = "spacciatore")
@TypeAlias("spacciatore")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "builderSpacciatore")
@EqualsAndHashCode(callSuper = false)
@AIScript(sovrascrivibile = false)
@AIEntity(recordName = "spacciatore", company = EACompanyRequired.nonUsata)
@AIList(fields = {"comune", "nickname"})
@AIForm(fields = {"provincia", "comune", "localita", "nickname", "solida", "liquida", "cellulare", "email", "contatto"})
public class Spacciatore extends AEntity {


    /**
     * versione della classe per la serializzazione
     */
    private final static long serialVersionUID = 1L;


    /**
     * comune
     * riferimento dinamico CON @DBRef
     */
    @NotNull(message = "La regione è obbligatoria")
    @DBRef
    @AIField(type = EAFieldType.combo, serviceClazz = RegioneService.class)
    @AIColumn(sortable = true, widthEM = 20)
    public Regione regione;


    /**
     * comune
     * riferimento dinamico CON @DBRef
     */
    @NotNull(message = "La provincia è obbligatoria")
    @DBRef
    @AIField(type = EAFieldType.combo, serviceClazz = ProvinciaService.class)
    @AIColumn(sortable = true, widthEM = 20)
    public Provincia provincia;


    /**
     * comune
     * riferimento dinamico CON @DBRef
     */
    @NotNull(message = "Il comune è obbligatorio")
    @DBRef
    @AIField(type = EAFieldType.combo, serviceClazz = ComuneService.class, widthEM = 14)
    @AIColumn(sortable = true, widthEM = 11)
    public Comune comune;


    /**
     * descrizione (obbligatoria, non unica) <br>
     */
    @Size(min = 2, max = 50)
    @Field("loc")
    @AIField(type = EAFieldType.text, widthEM = 12)
    @AIColumn(sortable = true, widthEM = 9)
    public String localita;


    /**
     * descrizione (obbligatoria, non unica) <br>
     */
    @NotNull(message = "Il nickName è obbligatorio")
    @Size(min = 2, max = 50)
    @Field("desc")
    @AIField(type = EAFieldType.text, required = true, widthEM = 12)
    @AIColumn(sortable = true, widthEM = 9, name = "nickname")
    public String nickname;


    /**
     * booleano
     */
    @AIField(type = EAFieldType.checkbox, name = "pasta madre solida")
    @AIColumn(sortable = true, widthEM = 8)
    public boolean solida;


    /**
     * booleano
     */
    @AIField(type = EAFieldType.checkbox, name = "pasta madre liquida")
    @AIColumn(sortable = true, widthEM = 8)
    public boolean liquida;


    /**
     * cellulare <br>
     */
    @AIField(type = EAFieldType.text, name = "telefono cellulare", widthEM = 8)
    @AIColumn(sortable = true, widthEM = 10)
    public String cellulare;


    /**
     * mail <br>
     */
    @AIField(type = EAFieldType.email, name = "indirizzo di posta elettronica", widthEM = 12)
    @AIColumn(sortable = true, widthEM = 18)
    public String email;

    /**
     * mail <br>
     */
    @AIField(type = EAFieldType.text, widthEM = 12, name = "Note aggiuntive (faceBook, orari, ecc.)")
    @AIColumn(sortable = true, widthEM = 18)
    public String contatto;


    /**
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return nickname;
    }// end of method


}// end of entity class