package es.uem.etl;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.uem.image.mnist.MnistFetcher;
import es.uem.image.mnist.MnistManager;

public class PreProceso {
	
	private static Logger log = LoggerFactory.getLogger(PreProceso.class); 
	private static File fileDir;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MnistFetcher mf = new MnistFetcher();
		try{
			fileDir = mf.downloadAndUntar();
			log.debug("Descargada coleccion en: "+fileDir.getAbsolutePath());
			MnistManager m = new MnistManager(fileDir.getAbsolutePath()+"/images-idx1-ubyte", fileDir.getAbsolutePath()+"/labels-idx1-ubyte"); 
			m.setCurrent(10); //index of the image that we are interested in 
			int[][] image = m.readImage(); 
			System.out.println("Label:" + m.readLabel()); 
			MnistManager.writeImageToPpm(image, fileDir.getAbsolutePath()+"/"+"10.ppm"); 
		}catch(Exception e){
			log.error("Error en PreProceso "+e.getMessage());
			e.printStackTrace();
		}
		
		
	}

}
