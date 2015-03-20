package symjava.examples;

import symjava.symbolic.*;
import static symjava.symbolic.Symbol.*;
import symjava.symbolic.utils.JIT;
import symjava.bytecode.BytecodeFunc;

public class Example0 {
	public static void main(String[] args) {
		Expr R = 0.127-(x*0.194/(y+0.194));
		Expr Rdy = R.diff(y);
		System.out.println(Rdy);
		
		BytecodeFunc func = JIT.compile(new Expr[]{x,y}, Rdy);
		System.out.println(func.apply(0.362, 0.556));
	}
}
