package upmc.ri.bin;

import upmc.ri.io.ImageNetParser;
import upmc.ri.struct.DataSet;
import upmc.ri.struct.Evaluator;
import upmc.ri.struct.instantiation.MultiClass;
import upmc.ri.struct.instantiation.MultiClassHier;
import upmc.ri.struct.model.LinearStructModel_Ex;
import upmc.ri.struct.training.SGDTrainer;

public class MultiClassClassif {
	
	public static void main(String args[]){
		String filename = "dataSet.ser";

		DataSet<double[], String> ds = new DataSet<double[], String>(filename);
		//MultiClass multiClass = new MultiClass(ImageNetParser.classesImageNet());
		MultiClass multiClass = new MultiClassHier(ImageNetParser.classesImageNet());

		LinearStructModel_Ex<double[], String> linearStructModel_Ex = new LinearStructModel_Ex<double[], String>(multiClass,
				ds.listtrain.get(0).input.length*multiClass.enumerateY().size());

		Evaluator<double[], String> evaluator = new Evaluator<double[], String>();
		evaluator.setListtrain(ds.listtrain);
		evaluator.setListtest(ds.listtest);
		evaluator.setModel(linearStructModel_Ex);
		
		int T = 300;
		double eta = 2e-1;//1e-2;
		double lambda=1e-6;
		SGDTrainer<double[], String> sgdTrainer = new SGDTrainer<double[], String>(evaluator, T, eta, lambda);
		sgdTrainer.train(ds.listtrain, linearStructModel_Ex);
		multiClass.confusionMatrix(linearStructModel_Ex.predictAll(ds.listtest), ds.listtest);
		System.out.println("FINI");
	}
}

