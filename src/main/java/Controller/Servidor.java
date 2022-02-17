package Controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Servidor {

	private static ServerSocket ss;
	public static final int PORT = 9999;
	public static final int CLIENTS = 2;	//NUMERO DE CLIENTES MAXIMO DEL SERVER.
	static Map<String, Socket> nickNameSocketMap = new HashMap<String, Socket>();
	
	private static Socket socket;
	private static DataOutputStream out;
	private static DataInputStream in;
	private static String currentUserNickName;
	 
	
	public static void main(String[] args){
		try {
			//CREO EL SERVERSOCKET POR EL PUERTO 9999
			ss = new ServerSocket(PORT);
			System.out.println("El servidor de la sala de chat se ha iniciado y está escuchando. PORT: " + PORT);

			while (true) {
				
				if (nickNameSocketMap.size()<CLIENTS) {
					try {
						socket = ss.accept();
						out = new DataOutputStream(socket.getOutputStream());
						in = new DataInputStream(socket.getInputStream());
						login();
						System.out.println("Un nuevo usuario está conectado al servidor, la información es: " +socket);
						 
						new Thread(new HiloServidor(socket,out,in,currentUserNickName)).start();
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
				try {
					Thread.currentThread();
					Thread.sleep(2500);
				} catch (InterruptedException e) {
				}
				
			}
			
		} catch (IOException e) {
			//e.printStackTrace();
		}

	}
	
	/**
	 * 	FUNCION PARA SETEAR EL NOMBRE DEL SOCKET Y GUARDARLO EN EL NICNAMESOCKETMAP DEL SERVIDOR
	 * @throws IOException
	 */
	private static void login() throws IOException {
		write("¡Bienvenido a la sala de chat!");
		write("Introduzca su apodo:");
		try {
			while (true) {
				String nickName = in.readUTF();
				
				if (!Servidor.nickNameSocketMap.containsKey(nickName)) {
					System.out.println("El usuario ha ingresado un apodo:" + nickName);
					currentUserNickName = nickName;
					Servidor.nickNameSocketMap.put(nickName, socket);
					break;
				} else {
					write("El apodo que ingresó ya existe, vuelva a ingresar:");
				}
				
			}
		} catch (Exception e) {
			System.out.println("Error al logear.");
		}
		
	}
	
	
	/**
	 * FUNCION PARA ENVIAR MENSAJE MEDIANTE UN DATAOPUTPUTSTREAM(out). UTILIZA LA
	 * CLASE SKOCKETSUTILS.
	 * 
	 * @param message QUE SE ENVIA AL SOCKET DEL DATAOUTPUTSTREAM(out)
	 * @throws IOException
	 */
	private static void write(String message) throws IOException {
		SocketUtils.writeToDataOutputStream(out, message);
	}
}
