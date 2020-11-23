import java.util.LinkedList;
import java.util.Arrays;

/**
 * Write a description of class Infix here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
class Infix
{
    public Infix() {}
    public Infix(String exp) {
      init(exp);
    }

    public Postfix getPostfix() {
      Postfix postfix = new Postfix();
      if (!this.isOperand()) {

          postfix.add(lhs.getPostfix());
          postfix.add(rhs.getPostfix());
          postfix.add(operator);

      } else { postfix.add(lhs.operand); }
      return postfix;
    }

    public Operand evaluate() {
      if (this.isOperand()) { return lhs.operand; }

      Operand lhv = lhs.evaluate();
      Operand rhv = rhs.evaluate();

      return operator.calc(lhv, rhv);
    }

    public String toString() {
      if (this.isOperand()) { return lhs.operand.toString(); }
      return lhs.toString(operator) + " " + operator.toString() + " " + rhs.toString(operator);
    }

    public String toTree() {
      String[] lht = lhs.toTree().split("\n");
      String[] rht = rhs.toTree().split("\n");

      int lhl = lht[0].length();
      int rhl = rht[0].length();

      int lhc = 0;
      int rhc = 0;
      while (lhc < lhl && lht[0].charAt(lhc) == ' ') {lhc++;}
      while (rhc < rhl && rht[0].charAt(rhc) == ' ') {rhc++;}
      if(lhc == 0) { lhc = lhl / 2; }
      if(rhc == 0) { rhc = rhl / 2; }

      String tree = repeat(' ', lhl + 1) + operator.toString() + repeat(' ', rhl + 1) + "\n";
      tree +=       repeat(' ', lhc + 1) + repeat('_', lhl - lhc) + "|" + repeat('_', rhc + 1) + repeat(' ', rhl - rhc) + "\n";
      tree +=       repeat(' ', lhc) + "|" + repeat(' ', lhl - lhc) + " " + repeat(' ', rhc + 1) + "|" + repeat(' ', rhl - rhc - 1) + "\n";

      int i = 0;
      while (i < lht.length || i < rht.length) {
        if ( i >= lht.length) { tree += repeat(' ', lhl); }
        else { tree += lht[i]; }
        tree += "   ";
        if ( i >= rht.length) { tree += repeat(' ', rhl); }
        else { tree += rht[i]; }
        tree += "\n";
        i++;
      }

      return tree;
    }

    private Value lhs;
    private Operator operator;
    private Value rhs;

    public static void main(String[] args) {
      Infix in = new Infix("1 + 2 * 3 ^ 7 * 9 ^ ( 6 - 8 ) + 10");
      System.out.println(in.toTree());
    }

    //---------------
    //---------------
    //--------------- The ugly non recursive code is bellow
    //---------------
    //---------------


    public Infix(Infix lhv, Operator op, Infix rhv) {
        lhs = new Value(lhv);
        operator = op;
        rhs = new Value(rhv);
    }

    public Infix(Operand operand) {
        lhs = new Value(operand);
        operator = null;
        rhs = null;
    }
    public boolean isOperand() { return operator == null && rhs == null && lhs != null; }

    private class Value {

      public Value(Operand op) {
        operand = op;
        expression = null;
      }
      public Value(String v) { set(v); }
      public Value(Infix exp) {
        if(!exp.isOperand()) {
         expression =  exp;
         operand = null;
        }
        else {
            expression = null;
            operand = exp.lhs.operand;
        }
      }
      public Value(LinkedList<String> operands, LinkedList<Operator> operators) {
          if(operators.size() == 0) { set(operands.get(0)); }
          else {
            Infix exp = new Infix();
            exp.init(operands, operators);
            expression = exp;
            operand = null;
          }
      }

      public Postfix getPostfix() {
        if (terminal()) { Postfix p = new Postfix(); p.add(operand); return p; }
        else { return expression.getPostfix(); }
      }

      public Operand evaluate() {
        if (terminal()) { return operand; }
        else { return expression.evaluate(); }
      }

      public String toTree() {
        if (operand != null) { return operand.toString(); }
        else { return expression.toTree(); }
      }

      public String toString( Operator parent ) {
        if (operand != null) { return operand.toString(); }
        else if (expression.operator.priority <= parent.priority ) { return "( " + expression.toString() + " )"; }
        else { return expression.toString(); }
      }

      private void set(String value) {
        if (value.length() > 0) {
          if (value.charAt(0) == '(') {
            expression = new Infix(value.substring(1, value.length() - 1));
            operand = null;
            return;
          }
          else {
            operand = new Operand(value);
            expression = null;
            return;
          }
        } throw new RuntimeException("Invalid Operand!");
      }

      public boolean terminal() { return operand != null; }

      private Operand operand;
      private Infix expression;
    }

    public void init(LinkedList<String> operands, LinkedList<Operator> operators) {
      if (operators.size() + 1 != operands.size() ) { throw new RuntimeException("Invalid Infix"); }
      if (operators.size() == 0) { lhs = new Value(new Operand(operands.get(0))); operator = null; rhs = null; return; }

      //base case
      if (operators.size() == 1) {
        lhs = new Value(operands.get(0));
        operator = operators.get(0);
        rhs = new Value(operands.get(1));
        return;
      }

      int mp = operators.get(0).priority;
      int pivit = 0;

      for (int i = 0; i < operators.size(); i++) {
        Operator op = operators.get(i);
        if (op.priority <= mp) { mp = op.priority; pivit = i; }
      }

      operator = operators.get(pivit);
      LinkedList<String> lhs_operands = new LinkedList<String>();
      LinkedList<Operator> lhs_operators = new LinkedList<Operator>();
      LinkedList<String> rhs_operands = new LinkedList<String>();
      LinkedList<Operator> rhs_operators = new LinkedList<Operator>();

      for (int i = 0; i <= pivit; i++) {
        lhs_operands.add(operands.get(i));
        if (i != pivit) { lhs_operators.add(operators.get(i)); }
      }

      for (int i = pivit + 1; i < operators.size(); i++) {
        rhs_operands.add(operands.get(i));
        rhs_operators.add(operators.get(i));
      } rhs_operands.add(operands.getLast());

      lhs = new Value(lhs_operands, lhs_operators);
      rhs = new Value(rhs_operands, rhs_operators);

    }

    public void init(String exp) {
      exp = exp.replaceAll("\\s", "");
      LinkedList<String> operands = new LinkedList<String>();
      LinkedList<Operator> operators = new LinkedList<Operator>();

      int lb = 0;
      int rb = 0;

      int i = 0;
      while ( i < exp.length() ) {
        char c = exp.charAt(i);
        Operator operator = Operator.get(c);

        if (operator != null && lb == rb) {
          String operand = exp.substring(0, i);
          exp = ( i < exp.length() - 1) ? exp.substring(i + 1, exp.length()) : "";
          operands.add(operand);
          operators.add(operator);
          i = exp.charAt(0) == '-' ? 0 : -1;
        }

        if (c == '(') { lb++; }
        if (c == ')') { rb++; }

        i++;
      }
      if(exp.length() > 0) { operands.add(exp); }

      if (rb != lb) { throw new RuntimeException("Invalid Parentheses!"); }

      init(operands, operators);
    }

    private static String repeat(char c, int n) {
        char[] arr = new char[n];
        Arrays.fill(arr, c);
        return new String(arr);
    }

}
