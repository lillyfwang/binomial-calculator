
/**
 * Postfix table based on code provided by Prof. Allen's class notes.
 * This class takes in the top of the stack and the next operator in the infix string.
 * It compares the two operators and returns an int that determines wheter to pop or push the operator.
 */
public class PostFix
{
    public int compareOperands(int a, int b)
    {
        final int POP=0;
        final int PUSH=1;
        final int MATCH=2;
        final int ERROR=3;
        final int EOL=012;
        final int DONE=4;

        /* wastes space but makes the table easy to generate */
        int[][] table = new int[256][256];
        table['(']['*']=PUSH;
        table['(']['/']=PUSH;
        table['(']['-']=PUSH;
        table['(']['+']=PUSH;
        table['(']['^']=PUSH;
        table['(']['(']=PUSH;
        table['('][EOL]=ERROR;
        table['('][')']=MATCH;
        table['+']['*']=PUSH;
        table['+']['/']=PUSH;
        table['+']['-']=POP;
        table['+']['+']=POP;
        table['+']['^']=PUSH;
        table['+']['(']=PUSH;
        table['+'][EOL]=POP;
        table['+'][')']=PUSH;
        table['-']['*']=PUSH;
        table['-']['/']=POP;
        table['-']['-']=PUSH;
        table['-']['+']=PUSH;
        table['-']['^']=PUSH;
        table['-']['(']=POP;
        table['-'][EOL]=POP;
        table['-'][')']=POP;
        table['*']['*']=POP;
        table['*']['/']=POP;
        table['*']['-']=POP;
        table['*']['-']=POP;
        table['*']['^']=PUSH;
        table['*']['(']=PUSH;
        table['*'][EOL]=POP;
        table['*'][')']=POP;
        table['^']['*']=POP;
        table['^']['/']=POP;
        table['^']['-']=POP;
        table['^']['+']=POP;
        table['^']['^']=PUSH;
        table['^']['(']=PUSH;
        table['^'][EOL]=POP;
        table['^'][')']=POP;
        table['/']['*']=PUSH;
        table['/']['/']=POP;
        table['/']['-']=POP;
        table['/']['-']=POP;
        table['/']['^']=POP;
        table['/']['(']=PUSH;
        table['/'][EOL]=POP;
        table['/'][')']=POP;
        table[')']['*']=ERROR;
        table[')']['/']=ERROR;
        table[')']['-']=ERROR;
        table[')']['-']=ERROR;
        table[')']['^']=ERROR;
        table[')']['(']=ERROR;
        table[')'][EOL]=ERROR;
        table[')'][')']=ERROR;
        table['#']['*']=PUSH;
        table['#']['/']=PUSH;
        table['#']['-']=PUSH;
        table['#']['+']=PUSH;
        table['#']['^']=PUSH;
        table['#']['(']=PUSH;
        table['#'][EOL]=DONE;
        table['#'][')']=ERROR;
        
        
        return table[a][b];
    }
}
