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
		this.cuenta = CuentaDAO.List_Cuenta_By_Id(Cuenta_UsuarioDAO.List_Relation_By_User(UsuarioDAO.List_User_By_Username(nickname).getId()).getCuenta().getId());
	}

	/**
	 * Método run el cual siempre recibe un mensaje del cliente y
	 * ejecuta el comando con la datos recibidos en el mensaje.
	 * 
	 * Como return envia de vuelta el mensaje con el codigo 100 
	 * y el usuario o cuenta guardados en el mensaje.
	 */
	public void run() {
		int comando= 0;
		try {
			
			if (!usuario.getAdministrador()) {//--CLIENTE----------------------------------------------------------
				while (comando>=-1) {
					this.cuenta = CuentaDAO.List_Cuenta_By_Id(UsuarioDAO.List_User_By_Username(usuario.getUsername()).getId());
					mensaje=(Mensaje)  in.readObject();
					comando = mensaje.getComando();

					try {
						switch (comando) {
						case 1:
							//CONSULTAR TODAS LAS CUENTAS DE LOS USUARIOS
							if (cuenta!=null && usuario!=null) {
								cuenta=CuentaDAO.List_Cuenta_By_Id(Cuenta_UsuarioDAO.List_Relation_By_User(
										UsuarioDAO.List_User_By_Username(currentUserNickName).getId()).getCuenta().getId());
								mensaje.setCuenta(cuenta);
								mensaje.setDescripcion("Cuenta actual del usuario");
								sendObject(mensaje);
							}else {
								mensaje.setDescripcion("Cuenta no encontrada por el usuario");
								sendObject(mensaje);
							}
							break;
						case 2:
							//RETIRAR X CANTIDAD DE DINERO
							cuenta=CuentaDAO.List_Cuenta_By_Id(Cuenta_UsuarioDAO.List_Relation_By_User(
									UsuarioDAO.List_User_By_Username(currentUserNickName).getId()).getCuenta().getId());
							if (cuenta!=null && mensaje.getDineroTransaccion()>=1 && cuenta.getMoney()>mensaje.getDineroTransaccion()) {
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
								cuenta=CuentaDAO.List_Cuenta_By_Id(Cuenta_UsuarioDAO.List_Relation_By_User(
										UsuarioDAO.List_User_By_Username(currentUserNickName).getId()).getCuenta().getId());
								CuentaDAO c = new CuentaDAO(cuenta);
								c.setMoney(c.getMoney()+(mensaje.getDineroTransaccion()));
								c.setTransactions(c.getTransactions()+"\nIngresados "+mensaje.getDineroTransaccion()+"€ por "+usuario.getName());
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
				while (comando>=-1) {
					mensaje=(Mensaje)  in.readObject();
					comando = mensaje.getComando();

					try {
						switch (comando) {
						case 1:
							//Ingresar un nuevo usuario en el banco.
							try {
								if (mensaje.getUser()!=null) {
									UsuarioDAO u = new UsuarioDAO(mensaje.getUser());		
									u.setId(-1);
									u.insert_update();
									mensaje.setDescripcion("Creado usuario usuario");
									mensaje.setUser(u.List_User_By_Username(mensaje.getUser().getUsername()));
									sendObject(mensaje);
								}
							} catch (Exception e) {
								mensaje.setDescripcion("Error al crear a un usuario(No repetir campo usuario)");
								sendObject(mensaje);
							}
							
							break;
						case 2:
							//Crear una nueva cuenta bancaria a un cliente 
							try {
								UsuarioDAO u = new UsuarioDAO(mensaje.getUser());
								Cuenta_UsuarioDAO r = new Cuenta_UsuarioDAO();
								if (mensaje.getUser()!=null) {
									//  CREACION/UPDATE DEL CLIENTE/OPERARIO
									u.setId(-1);
									u.insert_update();
									u = new UsuarioDAO(u.List_User_By_Username(mensaje.getUser().getUsername()));
									
									//	CREACION DE NUEVA CUENTA Y TRAERLA DE LA BBDD
									CuentaDAO c = new CuentaDAO(new Cuenta(-1, 0F, ""));
									c.insert();
									c = new CuentaDAO(c.List_Cuenta_By_MAXId());
									
									//  CREAR RELACION ENTRE AL NUEVA CUENTA Y EL USUARIO(CREADO/ACTUALIZADO)
									r = new Cuenta_UsuarioDAO(u, c);
									r.insert();
									
									mensaje.setDescripcion("Nueva cuenta asignada al usuario");
								}
							} catch (Exception e) {
								e.printStackTrace();
								mensaje.setDescripcion("Error al asignar cuenta al cliente");
								sendObject(mensaje);
							}
							break;
						case 3:
							//Ver los datos de una cuenta bancaria.
							try {
								int id_cuenta = mensaje.getCuenta().getId();
								mensaje.setCuenta(CuentaDAO.List_Cuenta_By_Id(id_cuenta));
								sendObject(mensaje);
							} catch (Exception e) {
								e.printStackTrace();
							}
							break;
						case 4:
							//Ver los datos de un cliente.
							try {
								int id_usuario = mensaje.getUser().getId();
								mensaje.setUser(UsuarioDAO.List_User_By_Id(id_usuario));
								sendObject(mensaje);
							} catch (Exception e) {
								e.printStackTrace();
							}
							break;
						case 5:
							//Eliminar una cuenta bancaria
							try {
								if (mensaje.getCuenta()!=null && mensaje.getCuenta().getId()>=0) {
									CuentaDAO cu = new CuentaDAO(mensaje.getCuenta());
									cu.remove_Cuenta();
									mensaje.setCuenta(null);
									mensaje.setDescripcion("Cuenta borrada con exito");
									sendObject(mensaje);
								}
							} catch (Exception e) {
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
	 * FUNCION PARA ENVIAR UN OBJETO AL CLIENTE CON EL SOCKET.
	 * @throws IOException
	 */
	private void sendObject(Mensaje mensaje) throws IOException {
		mensaje.setComando(100);
		out.flush();
		out.writeObject(new Mensaje(mensaje));

	}
	
}
