package Model.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Model.Cuenta;
import Utils.Conexion;

public class CuentaDAO extends Cuenta{
	private final static String INSERT = "INSERT INTO cuenta ( Dinero, Transacciones) "
			+ "VALUES (?,?) ";
	private final static String UPDATE = "INSERT INTO cuenta ( id, Dinero, Transacciones) "
			+ "VALUES (?,?,?) "
			+"ON DUPLICATE KEY UPDATE id=?, Dinero=?, Transacciones=?";
	private final static String SELECT_All = "SELECT * FROM cuenta";
	private final static String SELECT_by_Id = "SELECT * FROM cuenta WHERE id = ?";
	private final static String DELETE_by_Id = "DELETE FROM cuenta WHERE id = ?";

	public CuentaDAO() {
		super();
	}

	public CuentaDAO(Cuenta cuenta) {
		this.setId(cuenta.getId());
		this.setMoney(cuenta.getMoney());
		this.setTransactions(cuenta.getTransactions());

	}

	public CuentaDAO(Integer id) {
		this(CuentaDAO.List_Cuenta_By_Id(id));
	}

	/**
	 * List all the Accounts
	 *
	 * @return All the Accounts
	 */
	public static List<Cuenta> List_All_Cuentas() {
		List<Cuenta> result = new ArrayList<Cuenta>();
		Connection c = Conexion.getConexion();

		if (c != null) {
			try {
				PreparedStatement ps = c.prepareStatement(SELECT_All);
				ResultSet rs = ps.executeQuery();
				while (rs != null && rs.next()) {
					Cuenta cuenta = new Cuenta();
					cuenta.setId(rs.getInt("id"));
					cuenta.setMoney(rs.getFloat("Dinero"));
					cuenta.setTransactions(rs.getString("Transacciones"));
					result.add(cuenta);
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * List Account by id
	 *
	 * @param id unique for all the Account
	 * @return the Account with that id
	 */
	public static Cuenta List_Cuenta_By_Id(Integer id) {
		Cuenta result = new Cuenta();
		Connection c = Conexion.getConexion();

		if (c != null) {
			try {
				PreparedStatement ps = c.prepareStatement(SELECT_by_Id);
				ps.setInt(1, id);
				ResultSet rs = ps.executeQuery();
				if (rs.next()) {
					Cuenta cuenta = new Cuenta();
					cuenta.setId(rs.getInt("id"));
					cuenta.setMoney(rs.getFloat("Dinero"));
					cuenta.setTransactions(rs.getString("Transacciones"));
					result = cuenta;
				}
				rs.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}


	/**
	 * Create new Account.
	 * 
	 * @return true if the Account has been insert, false if not
	 */
	public int insert() {
		int rs = 0;
		Connection con = Conexion.getConexion();

		if (this.money>=0 && this.money!=null) {
			if (con != null) {
				try {
					PreparedStatement q = con.prepareStatement(INSERT);
					q.setFloat(1, this.money);
					q.setString(2, this.transactions);
					System.out.println(q.toString());
					rs = q.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return rs;
	}
	/**
	 * Update Accountif exist.
	 * 
	 * @return true if the Account has been updated, false if not
	 */
	public int update() {
		int rs = 0;
		Connection con = Conexion.getConexion();

		if (this.id>=0 && this.money!=null) {
			if (con != null) {
				try {
					PreparedStatement q = con.prepareStatement(UPDATE);
					q.setInt(1, this.id);
					q.setFloat(2, this.money);
					q.setString(3, this.transactions);
					q.setInt(4, this.id);
					q.setFloat(5, this.money);
					q.setString(6, this.transactions);
					rs = q.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return rs;
	}

	/**
	 * Remove a Account by id
	 *
	 * @param id unique for all the Account
	 * @return true if the Account has been removed, false if not
	 */
	public static boolean Remove_Cuenta_by_Id(Integer id) {
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
	 * Remove a Account
	 *
	 * @return true if the Account has been removed, false if not
	 */
	public boolean remove_Cuenta() {
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
