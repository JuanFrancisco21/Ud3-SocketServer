package Controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Set;

import Model.Cuenta;
import Model.Cuenta_Usuario;
import Model.Mensaje;
import Model.Usuario;
import Model.DAO.CuentaDAO;
import Model.DAO.Cuenta_UsuarioDAO;
import Model.DAO.UsuarioDAO;

public class HiloServidor implements Runnable {

	private Socket socket;
	private String currentUserNickName;
	private Usuario usuario;
	private Cuenta cuenta;
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
		this.usuario = UsuarioDAO.List_User_By_Username(nickname);
		this.cuenta = CuentaDAO.List_Cuenta_By_Id(UsuarioDAO.List_User_By_Username(nickname).getId());
	}

	public void run() {
		int comando= 0;
		try {
			
			if (!usuario.getAdministrador()) {//--CLIENTE----------------------------------------------------------
				sendMenuCliente();
				while (comando>=-1) {
					this.cuenta = CuentaDAO.List_Cuenta_By_Id(UsuarioDAO.List_User_By_Username(usuario.getUsername()).getId());
					mensaje=(Mensaje)  in.readObject();
					System.out.println(currentUserNickName + " Ingresó: " + mensaje.getComando());
					comando = mensaje.getComando();

					try {
						switch (comando) {
						case 1:
							//CONSULTAR TODAS LAS CUENTAS DE LOS USUARIOS
							mensaje.setCuenta(cuenta);
							mensaje.setDescripcion("Cuenta actual del usuario");
							sendObject(mensaje);
							break;
						case 2:
							//RETIRAR X CANTIDAD DE DINERO
							if (mensaje.getCuenta()!=null && mensaje.getDineroTransaccion()>=1) {
								CuentaDAO c = new CuentaDAO(cuenta);
								c.setMoney(c.getMoney()-(mensaje.getDineroTransaccion()));
								c.setTransactions(c.getTransactions()+"\nRetirados "+mensaje.getDineroTransaccion()+"€ por "+usuario.getName());
								c.update();
								mensaje.setCuenta(c);
								mensaje.setDescripcion("Retirados "+mensaje.getDineroTransaccion()+"€ a la cuenta");
								sendObject(mensaje);
							} else {
								mensaje.setDescripcion("Error al retirar dinero de la cuenta");
								sendObject(mensaje);
							}
							break;
						case 3:
							//INGRESAR X CATIDAD DE DINERO
							if (mensaje.getCuenta()!=null && mensaje.getDineroTransaccion()>=1) {
								CuentaDAO c = new CuentaDAO(cuenta);
								c.setMoney(c.getMoney()+(mensaje.getDineroTransaccion()));
								c.setTransactions(c.getTransactions()+"\nRetirados "+mensaje.getDineroTransaccion()+"€ por "+usuario.getName());
								c.update();
								mensaje.setCuenta(c);
								mensaje.setDescripcion("Ingrasados "+mensaje.getDineroTransaccion()+"€ a la cuenta");
								sendObject(mensaje);
							} else {
								mensaje.setDescripcion("Error al retirar dinero de la cuenta");
								sendObject(mensaje);
							}
							break;
						case 4:
							//SALIDA DEL SERVIDOR
							comando = -2;
							break;
							
						case 0:
						default:
							mensaje.setDescripcion("El comando que ingresó es ilegal, ¡vuelva a ingresar!");
							sendObject(this.mensaje);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
			} else {//----------------OPERARIO---------------------------------------------------------------------
				sendMenuOperador();
				while (comando>=-1) {
					mensaje=(Mensaje)  in.readObject();
					System.out.println(currentUserNickName + " Ingresó: " + mensaje.getComando());
					comando = mensaje.getComando();

					try {
						switch (comando) {
						case 1:
							//Ingresar un nuevo usuario en el banco.
							if (mensaje.getUser()!=null && mensaje.getUser().getId()>=0) {
								UsuarioDAO u = new UsuarioDAO(mensaje.getUser());		
								u.setId(-1);
								u.insert_update();
							}else {
								mensaje.setDescripcion("Error al crear a un usuario");
								sendObject(mensaje);
							}
							break;
						case 2:
							//Crear una nueva cuenta bancaria (se deberá asignar al menos a un cliente)
							if (mensaje.getUser()!=null && mensaje.getUser().getId()>=0) {
								CuentaDAO c = new CuentaDAO(new Cuenta(-1, 0F, ""));
								Cuenta_UsuarioDAO r = new Cuenta_UsuarioDAO(new Cuenta_Usuario(-1, mensaje.getUser(), c));
								c.insert();
								mensaje.setDescripcion("Creada cuenta");
								r.insert_update();
								mensaje.setDescripcion("Nueva cuenta asignada al usuario");
							}else {
								mensaje.setDescripcion("Error al crearle cuenta a un usuario");
								sendObject(mensaje);
							}
							break;
						case 3:
							//Ver los datos de una cuenta bancaria.
							int id_cuenta = Integer.parseInt(mensaje.getDescripcion());
							mensaje.setCuenta(CuentaDAO.List_Cuenta_By_Id(id_cuenta));
							if (mensaje.getUser().getId()<=0) {
								mensaje.setDescripcion("Cuenta no encontrado");
							}
							sendObject(mensaje);
							break;
						case 4:
							//Ver los datos de un cliente.
							mensaje.setUser(UsuarioDAO.List_User_By_Username(mensaje.getDescripcion()));
							mensaje.setCuenta(null);
							if (mensaje.getUser().getId()<=0) {
								mensaje.setDescripcion("Cliente no encontrado");
							}
							sendObject(mensaje);
							break;
						case 5:
							//Eliminar una cuenta bancaria
							if (mensaje.getCuenta()!=null && mensaje.getCuenta().getId()>=0) {
								CuentaDAO cu = new CuentaDAO(mensaje.getCuenta());
								cu.remove_Cuenta();
								mensaje.setCuenta(null);
								mensaje.setDescripcion("Cuenta borrada con exito");
								sendObject(mensaje);
							}else {
								mensaje.setDescripcion("Error al borrar cuenta a un usuario");
								sendObject(mensaje);
							}
							break;

						case 6:
							//SALIDA DEL SERVIDOR
							comando = -2;
							break;

						case 0:
						default:
							mensaje.setDescripcion("El comando que ingresó es ilegal, ¡vuelva a ingresar!");
							sendObject(this.mensaje);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
			}//----------------------------------------------------------------------------------------------------
			
			


			
		} catch (IOException e) {

			Servidor.nickNameSocketMap.remove(currentUserNickName);
			try {
				in.close();
				out.close();
				socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		} catch (ClassNotFoundException e1) {
			System.out.println("Error al obtener mensaje del cliente");
			e1.printStackTrace();
		} finally {
			Servidor.nickNameSocketMap.remove(currentUserNickName);
			try {
				in.close();
				out.close();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
	}

	
	/**
	 * METODO QUE ENVIA EL MENU AL CLIENTE.
	 * @throws IOException
	 */
	private void sendMenuCliente() throws IOException{
		mensaje.setDescripcion( "********************************************************"
							+ "\n*  Has iniciado sesión Nº" + Servidor.nickNameSocketMap.size()
							+                             "                             *"
							+ "\n*  1) Ver el saldo de la cuenta del cliente            *"
							+ "\n*  2) Sacar dinero de la cuenta                        *"
							+ "\n*  3) Ingresar dinero en la cuenta                     *"
							+ "\n*  4) Salir                                            *"
							+ "\n********************************************************");
		sendObject(this.mensaje);
	}
	/**
	 * METODO QUE ENVIA EL MENU AL CLIENTE.
	 * @throws IOException
	 */
	private void sendMenuOperador() throws IOException{
		mensaje.setDescripcion( "********************************************************"
							+ "\n*  Has iniciado sesión Nº" + Servidor.nickNameSocketMap.size()
							+                             "                             *"
							+ "\n*  1) Ingresar un nuevo usuario en el banco.			*"
							+ "\n*  2) Crear una nueva cuenta bancaria a (Cliente).     *"
							+ "\n*  3) Ver los datos de una cuenta bancaria.            *"
							+ "\n*  4) Ver los datos de un cliente.                     *"
							+ "\n*  4) Eliminar una cuenta bancaria.                    *"
							+ "\n*  4) Salir.                                           *"
							+ "\n********************************************************");
		sendObject(this.mensaje);
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
		sendObject(this.mensaje);
	}

	/**
	 * FUNCION PARA ENVIAR UN OBJETO AL CLIENTE CON EL SOCKET.
	 * @throws IOException
	 */
	private void sendObject(Mensaje mensaje) throws IOException {
		out.writeObject(new Mensaje(mensaje));
	}
	
}
