package upmc.ri.struct;

import upmc.ri.struct.instantiation.IStructInstantiation;
import upmc.ri.struct.model.IStructModel;

public abstract class LinearStructModel<X, Y> implements IStructModel<X,Y>{

	protected IStructInstantiation<X, Y> structInstantiation;
	protected double[] params;
	
	public LinearStructModel(IStructInstantiation<X, Y> structInstantiation,int dimpsi) {
		super();
		this.structInstantiation = structInstantiation;
		this.params = new double[dimpsi];
	}
	
	@Override
	public IStructInstantiation <X,Y> instantiation(){
		return structInstantiation;
	}

	@Override
	public double[] getParameters() {
		return this.params;
	}

	
}
