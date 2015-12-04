package upmc.ri.struct.training;

import java.util.List;
import java.util.Random;

import upmc.ri.struct.DataSet;
import upmc.ri.struct.Evaluator;
import upmc.ri.struct.STrainingSample;
import upmc.ri.struct.instantiation.IStructInstantiation;
import upmc.ri.struct.model.IStructModel;
import upmc.ri.struct.ranking.RankingInstantiation;
import upmc.ri.struct.ranking.RankingOutput;

public class SGDTrainer<X,Y> implements ITrainer<X,Y>{
	private Evaluator<X,Y> evaluator; // Permet de calculer l'évolution de la fonction de coût delat(yi, ŷi)
	private int T;
	private double eta;
	private double lambda;
	
	
	public SGDTrainer(Evaluator<X, Y> evaluator, int T, double eta,
			double lambda) {
		super();
		this.evaluator = evaluator;
		this.T = T;
		this.eta = eta;
		this.lambda = lambda;
	}

	private double normeW2(double[] w){
		double res = 0;
		for(int i=0; i<w.length; i++){
			res += w[i];
		}
		return res;
	}
	
	private double pdtScalaire(double[] psi, double[]w){
		double res=0;
		for(int i=0; i<psi.length; i++){
			res+=psi[i]*w[i];
		}
		return res;
	}
		
	/**
	 * Calcul de l'équation de la fonction objetive
	 * @return
	 */
	public double convexLoss(IStructModel<X,Y> structModel, DataSet<X,Y> dataSet){
		double[] w = structModel.getParameters();
		IStructInstantiation<X,Y> structInstantiation = structModel.instantiation();
		double normeW2 = normeW2(w);
		double somme = 0;
		int n = dataSet.listtrain.size();
		for(int i=0; i<n ; i++){
			STrainingSample<X, Y> sample = dataSet.listtrain.get(i);
			Y yHat = structModel.lai(sample); // argmax
			double max = structInstantiation.delta(sample.output, yHat) + pdtScalaire(structInstantiation.psi(sample.input, yHat), w);
			somme+=max-pdtScalaire(structInstantiation.psi(sample.input,sample.output), w);
		}
		return lambda/2.0 * normeW2 + 1.0/n * somme; // Ceci est le calcul de Pw
	}
	
	@Override
	public void train(List<STrainingSample<X, Y>> lts , IStructModel<X,Y> model) {
		int N = lts.size();
		double[] w = model.getParameters();
		Random generator = new Random();
		//if(!(lts.get(0).output instanceof RankingOutput)){
		IStructInstantiation <X,Y> structInstantiation = model.instantiation();
		for(int t=0; t<T; t++){
			if(t%10==0){
				System.out.println(t);
				evaluator.evaluate();
				System.out.println("Err train: "+evaluator.getErr_train());
				System.out.println("Err test:  "+evaluator.getErr_test());
			}
			for(int i=0; i<N; i++){
				int index = generator.nextInt(N);
				STrainingSample<X, Y> sample = lts.get(index);
				Y yHat = model.lai(sample); // Loss augmented inference
				double[] psi1 = structInstantiation.psi(sample.input, yHat);
				double[] psi2 = structInstantiation.psi(sample.input, sample.output);
				double[] gi = new double[psi1.length];
				for(int j=0; j<gi.length; j++){
					gi[j] = psi1[j] - psi2[j]; // Calcul du gradient
				}
				for(int j=0; j<w.length; j++){
					w[j] = w[j] - eta*(lambda*w[j] + gi[j]); // Mise à jour des poids
				}
			}
			//}
			/*
		}else{
			for(int t=0; t<T; t++){
				RankingInstantiation rankingInstantiation = new RankingInstantiation();
				@SuppressWarnings("unchecked")
				List<double[]> x = (List<double[]>) lts.get(0).input;
				RankingOutput y = (RankingOutput) lts.get(0).output;
				double[] psi = rankingInstantiation.psi(x, y);
				if(t%10==0){
					System.out.println(t);
					evaluator.evaluate();
					System.out.println("Err train: "+evaluator.getErr_train());
					System.out.println("Err test:  "+evaluator.getErr_test());
				}
				for(int i=0; i<N; i++){
					int index = generator.nextInt(N);
					STrainingSample<X, Y> sample = lts.get(index);
					Y yHat = model.lai(sample); // Loss augmented inference
					double[] gi = new double[psi.length];
					for(int j=0; j<gi.length; j++){
						gi[j] = psi[y.getRanking().g] - psi[j]; // Calcul du gradient
					}
					for(int j=0; j<w.length; j++){
						w[j] = w[j] - eta*(lambda*w[j] + gi[j]); // Mise à jour des poids
					}
				}
			}*/
		}
	}

	// GETTERS AND SETTERS
	
	public int getT() {
		return T;
	}

	public void setT(int t) {
		T = t;
	}

	public Evaluator<X, Y> getEvaluator() {
		return evaluator;
	}

	public void setEvaluator(Evaluator<X, Y> evaluator) {
		this.evaluator = evaluator;
	}

	public double getEta() {
		return eta;
	}

	public void setEta(double eta) {
		this.eta = eta;
	}

	public double getLambda() {
		return lambda;
	}

	public void setLambda(double lambda) {
		this.lambda = lambda;
	}

	
}
