package es.uem.image.mnist;


import java.io.BufferedWriter; 
import java.io.FileWriter; 
import java.io.IOException;

import org.apache.log4j.Logger;

import es.uem.etl.PreProceso; 
 
 
/**
 * <p> 
 * Utility class for working with the MNIST database. 
 * <p> 
 * Provides methods for traversing the images and labels data files separately, 
 * as well as simultaneously. 
 * <p> 
 * Provides also method for exporting an image by writing it as a PPM file. 
 * <p>  
 * Example usage: 
 * <pre> 
 *  MnistManager m = new MnistManager("t10k-images.idx3-ubyte", "t10k-labels.idx1-ubyte"); 
 *  m.setCurrent(10); //index of the image that we are interested in 
 *  int[][] image = m.readImage(); 
 *  System.out.println("Label:" + m.readLabel()); 
 *  MnistManager.writeImageToPpm(image, "10.ppm"); 
 * </pre> 
 */ 
public class MnistManager { 
    private MnistImageFile images; 
    private MnistLabelFile labels; 
	private static Logger log = Logger.getLogger(MnistManager.class);

    /**
     * Writes the given image in the given file using the PPM data format. 
     *  
     * @param image 
     * @param ppmFileName 
     * @throws java.io.IOException 
     */ 
    public void writeImageToVector(int numImages, String ppmFileName) throws IOException { 
        BufferedWriter ppmOutImages = null;
        BufferedWriter ppmOutLabels = null;
        String fileImages = ppmFileName+"mnist.images";
        String fileLabels = ppmFileName+"mnist.labels";
        int[][] image;
        int label;
        int rows;
        int cols;
        
        try { 
            ppmOutImages = new BufferedWriter(new FileWriter(fileImages));
            ppmOutLabels = new BufferedWriter(new FileWriter(fileLabels));
        	log.debug("Numero de images a procesar: "+numImages);
        	for(int index=1;index<=numImages;index++){
				setCurrent(index); //index of the image that we are interested in 
				image = readImage(); 
				label = readLabel();
				
	            rows = image.length; 
	            cols = image[0].length; 
	            //ppmOut.write("P3\n"); 
	            //ppmOut.write("" + rows + " " + cols + " 255\n"); 
	            for (int i = 0; i < rows; i++) { 
	                StringBuffer s = new StringBuffer(); 
	                for (int j = 0; j < cols; j++) { 
	                    s.append(image[i][j] + " " + image[i][j] + " " + image[i][j] + "  "); 
	                } 
	                
	                ppmOutImages.write(s.toString());
	                
	            }
	            ppmOutImages.write("\n");
	            ppmOutLabels.write(new Integer(label).toString()+"\n");
        	}
        } finally { 
            ppmOutImages.close();
            ppmOutLabels.close();
        } 
 
    } 
    
    /**
     * Writes the given image in the given file using the PPM data format. 
     *  
     * @param image 
     * @param ppmFileName 
     * @throws java.io.IOException 
     */ 
    public static void writeImageToPpm(int[][] image, String ppmFileName) throws IOException { 
        BufferedWriter ppmOut = null; 
        try { 
            ppmOut = new BufferedWriter(new FileWriter(ppmFileName)); 
 
            int rows = image.length; 
            int cols = image[0].length; 
            ppmOut.write("P3\n"); 
            ppmOut.write("" + rows + " " + cols + " 255\n"); 
            for (int i = 0; i < rows; i++) { 
                StringBuffer s = new StringBuffer(); 
                for (int j = 0; j < cols; j++) { 
                    s.append(image[i][j] + " " + image[i][j] + " " + image[i][j] + "  "); 
                } 
                ppmOut.write(s.toString()); 
            } 
        } finally { 
            ppmOut.close(); 
        } 
 
    } 
 
    /**
     * Constructs an instance managing the two given data files. Supports 
     * <code>NULL</code> value for one of the arguments in case reading only one 
     * of the files (images and labels) is required. 
     * 
     * @param imagesFile 
     *            Can be <code>NULL</code>. In that case all future operations 
     *            using that file will fail. 
     * @param labelsFile 
     *            Can be <code>NULL</code>. In that case all future operations 
     *            using that file will fail. 
     * @throws java.io.IOException 
     */ 
    public MnistManager(String imagesFile, String labelsFile) throws IOException { 
        if (imagesFile != null) { 
            images = new MnistImageFile(imagesFile, "r"); 
        } 
        if (labelsFile != null) { 
            labels = new MnistLabelFile(labelsFile, "r"); 
        } 
    } 
 
    /**
     * Reads the current image. 
     * 
     * @return matrix 
     * @throws java.io.IOException 
     */ 
    public int[][] readImage() throws IOException { 
        if (images == null) { 
            throw new IllegalStateException("Images file not initialized."); 
        } 
        return images.readImage(); 
    } 
 
    /**
     * Set the position to be read. 
     * 
     * @param index 
     */ 
    public void setCurrent(int index) { 
        images.setCurrentIndex(index); 
        labels.setCurrentIndex(index); 
    } 
 
    /**
     * Reads the current label. 
     * 
     * @return int 
     * @throws java.io.IOException 
     */ 
    public int readLabel() throws IOException { 
        if (labels == null) { 
            throw new IllegalStateException("labels file not initialized."); 
        } 
        return labels.readLabel(); 
    } 
 
    /**
     * Get the underlying images file as {@link MnistImageFile}. 
     *  
     * @return {@link MnistImageFile}. 
     */ 
    public MnistImageFile getImages() { 
        return images; 
    } 
 
    /**
     * Get the underlying labels file as {@link MnistLabelFile}. 
     *  
     * @return {@link MnistLabelFile}. 
     */ 
    public MnistLabelFile getLabels() { 
        return labels; 
    } 
}