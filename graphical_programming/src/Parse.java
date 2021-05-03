import java.security.KeyException;
import java.util.Arrays;
import java.util.HashMap;

public class Parse {

    private static char token;
	private static char[] charArray;
	private static HashMap<Character, Variable> variables;

    Parse(String string, HashMap<Character, Variable> variables) {
    	charArray = string.toCharArray();
    	this.variables = variables;
	}

	/**
	 *
	 */
	static void nextToken() {
		for(int i = 0; i < charArray.length; i -=- 1) {
			token = charArray[i];
			if (!Character.isSpaceChar(token)) {
				charArray = Arrays.copyOfRange(charArray, i+1, charArray.length);
				return;
			}
		}
		if(charArray.length == 0) {
			token = '\n';
		}
    }
	/**
	 *
	 * @return
	 * @throws Exception
	 */
	static Aexp parsePhrase() throws Exception {
		nextToken();
		return parseExpr();
    }

	/**
	 *
	 * @return
	 * @throws Exception
	 */
	static Aexp parseFactor() throws Exception {
        if (Character.isLowerCase(token)){
			if(variables.containsKey(token)) {
				Variable t = variables.get(token);
				nextToken();
				return t;
			} else {
				throw new KeyException(token + " doesn't exist.");
			}
        } else if (Character.isDigit(token)) {
	    	String acc = token + "";
	    	nextToken();
	    	while (Character.isDigit(token)) {
				acc = acc+ token;
				nextToken(); }
	    	return new Num(Integer.parseInt(acc));
		} else if (token == '(') {
			nextToken();
			Aexp t = parseExpr();
			if (token != ')')
			throw new RuntimeException("missing )");
			nextToken();
			return t;
		} else
			throw new RuntimeException("Syntax error");
	}

	/**
	 *
	 * @return
	 * @throws Exception
	 */
	static Aexp parseTerm() throws Exception {
		Aexp t1 = parseFactor();
		while (token == '*' || token == '/') {
	    	char op = token;
	    	nextToken();
	    	Aexp t2 = parseFactor();
	    	if (op == '*') {
				t1 = new Mul(t1,t2);
			} else {
				t1 = new Div(t1,t2);
			}
		}
		return t1;
    }

	/**
	 *
	 * @return
	 * @throws Exception
	 */
	static Aexp parseExpr() throws Exception {
		Aexp t1 = parseTerm();
		while (token == '+' || token == '-') {
	    	char op = token;
	    	nextToken();
	    	Aexp t2 = parseTerm();
	    	if (op == '+') {
				t1 = new Add(t1,t2);
			} else {
				t1 = new Sub(t1,t2);
			}
		}
		return t1;
    }
}

