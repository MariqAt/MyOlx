package Users;

import java.util.ArrayList;

import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;

import Users.RegularUser.Category;
import Users.RegularUser.Notice;
import Users.RegularUser.SortNotice;

public class Admin extends User {
	
	OLX olx;
	
	private static Admin instance;
	
	static ArrayList<Notice> pendingAds = new ArrayList<Notice>();;

	private Admin(String name, String mail, String gsm, String password, OLX olx) {
		super(name, mail, gsm, password);
		this.olx = olx;
	}
	
	public static Admin getInstance(String name, String mail, String gsm, String password, OLX olx){
		if(instance == null){
			instance = new Admin(name, mail, gsm, password, olx);
		}
		return instance;
	}

	public void reviewAds() {
		for (Notice n : pendingAds) {
			n.printInfo();
			System.out.println();
			this.approveAd(n);
			System.out.println();
		}
	}

	private void approveAd(Notice n) {
		Scanner sc = new Scanner(System.in);

		System.out.println("Do you approve this add, YES or NO");

		String approved;

		do {
			approved = sc.next();
		} while (!(approved.equals("YES") || approved.equals("NO")));

		if (approved.equals("YES")) {

			if (olx.ads.containsKey(n.getCategory())) {
				olx.ads.get(n.getCategory()).add(n);
			} else {

				olx.ads.put(n.getCategory(), new ArrayList<Notice>());
				olx.ads.get(n.getCategory()).add(n);
			}
			if (!n.getOuterType().poster.containsKey(SortNotice.ACTIVE)) {
				n.getOuterType().poster.put(SortNotice.ACTIVE, new TreeSet<>());
			}
			n.getOuterType().poster.get(SortNotice.ACTIVE).add(n);

			if (!olx.ads.containsKey(n.category)) {
				olx.ads.put(n.category, new ArrayList<Notice>());
			}
			olx.ads.get(n.category).add(n);
		} else {
			System.out.println("Add is not approved");
		}
	}

	public void deleteAd(Notice notice) {
		if (olx.ads.containsKey(notice.getCategory()) && olx.ads.get(notice.getCategory()).contains(notice)) {
			olx.ads.get(notice.getCategory()).remove(notice);
		}
	}

	public void archiveAd(Notice notice) {
		if (olx.archivedAds.containsKey(notice.getCategory())) {
			olx.archivedAds.get(notice.getCategory()).add(notice);
		} else {

			olx.archivedAds.put(notice.getCategory(), new TreeSet<Notice>());
			olx.archivedAds.get(notice.getCategory()).add(notice);
		}

		olx.ads.get(notice.getCategory()).remove(notice);
	}

	public void blockUser(RegularUser user) {

		user = null;

	}
	public void printUsers(){
		for (RegularUser ru : olx.regularusers){
			System.out.println(ru.toString());
		}
	}

}
