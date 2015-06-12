package lambdacloud.test;

import static com.sun.org.apache.bcel.internal.Constants.ACC_PUBLIC;
import static com.sun.org.apache.bcel.internal.Constants.ACC_SUPER;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lambdacloud.core.lang.LCVar;
import symjava.bytecode.BytecodeBatchFunc;
import symjava.bytecode.BytecodeFunc;
import symjava.bytecode.IR;
import symjava.logic.Logic;
import symjava.relational.Relation;
import symjava.symbolic.Expr;
import symjava.symbolic.Symbol;
import symjava.symbolic.utils.BytecodeUtils;
import symjava.symbolic.utils.FuncClassLoader;
import symjava.symbolic.utils.Utils;

import com.sun.org.apache.bcel.internal.generic.AALOAD;
import com.sun.org.apache.bcel.internal.generic.ALOAD;
import com.sun.org.apache.bcel.internal.generic.ARRAYLENGTH;
import com.sun.org.apache.bcel.internal.generic.ArrayType;
import com.sun.org.apache.bcel.internal.generic.ClassGen;
import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.DASTORE;
import com.sun.org.apache.bcel.internal.generic.GOTO;
import com.sun.org.apache.bcel.internal.generic.I2D;
import com.sun.org.apache.bcel.internal.generic.ICONST;
import com.sun.org.apache.bcel.internal.generic.IF_ICMPLT;
import com.sun.org.apache.bcel.internal.generic.IINC;
import com.sun.org.apache.bcel.internal.generic.ILOAD;
import com.sun.org.apache.bcel.internal.generic.ISTORE;
import com.sun.org.apache.bcel.internal.generic.InstructionConstants;
import com.sun.org.apache.bcel.internal.generic.InstructionFactory;
import com.sun.org.apache.bcel.internal.generic.InstructionHandle;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.bcel.internal.generic.LocalVariableGen;
import com.sun.org.apache.bcel.internal.generic.MethodGen;
import com.sun.org.apache.bcel.internal.generic.Type;

public class CompileUtils {
	
	public static IR getIR(Expr expr, Expr ...args) {
		ClassGen cg = _compile(expr, args);
		IR ir =  new IR();
		ir.type = 1;
		ir.name = cg.getJavaClass().getClassName();
		ir.bytes = cg.getJavaClass().getBytes();
		return ir;
	}
	
	public static BytecodeFunc compile(Expr expr, Expr ...args) {
		ClassGen cg = _compile(expr, args);
		FuncClassLoader<BytecodeFunc> fcl = new FuncClassLoader<BytecodeFunc>();
		BytecodeFunc fun = fcl.newInstance(cg);
		return fun;
	}
	
	public static ClassGen _compile(Expr expr, Expr ...args) {
		String packageName = "symjava.bytecode";
		String clsName = expr.getClass().getSimpleName() + System.currentTimeMillis();
		String fullClsName = packageName+"."+clsName;
		ClassGen cg = new ClassGen(fullClsName, "java.lang.Object",
				"<generated>", ACC_PUBLIC | ACC_SUPER, new String[]{"symjava.bytecode.BytecodeFunc"});
		ConstantPoolGen cp = cg.getConstantPool(); // cg creates constant pool
		InstructionList il = new InstructionList();
		InstructionFactory factory = new InstructionFactory(cg);
		
		short acc_flags = ACC_PUBLIC;
		MethodGen mg = new MethodGen(acc_flags, // access flags
				Type.DOUBLE, // return type
				new Type[] { // argument types
					new ArrayType(Type.DOUBLE, 1) 
				}, 
				new String[] { "args" }, // arg names
				"apply", fullClsName, // method, class
				il, cp);
		
		HashMap<String, Integer> argsMap = new HashMap<String, Integer>();
		if(args != null) {
			for(int i=0; i<args.length; i++) {
				argsMap.put(args[i].getLabel(), i);
			}
		}
		System.out.println(fullClsName);
		StringBuilder sb = new StringBuilder();
		sb.append("double apply(,");
		for(Expr a : args)
			sb.append("double ").append(a).append(",");
		sb.delete(sb.length()-1, sb.length());
		sb.append(");");
		System.out.println(sb.toString());
		
		// Declare local variables
		List<Expr> vars = Utils.extractSymbols(expr);
		for(Expr var : vars) {
			if(var instanceof LCVar) {
				LCVar cv = (LCVar)var;
				int indexLVT = BytecodeUtils.declareLocal(cv, mg, il);
				cv.setLVTIndex(indexLVT);
			}
		}
		
		expr.bytecodeGen(fullClsName, mg, cp, factory, il, argsMap, 1, null);

		mg.setMaxStack();
		cg.addMethod(mg.getMethod());
		il.dispose(); // Allow instruction handles to be reused
		
		cg.addEmptyConstructor(ACC_PUBLIC);
		
		try {
			cg.getJavaClass().dump("bin/symjava/bytecode/"+clsName+".class");
		} catch (java.io.IOException e) {
			System.err.println(e);
		}
		return cg;
	}
	
	public static BytecodeBatchFunc compileVec(Expr expr, Expr ...args) {
		ClassGen cg = _compileVec(expr, args);
		FuncClassLoader<BytecodeBatchFunc> fcl = new FuncClassLoader<BytecodeBatchFunc>();
		BytecodeBatchFunc fun = fcl.newInstance(cg);
		return fun;
	}
	
	public static ClassGen _compileVec(Expr expr, Expr[] args) {
		String packageName = "symjava.bytecode";
		String clsName = expr.getClass().getSimpleName() + System.currentTimeMillis();
		String fullClsName = packageName+"."+clsName;
		ClassGen cg = new ClassGen(fullClsName, "java.lang.Object",
				"<generated>", ACC_PUBLIC | ACC_SUPER, new String[]{"symjava.bytecode.BytecodeBatchFunc"});
		ConstantPoolGen cp = cg.getConstantPool(); // cg creates constant pool
		InstructionList il = new InstructionList();
		InstructionFactory factory = new InstructionFactory(cg);
		
		short acc_flags = ACC_PUBLIC;
		MethodGen mg = new MethodGen(acc_flags, // access flags
				Type.VOID, // return type
				new Type[] { // argument types
					new ArrayType(Type.DOUBLE, 1),
					Type.INT,
					new ArrayType(Type.DOUBLE, 2) 
				},
				new String[] { "outAry", "outPos", "args" }, // arg names
				"apply", fullClsName, // method, class
				il, cp);
		
		HashMap<String, Integer> argsMap = new HashMap<String, Integer>();
		if(args != null) {
			for(int i=0; i<args.length; i++) {
				argsMap.put(args[i].getLabel(), i);
			}
		}
		//special symbol for output
		argsMap.put("output", 1);
		
		System.out.println(fullClsName);
		StringBuilder sb = new StringBuilder();
		sb.append("void apply(double[] output, int outPos,");
		for(Expr a : args)
			sb.append("double[] ").append(a).append(",");
		sb.delete(sb.length()-1, sb.length());
		sb.append(");");
		System.out.println(sb.toString());
		
		// Declare local variables
		List<Expr> vars = Utils.extractSymbols(expr);
		for(Expr var : vars) {
			if(var instanceof LCVar) {
				LCVar cv = (LCVar)var;
				int indexLVT = BytecodeUtils.declareLocal(cv, mg, il);
				cv.setLVTIndex(indexLVT);
			}
		}
		
		expr.bytecodeGen(fullClsName, mg, cp, factory, il, argsMap, 3, null);

		il.append(InstructionConstants.RETURN);
		
		mg.setMaxStack();
		cg.addMethod(mg.getMethod());
		il.dispose(); // Allow instruction handles to be reused
		cg.addEmptyConstructor(ACC_PUBLIC);
		try {
			cg.getJavaClass().dump("bin/symjava/bytecode/"+clsName+".class");
		} catch (java.io.IOException e) {
			System.err.println(e);
		}
		return cg;
	}	
}
