import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Stack;

/**
 * Main class that functions as an Expression Tree Calculator by performing operations on a postfix string.
 * Takes infix user input in a GUI and translates it to postfix. It then creates a tree and traverses the tree
 * to determine the value of the user's expression. It finally regenerates the infix expression by traversing
 * the tree recursively.
 */
public class Window extends JFrame implements ActionListener
{
    /**
     * Main function of the class
     */    
    public static void main(String[] args)
    {
        Window w = new Window();
    }

    private JTextField f1 = new JTextField();
    private JTextPane instructions = new JTextPane();
    private JTextArea postExp = new JTextArea("Postfix Expression: N/A");
    private JTextArea graph = new JTextArea("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
    private JTextArea evaluation = new JTextArea("Evaluation of input: ");
    private JTextArea regenInfix = new JTextArea("Regenerated Infix: ");
    private static String infix="";
    private String userinput="";
    private String postfix = "";
    private BinaryNode root = new BinaryNode();

    /*
     * For the precedence table to translate infix expression to postfix expression
     */
    final int POP=0;
    final int PUSH=1;
    final int MATCH=2;
    final int ERROR=3;
    final int DONE=4;

    /**
     * Sets up the GUI and adds action listeners to the GUI components. Creates four buttons for different
     * functions, a section for user input, a text area for the outputted postfix string, an area for the
     * binary tree, an area for the evaluated expression, and finally an area to display the regenerated
     * infix expression.
     */
    public Window()
    {
        super("Expression Tree Calculator");
        this.setSize(800, 800);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        /*
         * Adds buttons and sets listeners to the GUI 
         */
        JPanel buttons = new JPanel(new FlowLayout());
        JButton b1 = new JButton("Translate to Postfix");
        b1.addActionListener(this);
        JButton b2 = new JButton("Generate Tree");
        b2.addActionListener(this);
        JButton b3 = new JButton("Evaluate Expression");
        b3.addActionListener(this);
        JButton b4 = new JButton("Regenerate Infix");
        b4.addActionListener(this);
        buttons.add(b1);
        buttons.add(b2);
        buttons.add(b3);
        buttons.add(b4);

        /*
         * Allows user to type in infix expression onto GUI
         */
        JPanel user = new JPanel(new GridLayout(2,2));
        JTextArea infixExp = new JTextArea("Enter infix expression to evaluate:");
        infixExp.setOpaque(false);
        user.add(infixExp);
        user.add(f1);

        /*
         * Organizes all aspects of GUI in the NORTH BorderLayout
         */
        JPanel top = new JPanel(new GridLayout(0,1));
        instructions.setOpaque(false);
        instructions.setText("");
        instructions.setForeground(Color.red);
        postExp.setOpaque(false);
        top.add(buttons);
        top.add(user);
        top.add(instructions);
        top.add(postExp);

        /*
         * Organizes all aspects of GUI in the SOUTH BorderLayout
         */
        JPanel bottom = new JPanel(new GridLayout(0,1));
        evaluation.setOpaque(false);
        regenInfix.setOpaque(false);
        bottom.add(evaluation);
        bottom.add(regenInfix);

        /*
         * Displays all aspects of GUI into ContentPane, sets visible
         */
        this.getContentPane().add(top, BorderLayout.NORTH);
        this.getContentPane().add(graph, BorderLayout.CENTER);
        this.getContentPane().add(bottom, BorderLayout.SOUTH);
        this.pack();
        this.setVisible(true);
    }

    /**
     * Action listeners for the buttons in the GUI.
     */
    public void actionPerformed(ActionEvent e)
    {   
        String action = e.getActionCommand();
        if (action.equals("Translate to Postfix"))
        {
            instructions.setText("");
            userinput = f1.getText();
            postfix = "";
            createPostfix();
            postExp.setText("Postfix Expression: "+ postfix);
            graph.setText("");
            evaluation.setText("Evaluation of input: ");
            regenInfix.setText("Regenerated Infix: ");
        }
        else if (action.equals("Generate Tree"))
        {
            if (postfix == null || postfix == "")
            {
                instructions.setText("Must generate postfix expression first!");
            }
            else 
            {
                root = null;
                generateTree();
                instructions.setText("");
            }
        }
        else if (action.equals("Evaluate Expression"))
        {
            instructions.setText("");
            evaluateExpression();
        }
        else if (action.equals("Regenerate Infix"))
        {
            regenerateInfix();
        }
    }

    /**
     * Generates a postfix expression from the user's infix expression by printing out the numbers and pushing
     * operators onto a stack. It determines when to print out an operator based on precedence (provided in 
     * table in PostFix class).
     */
    public void createPostfix()
    {
        String temp = userinput;
        temp = temp.replaceAll("\\s",""); //removes spaces
        int i = 0;
        int value = (int)temp.charAt(i);
        for (i = 0; i<temp.length(); i++) //makes sure the user input is valid
        {
            value = (int)temp.charAt(i); 
            if ((value>57 && value!='^') || value<40 || value == 44 || value ==46) 
            {
                instructions.setText("Please enter valid infix input!");
                return;
            }
        }
        if ((value<47 || value>58) && value != 41) //checks if there is a hanging operator (except left paren)
        {
            instructions.setText("Please enter valid infix input!");
            return;
        }

        temp+="\n";
        Stack<Character> operatorStack = new Stack<Character>();
        operatorStack.push('#');
        PostFix table = new PostFix();
        i=0;
        while (i < temp.length()) //going through all the infix string
        {
            value = (int)temp.charAt(i);
            char c = temp.charAt(i);
            if (value > 47 && value < 58) //ASCII values for 0 to 9
            {
                String number = temp.substring(i,i+1);
                i++;
                while (temp.charAt(i) > 47 && temp.charAt(i) < 58 && i<temp.length())
                {
                    number+= temp.substring(i,i+1);
                    i++;
                }
                postfix += number + " ";
            }
            else
            {
                int result = 0; //enters while loop
                while (result == POP)
                {
                    char topStack = operatorStack.peek();
                    if (c == ')')
                    {
                        while (topStack != '(')
                        {
                            postfix += operatorStack.pop()+" ";
                            topStack = operatorStack.peek();
                        }
                        operatorStack.pop(); //gets rid of the opening parentheses on the top of stack
                    }
                    result = table.compareOperands((int)topStack, value); //gets precedence from table
                    switch (result)
                    {
                        case POP:
                        postfix += operatorStack.pop()+" ";
                        break;
                        case PUSH:
                        operatorStack.push(temp.charAt(i));
                        break;
                        case DONE:
                        break;
                        case MATCH:
                        break;
                        default:
                        instructions.setText("Enter a valid infix expression!");
                        postExp.setText("Postfix Expression: N/A");
                        return;
                    }
                }
                i++;
            }
        }
    }

    /**
     * Generates an expression tree based on the postfix string. It pushes all the numbers onto the stack and 
     * pops the top two when it comes to an operator and puts it all in a binary node. It then pushes
     * the binary node onto the stack and continues this process for the whole postfix operation.
     */
    public void generateTree()
    {
        Stack<BinaryNode> operands = new Stack<BinaryNode>();
        String temp = postfix;
        int i=0;
        while (i< temp.length())
        {
            int value = (int)temp.charAt(i);

            BinaryNode node;
            if (value > 47 && value < 58) //ASCII values for 0 to 9
            {
                String number = temp.substring(i,i+1);
                i++;
                while (temp.charAt(i) > 47 && temp.charAt(i) < 58 && i<temp.length())
                {
                    number+= temp.substring(i,i+1);
                    i++;
                }

                node = new BinaryNode();
                node.setData(number);
            }
            else if (value == 32)
            {
                i++;
                continue;
            }
            else
            {
                node = new BinaryNode(temp.substring(i,i+1), operands.pop(), operands.pop());
                i++;
            }
            operands.push(node);
        }
        root = operands.pop();
        root.resetOutput();
        graph.setText(root.printTree(root,3));
    }

    /**
     * Evaluates the postfix expression
     */
    public void evaluateExpression()
    {
        double x = root.evaluate(root);
        evaluation.setText("Evaluation of input: "+x);
    }

    /**
     * Generates a fully parenthesized version of the infix expression based on the expression tree.
     */
    public void regenerateInfix()
    {
        String x = root.infix(root);
        regenInfix.setText("Regenerated infix: "+ x);
    }
}
