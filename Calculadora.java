package calculadora;

import pila.PilaA;

/**
 * 
 * @author Felipe, Toño, Diana y Jorge
 */
public class Calculadora {
    
    /**<pre>
     * Revisa que la sintaxis de la expresión escrita en la calculadora sea correcta.
     * 
     * @param infijo: utiliza como parámetro la expresión ingresada por el usuario.
     * @return 
     * Regresará
     * <ul>
     * <li> true: si no existen errores en la sintaxis </li>
     * <li> false: existe algún error en la sintaxis </li>
     * </ul>
     * </pre>
     */
    public static boolean revisaTodo(String infijo){
        boolean res = true;
        boolean puntos = false;
        PilaA<Character> pila = new PilaA(); //Pila utilizada como auxiliar para contener los paréntesis abiertos.
        char car = infijo.charAt(0), next, before, ultimo = infijo.charAt(infijo.length()-1); //Se checa el primer y el último caracter del String porque solo se llama al método si el String no está vacío.
        int i = 0;
        
        //Este if marca error si el primer caracter es es simbolo de multiplicación o división
        if(car == '*' || car == '/' || car == ')')
            res = false;
        //Que el primer caracter sea el símbolo de más o de menos es posible y por eso
        //simplemente aumentamos la i.
        if(car == '+' || car == '-')
            i++;
        //Si el último caracter es un operador o un punto se marca error ya que es incongruente.
        if(ultimo == '*' || ultimo == '/' || ultimo == '+' || ultimo == '-' || ultimo == '.'){
            res = false;
        }
        //Ciclo para recorrer la cadena infija caracter por caracter.
        while(i < infijo.length() && res){
            car = infijo.charAt(i);
            switch (car) {
                case '(':
                    pila.push(car);
                    //Si hay un paréntesis abierto se agrega a la pila.
                    if(i < infijo.length()-1){
                        next = infijo.charAt(i+1);
                        if(next == '*' || next == '/' || next == ')')
                            res = false;
                        if(i > 0 && res){
                            before = infijo.charAt(i-1);
                            if(before != '*' && before != '+' && before != '-' && before != '/' && before != '(')
                                res = false;
                        }
                    }   break;
                case ')':
                    before = infijo.charAt(i-1);
                    if(pila.isEmpty())
                        res = false; //Si se encuentra un paréntesis para cerrar y no hay uno abierto en la pila, es porque están incorrectos; es incorrecta la expresión.
                    else if(before == '*' || before == '+' || before == '-' || before == '/' || before == '(')
                        res = false;
                    else{
                        pila.pop();
                        //Se elimina de la pila el paréntesis que se cerró.
                        if(i < infijo.length()-1){
                            next = infijo.charAt(i+1);
                            if(next != '*' && next != '+' && next != '-' && next != '/' && next != ')')
                                //No puede haber un dígito o punto después de un paréntesis ya que
                                //no aceptamos los paréntesis como multiplicadores.
                                res = false;
                        }
                    }
                    break;
                default:
                    if(car == '*' || car == '/'){
                        next = infijo.charAt(i+1);
                        if(next == '*' || next == '/' || next == '+' || next == '-')
                            //No se puede tener un operador y después el símbolo de 
                            //multiplicación o división; es un error.
                            res = false;
                    }
                    //Este if funciona para mandar error si hay dos puntos en un mismo número.
                    //Si el boolean puntos es true es porque ya hay un punto y el número no ha terminado.
                    if(puntos && car == '.')
                        res = false;
                    if(car == '.' && res){
                        puntos = true;
                        //Si encuentra un punto el boolean "puntos" se vuelve true.
                        next = infijo.charAt(i+1);
                        if(!Character.isDigit(next)){
                            if(!Character.isDigit(next)){
                                if(i>0){
                                    before=infijo.charAt(i-1);
                                    if(!Character.isDigit(before)){
                                        res=false;
                                        //Si un punto no tiene un número antes ni después está incorrecto.
                                    }
                                }
                            }
                        }
                    }
                    else
                        if(car == '+' || car == '-' || car == '*' || car == '/'){
                            //Cada que encuentra un operador regresa el boolean "puntos" a false
                            //porque ya terminó el número.
                            puntos = false;
                        }
                    break;
            }
            i++;
        }
        //Si la pila no esta vacía significa que un paréntesis no se cerró por lo que está incorrecto.
        if(!pila.isEmpty())
            res = false;
        
        return res;
    }
    
    /**
     * <pre>
     * Utilizamos este método para reducir signos positivos y negativos.
     * 
     * @param oper: Toma como parámetro la expresión ingresada por el usuario en caso de que esta haya sido correcta.
     * @return Regresa la una operación equivalente con menor uso de signos.
     * Ejemplo:
     *      Si tenemos 
     *          "----7"
     *      Este método lo convertirá a
     *          "+7"
     * </pre>
     */
    public static String eliminarExcesos(String oper) {
        int i = 0;
        int menos = 0;
        int j;
        
        for(i = 0; i < oper.length(); i++){
            if(oper.charAt(i) == '+' || oper.charAt(i) == '-'){
                j = i;
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
     * Este método se utiliza para convertir la expresión que ingresó el usuario a una equivalente que la computadora pueda evaluar.
     * @param String -- Recibe como parámetro la expresión ingresada por el usuario después de ser revisada y redicida por el método corrigeExpresion().
     * @return String -- Regresa la expresión recibida en su forma postfija.
     */
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

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println(revisaTodo("3.+5"));
        System.out.println(resolver("5.*6"));
    }
    
}
