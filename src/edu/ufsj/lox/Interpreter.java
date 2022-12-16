package edu.ufsj.lox;
import java.text.DecimalFormat;

public class Interpreter implements Expr.Visitor<Object>{

    public void interpret(Expr expression){
        try{
            Object value = evaluate(expression);
            System.out.println(stringify(value));
        }catch (RuntimeError error){
            Lox.runtimeError(error);
        }
    }

    private String stringify(Object obj) {
        if (obj == null ) return "nil";
        if (obj instanceof Double) {
            String txt = obj.toString();
            if (txt.endsWith(".0")){
                txt = txt.substring(0,txt.length()-2);
            }
            return txt;
        }
        return obj.toString();
    }
    @Override
    public Object visitBinaryExpr(Expr.Binary expr) {
        Object left = evaluate(expr.left);
        Object right = evaluate(expr.right);

        switch (expr.operator.type){
            case MINUS -> {
                checkNumberOperands(expr.operator,left,right);
                return (double)left - (double)right;}
            case SLASH -> {
                checkNumberOperands(expr.operator,left,right);
                return (double)left / (double)right;}
            case STAR ->  {
                checkNumberOperands(expr.operator,left,right);
                return (double)left * (double)right;}
            case PLUS ->{
                if (left instanceof Double && right instanceof Double){
                    return (double)left + (double) right;
                }if (left instanceof String && right instanceof String){
                    return (String)left + (String) right;
                }if (left instanceof String && right instanceof Double){
                    return (String)left + DecimalFormat.getNumberInstance().format(right);
                }if (left instanceof Double && right instanceof String){
                    return DecimalFormat.getNumberInstance().format(left)+ (String)right;
                }
                throw new RuntimeError(expr.operator,"Operands must be two numbers or two strings");
            }

            case GREATER -> {
                checkNumberOperands(expr.operator,left,right);
                return (double) left > (double) right;
            }
            case GREATER_EQUAL -> {
                checkNumberOperands(expr.operator,left,right);
                return (double) left >= (double) right;
            }

            case LESS -> {
                checkNumberOperands(expr.operator,left,right);
                return (double) left < (double) right;
            }

            case LESS_EQUAL -> {
                checkNumberOperands(expr.operator,left,right);
                return (double) left <= (double) right;
            }

            case BANG_EQUAL -> {return !isEqual(left,right);}
            case EQUAL_EQUAL -> {return isEqual(left,right);}


        }
        return null;
    }

    private void checkNumberOperands(Token operator,Object left, Object right){
        if(left instanceof Double && right instanceof Double) return;
        throw new RuntimeError(operator, "Operands must be numbers");
    }

    @Override
    public Object visitGroupingExpr(Expr.Grouping expr) {
        return evaluate(expr.expression);
    }

    @Override
    public Object visitUnaryExpr(Expr.Unary expr) {
        Object right = evaluate(expr.right);

        switch(expr.operator.type) {
            case MINUS -> {
                checkNumberOperand(expr.operator,right);
                return -(double)right;}
            case BANG -> {return !isTruthy(right);}
            default -> {return null;}
        }
    }
    private void checkNumberOperand(Token operator, Object operand) {
        if (operand instanceof Double) return;
        throw new RuntimeError(operator,"Operand must be a number");
    }

    @Override
    public Object visitLiteralExpr(Expr.Literal expr) {
        return expr.value;
    }

    @Override
    public Object visitTernaryExpr(Expr.Ternary expr) {
        Expr ex = expr.expr;
        ex = (Expr.Binary) ex;
        Object left = ((Expr.Binary) ex).left;
        Object right = ((Expr.Binary) ex).left;
        boolean accept = false;
        switch(((Expr.Binary) ex).operator.type){

            case GREATER -> {
                accept = (double) left > (double) right;
            }
            case GREATER_EQUAL -> {
                accept = (double) left >= (double) right;
            }

            case LESS -> {
                accept = (double) left < (double) right;
            }

            case LESS_EQUAL -> {
                accept = (double) left <= (double) right;
            }
            case BANG_EQUAL -> {accept =  !isEqual(left,right);}
            case EQUAL_EQUAL -> {accept = isEqual(left,right);}

        }
        if(accept == true){
            System.out.println("ESQ");
            return left;
        }else{
            System.out.println("DIR");
            return right;

        }

    }

    private Object evaluate(Expr expr) {
        return expr.accept(this);
    }

    private Boolean isTruthy(Object obj) {
        if (obj == null) return false;
        if (obj instanceof Boolean) return (Boolean) obj;
        return true;
    }

    private boolean isEqual(Object a, Object b){
        if (a == null && b == null) return true;
        if (a == null ) return false;

        return a.equals(b);
    }
}
