package Users;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.TreeSet;

import Users.RegularUser.Category;
import Users.RegularUser.Notice;


public class OLX {
	
	TreeSet<RegularUser> regularUsers;
	// mail -> password -> user
	//HashMap<String, TreeSet<RegularUser>> regularUsers;
	
	TreeSet<RegularUser> loggedRegularUsers;
	
	TreeMap<Category, ArrayList<Notice>> ads;
	
	TreeMap<Category, TreeSet<Notice>> archivedAds;
	
	private static OLX instance;

	private OLX() {
		super();
		
		this.regularUsers = new TreeSet<RegularUser>();
		this.loggedRegularUsers = new TreeSet<RegularUser>();
		this.ads = new TreeMap<Category, ArrayList<Notice>>();
		this.archivedAds = new TreeMap<Category, TreeSet<Notice>>();
	}

	void regUser(RegularUser user){
		this.regularUsers.add(user);
	}

	public void logInUser (String mail, String password) {
		for (RegularUser regularUser : regularUsers) {
			if (regularUser.getMail().equals(mail)) {
				if (regularUser.getPassword().equals(password)) {
					//System.out.println("Wolcome " + regularUser.getName());
					this.loggedRegularUsers.add(regularUser);
					return;
				}
			} 
		}
		System.out.println("Your mail or your password aren't correct! Pease, try again!");
	}
	
	public void logOutUser (RegularUser user) {
		for (RegularUser regularUser : loggedRegularUsers) {
			if (regularUser.equals(user)) {
				this.loggedRegularUsers.remove(regularUser);
			}
		}
	}
	
	public static OLX getInstance(){
		if(instance == null){
			instance = new OLX();
		}
		return instance;
	}
	
}