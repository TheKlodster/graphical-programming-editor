public class Add extends Aexp {

    Aexp l,r;

    Add(Aexp l, Aexp r) { this.l = l; this.r = r; }

    public String toString() {
	return l.toString()+"+"+r.toString();
    }

    int eval() {
	return l.eval() + r.eval();
    }
}
