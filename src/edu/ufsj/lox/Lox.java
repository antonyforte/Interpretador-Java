package edu.ufsj.lox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Lox {
    static boolean hadError = false;
    public static void main(String args[]) throws IOException { //LE OS ARGUMENTOS NA ENTRADA
        if (args.length > 1 ) { // SE TIVER MAIS DE UM ARGUMENTO PRINTA UM ERRO
            System.err.println("Usage: jlox [script]");
            System.exit(100);
        } else if (args.length == 1) { // SE TIVER UM ARGUMENTO, RODA O SCRIPT
            runFile(args[0]);
        } else { // SE NAO TIVER ARGUMENTOS, RODA O TERMINAL DO INTERPRETADOR
            runPrompt();
        }
    }

    private static void runFile(String path) throws IOException { //FUNCAO QUE RODA UM SCRIPT
        byte[] bytes = Files.readAllBytes(Paths.get(path)); // LE OS BYTES DE TAL SCRIPT
        run(new String(bytes,Charset.defaultCharset()));  // RODA ESSE SCRIPT

        if (hadError) System.exit(101); // TESTAR SE TEVE ALGUM ERRO
    }

    private static void runPrompt () throws IOException { //FUNCAO QUE ENTRA EM MODO TERMINAL DO INTERPRETADOR
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        for(;;) { //LOOP INFINITO DO TERMINAL DO INTERPRETADOR
            System.out.print("> ");
            String line = reader.readLine(); // STRING QUE LE O QUE O USUARIO DIGITOU
            if (line == null) break; // QUANDO O USUARIO APERTA CTRL D, LINE RECEBERA NULL O QUE ENCERRARA O PROGRAMA
            run(line);
            hadError = false; //TESTAR SE O SISTEMA TEVE ERRO
        }
    }

    private static void run(String source) { //FUNCAO QUE SCANEIA A STRING E A SEPARA EM TOKENS
        ScannerI scanner = new ScannerI(source); // NOVO SCANNER
        List<Token> tokens = scanner.scanTokens(); //SCANEIA UMA LISTA DE TOKENS

        for (Token token : tokens) {
            System.out.println(token);
        }
    }

    static void error(int line, String message){
        report(line,"",message);
    }
    private static void report(int line, String where, String message){
        System.err.println("[line " + line + "] Error " + where + " : " + message);
        hadError = true;
    }
}
