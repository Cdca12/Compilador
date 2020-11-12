package compilador;

import utils.Simbolo;
import utils.TipoToken;
import utils.Token;

import java.util.Arrays;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

        Simbolo simbolo;
        Token token, tokenTipo, tokenValor;

        for (int i = 0; i < this.tokenRC.size(); i++) {

            boolean esExpresion = false;
            token = this.tokenRC.get(i);
            simbolo = new Simbolo();

            // Obtenemos los datos del Token
            simbolo.setIdentificador(token.getToken());
            simbolo.setPosicion(token.getRenglon());

            // TODO: Validar futuros tipos de dato
            if (token.getTipo() == TipoToken.ENTERO || token.getTipo() == TipoToken.BOOLEANO) {
//                simbolo.setTipoDato("Tipo de Dato");

            } else if (this.tokenRC.get(i).getTipo() == TipoToken.NUM) {
                if (this.tokenRC.get(i - 1).getToken().equals("=") && esOperador(this.tokenRC.get(i + 1).getToken())) {
                    simbolo.setValor(hacerExpresion(i));
                }

            } else if (token.getTipo() == TipoToken.ID) {

                // Si no tiene valor asignado
                simbolo.setTipoDato(this.tokenRC.get(i - 1).getToken());

                if (this.tokenRC.get(i + 1).getToken().equals(";")) {
                    simbolo.setValor("null");
                } else { // No se le da da valor por default
                    simbolo.setValor(this.tokenRC.get(i + 2).getToken());
                }

                //Si lo que continua despues de un '=' hasta un ';' es una expresión
                if (this.tokenRC.get(i + 1).getToken().equals("=") && esOperador(this.tokenRC.get(i + 3).getToken())) {
                    simbolo.setValor(hacerExpresion(i));
                    esExpresion = true;
                }


                // Si uno anterior es igual o un operador. Para validar si ya existe el identificador y esta definido
                if (!esExpresion && this.tokenRC.get(i - 1).getToken().equals("=") && existeToken(this.tokenRC.get(i).getToken()) || esOperador(this.tokenRC.get(i - 1).getToken())) {
                    continue;
                }

                if (!esExpresion && !(this.tokenRC.get(i - 1).getToken().equals("int") || this.tokenRC.get(i - 1).getToken().equals("boolean"))) {
                    simbolo.setTipoDato("Indefinido");
                    simbolo.setValor("-");
                }

                if (this.tokenRC.get(i - 1).getToken().equals("class")) {
                    simbolo.setValor("");
                }
            }

            if (!simbolo.getValor().equals("") && !simbolo.getTipoDato().equals(""))
                listaSimbolos.add(simbolo);

        }

    }

    private String hacerExpresion(int i) {
        StringBuilder valor = new StringBuilder();
        int k = i + 2;
        while (this.tokenRC.get(k).getTipo() != TipoToken.SEMI) {
            valor.append(this.tokenRC.get(k).getToken()).append(" ");
            k++;
        }

        return valor.toString();
    }

    private boolean esOperador(String operador) {
        String[] operadores = {"+", "-", "*", "/"};
        List<String> listaOperadores = Arrays.asList(operadores);
        return listaOperadores.contains(operador);
    }

    private boolean esOperando(int operando) {
        return operando == TipoToken.ID || operando == TipoToken.NUM;
    }

    private boolean existeToken(String identificador) {

        for (Simbolo simbolo : listaSimbolos) {
            if (simbolo.getIdentificador().equals(identificador)) {
                return true;
            }
        }
        return false;
    }

    private boolean sonLlaves(int tipoDato) {
        return tipoDato == TipoToken.LLAVEIZQ || tipoDato == TipoToken.LLAVEDER;
    }

    private boolean esOperador(int tipoDato) {
        return tipoDato == TipoToken.EQ || tipoDato == TipoToken.MENOR || tipoDato == TipoToken.MAYOR
                || tipoDato == TipoToken.D2EQ || tipoDato == TipoToken.MULT || tipoDato == TipoToken.MENOREQ
                || tipoDato == TipoToken.DIV || tipoDato == TipoToken.MAYOREQ || tipoDato == TipoToken.DIFERENTE
                || tipoDato == TipoToken.DIFEQ;
    }

    private boolean esCaracterEspecial(int tipoDato) {
        return tipoDato == TipoToken.SEMI;
    }

    private boolean esValor(int tipoDato) {
        return tipoDato == TipoToken.NUM || tipoDato == TipoToken.TRUEX || tipoDato == TipoToken.FALSEX;
    }


}
