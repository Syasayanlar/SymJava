package symjava.symbolic;

import static com.sun.org.apache.bcel.internal.generic.InstructionConstants.*;

import java.util.Map;

import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.InstructionConstants;
import com.sun.org.apache.bcel.internal.generic.InstructionFactory;
import com.sun.org.apache.bcel.internal.generic.InstructionHandle;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.bcel.internal.generic.MethodGen;

import symjava.symbolic.Expr.TYPE;
import symjava.symbolic.arity.BinaryOp;
import symjava.symbolic.utils.BytecodeUtils;
import symjava.symbolic.utils.Utils;

public class Remainder extends BinaryOp {
	public Remainder(Expr arg1, Expr arg2) {
		super(arg1, arg2);
		this.label = arg1+"%"+arg2;
		this.sortKey = this.label;
	}

	@Override
	public Expr simplify() {
		return this;
	}

	@Override
	public boolean symEquals(Expr other) {
		return false;
	}

	@Override
	public Expr diff(Expr expr) {
		//???
		return this;
	}
	
	@Override
	public InstructionHandle bytecodeGen(String clsName, MethodGen mg,
			ConstantPoolGen cp, InstructionFactory factory,
			InstructionList il, Map<String, Integer> argsMap, int argsStartPos, 
			Map<Expr, Integer> funcRefsMap) {
		InstructionHandle startPos = arg1.bytecodeGen(clsName, mg, cp, factory, il, argsMap, argsStartPos, funcRefsMap);
		TYPE ty = Utils.getConvertedType(arg1.getType(), arg2.getType());
		BytecodeUtils.typeCase(il, arg1.getType(), ty);
		arg2.bytecodeGen(clsName, mg, cp, factory, il, argsMap, argsStartPos, funcRefsMap);
		BytecodeUtils.typeCase(il, arg2.getType(), ty);		
		if(ty == TYPE.DOUBLE)
			il.append(DREM);
		else if(ty == TYPE.INT)
			il.append(IREM);
		else if(ty == TYPE.LONG)
			il.append(LREM);
		else if(ty == TYPE.FLOAT)
			il.append(FREM);
		else
			il.append(IREM);
		return startPos;
	}
}
