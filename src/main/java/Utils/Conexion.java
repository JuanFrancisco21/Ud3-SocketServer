package Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Conexion {
	private static Connection con;
	
	private final static String server="jdbc:Mysql://localhost";		//XMLUtil.loadDataXML().getServer();
	private final static String database="banco";		//XMLUtil.loadDataXML().getDatabase();
	private final static String username="root";		//XMLUtil.loadDataXML().getUserName();
	private final static String password="";		//XMLUtil.loadDataXML().getPassword();
	
	/**
	 * Metodo de conexion con la bbdd sql.
	 */
	public static void conecta() {
		 try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con=DriverManager.getConnection(server+"/"+database,username,password);
		} catch (ClassNotFoundException e) {
			System.out.println("Error al conectar la base de datos");
		} catch (SQLException e) {
			System.out.println("Error al conectar la base de datos");
			con=null;
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static Connection getConexion() {
		try {
			if (con == null) {
				conecta();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}
	
	public static void cerrarConexion() {
		try {
			con.close();
		} catch (SQLException e) {
			
		}
	}
		
	
	
}
