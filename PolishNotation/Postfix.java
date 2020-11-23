import java.util.LinkedList;

/**
 * Write a description of class Postfix here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Postfix
{
    Postfix() {
      postfix = new LinkedList<Pair<LinkedList<Operand>, LinkedList<Operator>>>();
      postfix.push(new Pair<LinkedList<Operand>, LinkedList<Operator>>(new LinkedList<Operand>(), new LinkedList<Operator>()));
    }

    Postfix(String exp) {
      this();

      int i = 0;
      while (i < exp.length()) {
        char c = exp.charAt(i);
        Operator operator = Operator.get(c);

        if (operator != null || c == ' ') {
          if(i > 0) { add(new Operand(exp.substring(0, i))); }
          if (operator != null) { add(operator); }
          exp = exp.substring(i + 1);
          i = -1;
        } i++;
      }
    }

    public void add(Operator operator) { postfix.getLast().getRight().add(operator); }
    public void add(Operand operand) {
      if ( postfix.getLast().getRight().size() > 0) { postfix.add(new Pair<LinkedList<Operand>, LinkedList<Operator>>(new LinkedList<Operand>(), new LinkedList<Operator>())); }
      postfix.getLast().getLeft().add(operand);
    }
    public void add(Postfix p) {
      for (Pair<LinkedList<Operand>, LinkedList<Operator>> unit: p.postfix) {
        for(Operand operand: unit.getLeft()) { add(operand); }
        for(Operator operator: unit.getRight()) { add(operator); }
      }
    }
    
    public Infix getInfix() {
      Stack<Infix> expressions = new Stack<Infix>();
        
        for (Pair<LinkedList<Operand>, LinkedList<Operator>> unit: postfix) {
        for(Operand operand: unit.getLeft()) { expressions.push(new Infix(operand)); }
        for(Operator operator: unit.getRight()) {
          Infix rhs = expressions.pop();
          Infix lhs = expressions.pop();
          expressions.push(new Infix(lhs, operator, rhs));
        }
      }
      
      return expressions.pop();
    }

    public Operand evaluate() {
      Stack<Operand> operands = new Stack<Operand>();

      for (Pair<LinkedList<Operand>, LinkedList<Operator>> unit: postfix) {
        for(Operand operand: unit.getLeft()) { operands.push(operand); }
        for(Operator operator: unit.getRight()) {
          Operand rhs = operands.pop();
          Operand lhs = operands.pop();
          operands.push(operator.calc(lhs, rhs));
        }
      }

      return operands.pop();
    }

    public String toString() {
      String rep = "";
      for (Pair<LinkedList<Operand>, LinkedList<Operator>> unit: postfix) {
        for(Operand operand: unit.getLeft()) { rep += operand.toString() + " "; }
        for(Operator operator: unit.getRight()) { rep += operator.toString() + " "; }
      } return rep;
    }

    private LinkedList<Pair<LinkedList<Operand>, LinkedList<Operator>>> postfix;

    public static void main(String[] args) {
      Infix test = new Infix("1 + 2 * 3 ^ 7 - 9 + ( 6- 2 )+ (3 ^ 2)");
      System.out.println(test.evaluate());
      System.out.println(test.getPostfix());
    }
}
