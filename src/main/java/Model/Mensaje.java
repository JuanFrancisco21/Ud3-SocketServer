package Model;

import java.io.Serializable;

public class Mensaje implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String usuario;
	private String contraseña;
	private String descripcion;
	private int comando;
	private int dineroTransaccion;
	private Usuario user;
	private Cuenta cuenta;
	
	

	
	
	public Mensaje(String usuario, String contraseña, String descripcion, int comando, int dineroTransaccion,
			Usuario user, Cuenta cuenta) {
		this.usuario = usuario;
		this.contraseña = contraseña;
		this.comando = comando;
		this.descripcion = descripcion;
		this.dineroTransaccion = dineroTransaccion;
		this.user = user;
		this.cuenta = cuenta;
	}

	public Mensaje(Mensaje mensaje) {
		this.usuario = mensaje.usuario;
		this.contraseña = mensaje.contraseña;
		this.comando = mensaje.comando;
		this.descripcion = mensaje.descripcion;
		this.dineroTransaccion = mensaje.dineroTransaccion;
		this.user = mensaje.user;
		this.cuenta = mensaje.cuenta;
	}

	public Mensaje() {
		this("","","", 0, 0, null, null);
	}
	
	

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getContraseña() {
		return contraseña;
	}

	public void setContraseña(String contraseña) {
		this.contraseña = contraseña;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public int getComando() {
		return comando;
	}

	public void setComando(int comando) {
		this.comando = comando;
	}

	public int getDineroTransaccion() {
		return dineroTransaccion;
	}

	public void setDineroTransaccion(int dineroTransaccion) {
		this.dineroTransaccion = dineroTransaccion;
	}

	public Usuario getUser() {
		return user;
	}

	public void setUser(Usuario user) {
		this.user = user;
	}

	public Cuenta getCuenta() {
		return cuenta;
	}

	public void setCuenta(Cuenta cuenta) {
		this.cuenta = cuenta;
	}

	@Override
	public String toString() {
		return "Mensaje [usuario=" + usuario + ", contraseña=" + contraseña + ", descripcion=" + descripcion
				+ ", comando=" + comando + ", dineroTransaccion=" + dineroTransaccion 
				+ ", user=" + user.username + ", cuenta=" + cuenta.money + "]";
	}



	
}
