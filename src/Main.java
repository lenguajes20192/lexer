/*
Lenguajes de Programacion 2019-2

Analizador Lexico

Autores:

Laura Morales
Jhon Alexander Sedano

*/


import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.regex.Pattern;



public class Main {

    static String[] resWords = new String[]{ "af", "and", "binding","by", "body", "cap", "coenter", "col", "create","destroy", "do",
            "else", "end", "external", "fa", "fi", "file", "final", "get", "getarg", "global", "if",
            "import", "int", "mod", "new","noop", "oc", "op", "or","procedure", "process", "read",
            "real", "ref", "res", "resource", "returns", "scanf", "sem", "send","sprintf", "st",
            "stop", "to", "val", "var", "write", "writes","receive","char","reply","next","string",
            "bool","ni","co","proc"};

    static String[] symbols = new String[]{"{","}",":=",":",",","[","]",";","(",")","+","++","<","<=",">",">=","!=",":=:","->",
            "-", "[]", "=", "%", ".", "|", "/", "*", "<<", ">>", "||", "**", "<<:=", ">>:=", "||:=", "**:=", "*:=", "/:=", "+:=",
            "-:=", "%:=", "%", "&:=", "&"};

    static String[] values = new String[]{"tk_llave_i", "tk_llave_d", "tk_asig", "tk_dos_puntos", "tk_coma", "tk_cor_izq",
            "tk_cor_der", "tk_punto_y_coma", "tk_par_izq", "tk_par_der", "tk_suma", "tk_incr", "tk_menorque",
            "tk_menor_igual", "tk_mayorque", "tk_mayor_igual", "tk_distinto", "tk_swap", "tk_ejecuta",
            "tk_menos", "tk_separa", "tk_igual", "tk_mod","tk_punto","tk_or", "tk_div", "tk_multi", "tk_lshift", "tk_rshift",
            "tk_concat", "tk_exp", "tk_lsasig", "tk_rsasig", "tk_concasig", "tk_expasig", "tk_mulasig", "tk_divasig", "tk_sumasig",
            "tk_resasig", "tk_remasig", "tk_rem", "tk_andasig", "tk_and"};

    static HashSet<String> h = new HashSet<String>();
    static HashMap<String, String> mp = new HashMap<String, String>();

    public static class Token {

        public int row;
        public int column;
        public String lexeme;
        public int type;

        public Token(int type, String lexemeValue, int rowValue, int columnValue){
            this.type= type;
            lexeme = lexemeValue;
            row = rowValue;
            column = columnValue;

        }
        public void printTok(){

            switch(type){
                case 1:
                    if(h.contains(lexeme))// if reserved
                        System.out.println("<" + lexeme+","+ row + ","+ column+">");
                    else
                        System.out.println("<id," + lexeme+"," + row + "," + column + ">");

                    break;

                case 2://belong to symbols
                    System.out.println("<" + mp.get(lexeme) + "," + row + "," + column + ">");
                    break;

                case 3://is number
                    System.out.println("<tk_num," + lexeme + "," + row + "," + column + ">");
                    break;

                case 4://is string
                    System.out.println("<tk_cadena," + lexeme + "," + row + "," + column + ">");
                    break;

                default:
                    break;
            }
        }

    }

    static void init() {
        h.addAll(Arrays.asList(resWords));

        //System.out.println("No match")

        for (int i = 0; i < symbols.length; i++) {
            mp.put(symbols[i], values[i]);
        }
    }

    static void printError(int row, int column){

        System.out.println(">>>Error_lexico(linea:" + row + ",posicion:" + (column + 1) + ")");

    }

    public static void main(String[] args) {
        init();
        try {

            File file =
                    new File("test2.txt");
            Scanner sc = new Scanner(file);

            String line;

            int row = 1;
            int column = 0;
            int start = 0;
            while (sc.hasNextLine()) {
                char st='1';
                line = sc.nextLine() ;
                line = line + '\n';
                for(int i = 0; i < line.length(); i++){
                    column = i + 1;
                    String character = Character.toString(line.charAt(i));
                    switch (st){
                        case '1':

                            if(line.charAt(i) == '\n'){
                                column--;
                                break;
                            }
                            start = i;
                            if(line.charAt(i) == '#'){
                                i = line.length();
                                break;
                            }
                            else if(line.charAt(i) == ' ' || Pattern.matches("[\t]", character)) {
                                break;

                            }else if(Pattern.matches("[a-zA-Z]", character)) {
                                st = '2';
                                break;
                            }else if(line.charAt(i) >='0' && line.charAt(i) <= '9'){
                                st = '3';
                                break;
                            }else if(line.charAt(i) == '"') {
                                st = '4';
                                break;

                            }else if(line.charAt(i) == ':') {
                                st = '6';
                                break;
                            }else if(line.charAt(i) == '<' || line.charAt(i) == '>' //caracteres que pueden ir solos o acompañados
                                    || line.charAt(i) == '-' || line.charAt(i) == '+' || line.charAt(i) == '='
                                    || line.charAt(i) == '|' || line.charAt(i) == '*' || line.charAt(i) == '/'
                                    || line.charAt(i) == '&' || line.charAt(i) == '%'){
                                st = '5';
                                break;

                            }else if(line.charAt(i) == '!'){//caracteres que no pueden ir solos
                                st = '7';
                                break;

                            }else if (line.charAt(i) == '['){
                                st = '8';
                                break;
                            }else if(mp.containsKey(String.valueOf(line.charAt(i)))){//se toman los solos
                                Token t = new Token(2, String.valueOf(line.charAt(i)), row, start + 1);
                                t.printTok();
                                break;
                            }else{
                                printError(row, start);
                                System.exit(0);
                            }
                        case '2':
                            if(line.charAt(i) == '#'){
                                Token t = new Token(1, line.substring(start , i), row, start + 1);
                                t.printTok();
                                i = line.length();
                                break;
                            }else if(line.charAt(i) >='0' && line.charAt(i) <= '9'){
                                break;
                            }else if(Pattern.matches("[a-zA-Z_]", character)) {
                                break;
                            }else if(line.charAt(i) == '-') {//variable resta
                                Token t = new Token(1, line.substring(start , i), row, start + 1);
                                Token t2 = new Token(2, String.valueOf(line.charAt(i)), row, start + 2);
                                t.printTok();
                                t2.printTok();
                                st = '1';
                                break;

                            }else{
                                Token t = new Token(1, line.substring(start , i), row, start + 1);
                                t.printTok();
                                st = '1';
                                i--;
                                break;
                            }
                        case '3':
                            if(line.charAt(i) == '#') {
                                Token t = new Token(3, line.substring(start, i), row, start + 1);
                                t.printTok();
                                i = line.length();
                                break;
                            }else if(line.charAt(i) >='0' && line.charAt(i) <= '9'){
                                break;

                            }else if(line.charAt(i) == '.'){//90.88.00
                                //System.out.println(line.charAt(i));
                                //System.out.println(line.substring(start, i));
                                if(line.substring(start, i).contains(".")) {
                                    Token t = new Token(3, line.substring(start , i), row, start + 1);
                                    t.printTok();
                                    i--;
                                    st = '1';
                                    break;
                                }
                                break;

                            }else if(line.charAt(i) == '-') {//num resta
                                Token t = new Token(3, line.substring(start , i), row, start + 1);
                                Token t2 = new Token(2, String.valueOf(line.charAt(i)), row, start + 1);
                                t.printTok();
                                t2.printTok();
                                st = '1';
                                break;

                            }
                            else{
                                Token t = new Token(3, line.substring(start , i), row, start + 1);
                                t.printTok();
                                st = '1';
                                i--;
                                break;
                            }
                        case '4':
                            if(line.charAt(i) == '\n'){
                                column = start + 1;
                                printError(row, start);
                                System.exit(0);
                            }else if(line.charAt(i) == '"'){
                                Token t = new Token(4, line.substring(start , i + 1), row, start + 1);
                                t.printTok();
                                st = '1';
                                break;
                            }else{
                                break;
                            }
                        case '5':
                            if(line.charAt(i) == '#') {
                                if(mp.containsKey(line.substring(start, i))) {
                                    Token t = new Token(2, line.substring(start, i), row, start + 1);
                                    t.printTok();
                                }else{
                                    printError(row, start);
                                    System.exit(0);
                                }
                                i = line.length();
                                break;

                            }else if (line.charAt(i - 1) == '-' && (line.charAt(i) >='0' && line.charAt(i) <= '9')){ // negative number
                                st = '3';
                                break;
                            }else if(line.charAt(i) == '='){
                                Token t = new Token(2, line.substring(start, i + 1), row, start + 1);
                                t.printTok();
                                break;
                            }else if(line.charAt(i -1) == '-' && line.charAt(i) == '>') {// tk_ejecuta
                                Token t = new Token(2, line.substring(start, i + 1), row, start + 1);
                                t.printTok();
                                st = '1';
                                break;
                            }else if(line.charAt(i) == '>'&& line.charAt(i - 1) == '>' || (line.charAt(i) == '<'&& line.charAt(i - 1) == '<')
                                    || (line.charAt(i) == '|' && line.charAt(i - 1) == '|') || (line.charAt(i) == '*' && line.charAt(i - 1) == '*')) {//right shift
                                if (line.charAt(i + 1) == ':' && line.charAt(i + 2) == '=') {

                                    Token t = new Token(2, line.substring(start, i + 3), row, start + 1);
                                    t.printTok();
                                    i += 2;
                                    st = '1';
                                    break;
                                } else {//prints concat, shifts and exp
                                    Token t = new Token(2, line.substring(start, i + 1), row, start + 1);
                                    t.printTok();
                                    st = '1';
                                    break;
                                }
                            }else if(line.charAt(i) == ':' && line.charAt(i + 1) == '='){
                                Token t = new Token(2, line.substring(start, i + 2), row, start + 1);
                                t.printTok();
                                i++;
                                st = '1';
                                break;
                            }else{
                                if(mp.containsKey(line.substring(start, i))){
                                    Token t = new Token(2, line.substring(start, i), row, start + 1);
                                    t.printTok();
                                    st='1';
                                    i--;
                                    break;
                                }else{
                                    column--;
                                    printError(row, start);
                                    System.exit(0);
                                }

                            }
                        case '6':
                            if(line.charAt(i) == '='){
                                break;
                            }else if(line.charAt(i) == ':' && line.charAt(i - 1) == '=') {// :=: found
                                Token t = new Token(2, line.substring(start, i + 1), row, start + 1);
                                t.printTok();
                                st = '1';
                                break;
                            }else if ((line.charAt(i - 1) == '*' && line.charAt(i - 1) == '=')){
                                Token t = new Token(2, line.substring(start, i + 1), row, start + 1);
                                t.printTok();
                                st = '1';
                                break;
                            }else{
                                Token t = new Token(2, line.substring(start, i), row, start + 1);
                                t.printTok();
                                i--;
                                st = '1';
                                break;
                            }
                        case '7':
                            if(line.charAt(i) == '='){
                                Token t = new Token(2, line.substring(start, i + 1), row, start + 1);
                                t.printTok();
                                st = '1';
                                break;
                            }else{
                                column--;
                                printError(row, start);
                                System.exit(0);
                            }
                        case '8':
                            if(line.charAt(i) == ']'){
                                Token t = new Token(2, line.substring(start, i + 1), row, start + 1);
                                t.printTok();
                                st = '1';
                                break;
                            }else{
                                Token t = new Token(2, line.substring(start, i), row, start + 1);
                                t.printTok();
                                i--;
                                st = '1';
                                break;
                            }

                    }
                }
                row++;

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}