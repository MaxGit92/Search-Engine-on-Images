package upmc.ri.struct.model;

import java.util.ArrayList;
import java.util.List;

import upmc.ri.struct.LinearStructModel;
import upmc.ri.struct.STrainingSample;
import upmc.ri.struct.instantiation.IStructInstantiation;
import upmc.ri.utils.VectorOperations;

public class LinearStructModel_Ex<X, Y> extends LinearStructModel<X, Y>{

	public LinearStructModel_Ex(IStructInstantiation<X, Y> structInstantiation,int dimpsi) {
		super(structInstantiation, dimpsi);
	}

	@Override
	public Y predict(STrainingSample<X, Y> ts) {
		double[] w = getParameters();
		IStructInstantiation<X,Y> structInstantiation = instantiation();
		double max = -100;
		Y yHat = null;
		for(Y y : structInstantiation.enumerateY()){
			double[] psi = structInstantiation.psi(ts.input, y);
			double res = VectorOperations.dot(psi, w);
			if(max<res){
				max=res;
				yHat = y;
			}
		}
		return yHat;
	}

	@Override
	public Y lai(STrainingSample<X, Y> ts) {
		double[] w = getParameters();
		double max = 0;
		Y yHat = null;
		for(Y y : structInstantiation.enumerateY()){
			double delta = structInstantiation.delta(ts.output, y);
			double[] psi = structInstantiation.psi(ts.input, y);
			double res = delta + VectorOperations.dot(psi, w);
			if(max<res){
				max=res;
				yHat = y;
			}
		}
		return yHat;
	}
	
	public List<Y> predictAll(List<STrainingSample<X, Y>> listTs) {
		List<Y> res = new ArrayList<Y>();
		for(STrainingSample<X, Y> sample : listTs){
			res.add(predict(sample));
		}
		return res;
	}
	

}
