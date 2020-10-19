package compilador;

import utils.Simbolo;
import utils.TipoToken;
import utils.Token;

import java.util.Arrays;

import java.util.ArrayList;

public class TablaSimbolos {

    private ArrayList<Simbolo> listaSimbolos;
    private ArrayList<Token> tokenRC;

    public TablaSimbolos(ArrayList<Token> tokenRC) {
        listaSimbolos = new ArrayList<Simbolo>();
        this.tokenRC = tokenRC;
        crearTablaSimbolos();
    }

    public ArrayList<Simbolo> getListaSimbolos() {
        return listaSimbolos;
    }

    public void setListaSimbolos(ArrayList<Simbolo> listaSimbolos) {
        this.listaSimbolos = listaSimbolos;
    }

    public void crearTablaSimbolos() {
        Simbolo simbolo = new Simbolo();

        Token token, tokenTipo, tokenValor;
        for (int i = 0; i < this.tokenRC.size(); i++) {
            token = this.tokenRC.get(i);

            simbolo = new Simbolo();

            // Obtenemos los datos del Token
            simbolo.setIdentificador(token.getToken());
            simbolo.setPosicion(token.getRenglon());

            // TODO: Validar futuros tipos de dato
            if (token.getTipo() == TipoToken.ENTERO || token.getTipo() == TipoToken.BOOLEANO) {
                simbolo.setTipoDato("Tipo de Dato");
            } else if (token.getTipo() == TipoToken.ID) {
                if (this.tokenRC.get(i - 1).getToken().equals("class")) {
                    simbolo.setTipoDato("Clase");
                    simbolo.setValor("");
                } else {
                    simbolo.setTipoDato(this.tokenRC.get(i - 1).getToken());
                    simbolo.setValor(this.tokenRC.get(i + 2).getToken());
                }
            } else if (esValor(token.getTipo())) {
                simbolo.setTipoDato("Valor");
                simbolo.setValor("");
            } else {
                if (sonLlaves(token.getTipo())) {
                    simbolo.setTipoDato("Llaves");
                    simbolo.setValor("");
                } else if (esOperador(token.getTipo())) {
                    simbolo.setTipoDato("Operador");
                    simbolo.setValor("");
                } else if (esCaracterEspecial(token.getTipo())) {
                    simbolo.setTipoDato("Caracter especial");
                    simbolo.setValor("");
                } else {
                    // Palabra reservada
                    simbolo.setTipoDato("Palabra reservada");
                }
            }
            listaSimbolos.add(simbolo);

        }

    }

    private boolean sonLlaves(int tipoDato) {
        return tipoDato == TipoToken.LLAVEIZQ || tipoDato == TipoToken.LLAVEDER;
    }

    private boolean esOperador(int tipoDato) {
        return tipoDato == TipoToken.EQ || tipoDato == TipoToken.MENOR || tipoDato == TipoToken.MAYOR
                || tipoDato == TipoToken.D2EQ || tipoDato == TipoToken.MENOR || tipoDato == TipoToken.MENOREQ
                || tipoDato == TipoToken.MAYOREQ || tipoDato == TipoToken.DIFERENTE || tipoDato == TipoToken.DIFEQ;
    }

    private boolean esCaracterEspecial(int tipoDato) {
        return tipoDato == TipoToken.SEMI;
    }

    private boolean esValor(int tipoDato) {
        return tipoDato == TipoToken.NUM || tipoDato == TipoToken.TRUEX || tipoDato == TipoToken.FALSEX;
    }


}
