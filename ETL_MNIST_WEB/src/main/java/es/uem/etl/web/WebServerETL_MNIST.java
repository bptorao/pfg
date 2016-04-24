package es.uem.etl.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;




public class WebServerETL_MNIST extends HttpServlet
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static Logger logger = Logger.getLogger(WebServerETL_MNIST.class);
	//private String enlace= "<p><a href=\"/scaf\">Acceda al contexto de la tarea a ejecutar</a><p>";
 
	public WebServerETL_MNIST(){}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        /*response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("<h1>"+tarea+"</h1>");
        response.getWriter().println("<H2>"+mensaje+"</H2>");
        //response.getWriter().println(enlace);
        //ENLACES TAREAS VODAFONE
        response.getWriter().println("<p><a href=\"/scaf/borradoSoportes?ejecuta=borradosoportes\">Enlace ejecución tarea Borrado Soportes</a><p>");
        response.getWriter().println("session=" + request.getSession(true).getId());
        */
        System.out.println("   doGet called with URI: " + request.getRequestURI());
    }

/*    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
    	  System.out.println("   doGet called with URI: " + request.getRequestURI());
    	}
*/
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
    	  System.out.println("   doPost called with URI: " + request.getRequestURI());
    }
/*

    public void handle(String target,
		            Request baseRequest,
		            HttpServletRequest request,
		            HttpServletResponse response) throws IOException, ServletException
		{
			response.setContentType("text/html;charset=utf-8");
			response.setStatus(HttpServletResponse.SC_OK);
			baseRequest.setHandled(true);
			response.getWriter().println("<h1>Prueba Web SCAF Vodafone</h1>");
		}

   
    
	public static void main(String[] args) throws Exception
		{
		Server server = new Server(8070);
		server.setHandler(new WebServerSCAF());
		
		server.start();
		server.join();
		}
	*/
	  public static void main(String[] args) throws Exception
	    {
	        Server server = new Server(8070);
	 
	        ServletContextHandler context0 = new ServletContextHandler(ServletContextHandler.SESSIONS);
	        context0.setContextPath("/");
	        context0.addServlet(new ServletHolder(new WebServerETL_MNIST()),"/*");
	        //context0.addServlet(new ServletHolder(new WebServerSCAF("SCAF","Borrado de Soportes")),"/borradoSoportes/*");
	        context0.addServlet(new ServletHolder(new PreProcesoServlet()),"/preProceso.do");
	        
	        //context0.addServlet(new ServletHolder(new WebServerSCAF("Bonjour le Monde")),"/fr/*");
	        ResourceHandler rh = new ResourceHandler();
	        rh.setResourceBase("public");

	        HandlerList hl = new HandlerList();
	        hl.setHandlers(new Handler[]{rh, context0});

	 
	        //ContextHandlerCollection contexts = new ContextHandlerCollection();
	        //contexts.setHandlers(new Handler[] { context0});
	 
	        //server.setHandler(contexts);
	        server.setHandler(hl);
	 
	        server.start();
	        server.join();
	    }
}


