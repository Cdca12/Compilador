package compilador;

import utils.Simbolo;
import utils.TipoToken;
import utils.Token;

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

        Token token;
        for (int i = 0; i < this.tokenRC.size(); i++) {
            token = this.tokenRC.get(i);
            simbolo = new Simbolo();
            // Obtenemos los datos del Token
            simbolo.setIdentificador(token.getToken());
            simbolo.setPosicion(token.getRenglon());
            if (token.getTipo() == TipoToken.ENTERO) {
                simbolo.setTipoDato("int");
                simbolo.setValor("0");
            } else if (token.getTipo() == TipoToken.BOOLEANO) {
                simbolo.setTipoDato("boolean");
                simbolo.setValor("false");
            }
            listaSimbolos.add(simbolo);
            // TODO: Validar futuros tipos de dato
        }
    }


}
