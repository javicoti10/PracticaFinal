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

 //Esta clase representa un servlet para el inicio de sesión
 //Se encarga de validar las credenciales de usuario y redirigir a la página de bienvenida o de inicio de sesión según corresponda
 
@WebServlet("/loginservlet")
public class loginservlet extends HttpServlet {
	 private static final long serialVersionUID = 1L;
	 
//Método para conectar la base de datos
	    private Connection connectToDatabase() throws Exception {
	        String url = "jdbc:mysql://localhost/bdjava";
	        String username = "root";
	        String password = "";
	        Class.forName("com.mysql.cj.jdbc.Driver");
	        return DriverManager.getConnection(url, username, password);
	    }
	    
//Valida las credenciales del usuario
	    private boolean validateCredentials(String user, String pass) throws Exception {
	        Connection connection = connectToDatabase();
	        PreparedStatement statement = connection.prepareStatement("SELECT * FROM usuarios WHERE username = ? AND password = ?");
	        statement.setString(1, user);
	        statement.setString(2, pass);
	        ResultSet resultSet = statement.executeQuery();
	        return resultSet.next();
	    }
	    
	 //Maneja las peticiones POST al servlet.
	 //Valida las credenciales del usuario y redirige según el resultado.
	    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	        String username = request.getParameter("username");
	        String password = request.getParameter("password");
	        try {
	            if (validateCredentials(username, password)) {
	                // Redirecciona a welcome.jsp con el parámetro username
	                response.sendRedirect("welcome.jsp?username=" + username);
	            } else {
	                // Redirecciona a login.jsp si las credenciales no son válidas
	                response.sendRedirect("login.jsp");
	            }
	        } catch (Exception e) {
	            throw new ServletException("Error validating user credentials", e);
	        }
	    }

	}
