package edu.ubb.warp.test;

import edu.ubb.warp.dao.DAOFactory;
import edu.ubb.warp.dao.UserDAO;
import edu.ubb.warp.exception.UserNameExistsException;
import edu.ubb.warp.logic.Hash;
import edu.ubb.warp.model.User;

public class UserInserter {
	public static void InsertUsers() {
		DAOFactory df = DAOFactory.getInstance();
		UserDAO uDao = df.getUserDAO();
		User u1 = new User();
		User u2 = new User();
		u1.setEmail("mail@mail.com");
		u1.setPassword(Hash.hashString("password"));
		u1.setPhoneNumber("0743288115");
		u1.setUserName("manager");
		u1.setAddress("Kolozsvar strada utca 12");
		try {
			uDao.insertUser(u1);
		} catch (UserNameExistsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
