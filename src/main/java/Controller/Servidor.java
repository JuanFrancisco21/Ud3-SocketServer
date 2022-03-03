package Controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import Model.Mensaje;
import Model.Usuario;
import Model.DAO.UsuarioDAO;

public class Servidor {

	private static ServerSocket ss;
	public static final int PORT = 9999;
	public static final int CLIENTS = 2;	//NUMERO DE CLIENTES MAXIMO DEL SERVER.
	static Map<String, Socket> nickNameSocketMap = new HashMap<String, Socket>();
	
	private static Socket socket;
	private static String currentUserNickName;
	private static ObjectOutputStream out;
	private static ObjectInputStream in;
	private static Mensaje mensaje = new Mensaje();
	 
	
	public static void main(String[] args){
		try {
			//CREO EL SERVERSOCKET POR EL PUERTO 9999
			ss = new ServerSocket(PORT);
			System.out.println("El servidor iniciado y escuchando. PORT: " + PORT);

			//BUCLE PARA ACEPTAR SOCKET COMPROBANDO LA BASE DE DATOS.
			while (true) {
				try {
					socket = ss.accept();
					out = new ObjectOutputStream(socket.getOutputStream());
					in  = new ObjectInputStream(socket.getInputStream());
					
					//SI SE INICIA SESION CREA EL HILO E INFORMA POR CONSOLA.
					if (login()) {
						new Thread(new HiloServidor(socket,currentUserNickName,mensaje,in,out)).start();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}	
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * 	FUNCION PARA SETEAR EL NOMBRE DEL SOCKET Y GUARDARLO EN EL NICNAMESOCKETMAP DEL SERVIDOR
	 *  ESTO SIRVE PARA TENER CONTROL DE LOS USUARIOS ONLINE Y NO CONECTAR DOS USUARIOS A LA VEZ.
	 * @throws IOException
	 */
	private static boolean login() throws IOException {
		boolean result = false;
		try {
			while (true) {
				mensaje = (Mensaje) in.readObject();
				String nickName = mensaje.getUsuario();
				
				if (checkDatabase()) {
					if (!Servidor.nickNameSocketMap.containsKey(nickName)) {
						currentUserNickName = nickName;
						Servidor.nickNameSocketMap.put(nickName, socket);
						mensaje.setDescripcion("logeado");
						sendObject(mensaje);
						result = true;
						break;
					}else {
						mensaje.setDescripcion("Usuario ya conectado");
						sendObject(mensaje);
					}
				} else {
					mensaje.setDescripcion("Usuario no registrado");
					sendObject(mensaje);
				}
				
			}
		} catch (Exception e) {
			System.out.println("Usuario Desconectado.");
		}
		return result;
	}
	
	/**
	 * M�TODO EL CUAL INDICA SI FUNCIONA LA BASE DE DATOS
	 * @return BOOLEANO TRUE/FALSE.
	 */
	private static boolean checkDatabase() {
		boolean result = false;
		Usuario usuario = UsuarioDAO.List_User_By_Username(mensaje.getUsuario());
		if (usuario.getId()>=0) {
			if (usuario.getPassword().equals(mensaje.getPassword())) {
				mensaje.setUser(usuario);
				result = true;
			}
		}
		return result;
	}
	
	/**
	 * FUNCION PARA ENVIAR UN OBJETO AL CLIENTE CON EL SOCKET.
	 * @throws IOException
	 */
	private static void sendObject(Mensaje mensaje) throws IOException {
		out.flush();
		out.writeObject(new Mensaje(mensaje));

	}

}
