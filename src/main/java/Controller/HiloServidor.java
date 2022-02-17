package Controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import Model.Usuario;

public class HiloServidor implements Runnable {

	private Socket socket;
	private DataOutputStream out;
	private DataInputStream in;
	private String currentUserNickName;

	/**
	 * CREACION DE UN HILO PARA MAGENAR EL SOCKET PASADO POR EL CONSTRUCTOR
	 * 
	 * @param socket DE UNO DE LOS CLIENTES DE SERVIDOR
	 * @throws IOException
	 */
	public HiloServidor(Socket socket, DataOutputStream out, DataInputStream in, String nickname) throws IOException {
		this.socket = socket;
		this.out = out;
		this.in = in;
		this.currentUserNickName = nickname;
	}

	public void run() {
		try {
			// ESCRIBE AL CLIENTE EL NUMERO DE CLIENTE Y CONFIRMA EL INICIO DE SESION
			/*
			 * write(currentUserNickName + " Has iniciado sesión de usuario con éxito Nº" +
			 * Servidor.nickNameSocketMap.size());
			 */
			write("*******************************************************************************************************"
					+ "\n*  Has iniciado sesión Nº" + Servidor.nickNameSocketMap.size()
					+ "                                                                            *"
					+ "\n*  1) [lista] para ver la lista de usuarios actualmente conectados                                    *"
					+ "\n*  2) [user] recibir un usuario con datos                                                             *"
					+ "\n*  3) [individual] para enviar mensajes a usuarios específicos                                        *"
					+ "\n*  4) [salir] para salir del chat                                                                     *"
					+ "\n*******************************************************************************************************");

			// SE LEE TODOS LOS DATOS INTRODUCIDOS POR CONSOLA Y SE ACUTA EN CONSECUENCIA AL
			// TEXTO.
			String input = "";

			while (!"salir".equals(input)) {
				input = in.readUTF();
				System.out.println(currentUserNickName + " Ingresó: " + input);
				try {
					switch (Integer.parseInt(input)) {
					case 1:
						showOnlineUsers();
						break;
					case 2:
						sendObject(socket, new Usuario(0, "Juan", "Aguilar", false, "juan", "1234"));
						break;
					case 3:
						write("hola");
						break;
					case 4:
						input = "salir";
						break;
	
					default:
						write("El comando que ingresó es ilegal, ¡vuelva a ingresar!");
					}
				} catch (Exception e) {
					// TODO: handle exception
				}

				

			}
			Servidor.nickNameSocketMap.remove(currentUserNickName);
		} catch (IOException e) {

			Servidor.nickNameSocketMap.remove(currentUserNickName);
			try {
				in.close();
				out.close();
				socket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		} finally {
			Servidor.nickNameSocketMap.remove(currentUserNickName);
			try {
				in.close();
			} catch (IOException e) {
				// e.printStackTrace();
			}
			try {
				out.close();
			} catch (IOException e) {
				// e.printStackTrace();
			}
			try {
				socket.close();
			} catch (IOException e) {
				// e.printStackTrace();
			}

		}
	}

	/**
	 * FUNCION QUE CREA UN STRING AL CUAL SE LE AÑADEN TODOS LOS CLIENTES(SOCKETS),
	 * CONECTADOS AL SERVIDOR.
	 * 
	 * @throws IOException
	 */
	private void showOnlineUsers() throws IOException {
		StringBuilder users = new StringBuilder();
		users.append("Los usuarios actualmente en línea son: \n");

		for (String nickName : Servidor.nickNameSocketMap.keySet()) {
			users.append("[").append(nickName).append("]\n");
		}
		write(users.toString());
	}

	/**
	 * FUNCION PARA ENVIAR UN OBJETO AL CLIENTE CON EL SOCKET.
	 * 
	 * @param socket con la conexión al cliente.
	 * @param usuario Objeto que se envia.
	 * @throws IOException
	 */
	private void sendObject(Socket socket, Usuario usuario) throws IOException {
		SocketUtils.writeObjectDataOutput(socket, usuario);
	}
	
	/**
	 * FUNCION PARA ENVIAR MENSAJE MEDIANTE UN DATAOPUTPUTSTREAM(out). UTILIZA LA
	 * CLASE SKOCKETSUTILS.
	 * 
	 * @param message QUE SE ENVIA AL SOCKET DEL DATAOUTPUTSTREAM(out)
	 * @throws IOException
	 */
	private void write(String message) throws IOException {
		SocketUtils.writeToDataOutputStream(out, message);
	}
}
