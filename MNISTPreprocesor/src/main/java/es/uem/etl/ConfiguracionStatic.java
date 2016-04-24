package es.uem.etl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.apache.log4j.Logger;





/**
 */
public class ConfiguracionStatic {
	private static Logger log = Logger.getLogger(ConfiguracionStatic.class); 
    private static Properties propiedades = null;
    private static final String CONFIGURATION_FILE = "es/uem/config/configuracion.properties";

  /*
   *bloque de inicializacion
   */
  static {
        try {

        init();

        } catch (Exception ex) {

            System.err.println("Config:No se puede abrir el fichero de propiedades: "+CONFIGURATION_FILE+": "+ex);
            propiedades=null;
        }

  }


    /**
    *  Constructor predeterminado
    */
    public ConfiguracionStatic()
    {

    }

    /**
    *  Inicializa los parametros por defecto
    */
    public static void init(){
       try{
       	if (propiedades==null) {
	       		 File file = new File(CONFIGURATION_FILE);
	       		 if(file!=null){
	       			log.debug("Config:Cargando propiedades del fichero:"+file.getAbsolutePath());
	       			InputStream inputStream = new FileInputStream(file);
	       			propiedades = new Properties();
		         	// InputStream inputStream = file.toURL().openStream();
		             propiedades.load(inputStream);

		             inputStream.close();
	       		 }
        
	         }else
	         	System.err.println("Config:No se puede encontrar el fichero properties. Usando ["+CONFIGURATION_FILE+"]");
      }
      catch(Exception e){
        System.out.println("Config:No se puede abrir el fichero de propiedades: "+e.getMessage());
        e.printStackTrace();
        propiedades = null;
      }

    }

 
    /**
    *  Devuelve el valor de una propiedad
    *  @param grupo Nombre del grupo de propiedades de configuracion
    *  @param propiedad Nombre de la propiedad dentro del grupo
    *  @return String El valor de la propiedad
    */
    public static String value(String grupo,String propiedad){
        if(propiedades == null)
            init();
        String valor=propiedades.getProperty(grupo+"."+propiedad, "");

        return valor;
    }


    /**
     *  @param grupo_propiedad, debe contener al menos un punto
     *  Devuelve el valor de una propiedad
     *  @return String El valor de la propiedad
     */
     public static String value(String grupo_propiedad){
         if(propiedades == null)
             init();

         String valor=null;
         if (grupo_propiedad.indexOf(".")!=-1){
             valor=propiedades.getProperty(grupo_propiedad);
         }
         return valor;
     }

    /**
     *  Cambia el valor de una propiedad
     *  @param grupo Nombre del grupo de propiedades de configuracion
     *  @param propiedad Nombre de la propiedad dentro del grupo
     *  @param valor Nuevo valor de la propiedad
     *
     *  @return String El valor de la propiedad
     */
     public synchronized static void setValue(String grupo,String propiedad, String valor){
        try{
     	if(propiedades == null)
             init();

         URL url =ConfiguracionStatic.class.getResource(CONFIGURATION_FILE);
         FileOutputStream outputStream;
         if (url!=null) {
         	propiedades.setProperty(grupo+"."+propiedad, valor);
         	outputStream = new FileOutputStream(url.getFile());
         	propiedades.store(outputStream, null);
         }
        }catch(Exception e){
        	log.error("Error cargando la propiedad "+grupo+"."+propiedad+"="+valor+" "+e.getMessage());
       }

     }

    /**
     * Obtiene una propiedad.
    *  @param grupo Nombre del grupo de propiedades de configuracion
    *  @param propiedad Nombre de la propiedad dentro del grupo
     * @param args argumentos que forman parte de la cadena resultado
     * @return
     */
    public static String getParamValue(String grupo,String propiedad, String args[]){
        // La cadena en el properties tendrá la forma "texto {0} otra parte {1} ... etc"
        // El primer argumento se sustituye por {0}, el segundo por {1}
        String cadena = value(grupo,propiedad);
        for (int i = 0; i < args.length; i ++) {
            int posicion = 0;
            if ( (posicion = cadena.indexOf("{" + i + "}")) != -1 ){
                cadena = cadena.substring(0, posicion) + args[i] + cadena.substring(posicion + 3, cadena.length());
            }
        }
        return cadena;
    }



}
