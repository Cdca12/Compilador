package compilador;

import utils.Simbolo;
import utils.TipoToken;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

public class AnalizadorSemantico {
    private ArrayList<Simbolo> listaSimbolos;
    private ArrayList<String> erroresSemanticos;

    public AnalizadorSemantico(TablaSimbolos tablaSimbolos) {
        this.listaSimbolos = tablaSimbolos.getListaSimbolos();
        erroresSemanticos = new ArrayList<>();
        analizar();
    }

    public ArrayList<String> getErroresSemanticos() {
        return erroresSemanticos;
    }

    private void analizar() {
        Simbolo simbolo;
        for (int i = 0; i < listaSimbolos.size(); i++) {
            simbolo = listaSimbolos.get(i);

            if (!validarAsignacion(simbolo)) {
                añadirError(simbolo);
            }

        }
        validarDeclaracion();
        validarDuplicados();
    }

    private boolean validarAsignacion(Simbolo simbolo) {
        // Asignar boolean en un int
        if (simbolo.getTipoDato().equals("int") && (simbolo.getValor().equals("true") || simbolo.getValor().equals("false"))) {
            return false;
        }

        // Asignar int en un boolean
        if (simbolo.getTipoDato().equals("boolean") && simbolo.getValor().matches("^[0-9]+$")) {
            return false;
        }
        return true;

    }

    private void añadirError(Simbolo simbolo) {
        erroresSemanticos.add("Error semántico en la línea " + simbolo.getPosicion() + ": tipos incompatibles. " +
                simbolo.getValor() + " no puede ser convertido a " + simbolo.getTipoDato() + " en \"" + simbolo.getIdentificador() + "\"");
    }

    private void validarDeclaracion() {
        for (Simbolo simbolo : listaSimbolos) {
            if (simbolo.getTipoDato().equals("Indefinido")) {
                erroresSemanticos.add("Error semántico en la línea " + simbolo.getPosicion() + ": variable no definida. " +
                        "La variable \"" + simbolo.getIdentificador() + "\" no ha sido definida.");
            }
        }
    }

    private void validarDuplicados() {
        Set<String> listaIdentificadores = new HashSet<>();
        ArrayList<Simbolo> listaDuplicados = new ArrayList<>();

        for (int i = 0; i < listaSimbolos.size(); i++) {
            if (listaSimbolos.get(i).getTipoDato().equals("int")
                    || listaSimbolos.get(i).getTipoDato().equals("boolean")
                    || listaSimbolos.get(i).getTipoDato().equals("Clase"))
                if (!listaIdentificadores.add(listaSimbolos.get(i).getIdentificador())) {
                    listaDuplicados.add(listaSimbolos.get(i));
                }
        }


        for (Simbolo simbolo : listaDuplicados) {
            erroresSemanticos.add("Error semántico en la línea " + simbolo.getPosicion() + ": variable ya declarada. " +
                    "La variable \"" + simbolo.getIdentificador() + "\" ya ha sido declarada anteriormente.");
        }


    }


}

