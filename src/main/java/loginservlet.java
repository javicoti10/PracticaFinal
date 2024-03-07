import java.io.IOException;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author [Javier Cotilla Segovia (2ºDAW)]
 * @version 1.0
 */

//Esta clase representa un servlet para el inicio de sesión
//Se encarga de validar las credenciales de usuario y redirigir a la página de bienvenida o mostrar un error según corresponda

@WebServlet("/loginservlet")
public class loginservlet extends HttpServlet {
	 private static final long serialVersionUID = 1L;
	 
/**
	* Método para conectar la base de datos.
	     * 
	* @return Una conexión a la base de datos.
	* @throws Exception Si hay algún problema al conectar con la base de datos.
*/
	 
	    private Connection connectToDatabase() throws Exception {
	        String url = "jdbc:mysql://localhost/bdjava";
	        String username = "root";
	        String password = "";
	        Class.forName("com.mysql.cj.jdbc.Driver");
	        return DriverManager.getConnection(url, username, password);
	    }
	    
	    
	  /**
	     * Valida las credenciales del usuario.
	     * 
	     * @param user El nombre de usuario.
	     * @param pass La contraseña del usuario.
	     * @return True si las credenciales son válidas, false en caso contrario.
	     * @throws Exception Si hay algún problema al validar las credenciales.
	  */
	    private boolean validateCredentials(String user, String pass) throws Exception {
	        Connection connection = connectToDatabase();
	        PreparedStatement statement = connection.prepareStatement("SELECT * FROM usuarios WHERE username = ? AND password = ?");
	        statement.setString(1, user);
	        statement.setString(2, pass);
	        ResultSet resultSet = statement.executeQuery();
	        return resultSet.next();
	    }
	    
	    
	    /**
	     * Maneja las peticiones POST al servlet.
	     * 
	     * @param request La solicitud HTTP.
	     * @param response La respuesta HTTP.
	     * @throws ServletException Si hay algún problema al manejar la solicitud.
	     * @throws IOException Si hay algún problema de entrada/salida.
	     */
	    // Valida las credenciales del usuario y redirige según el resultado.
	    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	        String username = request.getParameter("username");
	        String password = request.getParameter("password");
	        try {
	            if (validateCredentials(username, password)) {
	                // Redirecciona a welcome.jsp con el parámetro username
	                response.sendRedirect("welcome.jsp?username=" + username);
	            } else {
	                // Muestra un mensaje de error si las credenciales no son válidas
	                PrintWriter out = response.getWriter();
	                out.println("<html><body>");
	                out.println("<h2>Error: Las credenciales no son válidas</h2>");
	                out.println("</body></html>");
	            }
	        } catch (Exception e) {
	            throw new ServletException("Error validating user credentials", e);
	        }
	    }
	}


