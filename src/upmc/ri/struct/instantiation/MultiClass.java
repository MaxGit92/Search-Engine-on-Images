package upmc.ri.struct.instantiation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ejml.data.D1Matrix64F;
import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.MatrixVisualization;

import upmc.ri.struct.STrainingSample;

public class MultiClass implements IStructInstantiation<double[], String>{
	
	private Set<String> enumerate;
	private Map<String, Integer> matching;
	
	public MultiClass(Set<String> enumerate) {
		super();
		this.enumerate = enumerate;
		this.matching = matching();
	}

	/**
	 * Retourne la map permettant d'accéder plus rapidemment à un bow
	 * @param sizeBow
	 * @return
	 */
	public Map<String, Integer> indexation(int sizeBow){
		Map<String, Integer> index = new HashMap<String, Integer>();
		int cpt=0;
		for(String y : enumerate){
			index.put(y, sizeBow*cpt++);
		}
		return index;
	}
	
	@Override
	public double[] psi(double[] x, String y) {
		Map<String, Integer> index = indexation(x.length);
		double[] res = new double[index.size() * x.length];
		int j = 0;
		int debut = index.get(y);
		for(int i=debut; i<debut+x.length; i++){
			res[i] = x[j++];
		}
		return res;
	}

	@Override
	public double delta(String y1, String y2) {
		if(y1.equals(y2)) return 0;
		return 1;
	}

	@Override
	public Set<String> enumerateY() {
		return enumerate;
	}

	/**
	 * Attribue un numéro à une classe pour la matrice de confusion
	 * @return map<String, Integer>
	 */
	public Map<String, Integer> matching(){
		Map<String, Integer> matching = new HashMap<String, Integer>();
		int cpt=0;
		for(String s : enumerate){
			matching.put(s, cpt++);
		}
		return matching;
	}
	
	/**
	 * Crée la matrice de confusion
	 * @param predictions
	 * @param gt
	 */
	public void confusionMatrix(List<String> predictions, List<STrainingSample<double[], String>> gt){
		D1Matrix64F matrix = new DenseMatrix64F(enumerate.size(), enumerate.size());
		float taux = 0;
		for(int i=0; i<predictions.size(); i++){
			String pred = predictions.get(i);
			String correct = gt.get(i).output;
			if(matching.get(correct)<=3 && matching.get(pred)<=3) taux++;
			if(matching.get(correct)<=6 && matching.get(correct)>3 && matching.get(pred)<=6 && matching.get(pred)>3) taux++;
			if(matching.get(correct)>6 && matching.get(pred)>6) taux++;
			matrix.set(matching.get(correct),matching.get(pred),matrix.get(matching.get(correct),matching.get(pred))+1);
		}
		System.out.println("Taux de bonne classification : " + taux*100.0/predictions.size());
		MatrixVisualization.show(matrix, "Matrice de confusion");
		
	}
	
}
