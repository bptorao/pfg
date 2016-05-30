package bptg.uem.pfg.wclassifiers;


import java.io.IOException;

import java.io.StringReader;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;

import amten.ml.NNParams;
import amten.ml.matrix.Matrix;
import amten.ml.matrix.MatrixUtils;
import au.com.bytecode.opencsv.CSVReader;
import es.uem.etl.config.Configuracion;
import scala.Tuple2;



/**
 * Examples of using NeuralNetwork for classification.
 *
 * @author Johannes Amton - Version Adaptada PFG Bueka Torao - Integracion Hadoop & Spark
 * 
 *  
 */
public class NNClassificationMNISTHadoop  {
	
	private static Logger log = Logger.getLogger(NNClassificationMNISTHadoop.class);
	private static Configuracion configuracion = new Configuracion();
	

    /**
     * Performs classification of Handwritten digits,
     * using a subset (1000 rows) from the THE MNIST DATABASE.
     * <br></br>
     * Uses file uem/PFG/trabajo/Convolutional_Training_Digits_1000.csv
     *
     * @see <a href=" http://yann.lecun.com/exdb/mnist/">http://yann.lecun.com/exdb/mnist/</a></a>
     */
    public static void runMNISTClassificationHadoop() throws Exception {
    	boolean useConvolution = new Boolean(configuracion.value("nnclassifier.useConvolution"));
    	configuracion = new Configuracion();
    	Matrix data = null;
    	String fileName = "";
        
    	if (useConvolution) {
            System.out.println("Running classification on MNIST Digits dataset, with convolution...\n");
            log.debug("Running classification on MNIST Digits dataset, with convolution...\n");
        } else {
            System.out.println("Running classification on MNIST Digits dataset...\n");
            log.debug("Running classification on MNIST Digits dataset...\n");
        }
        // Read data from CSV-file
        int headerRows = new Integer(configuracion.value("nnclassifier.headerRows"));
        char separator = (configuracion.value("nnclassifier.separator")).charAt(0);
        fileName = configuracion.value("nnclassifier.filename");
        
        log.debug("Running for: ["+fileName+"] Separator: "+separator+" HeaderRows: "+headerRows+" ");
        
        if (configuracion.value("nnclassifier.readfrom").equalsIgnoreCase("hadoop")){
        	log.debug("Read data from CSV-file in Hadoop");
        	
        	data = readCSVfromHadoop(fileName, separator, headerRows);
        }else{
        	log.debug("Read data from CSV-file in disk");
        	log.debug("Use NNClassificationMNIST in order to run the algorithm without Hadoop & Spark");
        	System.exit(1);
        }
        System.out.println("Running training...");
        log.debug("Running training...");
        
        // Split data into training set and crossvalidation set.
        //float crossValidationPercent = 33;
        float crossValidationPercent = configuracion.valueFloat("nnclassifier.crossValidationPercent");
        Matrix[] split = MatrixUtils.split(data, crossValidationPercent, 0);
        Matrix dataTrain = split[0];
        Matrix dataCV = split[1];

        // First column contains the classification label. The rest are the indata.
        Matrix xTrain = dataTrain.getColumns(1, -1);
        Matrix yTrain = dataTrain.getColumns(0, 0);
        Matrix xCV = dataCV.getColumns(1, -1);
        Matrix yCV = dataCV.getColumns(0, 0);

        NNParams params = new NNParams();
        //params.numClasses = 10; // 10 digits to classify
        //params.hiddenLayerParams = useConvolution ? new NNParams.NNLayerParams[]{ new NNParams.NNLayerParams(20, 5, 5, 2, 2) , new NNParams.NNLayerParams(100, 5, 5, 2, 2) } :
        //    new NNParams.NNLayerParams[] { new NNParams.NNLayerParams(100) };
        //params.maxIterations = useConvolution ? 10 : 200;
        //params.learningRate = useConvolution ? 1E-2 : 0;
        
        params.numClasses = configuracion.valueInt("nnclassifier.numClasses");
        params.hiddenLayerParams = useConvolution ? new NNParams.NNLayerParams[]{ new NNParams.NNLayerParams(configuracion.valueInt("nnclasconvolution.NNLayerParams01"), configuracion.valueInt("nnclasconvolution.NNLayerParams02"), configuracion.valueInt("nnclasconvolution.NNLayerParams03"), configuracion.valueInt("nnclasconvolution.NNLayerParams04"), configuracion.valueInt("nnclasconvolution.NNLayerParams05")) , new NNParams.NNLayerParams(configuracion.valueInt("nnclasconvolution.NNLayerParams11"), configuracion.valueInt("nnclasconvolution.NNLayerParams12"), configuracion.valueInt("nnclasconvolution.NNLayerParams13"), configuracion.valueInt("nnclasconvolution.NNLayerParams14"), configuracion.valueInt("nnclasconvolution.NNLayerParams15")) } :
                                                    new NNParams.NNLayerParams[] { new NNParams.NNLayerParams(configuracion.valueInt("nnclassifier.NNLayerParams")) };
        params.maxIterations = useConvolution ? configuracion.valueInt("nnclasconvolution.maxIterations") : configuracion.valueInt("nnclassifier.maxIterations");
        params.learningRate = useConvolution ? configuracion.valueDouble("nnclasconvolution.learningRate") : configuracion.valueDouble("nnclassifier.learningRate");

        long startTime = System.currentTimeMillis();
        amten.ml.NeuralNetwork nn = new amten.ml.NeuralNetwork(params);
        nn.train(xTrain, yTrain);
        //objeto nn - ver donde se almacena
        System.out.println("\nTraining time: " + String.format("%.3g", (System.currentTimeMillis() - startTime) / 1000.0) + "s");

        int[] predictedClasses = nn.getPredictedClasses(xTrain);
        int correct = 0;
        for (int i = 0; i < predictedClasses.length; i++) {
            if (predictedClasses[i] == yTrain.get(i, 0)) {
                correct++;
            }
        }
        System.out.println("Training set accuracy: " + String.format("%.6g", (double) correct/predictedClasses.length*100) + "%");

        predictedClasses = nn.getPredictedClasses(xCV);
        correct = 0;
        for (int i = 0; i < predictedClasses.length; i++) {
            if (predictedClasses[i] == yCV.get(i, 0)) {
                correct++;
            }
        }
        
        System.out.println("Crossvalidation set accuracy: " + String.format("%.6g", (double) correct/predictedClasses.length*100) + "%");
    }
    
    public static class ParseLine implements FlatMapFunction<Tuple2<String, String>, String[]> {
        public Iterable<String[]> call(Tuple2<String, String> file) throws Exception {
          CSVReader reader = new CSVReader(new StringReader(file._2()));
          return reader.readAll();
        }
      }
    
    
    /**
     * Reads a CSV-file from Hadoop  into a Matrix.
     *
     * @param filename
     * @param separator Separator character between values.
     * @param headerLines Number of header lines to skip before reading data.
     * @return Matrix
     * @throws IOException
     */
	public static Matrix readCSVfromHadoop(String filename, char separator, int headerLines) throws IOException {
		
        
        JavaSparkContext sc = new JavaSparkContext(new SparkConf().setAppName("NNClassificationMNISTHadoop"));
        JavaPairRDD<String, String> csvData = sc.wholeTextFiles(filename);

        JavaRDD<String[]> keyedRDD = csvData.flatMap(new ParseLine());
        List<String[]> values = keyedRDD.collect();

		
		int numRows = values.size();
		int numCols = values.get(0).length;
		Matrix m = new Matrix(numRows, numCols);
		for (int row = headerLines; row < numRows; row++) {
			String[] rowValues = values.get(row);
			for (int col = 0; col < numCols; col++) {
				Double v = Double.parseDouble(rowValues[col]);
				m.set(row, col, v);
			}
		}

		return m;
	}
	
 
    public static void main(String[] args) throws Exception {
    	//runMNISTClassification(false);
        System.out.println("\n\n\n");
        log.info("runMNISTClassificationHadoop");
        runMNISTClassificationHadoop();
        System.out.println("\n\n\n");
        //runKaggleTitanicClassification();
    }
}
