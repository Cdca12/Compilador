package compilador;

import java.util.ArrayList;

public class TablaSimbolos {

    private ArrayList<Simbolo> listaSimbolos;

    public TablaSimbolos() {
        listaSimbolos = new ArrayList<Simbolo>();
    }

    public ArrayList<Simbolo> getListaSimbolos() {
        return listaSimbolos;
    }

    public void setListaSimbolos(ArrayList<Simbolo> listaSimbolos) {
        this.listaSimbolos = listaSimbolos;
    }

    public void añadirSimbolo(Simbolo simbolo) {
        this.listaSimbolos.add(simbolo);
    }


}
