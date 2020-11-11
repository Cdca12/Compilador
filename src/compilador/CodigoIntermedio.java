package compilador;

import utils.Cuadruplo;
import utils.Simbolo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class CodigoIntermedio {

    private class Expresion {
        public String id;
        public String expresion;

        private Expresion(String id, String expresion) {
            this.id = id;
            this.expresion = expresion;
        }
    }

    private class tmpValor {
        public String nombre;
        public double valor;

        private tmpValor(String nombre, double valor) {
            this.nombre = nombre;
            this.valor = valor;
        }
    }

    ArrayList<Simbolo> listaSimbolos;
    ArrayList<Expresion> listaExpresiones;
    ArrayList<Cuadruplo> listaCuadruplos;

    public CodigoIntermedio(ArrayList<Simbolo> listaSimbolos) {
        this.listaSimbolos = listaSimbolos;
        obtenerExpresiones();
        generarCuadruplos();
    }

    private void obtenerExpresiones() {
        listaExpresiones = new ArrayList<>();
        for (Simbolo simbolo : listaSimbolos) {
            String expresion = simbolo.getValor();
            // Cuádruplos: Expresión de 4 elementos
            if (expresion.split("\\s+").length >= 3) {
                listaExpresiones.add(new Expresion(simbolo.getIdentificador(), expresion));
            }
        }
    }

    private void generarCuadruplos() {
        for (Expresion ex : listaExpresiones) {
            listaCuadruplos = new ArrayList<>();
            ArrayList<String> elementos = new ArrayList<>(Arrays.asList(ex.expresion.split("\\s+")));
            int contadorTmp = 1;
            for (int i = 0; i < elementos.size(); i++) {
                if (elementos.get(i).equals("/") || elementos.get(i).equals("*")) {
                    generarCuadruplo(elementos, i, contadorTmp);
                    contadorTmp++;
                    i = -1;
                } else if (elementos.get(i).equals("+") || elementos.get(i).equals("-")) {
                    generarCuadruplo(elementos, i, contadorTmp);
                    contadorTmp++;
                    i = -1;
                } else if (elementos.size() == 1) {
                    listaCuadruplos.add(new Cuadruplo(
                            ex.id + " :=",  // Operador
                            elementos.get(i),       // Operador1
                            "",            // Operador2
                            ""              // Resultado
                    ));
                }
                imprimirTest();
            }

        }
    }

    private String generarTmp() {
        return new Random().nextFloat() >= 0.5 ? "tmpCarlos" : "tmpMauri";
    }

    private void generarCuadruplo(ArrayList<String> elementos, int i, int contador) {
        String tmp = generarTmp() + contador;
        listaCuadruplos.add(new Cuadruplo(
                elementos.get(i),       // Operador
                elementos.get(i - 1),   // Operador1
                elementos.get(i + 1),   // Operador2
                tmp                     // Resultado
        ));
        elementos.set(i, tmp);
        elementos.remove(i + 1);
        elementos.remove(i - 1);

    }

    private void imprimirTest() {
        for (Cuadruplo cuadruplo : listaCuadruplos) {
            System.out.println(
                cuadruplo.getOperador() + " " +
                cuadruplo.getOperador1() + " " +
                cuadruplo.getOperador2() + " " +
                cuadruplo.getResultado() + " "
            );
        }
    }

}


