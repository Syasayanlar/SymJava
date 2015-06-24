package lambdacloud.test;

import lambdacloud.core.CloudConfig;
import lambdacloud.core.CloudFunc;
import lambdacloud.core.CloudSD;
import symjava.bytecode.BytecodeFuncImp1;

public class TestClassFile {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CloudConfig.setTarget("server");
		
		CloudFunc f = new CloudFunc(BytecodeFuncImp1.class);
		
		CloudSD input = new CloudSD("input").init(new double[]{3, 4});
		
		CloudSD output = new CloudSD("output").resize(1);
		f.apply(output, input);
		
		if(output.fetchToLocal()) {
			for(double d : output.getData()) {
				System.out.println(d);
			}
		}
	}
}
