package upmc.ri.bin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import upmc.ri.index.ImageFeatures;
import upmc.ri.index.VIndexFactory;
import upmc.ri.io.ImageNetParser;
import upmc.ri.struct.DataSet;
import upmc.ri.struct.STrainingSample;
import upmc.ri.utils.PCA;

public class VisualIndexes implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6693874192067817143L;
	
	public static DataSet<double[], String> creerDataSet(String path) throws Exception{
		Set<String> labels = ImageNetParser.classesImageNet();
		
		List<STrainingSample<double[], String>> listtrain = new ArrayList<STrainingSample<double[], String>>();
		List<STrainingSample<double[], String>> listtest = new ArrayList<STrainingSample<double[], String>>();
		
		for (String label : labels) {
			int cpt=0;
			List<List<Integer>> words = ImageNetParser.getWords(path + label + ".txt");
			int sizetrain = (int) (words.size()*0.8);
			for(List<Integer> word : words){
				double[] BoW = VIndexFactory.computeBow(word);
				if(cpt<sizetrain) listtrain.add(new STrainingSample<double[], String>(BoW, label));
				else listtest.add(new STrainingSample<double[], String>(BoW, label));
				cpt++;
			}
		}
		return new DataSet<double[], String>(listtrain, listtest);
	}
	
	// nbComp = 250
	public static DataSet<double[], String> enregistrerDataSet(String sourcePath, String destPath, int nbComp) throws Exception {
		DataSet<double[], String> dataset = VisualIndexes.creerDataSet(sourcePath);
		if (nbComp > 0){
			dataset = PCA.computePCA(dataset, nbComp);
		}
		dataset.enregisterDataSet(destPath);
		return dataset;
	}

	public static <X, Y> void main(String[] args) throws Exception {
		String destPath = "/tmp/dataSet.ser";
		
		int nbComp = 250;
		String sourcePath = "/users/nfs/Enseignants/thomen/Bases/ImageNet/BoF/txt/";
		VisualIndexes.enregistrerDataSet(sourcePath, destPath, nbComp);
		/*
		@SuppressWarnings("rawtypes")
		DataSet<X,Y> dataset = new DataSet<X, Y>(destPath);
		System.out.println(dataset.listtrain.get(2).input);
		*/
		
	}
	
}
