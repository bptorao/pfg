package es.uem.etl.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Properties;

import org.apache.log4j.Logger;


/**
 */
public class Configuracion {
	private static Logger log = Logger.getLogger(Configuracion.class); 
    private Properties propiedades = null;
    private static final String CONFIGURATION_FILE = "es/uem/config/configuracion.properties";
    private HashMap propiedadesMap = null;


    /**
    *  Constructor predeterminado
    */
    public Configuracion()
    {
    	init();
    	propiedadesMap = new HashMap<String,String>();
    }

    /**
     *  Constructor predeterminado
     */
     public Configuracion(HashMap params)
     {
     	init();
     	if(params == null){
     		propiedadesMap = new HashMap<String,String>();
     	}else{
     		propiedadesMap = params;
     	}
     }
     
    /**
    *  Inicializa los parametros por defecto
    */
    public void init(){
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
     *  @param grupo_propiedad, debe contener al menos un punto
     *  Devuelve el valor de una propiedad
     *  @return String El valor de la propiedad
     */
     public String value(String grupo_propiedad){
    	 String valor=null;
    	 if(propiedades == null)
             init();
         if(propiedadesMap.containsKey(grupo_propiedad)){
        	 valor = (String)propiedadesMap.get(grupo_propiedad);
         }else{
        	 if (grupo_propiedad.indexOf(".")!=-1){
                 valor=propiedades.getProperty(grupo_propiedad);
             }
         }
         return valor;
     }

     /**
      *  @param grupo_propiedad, debe contener al menos un punto
      *  Devuelve el valor de una propiedad parseado a Int
      *  @return int El valor de la propiedad parseado a int
      */
      public int valueInt(String grupo_propiedad){
     	 String valor=null;
     	 if(propiedades == null)
              init();
          if(propiedadesMap.containsKey(grupo_propiedad)){
         	 valor = (String)propiedadesMap.get(grupo_propiedad);
          }else{
         	 if (grupo_propiedad.indexOf(".")!=-1){
                  valor=propiedades.getProperty(grupo_propiedad);
              }
          }
          return new Integer(valor.trim()).intValue();
      }
      
      /**
       *  @param grupo_propiedad, debe contener al menos un punto
       *  Devuelve el valor de una propiedad parseado a double
       *  @return double El valor de la propiedad parseado a double
       */
       public double valueDouble(String grupo_propiedad){
      	 String valor=null;
      	 if(propiedades == null)
               init();
           if(propiedadesMap.containsKey(grupo_propiedad)){
          	 valor = (String)propiedadesMap.get(grupo_propiedad);
           }else{
          	 if (grupo_propiedad.indexOf(".")!=-1){
                   valor=propiedades.getProperty(grupo_propiedad);
               }
           }
           return new Double(valor.trim()).doubleValue();
       }

       /**
        *  @param grupo_propiedad, debe contener al menos un punto
        *  Devuelve el valor de una propiedad parseado a float
        *  @return double El valor de la propiedad parseado a float
        */
        public float valueFloat(String grupo_propiedad){
       	 String valor=null;
       	 if(propiedades == null)
                init();
            if(propiedadesMap.containsKey(grupo_propiedad)){
           	 valor = (String)propiedadesMap.get(grupo_propiedad);
            }else{
           	 if (grupo_propiedad.indexOf(".")!=-1){
                    valor=propiedades.getProperty(grupo_propiedad);
                }
            }
            return new Float(valor.trim()).floatValue();
        }

}
