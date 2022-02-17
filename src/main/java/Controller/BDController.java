package Controller;

import Model.Cuenta;
import Model.Cuenta_Usuario;
import Model.Usuario;
import Model.DAO.CuentaDAO;
import Model.DAO.Cuenta_UsuarioDAO;
import Model.DAO.UsuarioDAO;

public class BDController {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		UsuarioDAO u=new UsuarioDAO(new Usuario(-1,"Cristian","Repullo",false,"cristian","1234"));
		CuentaDAO c=new CuentaDAO(new Cuenta(5, 10000F, ""));
		Cuenta_UsuarioDAO cu=new Cuenta_UsuarioDAO();
		
//		System.out.println(u.Remove_User_by_Id(null));
		for (Usuario s : u.List_All_User()) {
			System.out.println(s);
		}
//		System.out.println(u.List_User_By_Id(3));
		System.out.println(u.List_User_By_Username("juan"));

//		System.out.println(c.insert());
//		System.out.println(c.update());
//		System.out.println(c.Remove_Cuenta_by_Id(5));
//		for (Cuenta s : c.List_All_Cuentas()) {
//			System.out.println(s);
//		}
//		System.out.println(c.List_Cuenta_By_Id(3));

		
		/*for (Cuenta_Usuario s : cu.List_All_Relation()) {
			System.out.println(s);
		}
		System.out.println(cu.List_Relation_By_User(2));
		*/
	}

}
