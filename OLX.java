package Users;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;

import Users.RegularUser.Category;
import Users.RegularUser.Notice;


public class OLX {
	
	
	
	 TreeSet<RegularUser> regularusers;
	
	TreeMap<Category, ArrayList<Notice>> ads;
	
	TreeMap<Category, TreeSet<Notice>> archivedAds;
	
	private static OLX instance;

	
	private OLX() {
		super();
		
		this.regularusers = new TreeSet<RegularUser>();
		this.ads = new TreeMap<Category, ArrayList<Notice>>();
		this.archivedAds = new TreeMap<Category, TreeSet<Notice>>();
	}

	void regUser(RegularUser user){
		this.regularusers.add(user);
	}

	
	public static OLX getInstance(){
		if(instance == null){
			instance = new OLX();
		}
		return instance;
	}
	
}