import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.text.*;

public class gui extends JPanel implements ActionListener
{
    public gui() {
        super();
        input = new JTextField(20);
        output = new JTextField(20);
        result = new JTextField(20);


        tree = new JTextPane();

        MutableAttributeSet set = new SimpleAttributeSet();
        StyleConstants.setLineSpacing(set, -.2f);
        tree.setParagraphAttributes(set, false);

        //Making font monospaced
        tree.setFont(new Font("Monospaced", Font.PLAIN, 15));

        translate = new JButton("translate");
        evaluate = new JButton("evaluate");
        translate.addActionListener(this);
        evaluate.addActionListener(this);

        String[] data = {"Infix", "Postfix"};
        types = new JComboBox<String>( data );

        JPanel translator = new JPanel();
        translator.setLayout(new GridLayout(2, 2, 10, 10));
        translator.add(types).setLocation(0, 0);
        translator.add(input).setLocation(1, 0);
        translator.add(translate).setLocation(0, 0);
        translator.add(output).setLocation(1, 0);

        JPanel evaluator = new JPanel();
        evaluator.add(evaluate);
        evaluator.add(result);

        BoxLayout boxlayout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(boxlayout);

        add(translator);
        add(evaluator);
        add(tree);

        infix = new Infix();
        postfix = new Postfix();
    }


    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        if (cmd.equals("translate") || cmd.equals("evaluate")) {
            String type = (String)types.getSelectedItem();

            if (type.equals("Infix")) {
                try {
                    infix = new Infix(input.getText());
                    postfix = infix.getPostfix();
                    output.setText(postfix.toString());
                }
                catch (RuntimeException exp) {
                    //System.out.println(exp);
                }
            }
            else if (type.equals("Postfix")) {
                try {
                    postfix = new Postfix(input.getText());
                    infix = postfix.getInfix();
                    output.setText(infix.toString());
                }
                catch (RuntimeException exp) {}
            }

            tree.setText(infix.toTree());

        }

        if (cmd.equals("evaluate")) {
            try { result.setText(postfix.evaluate().toString()); }
            catch (RuntimeException exp) {}
        }
    }


    private JTextField input, output;
    private JComboBox<String> types;
    private JButton translate;

    private JButton evaluate;
    private JTextField result;

    private Infix infix;
    private Postfix postfix;

    private JTextPane tree;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Polish Notation Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);

        gui g = new gui();

        frame.add(g);

        frame.setVisible(true);
    }
}
