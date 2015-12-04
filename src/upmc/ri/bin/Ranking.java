package upmc.ri.bin;

import java.util.List;

import upmc.ri.struct.DataSet;
import upmc.ri.struct.Evaluator;
import upmc.ri.struct.model.RankingStructModel;
import upmc.ri.struct.ranking.RankingFunctions;
import upmc.ri.struct.ranking.RankingInstantiation;
import upmc.ri.struct.ranking.RankingOutput;
import upmc.ri.struct.training.SGDTrainer;
import upmc.ri.utils.Drawing;

public class Ranking {
	public static void main(String args[]){
		String filename = "/tmp/dataSet.ser";

		DataSet<double[], String> ds = new DataSet<double[], String>(filename);
		String classquery = "ambulance";
		DataSet<List<double[]>, RankingOutput> dataSet = RankingFunctions.convertClassif2Ranking(ds, classquery);
		RankingInstantiation rankingInstantiation = new RankingInstantiation();
		int dimpsi = 250;
		RankingStructModel rankingStructModel = new RankingStructModel(rankingInstantiation, dimpsi);

		Evaluator<List<double[]>, RankingOutput> evaluator = new Evaluator<List<double[]>, RankingOutput>();
		evaluator.setListtrain(dataSet.listtrain);
		evaluator.setListtest(dataSet.listtest);
		evaluator.setModel(rankingStructModel);

		int T = 50;
		double eta = 10;  //1e-2
		double lambda=1e-6;
		SGDTrainer<List<double[]>, RankingOutput> sgdTrainer = new SGDTrainer<List<double[]>, RankingOutput>(evaluator, T, eta, lambda);
		sgdTrainer.train(dataSet.listtrain, rankingStructModel);
		
		double[][] rp = RankingFunctions.recalPrecisionCurve(dataSet.listtrain.get(0).output);
		Drawing.traceRecallPrecisionCurve(dataSet.listtrain.get(0).output.getNbPlus(), rp);
		
		System.out.println("FINI (pour toi Paul)");

	}
}