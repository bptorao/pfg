package es.uem.etl;

import java.io.File;
import org.apache.log4j.Logger;

import es.uem.image.mnist.MnistFetcher;
import es.uem.image.mnist.MnistManager;

public class PreProceso {
	
	//private static Logger log = LoggerFactory.getLogger(PreProceso.class); 
	private static Logger log = Logger.getLogger(PreProceso.class);
	
	private static File fileDirWork;
	private static File fileDirDownload;
	private static int numImages = 0;
	
	public static void main(String[] args) {
		MnistFetcher mf = new MnistFetcher();
		
		try{
			//leemos el numero de imagenes a leer
			numImages = (new Integer(Configuracion.value("obtenerN.images"))).intValue();
			log.debug("Preparando para procesar: "+numImages+" images");
			//obtenemos la coleccion
			if(Configuracion.value("descarga.mnist").equalsIgnoreCase("true")){
				fileDirDownload = mf.downloadAndUntar(Configuracion.value("directorio.mnist"));
				fileDirWork = new File(Configuracion.value("directorio.mnist"));
				log.debug("Descargada colección en: "+fileDirDownload.getAbsolutePath());	
			}else{
				fileDirDownload = new File(Configuracion.value("directorio.mnist")+"/MNIST");
				fileDirWork = new File(Configuracion.value("directorio.mnist"));
				log.debug("Colección previamente descargada en: "+fileDirDownload.getAbsolutePath()+"/MNIST");
			}
			MnistManager m = new MnistManager(fileDirDownload.getAbsolutePath()+"/"+Configuracion.value("mnist.images"), fileDirDownload.getAbsolutePath()+"/"+Configuracion.value("mnist.label"));
			
			//leemos configuracion y generamos los ficheros
			switch(configuracionSalida()){
			case 0:
				//configuracion no reconocida
				log.debug("Configuración no reconocida");
				break;
			case 1:
				// crea imagenes PPM
				log.debug("Generando imágenes PPM");
				log.debug("Write Image To PPM: "+numImages);
				for(int i=1;i<=numImages;i++){
					m.setCurrent(i); //index of the image that we are interested in 
					int[][] image = m.readImage(); 
					log.debug("Label:" + m.readLabel()); 
					MnistManager.writeImageToPpm(image, fileDirWork.getAbsolutePath()+"/Num_"+String.format("%05d",i)+"_label"+m.readLabel()+".ppm");
				}
				log.debug("Procesadas "+numImages+" imágenes");
				log.debug("Descargada colección en: "+fileDirDownload.getAbsolutePath());
				log.debug("Ficheros generados en: "+fileDirWork.getAbsolutePath());
				break;
			case 2:
				// crea ficheros con vectores para las imágenes
				log.debug("Generando vectores: ficheros images y labels ");
				m.writeImageToVector(numImages, fileDirWork.getAbsolutePath()+"/Vector_");
				log.debug("Procesados dos ficheros Vector_mnist.images y Vector_mnist.labels con "+numImages+" procesadas");
				log.debug("Descargada colección en: "+fileDirDownload.getAbsolutePath());
				log.debug("Ficheros generados en: "+fileDirWork.getAbsolutePath());
				break;
			case 3:
				// crea ficheros con vectores formato convolutional
				log.debug("Generando vectores formato convolutional: fichero Convolutional_Training_Digits_"+numImages+".csv");
				m.writeImageToVectorConvolutional(numImages, fileDirWork.getAbsolutePath()+"/Convolutional_Training_Digits_"+numImages+"");
				log.debug("Procesados fichero  Convolutional_Training_Digits_"+numImages+".csv");
				log.debug("Descargada colección en: "+fileDirDownload.getAbsolutePath());
				log.debug("Ficheros generados en: "+fileDirWork.getAbsolutePath());
				break;
			}
			
		}catch(Exception e){
			log.error("Error en PreProceso: "+e.getMessage());
			e.printStackTrace();
		}
		
	}

	private static int configuracionSalida(){
		int configValue = 0;
		String writeImage;
		
		writeImage = Configuracion.value("writeImage.to");
		if(writeImage.equalsIgnoreCase("ppm")){
			configValue = 1;
		}
		if(writeImage.equalsIgnoreCase("vector")){
			configValue = 2;
		}
		if(writeImage.equalsIgnoreCase("vector_convolutional")){
			configValue = 3;
		}
		return configValue;
	}

	
}
