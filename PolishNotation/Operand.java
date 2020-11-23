
public class Operand
{
    public Operand(String val) { constant = Double.parseDouble(val); }
    public Operand(double val) { constant = val; }

    public static Operand add(Operand a, Operand b) { return new Operand(a.constant + b.constant); }
    public static Operand sub(Operand a, Operand b) { return new Operand(a.constant - b.constant); }
    public static Operand mult(Operand a, Operand b) { return new Operand(a.constant * b.constant); }
    public static Operand div(Operand a, Operand b) { return new Operand(a.constant / b.constant); }
    public static Operand pow(Operand a, Operand b) { return new Operand(Math.pow(a.constant, b.constant)); }

    public String toString() {
      return String.valueOf(constant);
    }

    private double constant;
}
