import javafx.util.Pair;
import java.util.LinkedList;

public enum Operator
{
    ADD('+', 0, (Operand a, Operand b) -> Operand.add(a, b)),
    SUB('-', 1, (Operand a, Operand b) -> Operand.sub(a, b)),
    MULT('*', 2, (Operand a, Operand b) -> Operand.mult(a, b)),
    DIV('/', 3, (Operand a, Operand b) -> Operand.div(a, b)),
    POWER('^', 4, (Operand a, Operand b) -> Operand.pow(a, b));

    public static Operator get(char r) {
        for (Operator operator : Operator.values()) {
            if(operator.rep == r) { return operator; }
        }

        return null;
    }

    public Operand calc(Operand a, Operand b) { return operator.operate(a, b); }

    public String toString() { return Character.toString(rep); }

    public static Pair<LinkedList<String>, LinkedList<Operator>> split(String exp) {
      exp = exp.replaceAll("\\s", "");
      LinkedList<String> operands = new LinkedList<String>();
      LinkedList<Operator> operators = new LinkedList<Operator>();

      int i = 0;
      while ( i < exp.length() ) {
        char c = exp.charAt(i);
        Operator operator = Operator.get(c);

        if (operator != null) {
          String operand = exp.substring(0, i);
          exp = ( i < exp.length() - 1) ? exp.substring(i + 1, exp.length()) : "";
          operands.add(operand);
          operators.add(operator);
          i = 0;
        }
        i++;
      }
      if(exp.length() > 0) { operands.add(exp); }

      return new Pair<LinkedList<String>, LinkedList<Operator>>(operands, operators);
    }

    private Operator(char r, int p, Operation op) {
        rep = r; priority = p; operator = op;
    }

    private char rep;
    public final int priority;
    private Operation operator;

    private interface Operation {
      Operand operate(Operand a, Operand b);
    }
}
