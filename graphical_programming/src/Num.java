public class Num extends Aexp {

    int value = 0;

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
