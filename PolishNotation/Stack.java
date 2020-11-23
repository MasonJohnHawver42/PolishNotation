import java.util.LinkedList;

/**
 * Write a description of class Stack here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
class Stack<T>
{
    public Stack() { stack = new LinkedList(); }
    
    public T peek() { return stack.getFirst(); }
    public T pop() { return stack.removeFirst(); }
    
    public void push(T data) { stack.addFirst(data); }
    
    public boolean empty() { return stack.size() == 0; }
    
    public String toString() { return stack.toString(); }
    
    private LinkedList<T> stack;
}
