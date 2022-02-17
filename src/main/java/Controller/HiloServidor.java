package Controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import Model.Mensaje;

public class HiloServidor implements Runnable {

	private Socket socket;
	private String currentUserNickName;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private Mensaje mensaje;

	/**
	 * CREACION DE UN HILO PARA MAGENAR EL SOCKET PASADO POR EL CONSTRUCTOR
	 * 
	 * @param socket DE UNO DE LOS CLIENTES DE SERVIDOR
	 * @throws IOException
	 */
	public HiloServidor(Socket socket, String nickname, Mensaje mensaje,  ObjectInputStream in, ObjectOutputStream out) throws IOException {
		this.socket = socket;
		this.currentUserNickName = nickname;
		this.mensaje = mensaje;
		this.out = out;
		this.in = in;
	}

	public void run() {
		try {
			// ESCRIBE AL CLIENTE EL NUMERO DE CLIENTE Y CONFIRMA EL INICIO DE SESION
			mensaje.setDescripcion("*******************************************************************************************************"
								+ "\n*  Has iniciado sesión Nº" + Servidor.nickNameSocketMap.size()
								+                             "                                                                            *"
								+ "\n*  1) [lista] para ver la lista de usuarios actualmente conectados                                    *"
								+ "\n*  2) [user] recibir un usuario con datos                                                             *"
								+ "\n*  3) [individual] para enviar mensajes a usuarios específicos                                        *"
								+ "\n*  4) [salir] para salir del chat                                                                     *"
								+ "\n*******************************************************************************************************");
			sendObject(new Mensaje(this.mensaje));
			
			
			// SE LEE TODOS LOS DATOS INTRODUCIDOS POR CONSOLA Y SE ACUTA EN CONSECUENCIA AL
			// TEXTO.
			int opcion= 0;

			while (opcion>=-1) {
				mensaje=(Mensaje)  in.readObject();
				mensaje.setDescripcion("");
				System.out.println(currentUserNickName + " Ingresó: " + mensaje.getComando());
				opcion = mensaje.getComando();

				try {
					switch (opcion) {
					case 1:
						showOnlineUsers();
						break;
					case 2:
						sendObject(new Mensaje(this.mensaje));
						break;
					case 3:
						break;

					case 0:
					default:
						mensaje.setDescripcion("El comando que ingresó es ilegal, ¡vuelva a ingresar!");
						sendObject(new Mensaje(this.mensaje));
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				
			}
		} catch (IOException e) {

			Servidor.nickNameSocketMap.remove(currentUserNickName);
			try {
				socket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		} catch (ClassNotFoundException e1) {
			System.out.println("Error al obtener mensaje del cliente");
			e1.printStackTrace();
		} finally {
			Servidor.nickNameSocketMap.remove(currentUserNickName);
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
		String users = "";
		users=users+("Los usuarios actualmente en línea son: \n");

		for (String nickName : Servidor.nickNameSocketMap.keySet()) {
			users=users+("[")+(nickName)+("]\n");
		}
		this.mensaje.setDescripcion(users);
		sendObject(new Mensaje(this.mensaje));
	}

	/**
	 * FUNCION PARA ENVIAR UN OBJETO AL CLIENTE CON EL SOCKET.
	 * @throws IOException
	 */
	private void sendObject(Mensaje mensaje) throws IOException {
		out.writeObject(mensaje);
	}
	
}
