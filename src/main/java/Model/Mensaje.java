package Model;

import java.io.Serializable;

public class Mensaje implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String usuario;
	private String contraseña;
	private int comando;
	private String descripcion;
	private int dinero;
	
	

	public Mensaje(String usuario, String contraseña, int comando, String descripcion, int dinero) {
		this.usuario = usuario;
		this.contraseña = contraseña;
		this.comando = comando;
		this.descripcion = descripcion;
		this.dinero = dinero;
	}
	
	public Mensaje(Mensaje mensaje) {
		this.usuario = mensaje.usuario;
		this.contraseña = mensaje.contraseña;
		this.comando = mensaje.comando;
		this.descripcion = mensaje.descripcion;
		this.dinero = mensaje.dinero;
	}

	public Mensaje() {
		this("","",0,"",0);
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

	public int getComando() {
		return comando;
	}

	public void setComando(int comando) {
		this.comando = comando;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public int getDinero() {
		return dinero;
	}

	public void setDinero(int dinero) {
		this.dinero = dinero;
	}

	@Override
	public String toString() {
		return "Mensaje [usuario=" + usuario + ", contraseña=" + contraseña + ", comando=" + comando + ", descripcion="
				+ descripcion + ", dinero=" + dinero + "]";
	}

	
}
