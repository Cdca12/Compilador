package compilador;

import java.util.ArrayList;

public class AnalizadorSintactico {

    private ArrayList<Token> tokenRC;
    private String token = "", esperado = "";
    private int tipo, contando = 0, flag = 0;
    private String estructura = "";

    final int clase = 0, publico = 1, privado = 2, whilex = 3, entero = 4, booleano = 5, llaveizq = 6, llaveder = 7,
            EQ = 8, semi = 9, menor = 10, mayor = 11, d2EQ = 12, menorEQ = 13, mayorEQ = 14, diferente = 15, difEQ = 16,
            truex = 17, falsex = 18, brackizq = 19, brackder = 20, div = 21, mas = 22, menos = 23, mult = 24, ifx = 25,
            num = 50, ID = 52; // bool = 21,

    public AnalizadorSintactico(ArrayList<Token> tokenRC) {
        this.tokenRC = tokenRC;
        try {
            this.token = this.tokenRC.get(0).getToken();
            this.tipo = this.tokenRC.get(0).getTipo();
        } catch (Exception e) {
            CompiladorFrame.consola.append("El archivo está vacío");
        }
        analizar();
    }

    public void avanzar() {
        try {
            tipo = tokenRC.get(contando).getTipo();
            token = tokenRC.get(contando).getToken();
        } catch (Exception e) {

        }
    }

    public void consumir(int esp) {
        if (tipo == esp) {
            contando++;
            if (contando < tokenRC.size()) {
                avanzar();
            }
        } else {
            error(esp);
        }
    }

    public void analizar() {
        if (tipo == publico || tipo == privado) {
            consumir(tipo);
        }
        consumir(clase);
        consumir(ID);
        consumir(llaveizq);
        while (tipo == publico || tipo == privado) {
            consumir(tipo);
            declararTipo();
        }
        while (tipo == entero || tipo == booleano) {
            declararTipo();
        }
        if (this.tipo == whilex || this.tipo == ifx || this.tipo == entero || this.tipo == booleano) {
            status();
        }
        consumir(llaveder);
        if (contando < tokenRC.size()) {
            error(1);
        }
        estructura = "estructura correcta";
    }

    public void declararTipo() {
        String token = "";
        switch (tipo) {
            case entero:
                consumir(entero);
                token = this.token;
                consumir(ID);
                if (tipo == EQ) {
                    consumir(EQ);
                    if (tipo == num) {
                        consumir(num);
                    } else if (tipo == falsex) {
                        consumir(falsex);
                    } else {    // if(type == truex)
                        consumir(truex);
                    }
                }
                consumir(semi);
                break;
            case booleano:
                consumir(booleano);
                token = this.token;
                consumir(ID);
                if (tipo == EQ) {
                    consumir(EQ);
                    if (tipo == num) {
                        consumir(num);
                    } else if (tipo == falsex) {
                        consumir(falsex);
                    } else // if(type == truex)
                    {
                        consumir(truex);
                    }
                }
                consumir(semi);
                break;
            // TODO: Añadir otro tipo
//            case char:
//                eat(char);
//                tok = this.token;
//                eat(ID);
//                if (tipo == EQ) {
//                    eat(EQ);
//                    if (tipo == num) {
//                        eat(num);
//                    } else if (tipo == falsex) {
//                        eat(falsex);
//                    } else // if(type == truex)
//                    {
//                        eat(truex);
//                    }
//                }
//                eat(semi);
//                break;            // TODO: Añadir otro tipo
//            case char:
//                eat(char);
//                tok = this.token;
//                eat(ID);
//                if (tipo == EQ) {
//                    eat(EQ);
//                    if (tipo == num) {
//                        eat(num);
//                    } else if (tipo == falsex) {
//                        eat(falsex);
//                    } else // if(type == truex)
//                    {
//                        eat(truex);
//                    }
//                }
//                eat(semi);
//                break;

        }
    }

    public void VarDeclarator() {
        consumir(EQ);
        if (tipo == num) {
            consumir(num);
        }

        if (tipo == falsex) {
            consumir(falsex);
        }

        if (tipo == truex) {
            consumir(truex);
        }
    }

    public void status() {
        switch (tipo) {
            case ifx:
                consumir(ifx);
                consumir(brackizq);

                checarExpresion();
                
                consumir(brackder);
                consumir(llaveizq);

                while (tipo == whilex || tipo == ifx || tipo == ID || tipo == booleano || tipo == entero) {
                    status(); // para llamar otro statement dentro del statement
                }
                consumir(llaveder);
                break;

            case whilex:
                consumir(whilex);
                consumir(brackizq);

                checarExpresion();
                
                consumir(brackder);
                consumir(llaveizq);
                while (tipo == whilex || tipo == ifx || tipo == booleano || tipo == entero || tipo == publico
                        || tipo == privado) {
                    status(); // para llamar otro statement dentro del statement
                }
                consumir(llaveder);
                break;
            case ID:
                consumir(ID);
                consumir(EQ);

                expresionAritmetica();
                consumir(semi);
                while (tipo == whilex || tipo == ifx || tipo == booleano || tipo == entero) {
                    status(); // para llamar otro statement dentro del statement
                }
                break;
            case booleano:
                declararTipo();
                break;
            case entero:
                declararTipo();
                break;
            case publico:
                consumir(publico);
                declararTipo();
                break;
            case privado:
                consumir(privado);
                declararTipo();
                break;
            default:
                error();
        }
    }

    public void checarExpresion() {
        switch (tipo) {
            case ID:
                if (tipo == ID) {
                    consumir(ID);
                } else// if(type == num)
                {
                    consumir(num);
                }
                if (checarSimbolosLogicos()) {
                    if (tipo == ID) {
                        consumir(ID);
                    } else // if(type == num)
                    {
                        consumir(num);
                    }
                }
                break;
            default:
                error();
                break;
        }
    }

    public void expresionAritmetica() {
        switch (tipo) {
            case num:
                consumir(num);
                if (checarSimbolosOperando()) {
                    consumir(num);
                }
                break;
            default:
                error();
                break;
        }
    }

    public void error(int type) {
        String tipo = checarValoresInversos(type);
        if (type == 0) {
            tipo = "\nError sintáctico, se esperaba una expresión *class* al comienzo";
        } else if (type == 1) {
            tipo = "\nError sintáctico en los límites, se encontró al menos un token después de la última llave cerrada, token ** "
                    + token + " ** en linea ** " + tokenRC.get(contando).getRenglon() + " **, No. de token ** "
                    + tokenRC.get(contando).getColumna() + " **";
        } else if (type == 2) {
            tipo = "\nError sintáctico en asignación, se esperaba un operador y operando antes de ** " + token
                    + " ** en linea ** " + tokenRC.get(contando).getRenglon() + " **, No. de token ** "
                    + tokenRC.get(contando).getColumna() + " **";
        } else if (type == 3) {
            tipo = "\nError sintáctico en validación, se esperaba un operador lógico en lugar de ** " + token
                    + " ** en linea ** " + tokenRC.get(contando).getRenglon() + " **, No. de token ** "
                    + tokenRC.get(contando).getColumna() + " **";
        } else {
            tipo = "\nError sintáctico en token ** " + token + " ** se esperaba un token ** "
                    + tipo + " **";
        }

        CompiladorFrame.consola.append(tipo);
    }

    public void error() {
        CompiladorFrame.consola.append("Error en la sintaxis, con el siguiente token ** " + token + " ** en linea ** "
                + tokenRC.get(contando).getRenglon() + " **, No. de token ** " + tokenRC.get(contando).getColumna()
                + " **");
    }

    public boolean checarSimbolosLogicos() {
        if (tipo == menor || tipo == mayor || tipo == menorEQ || tipo == mayorEQ
                || tipo == d2EQ/* type == mayor || type == dobleEQ || */) {
            consumir(tipo);
            return true;
        } else {
            error(3);
        }
        return false;
    }

    public boolean checarSimbolosOperando() {
        if (tipo == menos || tipo == mas || tipo == div || tipo == mult) {
            consumir(tipo);
            return true;
        } else {
            error(2);
        }
        return false;
    }

    public String checarValoresInversos(int tipo) {
        String cadenas[] = {
            "class", "public", "private", "while", "int", "boolean", "{", "}", "=", ";", "<", ">", // 12...
            // Aunque no se usa como tal el "!" solo, sirve para que no lance // error
            "==", "<=", ">=", "!", "!=", "true", "false", "(", ")", "/", "+", "-", "*", "if"
        };
        if (tipo == 50) {
            return "numérico";
        }
        if (tipo == 52) {
            return "identificador";
        }
        return cadenas[tipo];
    }
}
