package upmc.ri.struct.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import upmc.ri.struct.LinearStructModel;
import upmc.ri.struct.STrainingSample;
import upmc.ri.struct.instantiation.IStructInstantiation;
import upmc.ri.struct.ranking.RankingFunctions;
import upmc.ri.struct.ranking.RankingOutput;
import upmc.ri.utils.ArrayUtils;
import upmc.ri.utils.VectorOperations;

public class RankingStructModel extends LinearStructModel<List<double[]>, RankingOutput>{

	public RankingStructModel(
			IStructInstantiation<List<double[]>, RankingOutput> structInstantiation,
			int dimpsi) {
		super(structInstantiation, dimpsi);
	}

	@Override
	public RankingOutput predict(STrainingSample<List<double[]>, RankingOutput> ts) {
		List<double[]> X = ts.input;
		double[] w = getParameters();
		
	    double[] dots = new double[X.size()];
		List<Integer> ranking = new ArrayList<Integer>();
		
		for(int i=0; i<X.size(); i++)
			dots[i]=VectorOperations.dot(w, X.get(i));
		
		int[] sorted = ArrayUtils.argsort(dots, false);
		for(int i=0; i<sorted.length; i++)
			ranking.add(sorted[i]);
		
		return new RankingOutput(ts.output.getNbPlus(), ranking, ts.output.getLabelsGT());
	}

	@Override
	public RankingOutput lai(STrainingSample<List<double[]>, RankingOutput> ts) {
		double[] w = getParameters();
		return RankingFunctions.loss_augmented_inference(ts, w);
	}

}
