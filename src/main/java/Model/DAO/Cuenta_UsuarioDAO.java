package Model.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Model.Cuenta;
import Model.Cuenta_Usuario;
import Model.Usuario;
import Utils.Conexion;

public class Cuenta_UsuarioDAO extends Cuenta_Usuario{
	private final static String INSERT = "INSERT INTO cuenta_usuario ( id_usuario, id_cuenta) "
			+ "VALUES (?,?) ";
	private final static String INSERTUPDATE = "INSERT INTO cuenta_usuario (id_usuario, id_cuenta) "
			+ "VALUES (?,?) "
			+ "ON DUPLICATE KEY UPDATE id=?, id_usuario=?, id_cuenta=?";
	private final static String SELECT_All = "SELECT * FROM cuenta_usuario";
	private final static String SELECT_by_Id = "SELECT * FROM cuenta_usuario WHERE id = ?";
	private final static String SELECT_by_User = "SELECT *  FROM cuenta_usuario WHERE id_usuario = ?";
	private final static String SELECT_by_Cuenta = "SELECT *  FROM cuenta_usuario WHERE id_cuenta = ?";
	private final static String DELETE_by_Id = "DELETE FROM cuenta_usuario WHERE id = ?";

	public Cuenta_UsuarioDAO() {
		super();
	}

	public Cuenta_UsuarioDAO(Cuenta_Usuario relacion) {
		this.setId(relacion.getId());
		this.setUsuario(relacion.getUsuario());
		this.setCuenta(relacion.getCuenta());

	}
	public Cuenta_UsuarioDAO(Usuario usuario, Cuenta cuenta) {
		this.setId(-1);
		this.setUsuario(usuario);
		this.setCuenta(cuenta);

	}

	public Cuenta_UsuarioDAO(Integer id) {
		this(Cuenta_UsuarioDAO.List_Relation_By_Id(id));
	}

	/**
	 * List all the Cuenta_Usuario
	 *
	 * @return All the Cuenta_Usuario
	 */
	public static List<Cuenta_Usuario> List_All_Relation() {
		List<Cuenta_Usuario> result = new ArrayList<Cuenta_Usuario>();
		Connection c = Conexion.getConexion();

		if (c != null) {
			try {
				PreparedStatement ps = c.prepareStatement(SELECT_All);
				ResultSet rs = ps.executeQuery();
				while (rs != null && rs.next()) {
					Cuenta_Usuario relacion = new Cuenta_Usuario();
					relacion.setId(rs.getInt("id"));
					relacion.setUsuario(UsuarioDAO.List_User_By_Id(rs.getInt("id_usuario")));
					relacion.setCuenta(CuentaDAO.List_Cuenta_By_Id(rs.getInt("id_cuenta")));
					result.add(relacion);
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * List Cuenta_Usuario by id
	 *
	 * @param id unique for all the Cuenta_Usuario
	 * @return the Cuenta_Usuario with that id
	 */
	public static Cuenta_Usuario List_Relation_By_Id(Integer id) {
		Cuenta_Usuario result = new Cuenta_Usuario();
		Connection c = Conexion.getConexion();

		if (c != null) {
			try {
				PreparedStatement ps = c.prepareStatement(SELECT_by_Id);
				ps.setInt(1, id);
				ResultSet rs = ps.executeQuery();
				if (rs.next()) {
					Cuenta_Usuario relacion = new Cuenta_Usuario();
					relacion.setId(rs.getInt("id"));
					relacion.setUsuario(UsuarioDAO.List_User_By_Id(rs.getInt("id_usuario")));
					relacion.setCuenta(CuentaDAO.List_Cuenta_By_Id(rs.getInt("id_cuenta")));
					result = relacion;
				}
				rs.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * List Cuenta_Usuario by id_user
	 *
	 * @param id_user unique for all the Cuenta_Usuario
	 * @return the Cuenta_Usuario with that id_user
	 */
	public static Cuenta_Usuario List_Relation_By_User(Integer id_user) {
		Cuenta_Usuario result = new Cuenta_Usuario();
		try {
			Connection c = Conexion.getConexion();

			if (c != null) {
				try {
					PreparedStatement ps = c.prepareStatement(SELECT_by_User);
					ps.setInt(1, id_user);
					ResultSet rs = ps.executeQuery();
					if (rs.next()) {
						Cuenta_Usuario relacion = new Cuenta_Usuario();
						relacion.setId(rs.getInt("id"));
						relacion.setUsuario(UsuarioDAO.List_User_By_Id(rs.getInt("id_usuario")));
						relacion.setCuenta(CuentaDAO.List_Cuenta_By_Id(rs.getInt("id_cuenta")));
						if (relacion.getUsuario()!=null && relacion.getCuenta()!=null) {
							result = relacion;
							
						}
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
	 * List Cuenta_Usuario by id_cuenta
	 *
	 * @param id_cuenta unique for all the Cuenta_Usuario
	 * @return the Cuenta_Usuario with that id_cuenta
	 */
	public static Cuenta_Usuario List_Relation_By_Account(Integer id_cuenta) {
		Cuenta_Usuario result = new Cuenta_Usuario();
		try {
			Connection c = Conexion.getConexion();

			if (c != null) {
				try {
					PreparedStatement ps = c.prepareStatement(SELECT_by_Cuenta);
					ps.setInt(1, id_cuenta);
					ResultSet rs = ps.executeQuery();
					if (rs.next()) {
						Cuenta_Usuario relacion = new Cuenta_Usuario();
						relacion.setId(rs.getInt("id"));
						relacion.setUsuario(UsuarioDAO.List_User_By_Id(rs.getInt("id_usuario")));
						relacion.setCuenta(CuentaDAO.List_Cuenta_By_Id(rs.getInt("id_cuenta")));
						if (relacion.getUsuario()!=null && relacion.getCuenta()!=null) {
							result = relacion;
							
						}
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
				q.setInt(1, this.usuario.getId());
				q.setInt(2, this.cuenta.getId());
				q.setInt(2, this.id);
				q.setInt(3, this.usuario.getId());
				q.setInt(4, this.cuenta.getId());
				rs = q.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return rs;
	}
	
	
	/**
	 * Create new User if don´t exist.
	 * 
	 * @return true if the User has been insert.
	 */
	public synchronized int insert() {
		int rs = 0;
		Connection con = Conexion.getConexion();

		if (con != null) {
			try {
				PreparedStatement q = con.prepareStatement(INSERT);
				q.setInt(1, this.usuario.getId());
				q.setInt(2, this.cuenta.getId());
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
	public synchronized static boolean Remove_Relation_by_Id(Integer id) {
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
	public synchronized boolean remove_Relation() {
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
