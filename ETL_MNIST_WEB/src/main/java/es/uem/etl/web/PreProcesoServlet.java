package es.uem.etl.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.uem.etl.PreProceso;

public class PreProcesoServlet extends HttpServlet 
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        String q = req.getParameter("ejecuta");
        PrintWriter out = resp.getWriter();
        
        try{
        
	        PreProceso.ejecuta();
	
	        out.println("<html>");
	        out.println("<body>");
	        out.println("Tarea ETL MNIST Ejecutada");
	        out.println("</body>");
	        out.println("</html>");
        
        }catch(Exception e){
        	out.println("<html>");
            out.println("<body>");
            out.println("Error Ejecutando ETL MNIST");
            out.println("</body>");
            out.println("</html>");
        }
        
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
    	 PrintWriter out = response.getWriter();
    	//String field = request.getParameter("ejecuta");
	    
	    //System.out.print("parametro leido: "+field);
	    
	    response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        //response.getWriter().println("<H2>Tarea ejecutada: "+field+" </H2>");
        
        try{
            
	        PreProceso.ejecuta();
	
	        out.println("<html>");
	        out.println("<body>");
	        out.println("Tarea ETL MNIST Ejecutada");
	        out.println("</body>");
	        out.println("</html>");
        
        }catch(Exception e){
        	out.println("<html>");
            out.println("<body>");
            out.println("Error Ejecutando ETL MNIST");
            out.println("</body>");
            out.println("</html>");
        }
        
        
        response.getWriter().println("session=" + request.getSession(true).getId());
        
    }
}