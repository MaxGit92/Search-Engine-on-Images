package upmc.ri.struct;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class DataSet<X,Y>  implements Serializable{	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3417522594699229035L;

	public List<STrainingSample<X,Y>> listtrain;
	public List<STrainingSample<X,Y>> listtest;
	
	public DataSet(List<STrainingSample<X, Y>> listtrain,List<STrainingSample<X, Y>> listtest) {
		super();
		this.listtrain = listtrain;
		this.listtest = listtest;
	}
	
	/**
	 * Deuxième constructeur pour charger et créer le dataSet à partir d'un objet serialisé
	 * @param filename
	 */
	public DataSet(String filename) {
		super();
		try {
			@SuppressWarnings("unchecked")
			DataSet<X,Y> dataset = (DataSet<X, Y>) chargerDataSet(filename);
			this.listtrain = dataset.listtrain;
			this.listtest = dataset.listtest;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Set<Y> outputs(){
		Set<Y> out= new LinkedHashSet<Y>();
		for(STrainingSample<X,Y> st : listtrain){
			out.add(st.output);
		}
		return out;
	}
	
	/**
	 * Serialise l'objet dataset dans le fichier nommé nomFichier
	 * @param nomFichier
	 * @throws IOException
	 */
	public void enregisterDataSet(String nomFichier) throws IOException{
		File fichier =  new File(nomFichier) ;
		ObjectOutputStream oos = null;
		try {
			 // ouverture d'un flux sur un fichier
			oos = new ObjectOutputStream(new FileOutputStream(fichier));
			 // serialization de l'objet
			oos.writeObject(this) ;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			// fermeture du flux
			oos.close();
		}
		
	}
	
	/**
	 * Mï¿½thode static qui renvoie un dataset qui est sï¿½rialisï¿½ dans le fichier nommï¿½ nomfichier. 
	 * @param nomFichier
	 * @return Index
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public DataSet<X,Y> chargerDataSet(String nomFichier) throws IOException, ClassNotFoundException{
		File fichier =  new File(nomFichier) ;
		ObjectInputStream ois = null;
		DataSet<X,Y> dataset = null;
		try {
			// ouverture d'un flux sur un fichier
			ois = new ObjectInputStream(new FileInputStream(fichier));
			// dï¿½sï¿½rialization de l'objet
			dataset = (DataSet<X,Y>)ois.readObject() ;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// Fermeture du fihcier
			ois.close();
		}
		return dataset;
	}
}
