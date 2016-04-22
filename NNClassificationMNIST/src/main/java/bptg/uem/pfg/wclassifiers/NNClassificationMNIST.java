package bptg.uem.pfg.wclassifiers;

import amten.ml.NNParams;
import amten.ml.matrix.Matrix;
import amten.ml.matrix.MatrixUtils;

/**
 * Examples of using NeuralNetwork for classification.
 *
 * @author Johannes AmtÃ©n - Versión Adaptada PFG Bueká Torao
 * 
 *  
 */
public class NNClassificationMNIST {

    /**
     * Performs classification of Handwritten digits,
     * using a subset (1000 rows) from the THE MNIST DATABASE.
     * <br></br>
     * Uses file uem/PFG/trabajo/Convolutional_Training_Digits_1000.csv
     *
     * @see <a href=" http://yann.lecun.com/exdb/mnist/">http://yann.lecun.com/exdb/mnist/</a></a>
     */
    public static void runMNISTClassification(boolean useConvolution) throws Exception {
        if (useConvolution) {
            System.out.println("Running classification on MNIST Digits dataset, with convolution...\n");
        } else {
            System.out.println("Running classification on MNIST Digits dataset...\n");
        }
        // Read data from CSV-file
        int headerRows = 1;
        char separator = ',';
        Matrix data = MatrixUtils.readCSV("c:/uem/PFG/trabajo/Convolutional_Training_Digits_1000.csv", separator, headerRows);
        
        //Integrar con Hadoop aquí para obtener la matriz
        
        // Split data into training set and crossvalidation set.
        float crossValidationPercent = 33;
        Matrix[] split = MatrixUtils.split(data, crossValidationPercent, 0);
        Matrix dataTrain = split[0];
        Matrix dataCV = split[1];

        // First column contains the classification label. The rest are the indata.
        Matrix xTrain = dataTrain.getColumns(1, -1);
        Matrix yTrain = dataTrain.getColumns(0, 0);
        Matrix xCV = dataCV.getColumns(1, -1);
        Matrix yCV = dataCV.getColumns(0, 0);

        NNParams params = new NNParams();
        params.numClasses = 10; // 10 digits to classify
        params.hiddenLayerParams = useConvolution ? new NNParams.NNLayerParams[]{ new NNParams.NNLayerParams(20, 5, 5, 2, 2) , new NNParams.NNLayerParams(100, 5, 5, 2, 2) } :
                                                    new NNParams.NNLayerParams[] { new NNParams.NNLayerParams(100) };
        params.maxIterations = useConvolution ? 10 : 200;
        params.learningRate = useConvolution ? 1E-2 : 0;

        long startTime = System.currentTimeMillis();
        amten.ml.NeuralNetwork nn = new amten.ml.NeuralNetwork(params);
        nn.train(xTrain, yTrain);
        System.out.println("\nTraining time: " + String.format("%.3g", (System.currentTimeMillis() - startTime) / 1000.0) + "s");

        int[] predictedClasses = nn.getPredictedClasses(xTrain);
        int correct = 0;
        for (int i = 0; i < predictedClasses.length; i++) {
            if (predictedClasses[i] == yTrain.get(i, 0)) {
                correct++;
            }
        }
        System.out.println("Training set accuracy: " + String.format("%.3g", (double) correct/predictedClasses.length*100) + "%");

        predictedClasses = nn.getPredictedClasses(xCV);
        correct = 0;
        for (int i = 0; i < predictedClasses.length; i++) {
            if (predictedClasses[i] == yCV.get(i, 0)) {
                correct++;
            }
        }
        System.out.println("Crossvalidation set accuracy: " + String.format("%.3g", (double) correct/predictedClasses.length*100) + "%");
    }

 
    public static void main(String[] args) throws Exception {
    	runMNISTClassification(false);
        System.out.println("\n\n\n");
        runMNISTClassification(true);
        System.out.println("\n\n\n");
        //runKaggleTitanicClassification();
    }
}
