package it.algos.vaadflow.enumeration;

import static it.algos.vaadflow.application.FlowCost.*;

/**
 * Project vaadflow
 * Created by Algos
 * User: gac
 * Date: dom, 23-dic-2018
 * Time: 09:23
 */
public enum EAMenu implements IAEnum {

    routers, tabs, buttons, popup, flowing, vaadin;


    public static EAMenu getMenu(String nameMenu) {
        EAMenu menu = null;

        for (EAMenu menuTmp : EAMenu.values()) {
            if (menuTmp.toString().equals(nameMenu)) {
                menu = menuTmp;
            }// fine del blocco if
        }// end of for cycle

        return menu;
    }// end of static method


    /**
     * Stringa di valori (text) da usare per memorizzare la preferenza <br>
     * La stringa è composta da tutti i valori separati da virgola <br>
     * Poi, separato da punto e virgola, viene il valore corrente <br>
     *
     * @return stringa di valori e valore di default
     */
    @Override
    public String getPref() {
        String testo = VUOTA;

        for (EAMenu eaMenu : EAMenu.values()) {
            testo += eaMenu.name();
            testo += VIRGOLA;
        }// end of for cycle

        testo = testo.substring(0, testo.length() - 1);
        testo += PUNTO_VIRGOLA;
        testo += name();

        return testo;
    }// end of method

}// end of enumeration
