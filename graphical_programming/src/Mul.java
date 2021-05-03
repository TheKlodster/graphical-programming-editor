public class Mul extends Aexp {

    Aexp l,r;

    public String toString() {
	return l.toString()+"*"+r.toString();
    }
    
    Mul(Aexp l, Aexp r) {
	this.l = l; this.r = r;
    }

    int eval() {
	return l.eval() * r.eval();
    }
	
}
