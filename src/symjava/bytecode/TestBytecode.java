package symjava.bytecode;

import java.math.BigInteger;

public class TestBytecode {
	public static double fun(double[] args) {
//		return args[0] + args[1];
		double s = Math.pow(2, 3);
		return s;
	}
	public static void main(String[] args) {
//		BigInteger b = 2;
//		BigInteger c = BigInteger.valueOf(3);
//		System.out.println(b<c);
		
//		double a = 1.0;
//		double b = 2.0;
//		double c = 3.0;
//		Boolean r = (a == b) | (a != c);
		
//		int a = 1;
//		int b = 0;
//		boolean c = true, d = false;
//		System.out.println(!c);
		
//		boolean i =false;
//		i=true;
//		System.out.println(i);
		
//		byte i =0;
//		i=1;
//		System.out.println(i);
		
//		char i =0;
//		i=1;
//		System.out.println(i);
		
//		short i =0;
//		i=1;
//		System.out.println(i);
		
		long l = 1;
		float f = 2.0f;
		System.out.println(l+f);
		
//		String s = null;
//		for(int i=0; i<10.2; i++) {
//			s = args[i];
//		}
//		System.out.println(s);

/*
		double s = 1;
         6: dcmpl         
         7: ifle          17
		if(s > 0.5)
			s++;
		else
			s--;

         6: dcmpg         
         7: ifge          17
		if(0.5 < s)
			s++;
		else
			s--;
		
//        6: dcmpl         
//        7: iflt          17
		if(0.5 >= s)
			s++;
		else
			s--;

//        6: dcmpg         
//        7: ifgt          17
		if(0.5 <= s)
			s++;
		else
			s--;
 */
	}
}