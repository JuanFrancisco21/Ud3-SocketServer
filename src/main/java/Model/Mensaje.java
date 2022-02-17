package Model;

import java.io.Serializable;

public class Mensaje implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String usuario;
	private String contrase�a;
	private int comando;
	private String descripcion;
	private int dinero;
	
	

	public Mensaje(String usuario, String contrase�a, int comando, String descripcion, int dinero) {
		this.usuario = usuario;
		this.contrase�a = contrase�a;
		this.comando = comando;
		this.descripcion = descripcion;
		this.dinero = dinero;
	}
	
	public Mensaje(Mensaje mensaje) {
		this.usuario = mensaje.usuario;
		this.contrase�a = mensaje.contrase�a;
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

	public String getContrase�a() {
		return contrase�a;
	}

	public void setContrase�a(String contrase�a) {
		this.contrase�a = contrase�a;
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
		return "Mensaje [usuario=" + usuario + ", contrase�a=" + contrase�a + ", comando=" + comando + ", descripcion="
				+ descripcion + ", dinero=" + dinero + "]";
	}

	
}
