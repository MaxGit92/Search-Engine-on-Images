package upmc.ri.struct.instantiation;

import java.util.Map;
import java.util.Set;

import edu.cmu.lti.lexical_db.ILexicalDatabase;
import edu.cmu.lti.lexical_db.NictWordNet;
import edu.cmu.lti.ws4j.RelatednessCalculator;
import edu.cmu.lti.ws4j.impl.WuPalmer;

public class MultiClassHier extends MultiClass{

	private double[][] distances;
	private Map<String, Integer> matching;

	public MultiClassHier(Set<String> enumerate) {
		super(enumerate);
		this.distances=new double[enumerate.size()][enumerate.size()];
		this.matching = matching();
		ILexicalDatabase db = new NictWordNet();
		RelatednessCalculator calculator = new WuPalmer(db);
		for(String s1 : enumerate){
			for(String s2 : enumerate){
				if(!s1.equals(s2))
					// Nous multiplions par deux pour centrer autour de 1
					distances[matching.get(s1)][matching.get(s2)] = 2*(1-calculator.calcRelatednessOfWords(s1, s2));
				else
					distances[matching.get(s1)][matching.get(s2)] = 0;
			}
		}
	}
	
	@Override
	public double delta(String y1, String y2) {
		return distances[matching.get(y1)][matching.get(y2)];
	}
	
//	public static void main(String args[]){
//		ILexicalDatabase db = new NictWordNet();
//		RelatednessCalculator calculator = new WuPalmer(db);
//		String w1 = "m";
//		String w2 = "dick";
//		System.out.println(calculator.calcRelatednessOfWords(w1, w2));
//	}
	
}
