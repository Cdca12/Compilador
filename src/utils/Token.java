package utils;

public class Token {

    private String token;
    private int renglon;
    private int columna;
    private int tipo;

    public Token(String token, int renglon, int columna, int tipo) {
        this.token = token;
        this.renglon = renglon;
        this.columna = columna;
        this.tipo = tipo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getRenglon() {
        return renglon;
    }

    public void setRenglon(int renglon) {
        this.renglon = renglon;
    }

    public int getColumna() {
        return columna;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

}
