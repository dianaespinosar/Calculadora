package calculadora;

import pila.PilaA;

/**
 * 40*9+89+-54
 * 40 9 * 89 + -54 + 
 * @author Felipe
 */
public class Calculadora {
    
    public static String convertirPostfijo(String exp){
        StringBuilder str = new StringBuilder();
        PilaA<Character> pila = new PilaA();
        int i = 0;
        int j;
        while (i < exp.length()){
            j = 0;
            switch (exp.charAt(i)) {
                
                case '+':
                case '-':
                    if(i == 0)
                        str.append(exp.charAt(i));
                    else if(exp.charAt(i-1) == '*' || exp.charAt(i-1) == '/' || exp.charAt(i-1) == '(')
                        str.append(exp.charAt(i));
                    else {
                        while (!pila.isEmpty() && pila.peek() != '('){
                            str.append(pila.pop());
                            str.append(" ");
                        }
                        pila.push(exp.charAt(i));
                    }
                    break;
                    
                case '/':
                case '*':
                    pila.push(exp.charAt(i));
                    break;
                    
                case '(':
                    pila.push(exp.charAt(i));
                    break;
                    
                case ')':
                    while(!pila.isEmpty() && pila.peek() != '('){
                        str.append(pila.pop());
                        str.append(" ");
                    }
                    pila.pop();
                    break;
                    
                default:
                    while ((i+j) < exp.length() &&
                            exp.charAt(i+j) != '(' &&
                            exp.charAt(i+j) != ')' &&
                            exp.charAt(i+j) != '*' &&
                            exp.charAt(i+j) != '/' &&
                            exp.charAt(i+j) != '+' &&
                            exp.charAt(i+j) != '-'){
                        j++;
                    }
                    str.append(exp.substring(i, i+j));
                    str.append(" ");
                    i += j-1;
                    break;
            }
            i++;
        }
        
        while (!pila.isEmpty()){
            str.append(pila.pop());
            str.append(" ");
        }
        return str.toString();
    }
    
    public static <T> double evaluarPostfijo(String postfijo){
        double num1;
        double num2;
        PilaA<Double> pila = new PilaA();
        String[] aux = postfijo.split(" ");
        
        for (String aux1 : aux) {
            try {
                double ope = Double.parseDouble(aux1);
                pila.push(ope);
            } catch (NumberFormatException e) {
                num1 = pila.pop();
                num2 = pila.pop();
                switch (aux1) {
                    case "+":
                        num2 += num1;
                        pila.push(num2);
                        break;
                    case "-":
                        num2 -= num1;
                        pila.push(num2);
                        break;
                    case "*":
                        num2 *= num1;
                        pila.push(num2);
                        break;
                    case "/":
                        num2 /= num1;
                        pila.push(num2);
                        break;
                }
            }
        }
        return pila.peek();
    }
    
    public static double resolver (String exp){
        String postfijo = convertirPostfijo(exp);
        return evaluarPostfijo(postfijo);
    }
    
    public static String eliminarExcesos(String oper) {
        String aux;
        int i = 0;
        int menos = 0;
        int j;
        
        
        for(i = 0; i < oper.length(); i++){
            if(oper.charAt(i) == '+' || oper.charAt(i) == '-'){
                j = i + 1;
                while (oper.charAt(j) == '-' || oper.charAt(j) == '+'){
                    if (oper.charAt(j) == '-')
                        
                        menos++;
                    j++;
                }
                if (menos % 2 == 0){
                    oper = oper.substring(0, i) + "+" + oper.substring(j);
                }
                else
                    oper = oper.substring(0, i) + "-" + oper.substring(j);
            }
            
        }
        
        return oper;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        System.out.println(resolver("5+(-7*8/2)"));
    }
    
}