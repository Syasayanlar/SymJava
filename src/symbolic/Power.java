package symbolic;

import java.util.List;

public class Power extends UnaryOp {
	public int exponent;
	public Power(Expr base, int exponent) {
		super(base);
		this.exponent = exponent;
		if(base instanceof Symbol)
			name = base + "^" + exponent;
		else
			name = "("+base + ")^" + exponent;
	}
	
	@Override
	public Expr subs(Expr from, Expr to) {
		return new Power(base.subs(from, to), exponent);
	}

	@Override
	public Expr diff(Expr expr) {
		if(exponent == 2)
			return Symbol.C2.multiply(base).multiply(base.diff(expr));
		else {
			SymInteger i = new SymInteger(exponent);
			return i.multiply(new Power(base, exponent - 1)).multiply(base.diff(expr));
		}
	}

	@Override
	public Expr simplify() {
		return new Power(base.simplify(), exponent);
	}

	@Override
	public boolean symEquals(Expr other) {
		if(other instanceof Power) {
			Power o = (Power)other;
			if(base.symEquals(o.base) && exponent == o.exponent)
				return true;
		}
		return false;
	}

	@Override
	protected void flattenAdd(List<Expr> outList) {
		outList.add(this);
	}

	@Override
	protected void flattenMultiply(List<Expr> outList) {
		outList.add(this);
	}

}
