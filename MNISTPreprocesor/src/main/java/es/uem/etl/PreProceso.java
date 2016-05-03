package es.uem.etl;

import java.io.File;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;

import es.uem.etl.config.Configuracion;
import es.uem.image.mnist.MnistFetcher;
import es.uem.image.mnist.MnistManager;

public class PreProceso {
	
	//private static Logger log = LoggerFactory.getLogger(PreProceso.class); 
	private static Logger log = Logger.getLogger(PreProceso.class);
	private  Configuracion configuracion = null;
	private  File fileDirWork;
	private  File fileDirDownload;
	private  int numImages = 0;
	private static final long SEGUNDOS = 1000;
	private static final long MINUTOS = 60;
	
	public void ejecuta(){
		HashMap paramsMap = null;
		ejecuta(paramsMap);
	}
	
	public void ejecuta(HashMap paramsMap) {
		MnistFetcher mf = new MnistFetcher();
		configuracion = new Configuracion(paramsMap);
		long totalTodo =0;
		long finProc = 0;
		long iniProc = System.currentTimeMillis();
		
		try{
			//leemos el numero de imagenes a leer
			numImages = (new Integer(configuracion.value("obtenerN.images"))).intValue();
			log.debug("Preparando para procesar: "+numImages+" images");
			//obtenemos la coleccion
			if(configuracion.value("descarga.mnist").equalsIgnoreCase("true")){
				fileDirDownload = mf.downloadAndUntar(configuracion.value("directorio.mnist"));
				fileDirWork = new File(configuracion.value("directorio.mnist"));
				log.debug("Descargada colección en: "+fileDirDownload.getAbsolutePath());	
			}else{
				fileDirDownload = new File(configuracion.value("directorio.mnist")+"/MNIST");
				fileDirWork = new File(configuracion.value("directorio.mnist"));
				log.debug("Colección previamente descargada en: "+fileDirDownload.getAbsolutePath()+"/MNIST");
			}
			MnistManager m = new MnistManager(fileDirDownload.getAbsolutePath()+"/"+configuracion.value("mnist.images"), fileDirDownload.getAbsolutePath()+"/"+configuracion.value("mnist.label"));
			
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
			finProc = System.currentTimeMillis();
			totalTodo = (finProc-iniProc)/SEGUNDOS;
			log.debug("Tiempo de Procesamiento: "+(((totalTodo/MINUTOS)==0)?totalTodo+"s.":((totalTodo/MINUTOS)+"m."))+" ");
		}catch(Exception e){
			finProc = System.currentTimeMillis();
			totalTodo = (finProc-iniProc)/SEGUNDOS;
			log.debug("Tiempo de Procesamiento hasta ERROR: "+(((totalTodo/MINUTOS)==0)?totalTodo+"s.":((totalTodo/MINUTOS)+"m."))+" ");
			log.error("Error en PreProceso: "+e.getMessage());
			e.printStackTrace();
		}
		
	}

	private int configuracionSalida(){
		int configValue = 0;
		String writeImage;
		
		writeImage = configuracion.value("writeImage.to");
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
	
	public static void main(String[] args) {
		PreProceso preproceso = new PreProceso();
		preproceso.ejecuta();
	}
	
}
