package upmc.ri.bin;

import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import upmc.ri.io.ImageNetParser;
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
		String filename = "dataSet.ser";

		DataSet<double[], String> ds = new DataSet<double[], String>(filename);
		Set<String> labels = ImageNetParser.classesImageNet();
		
		for(String label : labels){
			if(!label.equals("acoustic_guitar")) continue;
			DataSet<List<double[]>, RankingOutput> dataSet = RankingFunctions.convertClassif2Ranking(ds, label);
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
			
			RankingOutput prediction = rankingStructModel.predict(dataSet.listtest.get(0));
			double[][] rp = RankingFunctions.recalPrecisionCurve(prediction);

			double AP = RankingFunctions.averagePrecision(prediction);
			System.out.println("AP " + AP);
			BufferedImage im = Drawing.traceRecallPrecisionCurve(prediction.getNbPlus(),rp);
			try {
				ImageIO.write(im,"jpg", new File("RP_"+label+".jpg"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		System.out.println("FINI");

	}
}