package alex.calc;
/*
 * Author: Alex Matthews
 * Ireland
 */	
import javax.script.*;

public class CalcUtilities {
	
	private static ScriptEngineManager mgr = new ScriptEngineManager();
	private static ScriptEngine engine = mgr.getEngineByName("JavaScript");
	
	public static double reciprocal(double num) {
		return 1 / num;	
	}

	
	public static double squared(double num) {
		return num * num;
	}
	
	public static double sqrRoot(double num) {
		return Math.sqrt(num);
	}
	
	public static Object evaluate(String equation) throws ScriptException {
		return engine.eval(equation);
	}
}
