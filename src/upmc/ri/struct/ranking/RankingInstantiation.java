package upmc.ri.struct.ranking;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import upmc.ri.struct.instantiation.IStructInstantiation;

public class RankingInstantiation implements IStructInstantiation<List<double[]>, RankingOutput>{
	
	@Override
	public double[] psi(List<double[]> x, RankingOutput y) {
		
		double[] psi = new double[x.get(0).length];
		
		int cptPos = 0; // compte le nombre de positif compté pour optimiser algorithme
		int cptNeg = 0; // compte le nombre de negatif compté pour optimiser algorithme
		List<Integer> labelsGT = y.getLabelsGT();
		List<Integer> ranking = y.getRanking();
		List<Integer> positionning = y.getPositionningFromRanking();
		
		for(int i=0; i<labelsGT.size(); i++){
			if(labelsGT.get(i)==1){
				cptPos++;
				cptNeg = 0;
				for(int j=0; j<labelsGT.size(); j++){
					if(labelsGT.get(j)==-1){
						cptNeg++;
						int signe = -1;
						if(positionning.get(i)<positionning.get(j)) signe = 1;
						for(int k=0; k<psi.length; k++){
							psi[k] += signe*(x.get(i)[k]-x.get(j)[k]);
						}
					}
					if(cptNeg==(labelsGT.size()-y.getNbPlus())) break; // Cas ou il n'y a plus de moins
				}
				if(cptPos==y.getNbPlus()) break; // Cas ou il n'y a plus de plus
			}
		}
		return psi;
	}

	@Override
	public double delta(RankingOutput y1, RankingOutput y2) {
		return 1-RankingFunctions.averagePrecision(y2);
	}

	@Override
	public Set<RankingOutput> enumerateY() {
		// Nous ne pouvons pas la définir car pas d'énumération exhaustive de y
		return null;
	}
	

}
