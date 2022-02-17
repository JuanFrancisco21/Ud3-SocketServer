package Controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Scanner;

import Model.Usuario;

public class Cliente {

	private static Scanner scanner = new Scanner(System.in);
	private static Socket socket;
	private static DataOutputStream out;
	private static DataInputStream in;

	public static void main(String[] args) {
		try {
			socket = new Socket("localhost", 9999);
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream()); 
			
			ClientMessageReceiver messageReceiver = new ClientMessageReceiver(socket, in);
			new Thread(messageReceiver).start();
			String input = null;
			do {
				input = scanner.nextLine();
				write(out, input);
			} while (!"salir".equals(input));
			messageReceiver.stop();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	private static void write(DataOutputStream dos, String message) throws IOException {
		dos.writeUTF(message);
		dos.flush();
	}
	
}


class ClientMessageReceiver implements Runnable {
	
	private Socket cliente;
	private DataInputStream dis;
	private boolean timeToStop = false;

	public ClientMessageReceiver(Socket socket,DataInputStream dis) throws IOException {
		this.cliente = socket;
		this.dis = dis;
	}

	public void stop() {
		timeToStop = true;
	}

	public void run() {
		while (!timeToStop) {
				
			try {
				//System.out.println(dis.readUTF());
				
				if (cliente.isConnected()) {
				System.out.println(SocketUtils.readObjectDataOutput(cliente));
				}
			} catch (IOException e) {
				e.printStackTrace();
				//if(e instanceof )
				if ("Connection reset".equals(e.getMessage())) {
					System.out.println("¡La conexión al servidor ha sido interrumpida!");
					break;
				}
				if (!timeToStop) {
					e.printStackTrace();					
				}
			}
		}
		
		
	}
	

}