package edu.ufsj.tool;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

public class GenerateAst {
    public static void main(String args[]) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: java edu.ufsj.tool.GenerateAst" + " <output dir>");

            System.exit(64);
        }
        String outputDir = args[0];
        defineAst(outputDir, "Expr", Arrays.asList(
                "Binary    : Expr left, Token operator, Expr right",
                "Grouping  : Expr expression",
                "Unary     : Token operator, Expr right",
                "Literal   : Object value"));
    }

    private static void defineAst(String outputDir,String baseName, List<String> types) throws FileNotFoundException, UnsupportedEncodingException {
        String path = outputDir + "/" + baseName + ".java";
        PrintWriter writer = new PrintWriter(path,"UTF-8");
        writer.println("package edu.ufsj.tool");
        writer.println();
        writer.println("import java.util.List");
        writer.println();
        writer.println("class " + baseName + " {");

        for (String type : types) {
            String className = type.split(":")[0].trim();
            String fields = type.split(":")[1].trim();
            defineType(writer, baseName, className, fields);
        }

        writer.println("}");
        writer.close();
    }

    private static void defineType(PrintWriter writer, String baseName, String className, String fieldList) {
        writer.println("\tstatic class " + className + " extends " + baseName + " {");
        //construtor
        writer.println("\t\t" + className + "(" + fieldList + ") {");

        //inicializa parametros
        String[] fields = fieldList.split(", ");
        for (String field : fields) {
            String name = field.split(" ")[1];
            writer.println("\t\t\tthis." + name + " = " + name + ";");
        }
        writer.println("\t\t}");

        writer.println();
        for (String field : fields) {
            writer.println("\tfinal " + field + ";");
        }
        writer.println("\t}");//fecha def subclasss
        writer.println();

    }
}