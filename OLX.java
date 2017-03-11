package Users;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;

import Users.OLX.User.RegularUser;
import Users.OLX.User.RegularUser.Category;
import Users.OLX.User.RegularUser.Notice;
import Users.OLX.User.RegularUser.SortNotice;
import Users.OLX.User.RegularUser.StateGood;
import Users.OLX.User.RegularUser.Type;

public class OLX {

	private TreeSet<OLX.User.RegularUser> regularUsers;
	// mail -> password -> user
	// HashMap<String, TreeSet<RegularUser>> regularUsers;

	private TreeSet<OLX.User.RegularUser> loggedRegularUsers;

	private TreeMap<OLX.User.RegularUser.Category, ArrayList<OLX.User.RegularUser.Notice>> ads;

	private TreeMap<OLX.User.RegularUser.Category, TreeSet<OLX.User.RegularUser.Notice>> archivedAds;

	private static OLX instance;

	private OLX() {
		super();

		this.regularUsers = new TreeSet<User.RegularUser>();
		this.loggedRegularUsers = new TreeSet<User.RegularUser>();
		this.ads = new TreeMap<OLX.User.RegularUser.Category, ArrayList<OLX.User.RegularUser.Notice>>();
		this.archivedAds = new TreeMap<OLX.User.RegularUser.Category, TreeSet<OLX.User.RegularUser.Notice>>();
	}

	void regUser(User.RegularUser user) {
		this.regularUsers.add(user);
	}

	public void logInUser(String mail, String password) {
		for (User.RegularUser regularUser : regularUsers) {
			if (regularUser.getMail().equals(mail)) {
				User user = (User) regularUser;
				if (user.getPassword().equals(password)) {
					// System.out.println("Wolcome " + regularUser.getName());
					this.loggedRegularUsers.add(regularUser);
					return;
				}
			}
		}
		System.out.println("Your mail or your password aren't correct! Pease, try again!");
	}

	public void logOutUser(User.RegularUser user) {
		for (User.RegularUser regularUser : loggedRegularUsers) {
			if (regularUser.equals(user)) {
				this.loggedRegularUsers.remove(regularUser);
			}
		}
	}

	public static OLX getInstance() {
		if (instance == null) {
			instance = new OLX();
		}
		return instance;
	}

	public static abstract class User implements Comparable<User> {

		private String name;
		private String mail;
		private String gsm;
		private String password;
		

		private TreeMap<User, TreeSet<Message>> messages;

		public User(String name, String mail, String gsm) {
			if (name != null && !name.isEmpty()) {
				this.name = name;
			}
			if (mail != null && !mail.isEmpty()) {
				this.mail = mail;
			}
			if (gsm != null && !gsm.isEmpty()) {
				this.gsm = gsm;
			}
			this.password = createPassword();
			this.messages = new TreeMap<User, TreeSet<Message>>();
		}

		String getName() {
			return this.name;
		}

		String getMail() {
			return this.mail;
		}

		String getGsm() {
			return this.gsm;
		}

		public void sendMessage(User receiver, String content) {

			if (!this.messages.containsKey(receiver)) {
				this.messages.put(receiver, new TreeSet<Message>());
			}
			Message message = Message.createMessage(this, receiver, content);

			messages.get(receiver).add(message);
			if (!receiver.messages.containsKey(this)) {
				receiver.messages.put(this, new TreeSet<Message>());
			}
			receiver.messages.get(this).add(message);

		}

		private static String createPassword() {
			Scanner sc = new Scanner(System.in);
			boolean lowerCaseLetter = false;
			boolean upperCaseLetter = false;
			boolean number = false;
			String attemptedPassword;
			do { System.out.println("Please input password between 8 and 20 characters, with at least 1 number, capital letter and lower case letter");

			attemptedPassword = sc.nextLine();
			
			for (int index = 0; index <= attemptedPassword.length() - 1; index++) {
				if (attemptedPassword.charAt(index) >= 97 && attemptedPassword.charAt(index) <= 122) {
					lowerCaseLetter = true;
					break;
				}
			}
			
			for (int index = 0; index <= attemptedPassword.length() - 1; index++) {
				if (attemptedPassword.charAt(index) >= 65 && attemptedPassword.charAt(index) <= 90) {
					upperCaseLetter = true;
					break;
				}
			}
			
			for (int index = 0; index <= attemptedPassword.length() - 1; index++) {
				if (attemptedPassword.charAt(index) >= 48 && attemptedPassword.charAt(index) <= 57) {
					number = true;
					break;
				}
			}
			} while ( !(lowerCaseLetter && upperCaseLetter && number && attemptedPassword.length() > 8 && attemptedPassword.length()<20) );
			System.out.println("Password successful");
			return attemptedPassword;

		}
		private String getPassword() {
			return this.password;
		}
		public void printMessage() {
			for (Map.Entry<User, TreeSet<Message>> entry : this.messages.entrySet()) {
				System.out.println("USER: " + entry.getKey().name);
				System.out.println("---------------");
				for (Message m : entry.getValue()) {
					System.out.println(m.toString());
				}
			}
		}

		@Override
		public int compareTo(User arg0) {
			return this.mail.compareTo(arg0.getMail());
		}

		@Override
		public String toString() {
			return this.name + " " + this.mail + " " + this.gsm;
		}

		public static class RegularUser extends User {

			public enum Category {
				ESTATES, ANIMALS, FASHION
			}

			public enum Type {
				PRIVATE, BUSINESS
			}

			public enum StateGood {
				NEW, USED
			}

			public enum SortNotice {
				ACTIVE, ARCHIVE
			}

			private SortNotice sortNotice;
			private TreeMap<SortNotice, TreeSet<Notice>> poster;
			private TreeSet<Notice> view;

			private static int uniqueID = 1;

			private RegularUser(String name, String mail, String gsm) {
				super(name, mail, gsm);

				this.poster = new TreeMap<>();
				this.view = new TreeSet<>();
			}

			public static RegularUser createUser(String name, String mail, String gsm, OLX olx) {
				RegularUser user = new RegularUser(name, mail, gsm);
				olx.regUser(user);
				return user;
			}

			public void logInOlx() {
				User user = (User) this;
				OLX.getInstance().logInUser(this.getMail(), user.getPassword());
			}

			public void logOutOlx() {
				OLX.getInstance().logOutUser(this);
			}

			public void addNotice(OLX.User.RegularUser.Notice n) {
				OLX.Admin.getCreatedInstance().pendingAds.add(n);
			}

			public void deleteNotice(Notice n) {
				SortNotice arch = SortNotice.ARCHIVE;
				if (this.poster.containsKey(arch)) {
					if (this.poster.get(arch).contains(n)) {
						this.poster.get(arch).remove(n);
					}
				}
			}

			public void lookNotice(Notice n, SortNotice sort) {
				if (this.poster.containsKey(sort)) {
					if (poster.get(sort).contains(n)) {
						n.printInfo();
					}
				}
			}

			public void archivedNotice(Notice n) {
				SortNotice arch = SortNotice.ARCHIVE;
				SortNotice act = SortNotice.ACTIVE;
				if (poster.get(act).contains(n)) {
					if (!poster.containsKey(arch)) {
						poster.put(arch, new TreeSet<>());
					}
					poster.get(arch).add(n);
					poster.get(act).remove(n);
				}
			}

			public Notice redactionNotice(SortNotice sort, Notice n) {
				if (!poster.get(sort).contains(n)) {
					return null;
				}
				return n;
			}

			public void searchNoticeByPrice(OLX olx) {
				sortedNotice(new Comparator<Notice>() {

					@Override
					public int compare(Notice o1, Notice o2) {
						return o1.price - o2.price;
					}
				}, olx);
			}

			private void sortedNotice(Comparator<OLX.User.RegularUser.Notice> comparator, OLX olx) {

				ArrayList<OLX.User.RegularUser.Notice> list = new ArrayList<>();
				for (Map.Entry<OLX.User.RegularUser.Category, ArrayList<OLX.User.RegularUser.Notice>> entry : olx.ads
						.entrySet()) {
					list.addAll(entry.getValue());
				}
				Collections.sort(list, comparator);
				for (Notice n : list) {
					n.printInfo();
				}
			}

			public void viewNotice(Notice notice) {
				this.view.add(notice);
			}

			public void activedNotice(Notice n) {
				if (this.poster.get(SortNotice.ARCHIVE).contains(n)) {
					addNotice(n);
					this.poster.get(SortNotice.ARCHIVE).remove(n);
				}
			}

			public void printAllNotice() {
				for (Map.Entry<SortNotice, TreeSet<Notice>> entry : poster.entrySet()) {
					System.out.println("SORT: " + entry.getKey());
					System.out.println("---------");
					for (Notice notice : entry.getValue()) {
						notice.printInfo();
						System.out.println("------");
					}
				}
			}

			public void printViewNotice() {
				System.out.println("View:");
				for (Notice notice : view) {
					notice.printInfo();
				}
			}

			

			public class Notice implements Comparable<OLX.User.RegularUser.Notice> {
				private RegularUser user;
				private String title;
				Category category;
				private Type type;
				private int price;
				private String desription;
				private StateGood state;
				private String mail;
				private String gsm;
				private String name;
				private LocalDate date;
				private int id;
				// picture

				public Notice(String title, Category category, Type type, int price, String desription,
						StateGood state) {
					this.user = RegularUser.this;
					setTitle(title);
					this.category = category;
					this.type = type;
					setPrice(price);
					setDesription(desription);
					setState(state);
					this.mail = user.getMail();
					this.gsm = user.getGsm();
					this.name = user.getName();
					this.date = LocalDate.now();
					this.id = uniqueID;
					RegularUser.this.uniqueID++;
				}

				public void setTitle(String title) {
					if (title != null && !title.isEmpty()) {
						this.title = title;
					} else {
						this.title = "something for sell";
					}
				}

				public void setPrice(int price) {
					if (price > 0) {
						this.price = price;
					}
				}

				public void setDesription(String desription) {
					if (desription != null && !desription.isEmpty()) {
						this.desription = desription;
					}
				}

				public void setState(StateGood state) {
					this.state = state;
				}

				public void printInfo() {
					System.out.println("Title: " + this.title);
					System.out.println("Category: " + this.category);
					System.out.println("PRIVATE OR BUSINESS: " + this.type);
					System.out.println("Price: " + this.price + " lv");
					System.out.println("Description: " + this.desription);
					System.out.println("State: " + this.state);
					System.out.println("Mail: " + this.mail);
					System.out.println("Contact person: " + this.name);
					System.out.println("ID: " + this.id);
				}

				RegularUser getOuterType() {
					return RegularUser.this;
				}

				int getId() {
					return this.id;
				}

				Category getCategory() {
					return this.category;
				}

				@Override
				public int compareTo(Notice o) {
					return this.getId() - o.getId();
				}
			}

		}
	}

	public static class Admin extends User {

		OLX olx;

		private static Admin instance;

		private static ArrayList<OLX.User.RegularUser.Notice> pendingAds = new ArrayList<OLX.User.RegularUser.Notice>();;

		private Admin(String name, String mail, String gsm, OLX olx) {
			super(name, mail, gsm);
			this.olx = olx;
		}

		public static Admin getInstance(String name, String mail, String gsm, OLX olx) {
			if (instance == null) {
				instance = new Admin(name, mail, gsm, olx);
			}
			return instance;
		}

		public static Admin getCreatedInstance() {

			return instance;
		}

		public void reviewAds() {
			for (OLX.User.RegularUser.Notice n : pendingAds) {
				n.printInfo();
				System.out.println();
				this.approveAd(n);
				System.out.println();
			}
		}

		private void approveAd(OLX.User.RegularUser.Notice n) {
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

					olx.ads.put(n.getCategory(), new ArrayList<OLX.User.RegularUser.Notice>());
					olx.ads.get(n.getCategory()).add(n);
				}
				if (!n.getOuterType().poster.containsKey(SortNotice.ACTIVE)) {
					n.getOuterType().poster.put(OLX.User.RegularUser.SortNotice.ACTIVE, new TreeSet<>());
				}
				n.getOuterType().poster.get(SortNotice.ACTIVE).add(n);

				if (!olx.ads.containsKey(n.category)) {
					olx.ads.put(n.category, new ArrayList<OLX.User.RegularUser.Notice>());
				}
				olx.ads.get(n.category).add(n);
			} else {
				System.out.println("Add is not approved");
			}
		}

		public void deleteAd(OLX.User.RegularUser.Notice notice) {
			if (olx.ads.containsKey(notice.getCategory()) && olx.ads.get(notice.getCategory()).contains(notice)) {
				olx.ads.get(notice.getCategory()).remove(notice);
			}
		}

		public void archiveAd(OLX.User.RegularUser.Notice notice) {
			if (olx.archivedAds.containsKey(notice.getCategory())) {
				olx.archivedAds.get(notice.getCategory()).add(notice);
			} else {

				olx.archivedAds.put(notice.getCategory(), new TreeSet<OLX.User.RegularUser.Notice>());
				olx.archivedAds.get(notice.getCategory()).add(notice);
			}

			olx.ads.get(notice.getCategory()).remove(notice);
		}

		public void blockUser(RegularUser user) {

			user = null;

		}

		public void printUsers() {
			for (RegularUser ru : olx.regularUsers) {
				System.out.println(ru.toString());
			}
		}
	}

}