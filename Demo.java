package demo;


import java.time.LocalDate;

import Users.Admin;
import Users.OLX;
import Users.RegularUser;
import Users.RegularUser.Category;
import Users.RegularUser.Notice;
import Users.RegularUser.SortNotice;
import Users.RegularUser.StateGood;
import Users.RegularUser.Type;


public class Demo {

	public static void main(String[] args) {
		
		OLX olx = OLX.getInstance();
		
		RegularUser mimi = RegularUser.createUser("Maria", "mimi@abv.bg", "0899511111", "mimi12345", olx);
		Notice noticeM1 = mimi.new Notice("Balna Roklq", Category.FASHION, Type.PRIVATE, 100, "size: M", StateGood.NEW);
		Notice noticeM2 = mimi.new Notice("Huski", Category.ANIMALS, Type.PRIVATE, 400, "Na 2 meseca", StateGood.NEW);
		Notice noticeM3 = mimi.new Notice("Ednostaen ap", Category.ESTATES, Type.PRIVATE, 80_000, "Centur, za remont", StateGood.USED);
		
		mimi.addNotice(noticeM1);
		mimi.addNotice(noticeM2);
		mimi.addNotice(noticeM3);
		
		mimi.lookNotice(noticeM2, SortNotice.ACTIVE);
		System.out.println("----------");
		
		RegularUser vanka = RegularUser.createUser("Ivan", "vanka@abv.bg", "0899665544", "vanka12345", olx);
		Notice noticeV1 = vanka.new Notice("Mezonet", Category.ESTATES, Type.BUSINESS, 150_000, "St. grad, nov", StateGood.NEW);
		Notice noticeV2 = vanka.new Notice("Vila", Category.ESTATES, Type.BUSINESS, 300_000, "Dragalevci, nov", StateGood.NEW);
		Notice noticeV3 = vanka.new Notice("Dvystaen", Category.ESTATES, Type.BUSINESS, 100_000, "St. grad, za remont", StateGood.USED);
		Admin admin = Admin.getInstance("Pesho", "Pesho@gmail.com", "0888888888", "peshoJeqlzoto", olx);
		vanka.addNotice(noticeV1);
		vanka.addNotice(noticeV2);
		vanka.addNotice(noticeV3);
		
		admin.reviewAds();
		
		vanka.archivedNotice(noticeV1);
		vanka.archivedNotice(noticeV2);
		
		vanka.printAllNotice();
		
		vanka.activedNotice(noticeV2);
		vanka.printAllNotice();
		
		mimi.sendMessage(vanka, "Zdr, moje li da yredim ogled?");
		vanka.sendMessage(mimi, "Ogledi se pravqt samo v petyk");
		mimi.sendMessage(vanka, "V kolko chasa?");
		vanka.sendMessage(mimi, "13, na bylevard Bylgariq 147");
		mimi.printMessage();
		System.out.println("-------------");
		vanka.printMessage();
		
		System.out.println();
		System.out.println("----PROMENQME OBQVA----");
		Notice n = mimi.redactionNotice(SortNotice.ACTIVE, noticeM1);
		n.setTitle("Balna Roklq Stela Borisova");
		n.setDesription("Size: Yniversal");
		
		mimi.lookNotice(noticeM1, SortNotice.ACTIVE);
		
		System.out.println();
		vanka.viewNotice(noticeM3);
		vanka.printViewNotice();
		
		mimi.searchNoticeByPrice(olx);
		
		admin.printUsers();
		
		
	}

}
