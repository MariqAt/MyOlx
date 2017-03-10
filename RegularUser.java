package Users;

import java.io.File;
import java.io.Serializable;
import java.security.KeyStore.Entry;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;

import Users.RegularUser.Category;
import Users.RegularUser.Notice;

public class RegularUser extends User {

	public class Notice implements Comparable<Notice> {

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

		public Notice(String title, Category category, Type type, int price, String desription, StateGood state) {
			setTitle(title);
			this.category = category;
			this.type = type;
			setPrice(price);
			setDesription(desription);
			setState(state);
			this.mail = RegularUser.this.mail;
			this.gsm = RegularUser.this.gsm;
			this.name = RegularUser.this.name;
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
		int getId(){
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
	TreeMap<SortNotice, TreeSet<Notice>> poster;
	private TreeSet<Notice> view;

	private static int uniqueID = 1;

	private RegularUser(String name, String mail, String gsm, String password) {
		super(name, mail, gsm, password);

		this.poster = new TreeMap<>();
		this.view = new TreeSet<>();
	}

	public static RegularUser createUser(String name, String mail, String gsm, String password, OLX olx) {
		RegularUser user = new RegularUser(name, mail, gsm, password);
		olx.regUser(user);
		return user;
	}
	
	public void logInOlx () {
		OLX.getInstance().logInUser(this.mail, this.password);
	}
	
	public void logOutOlx () {
		OLX.getInstance().logOutUser(this);
	}

	public void addNotice(Notice n) {
		Admin.pendingAds.add(n);
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

	private void sortedNotice(Comparator<Notice> comparator, OLX olx) {

		ArrayList<Notice> list = new ArrayList<>();
		for (Map.Entry<Category, ArrayList<Notice>> entry : olx.ads.entrySet()) {
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

	public void printMessage() {
		for (Map.Entry<User, TreeSet<Message>> entry : messages.entrySet()) {
			System.out.println("USER: " + entry.getKey().name);
			System.out.println("---------------");
			for (Message m : entry.getValue()) {
				System.out.println(m.toString());
			}
		}
	}

	public void printViewNotice() {
		System.out.println(this.name + " view:");
		for (Notice notice : view) {
			notice.printInfo();
		}
	}

	

}
