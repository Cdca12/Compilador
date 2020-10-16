package compilador;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.regex.*;
import javax.swing.JOptionPane;

public class AnalizadorLexico {

    private int renglon = 1, columna = 1, col2 = 0, cont = 0, contador = -1;
    private int retEQ = 0, retMayEQ = 0, retMenEQ = 0, retDif = 0;
    private boolean hayErrores = false;
    private ArrayList<String> erroresLexicos;
    private ArrayList<Token> tokenRC;
    private String ruta;

    public AnalizadorLexico(String ruta) {
        erroresLexicos = new ArrayList<String>();
        tokenRC = new ArrayList<Token>();
        this.ruta = ruta;

        analizar(this.ruta);

        if (!hayErrores) /*{*/ {
            erroresLexicos.add("No hay errores lexicos");
        }
    }

    public ArrayList<String> getErroresLexicos() {
        return erroresLexicos;
    }

    public ArrayList<Token> getTokenRC() {
        return tokenRC;
    }

    public boolean getHayErrores() {
        return hayErrores;
    }

    private void analizar(String ruta) {
        String linea = "", token = "";
        StringTokenizer tokenizer;
        try {
            FileReader file = new FileReader(ruta);
            BufferedReader archivoEntrada = new BufferedReader(file);
            linea = archivoEntrada.readLine();

            while (linea != null) {
                columna = 0;
                linea = espacios(linea);
                tokenizer = new StringTokenizer(linea);
                while (tokenizer.hasMoreTokens()) //DENTRO DE ESTE WHILE, EN EL METODO analizadorLexico SE MANDA CADA TOKEN, CADA PALABRA
                {
                    columna++;
                    token = tokenizer.nextToken();
                    analizadorLexico(token);

                }
                linea = archivoEntrada.readLine();
                renglon++;
            }
            archivoEntrada.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "No se encontro el archivo favor de checar la ruta", "Alerta", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void analizadorLexico(String token) {
        token = Junta(token);
        if (token.equals("==") || token.equals(">=") || token.equals("<=") || token.equals("!=")) {
            return;
        }
        String cadenas[] = {"class", "public", "private", "while", "int", "boolean", "{", "}", "=", ";", "<", ">", //12... Aunque no se usa como tal el "!" solo, sirve para que no lance error
            "==", "<=", ">=", "!", "!=", "true", "false", "(", ")", "/", "+", "-", "*", "if"};		//14   total = 26, de 0 al 25 + nums e id --> 0 - 27
        int tipo = -1;
        for (int i = 0; i < cadenas.length; i++) {
            if (token.equals(cadenas[i])) {
                tipo = i;
            }
        }
        if (token.matches("^[0-9]?$")) {
            tipo = 50;
        }
        if (token.matches("^[0-9][0-9]?$")) {
            tipo = 50;
        }
        if (token.matches("^[0-9][0-9][0-9]+?$")) {//error en numeros
            erroresLexicos.add("Error Léxico, se esperaba una longitud de 2 dígitos en el número \"" + token + "\" en la linea " + renglon + ", No. de token " + columna + " ");
            tokenRC.add(new Token(token, renglon, columna, tipo));
            hayErrores = true;
            return;
        }

        if (tipo == -1) {
            Pattern pat = Pattern.compile("^[a-zA-Z]+[0-9]*+$");
            Matcher mat = pat.matcher(token);
            if (mat.find()) {
                tipo = 52;
            } else {
                erroresLexicos.add("Error Léxico en la linea \"" + renglon + "\" No. de token \"" + columna + "\" nombre del token \"" + token + "\", algunos signos no se admiten.");
                tokenRC.add(new Token(token, renglon, columna, tipo));
                hayErrores = true;
                return;
            }
        }
        tokenRC.add(new Token(token, renglon, columna, tipo));
    }

    public String espacios(String linea) {
        for (String cadena : Arrays.asList("(", ")", "{", "}", "=", ";", "*", "-", "+", "<", "/", ">", "!")) {
            if (linea.contains(cadena)) {
                linea = linea.replace(cadena, " " + cadena + " ");
            }
        }
        return linea;
    }

    public String Junta(String token) { //hicimos esto ya que se buggeaba cuando trataba de poner un token tipo != <= >= ==
        contador++;

        if (token.equals("<")) {
            retMenEQ++;
        } else if (token.equals(">")) {
            retMayEQ++;
        } else if (token.equals("!")) {
            retDif++;
        } else if (token.equals("=")) {
            retEQ++;
            retDif++;
            retMayEQ++;
            retMenEQ++;

        } else {
            retEQ = 0;
            retDif = 0;
            retMayEQ = 0;
            retMenEQ = 0;
        }

        if (retEQ == 2 || retMenEQ == 2 || retMayEQ == 2 || retDif == 2) {
            tokenRC.remove(contador - 1);

            if (retEQ == 2) {
                tokenRC.add(new Token("==", renglon, columna, 11));
                token = "==";
            } else if (retMayEQ == 2) {
                tokenRC.add(new Token(">=", renglon, columna, 12));
                token = ">=";
            } else if (retMenEQ == 2) {
                tokenRC.add(new Token("<=", renglon, columna, 13));
                token = "<=";
            } else if (retDif == 2) {
                tokenRC.add(new Token("!=", renglon, columna, 14));
                token = "!=";
            }
            contador--;
        }
        return token;
    }
}
