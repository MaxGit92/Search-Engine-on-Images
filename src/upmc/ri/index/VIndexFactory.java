package upmc.ri.index;

import java.util.List;

public class VIndexFactory {
	
	public static double[] computeBow(List<Integer> words){
		double[] BoW = new double[1000];
		for(Integer word : words){
			BoW[word]++;
		}
		int N = words.size();

		for(int i=0; i<BoW.length; i++){
			BoW[i]/=N;
		}
		return BoW;
	}

}
