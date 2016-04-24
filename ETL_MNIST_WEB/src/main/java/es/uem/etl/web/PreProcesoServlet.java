package es.uem.etl.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import es.uem.etl.PreProceso;

public class PreProcesoServlet extends HttpServlet 
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(PreProceso.class);

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        String q = req.getParameter("ejecuta");
        PrintWriter out = resp.getWriter();
        
        System.out.println("   doPost called with URI: " + req.getRequestURI());
        
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
    	PreProceso preproceso = new PreProceso();
    	PrintWriter out = response.getWriter();
    	HashMap<String,String> paramsMap = new HashMap();
    	log.debug("ETL MNIST Pre procesando colección - generador ficheros de imágenes");
    	
    	String descarga = request.getParameter("descarga");
    	String directorio = request.getParameter("directorio");
    	String obtenerNimages = request.getParameter("obtenerNimages");
    	String writeImage = request.getParameter("writeImageto");
    	
    	log.debug("Parametros leídos desde web: ");
    	log.debug("Parametro: descarga.mnist = "+descarga);
    	log.debug("Parametro: directorio.mnist = "+directorio);
    	log.debug("Parametro: obtenerN.images = "+obtenerNimages);
    	log.debug("Parametro: writeImage.to = "+writeImage);
    	

    	
    	if(descarga!=null && !descarga.equals(""))
    		paramsMap.put("descarga.mnist", descarga);
    	
    	if(directorio!=null && !directorio.equals(""))
    		paramsMap.put("directorio.mnist", directorio);
    	
    	if(obtenerNimages!=null && !obtenerNimages.equals(""))
    		paramsMap.put("obtenerN.images", obtenerNimages);
    	
    	if(writeImage!=null && !writeImage.equals(""))
    		paramsMap.put("writeImage.to", writeImage);
    	
    
	    //System.out.print("parametro leido: "+field);
	    
	    response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        //response.getWriter().println("<H2>Tarea ejecutada: "+field+" </H2>");
        
        try{
            
        	preproceso.ejecuta(paramsMap);
	
	        out.println("﻿﻿<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">"
						+"<html xmlns=\"http://www.w3.org/1999/xhtml\">                                                                     "
						+"<head>                                                                                                            "
						+"<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" />                                         "
						+"<title>ETL MNIST Web</title>                                                                                      "
						+"<!--<link href=\"estilos.css\" rel=\"stylesheet\" type=\"text/css\" />-->                                         "
						+"    <!-- Bootstrap -->                                                                                            "
						+"    <link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css\" />    "
						+"	<!-- Script -->                                                                                                  "
						+"	   <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->                                                "
						+"    <script src=\"https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js\"></script>                   "
						+"    <!-- Include all compiled plugins (below), or include individual files as needed -->                          "
						+"    <script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js\" ></script>                "
						+"	<!-- -->                                                                                                         "
						+"	<script src=\"script.js\" type=\"text/javascript\"></script>                                                     "
						+"</head>                                                                                                           "
						+"<body>                                                                                                            "
						+"	<div class=\"page-header\">                                                                                      "
						+"		<img src=\"UEM_logo.png\" class=\"img\" width=\"367\" height=\"92\" alt=\"\" /><br />                          "
						+"		<h2>ETL MNIST Web<small> Proyecto Fin de Grado </small></h2>                                                   "
						+"                                                                                                                  "
						+"		<h4>PreProceso de imágenes MNIST <strong>Colección: http://yann.lecun.com/exdb/mnist/</strong></h4>            "
						+"                                                                                                                  "
						+"	</div>                                                                                                           "
						+"		<div>                                                                                                          "
						+"		<div class=\"btn-group btn-group-justified\" role=\"group\" aria-label=\"...\">                                "
						+"			<a href=\"/index.html\" class=\"modulomenu\" title=\"\">ETL</a>                                              "
						+"			<a href=\"http://yann.lecun.com/exdb/mnist/\" class=\"modulomenu\" title=\"\">Colección</a>                  "
						+"		</div>                                                                                                         "
						+"		                                                                                                               "
						+"<!-- / header-->                                                                                                  "
						+"<!-- content -->                                                                                                  "
						+"                                                                                                                  "
						+"		<div id=\"cuerpo\" class=\"panel panel-default\">                                                              "
						+"			<div id=\"cuerpocentro\"  class=\"panel-body\">                                                              "
						+"				<div class=\"row\">                                                                                        "
						+"					                                                                                                         "
						+"					                                                                                                         "
						+"				</div>                                                                                                     "
						+"				<div class=\"row\">                                                                                        ");
	        out.println("<h1>Tarea ETL MNIST Ejecutada</h1>");
	        out.println("<a href=\"/index.html\" class=\"modulomenu\" title=\"\">Volver</a> ");
	        out.println("﻿ </div> "
						+"			</div>                                      "
						+"		</div>                                        "
						+"	</div>                                          "
						+"	<div>                                           "
						+"		<ol class=\"breadcrumb\">                     "
						+"			<li><a href=\"/index.html\">Home</a></li>   "
						+"		</ol>                                         "
						+"	</div>		                                      "
						+"</body>                                           "
						+"</html>                                           ");
        
        }catch(Exception e){
        	out.println("<html><head>");
	        out.println("</head><body>");
            out.println("Error Ejecutando ETL MNIST");
            out.println("<a href=\"/index.html\" class=\"modulomenu\" title=\"\">Volver</a>");
            out.println("</body>");
            out.println("</html>");
        }
        
        
        //response.getWriter().println("session=" + request.getSession(true).getId());
        
    }
}