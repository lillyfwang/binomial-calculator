/**
 * Implementation of a BinaryNode used in the expression tree. Contains data and left and right BinaryNode
 * children. This also has some basic functions of a tree, including printing out the tree, evaluating the
 * tree, and creating an infix notation.
 */
class BinaryNode 
{
    private String data;            // The data in the node
    private BinaryNode left;         // Left child
    private BinaryNode right;        // Right child
    static String output;
    static String infix;

    /**
     * Default contructor for BinaryNode
     */
    BinaryNode()
    {
        data = "";
        left =  null;
        right = null;
    }    

    /**
     * Constructor for BinaryNode
     */
    BinaryNode(String theElement ,BinaryNode lt, BinaryNode rt )
    {
        data = theElement;
        left = lt;
        right = rt;
    }

    /**
     * A mutator method for the left child.
     */
    public void setLeftChild(BinaryNode lt)
    {
        left = lt;
    }

    /**
     * A mutator method for the right child
     */
    public void setRightChild(BinaryNode rt)
    {
        right = rt;
    }

    /**
     * A mutator method for the data
     */
    public void setData(String theElement)
    {
        data = theElement;
    }

    /**
     * An accessor method for the data
     */
    public String getData()
    {
        return data;
    }

    /**
     * Sets the string that stores the tree graphically.
     */
    public void resetOutput()
    {
        output = "";
    }

    /**
     * Creates a string that represents the tree graphically
     */
    public static String printTree(BinaryNode t , int indent)
    {
        if( t != null )
        {
            printTree( t.right, indent + 3 );
            for(int i=0;i<indent;i++)
                output += ("   ");
            output+=t.data+"\n";
            printTree( t.left , indent + 3 );
        }
        return output;
    }

    /**
     * Traverses the tree and evaluates the expression tree.
     */
    public double evaluate(BinaryNode node){
        if(node.data == "") 
            return 0;
        else
        {
            char operator;
            switch (node.data.charAt(0))
            {
                case '+':
                return (evaluate(node.right) + evaluate(node.left));
                case '-':
                return (evaluate(node.right) - evaluate(node.left));
                case '*':
                return (evaluate(node.right) * evaluate(node.left));
                case '/':
                return (evaluate(node.right) / evaluate(node.left));
                case '^':
                return (Math.pow(evaluate(node.right), evaluate(node.left)));
                default:
                return Integer.parseInt(node.data);
            }
        }
    }

    /**
     * Creates an infix expression by traversing the expression tree.
     */
    public String infix(BinaryNode node)
    {
        infix = "";
        if(node.data == "") 
            return "";
        else
        {
            switch (node.data.charAt(0))
            {
                case '+':
                return ("("+infix(node.right) +"+"+ infix(node.left)+")");
                case '-':
                return ("("+infix(node.right) +"-"+ infix(node.left)+")");
                case '*':
                return ("("+infix(node.right) +"*"+ infix(node.left)+")");
                case '/':
                return ("("+infix(node.right) +"/"+ infix(node.left)+")");
                case '^':
                return ("("+infix(node.right) +"^"+ infix(node.left)+")");
                default:
                return node.data;
            }
        }
    }
}