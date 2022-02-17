package Model.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Model.Usuario;
import Utils.Conexion;

public class UsuarioDAO extends Usuario {
	private final static String INSERTUPDATE = "INSERT INTO usuario (Nombre, Apellido, Administrador, usuario, contraseña) "
			+ "VALUES (?,?,?,?,?) "
			+ "ON DUPLICATE KEY UPDATE Nombre=?, Apellido=?, Administrador=?, usuario=?, contraseña=?";
	private final static String SELECT_All = "SELECT * FROM usuario";
	private final static String SELECT_by_Id = "SELECT * FROM usuario WHERE id = ?";
	private final static String SELECT_by_Username = "SELECT *  FROM usuario WHERE usuario = ?";
	private final static String DELETE_by_Id = "DELETE FROM usuario WHERE id = ?";

	public UsuarioDAO() {
		super();
	}

	public UsuarioDAO(Usuario user) {
		this.setId(user.getId());
		this.setName(user.getName());
		this.setApellido(user.getApellido());
		this.setAdministrador(user.getAdministrador());
		this.setUsername(user.getUsername());
		this.setPassword(user.getPassword());

	}

	public UsuarioDAO(Integer id) {
		this(UsuarioDAO.List_User_By_Id(id));
	}

	/**
	 * List all the Users
	 *
	 * @return All the Users
	 */
	public static List<Usuario> List_All_User() {
		List<Usuario> result = new ArrayList<Usuario>();
		Connection c = Conexion.getConexion();

		if (c != null) {
			try {
				PreparedStatement ps = c.prepareStatement(SELECT_All);
				ResultSet rs = ps.executeQuery();
				while (rs != null && rs.next()) {
					Usuario user = new Usuario();
					user.setId(rs.getInt("id"));
					user.setName(rs.getString("Nombre"));
					user.setApellido(rs.getString("Apellido"));
					user.setAdministrador(rs.getBoolean("Administrador"));
					user.setUsername(rs.getString("usuario"));
					user.setPassword(rs.getString("contraseña"));
					result.add(user);
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * List Users by id
	 *
	 * @param id unique for all the User
	 * @return the User with that id
	 */
	public static Usuario List_User_By_Id(Integer id) {
		Usuario result = new Usuario();
		Connection c = Conexion.getConexion();

		if (c != null) {
			try {
				PreparedStatement ps = c.prepareStatement(SELECT_by_Id);
				ps.setInt(1, id);
				ResultSet rs = ps.executeQuery();
				if (rs.next()) {
					Usuario user = new Usuario();
					user.setId(rs.getInt("id"));
					user.setName(rs.getString("Nombre"));
					user.setApellido(rs.getString("Apellido"));
					user.setAdministrador(rs.getBoolean("Administrador"));
					user.setUsername(rs.getString("usuario"));
					user.setPassword(rs.getString("contraseña"));
					result = user;
				}
				rs.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * List Users by username
	 *
	 * @param username unique for all the User
	 * @return the User with that name
	 */
	public static Usuario List_User_By_Username(String username) {
		Usuario result = new Usuario();
		try {
			Connection c = Conexion.getConexion();

			if (c != null) {
				try {
					PreparedStatement ps = c.prepareStatement(SELECT_by_Username);
					ps.setString(1, username);
					ResultSet rs = ps.executeQuery();
					if (rs.next()) {
						Usuario user = new Usuario();
						user.setId(rs.getInt("id"));
						user.setName(rs.getString("Nombre"));
						user.setApellido(rs.getString("Apellido"));
						user.setAdministrador(rs.getBoolean("Administrador"));
						user.setUsername(rs.getString("usuario"));
						user.setPassword(rs.getString("contraseña"));
						result = user;
					}
					rs.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Create new User if don´t exist, or update if exist.
	 * 
	 * @return true if the User has been updated/insert, false if not
	 */
	public synchronized int insert_update() {
		int rs = 0;
		Connection con = Conexion.getConexion();

		if (con != null) {
			try {
				PreparedStatement q = con.prepareStatement(INSERTUPDATE);
				q.setString(1, this.name);
				q.setString(2, this.apellido);
				q.setBoolean(3, this.administrador);
				q.setString(4, this.username);
				q.setString(5, this.password);
				q.setString(6, this.name);
				q.setString(7, this.apellido);
				q.setBoolean(8, this.administrador);
				q.setString(9, this.username);
				q.setString(10, this.password);
				rs = q.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return rs;
	}

	/**
	 * Remove a User by id
	 *
	 * @param id unique for all the User
	 * @return true if the User has been removed, false if not
	 */
	public synchronized  static boolean Remove_User_by_Id(Integer id) {
		boolean result = false;
		Connection c = Conexion.getConexion();

		if (c != null) {
			try {
				PreparedStatement ps = c.prepareStatement(DELETE_by_Id);
				ps.setInt(1, id);
				int i = ps.executeUpdate();
				if (i == 1) {
					result = true;
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * Remove a User
	 *
	 * @return true if the User has been removed, false if not
	 */
	public synchronized boolean remove_User() {
		boolean result = false;
		Connection c = Conexion.getConexion();

		if (c != null) {
			try {
				PreparedStatement ps = c.prepareStatement(DELETE_by_Id);
				ps.setInt(1, this.getId());
				int i = ps.executeUpdate();
				if (i == 1) {
					result = true;
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

}
