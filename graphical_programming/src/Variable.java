public class Variable extends Aexp {

    char var;
    int value;


    Variable(char var, int value) {
        this.var = var;
        this.value = value;
    }

    public String toString(char var) { return var+""; }

    int eval() {

        return value; }
}
