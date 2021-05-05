public class Variable extends Aexp {

    char var;
    int value;


    Variable(char var, int value) {
        this.var = var;
        this.value = value;
    }

    int eval() {

        return value; }
}
