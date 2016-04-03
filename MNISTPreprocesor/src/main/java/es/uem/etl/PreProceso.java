package es.uem.etl;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.uem.image.mnist.MnistFetcher;
import es.uem.image.mnist.MnistManager;

public class PreProceso {
	
	private static Logger log = LoggerFactory.getLogger(PreProceso.class); 
	private static File fileDir;
	private static int numImages = 10;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MnistFetcher mf = new MnistFetcher();
		try{
			fileDir = mf.downloadAndUntar();
			log.debug("Descargada colección en: "+fileDir.getAbsolutePath());
			MnistManager m = new MnistManager(fileDir.getAbsolutePath()+"/images-idx1-ubyte", fileDir.getAbsolutePath()+"/labels-idx1-ubyte");
			for(int i=1;i<=numImages;i++){
				m.setCurrent(i); //index of the image that we are interested in 
				int[][] image = m.readImage(); 
				log.debug("Label:" + m.readLabel()); 
				MnistManager.writeImageToPpm(image, fileDir.getAbsolutePath()+"/Num_"+String.format("%05d",i)+"_label"+m.readLabel()+".ppm");
			}
			log.debug("Procesadas "+numImages+" imágenes");
			log.debug("Descargada colección en: "+fileDir.getAbsolutePath());
		}catch(Exception e){
			log.error("Error en PreProceso "+e.getMessage());
			e.printStackTrace();
		}
		
		
	}

}
