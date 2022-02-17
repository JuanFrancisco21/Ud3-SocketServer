package Controller;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

import Model.Usuario;

public class SocketUtils {

	public static void writeToSocket(Socket socket, String message) throws IOException {
		try {
			writeToOutputStream(socket.getOutputStream(), message);

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public static void writeToDataOutputStream(DataOutputStream dos, String message) throws IOException {
		try {
			dos.writeUTF(message);
			dos.flush();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public static void writeToOutputStream(OutputStream os, String message) throws IOException {
		try {
			writeToDataOutputStream(new DataOutputStream(os), message);

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public static void writeObjectDataOutput(Socket socket, Usuario usuario) throws IOException {
		try {
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			out.writeObject(usuario);
		} catch (SocketException se) {
			se.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static Usuario readObjectDataOutput(Socket socket) throws IOException {
		Usuario result = new Usuario();
		try {
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			result = (Usuario) in.readObject();
		
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("hola");
		}
		return result;
	}
}
