public class Num extends Aexp {

    int value;

    Num(int value) {
        this.value = value;
    }

    public String toString(){
	return value+"";
    }

    int eval() {
	return this.value;
    }
}