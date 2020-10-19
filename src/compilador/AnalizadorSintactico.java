package compilador;

import java.util.ArrayList;

import views.CompiladorFrame;
import utils.TipoToken;
import utils.Token;

public class AnalizadorSintactico {

    private ArrayList<Token> tokenRC;
    private String token = "", esperado = "";
    private int tipo, contando = 0, flag = 0;
    private String estructura = "";


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
        if (tipo == TipoToken.PUBLICO || tipo == TipoToken.PRIVADO) {
            consumir(tipo);
        }
        consumir(TipoToken.CLASE);
        consumir(TipoToken.ID);
        consumir(TipoToken.LLAVEIZQ);
        while (tipo == TipoToken.PUBLICO || tipo == TipoToken.PRIVADO) {
            consumir(tipo);
            declararTipo();
        }
        while (tipo == TipoToken.ENTERO || tipo == TipoToken.BOOLEANO) {
            declararTipo();
        }
        if (this.tipo == TipoToken.WHILEX || this.tipo == TipoToken.IFX || this.tipo == TipoToken.ENTERO || this.tipo == TipoToken.BOOLEANO) {
            status();
        }
        consumir(TipoToken.LLAVEDER);
        if (contando < tokenRC.size()) {
            error(1);
        }
        estructura = "estructura correcta";
    }

    public void declararTipo() {
        String token = "";
        switch (tipo) {
            case TipoToken.ENTERO:
                consumir(TipoToken.ENTERO);
                token = this.token;
                consumir(TipoToken.ID);
                if (tipo == TipoToken.EQ) {
                    consumir(TipoToken.EQ);
                    if (tipo == TipoToken.NUM) {
                        consumir(TipoToken.NUM);
                    } else if (tipo == TipoToken.FALSEX) {
                        consumir(TipoToken.FALSEX);
                    } else {    // if(type == truex)
                        consumir(TipoToken.TRUEX);
                    }
                }
                consumir(TipoToken.SEMI);
                break;
            case TipoToken.BOOLEANO:
                consumir(TipoToken.BOOLEANO);
                token = this.token;
                consumir(TipoToken.ID);
                if (tipo == TipoToken.EQ) {
                    consumir(TipoToken.EQ);
                    if (tipo == TipoToken.NUM) {
                        consumir(TipoToken.NUM);
                    } else if (tipo == TipoToken.FALSEX) {
                        consumir(TipoToken.FALSEX);
                    } else // if(type == truex)
                    {
                        consumir(TipoToken.TRUEX);
                    }
                }
                consumir(TipoToken.SEMI);
                break;
            // TODO: Añadir otro tipo
//            case char:
//                eat(char);
//                tok = this.token;
//                eat(TipoToken.ID);
//                if (tipo == TipoToken.EQ) {
//                    eat(TipoToken.EQ);
//                    if (tipo == TipoToken.NUM) {
//                        eat(TipoToken.NUM);
//                    } else if (tipo == falsex) {
//                        eat(falsex);
//                    } else // if(type == truex)
//                    {
//                        eat(truex);
//                    }
//                }
//                eat(TipoToken.SEMI);
//                break;            // TODO: Añadir otro tipo
//            case char:
//                eat(char);
//                tok = this.token;
//                eat(TipoToken.ID);
//                if (tipo == TipoToken.EQ) {
//                    eat(TipoToken.EQ);
//                    if (tipo == TipoToken.NUM) {
//                        eat(TipoToken.NUM);
//                    } else if (tipo == falsex) {
//                        eat(falsex);
//                    } else // if(type == truex)
//                    {
//                        eat(truex);
//                    }
//                }
//                eat(TipoToken.SEMI);
//                break;

        }
    }

    public void VarDeclarator() {
        consumir(TipoToken.EQ);
        if (tipo == TipoToken.NUM) {
            consumir(TipoToken.NUM);
        }

        if (tipo == TipoToken.FALSEX) {
            consumir(TipoToken.FALSEX);
        }

        if (tipo == TipoToken.TRUEX) {
            consumir(TipoToken.TRUEX);
        }
    }

    public void status() {
        switch (tipo) {
            case TipoToken.IFX:
                consumir(TipoToken.IFX);
                consumir(TipoToken.BRACKIZQ);

                checarExpresion();
                
                consumir(TipoToken.BRACKDER);
                consumir(TipoToken.LLAVEIZQ);

                while (tipo == TipoToken.WHILEX || tipo == TipoToken.IFX || tipo == TipoToken.ID || tipo == TipoToken.BOOLEANO || tipo == TipoToken.ENTERO) {
                    status(); // para llamar otro statement dentro del statement
                }
                consumir(TipoToken.LLAVEDER);
                break;

            case TipoToken.WHILEX:
                consumir(TipoToken.WHILEX);
                consumir(TipoToken.BRACKIZQ);

                checarExpresion();
                
                consumir(TipoToken.BRACKDER);
                consumir(TipoToken.LLAVEIZQ);
                while (tipo == TipoToken.WHILEX || tipo == TipoToken.IFX || tipo == TipoToken.BOOLEANO || tipo == TipoToken.ENTERO || tipo == TipoToken.PUBLICO
                        || tipo == TipoToken.PRIVADO) {
                    status(); // para llamar otro statement dentro del statement
                }
                consumir(TipoToken.LLAVEDER);
                break;
            case TipoToken.ID:
                consumir(TipoToken.ID);
                consumir(TipoToken.EQ);

                expresionAritmetica();
                consumir(TipoToken.SEMI);
                while (tipo == TipoToken.WHILEX || tipo == TipoToken.IFX || tipo == TipoToken.BOOLEANO || tipo == TipoToken.ENTERO) {
                    status(); // para llamar otro statement dentro del statement
                }
                break;
            case TipoToken.BOOLEANO:
                declararTipo();
                break;
            case TipoToken.ENTERO:
                declararTipo();
                break;
            case TipoToken.PUBLICO:
                consumir(TipoToken.PUBLICO);
                declararTipo();
                break;
            case TipoToken.PRIVADO:
                consumir(TipoToken.PRIVADO);
                declararTipo();
                break;
            default:
                error();
        }
    }

    public void checarExpresion() {
        switch (tipo) {
            case TipoToken.ID:
                if (tipo == TipoToken.ID) {
                    consumir(TipoToken.ID);
                } else// if(type == TipoToken.NUM)
                {
                    consumir(TipoToken.NUM);
                }
                if (checarSimbolosLogicos()) {
                    if (tipo == TipoToken.ID) {
                        consumir(TipoToken.ID);
                    } else // if(type == TipoToken.NUM)
                    {
                        consumir(TipoToken.NUM);
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
            case TipoToken.NUM:
                consumir(TipoToken.NUM);
                if (checarSimbolosOperando()) {
                    consumir(TipoToken.NUM);
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
        if (tipo == TipoToken.MENOR || tipo == TipoToken.MAYOR || tipo == TipoToken.MENOREQ || tipo == TipoToken.MAYOREQ
                || tipo == TipoToken.D2EQ/* type == TipoToken.MAYOR || type == dobleTipoToken.EQ || */) {
            consumir(tipo);
            return true;
        } else {
            error(3);
        }
        return false;
    }

    public boolean checarSimbolosOperando() {
        if (tipo == TipoToken.MENOS || tipo == TipoToken.MAS || tipo == TipoToken.DIV || tipo == TipoToken.MULT) {
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
            return "TipoToken.NUMérico";
        }
        if (tipo == 52) {
            return "identificador";
        }
        return cadenas[tipo];
    }
}
