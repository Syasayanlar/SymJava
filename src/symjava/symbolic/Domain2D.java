package symjava.symbolic;

import java.util.ArrayList;
import java.util.List;

public class Domain2D implements Domain {
	String label = null;
	List<Expr> coordVars = new ArrayList<Expr>();

	/**
	 * Construct a 2D domain with a given label(name) and
	 * a list of coordinate variables
	 * @param label
	 * @param coordVars
	 */
	public Domain2D(String label, List<Expr> coordVars) {
		this.label = label;
		this.coordVars.addAll(coordVars);
	}
	
	public Domain2D(String label, Expr ...coordVars) {
		this.label = label;
		for(Expr e : coordVars) {
			this.coordVars.add(e);
		}
	}	
	
	@Override
	public Domain getBoundary() {
		return null;
	}

	@Override
	public List<Expr> getCoordVars() {
		return this.coordVars;
	}

	@Override
	public String toString() {
		return this.label;
	}
}
