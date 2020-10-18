package compilador;

/**
 * @author Carlos Contreras
 * @author Mauricio García
 */
public class Simbolo {

    private String identificador;
    private String tipoDato;
    private int posicion;
    private String valor;

    public Simbolo(String identificador, String tipoDato, int posicion, String valor) {
        this.identificador = identificador;
        this.tipoDato = tipoDato;
        this.posicion = posicion;
        this.valor = valor;
    }
}
