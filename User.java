package Users;

import java.util.TreeMap;
import java.util.TreeSet;

public abstract class User implements Comparable<User> {

	String name;
	String mail;
	String gsm;
	String password;
	static final int MIN_NUMBER_LETTER_FOR_PASSWORD = 8;

	TreeMap<User, TreeSet<Message>> messages;

	public User(String name, String mail, String gsm, String password) {
		if (name != null && !name.isEmpty()) {
			this.name = name;
		}
		if (mail != null && !mail.isEmpty()) {
			this.mail = mail;
		}
		if (gsm != null && !gsm.isEmpty()) {
			this.gsm = gsm;
		}
		if (password != null && !password.isEmpty()) {
			if (password.length() >= MIN_NUMBER_LETTER_FOR_PASSWORD) {
				this.password = password;
			}
		}
		this.messages = new TreeMap<User, TreeSet<Message>>();
	}

	String getName() {
		return this.name;
	}
	String getMail(){
		return this.mail;
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

	@Override
	public int compareTo(User arg0) {
		return this.mail.compareTo(arg0.getMail());
	}
	@Override
	public String toString(){
		return this.name + " " + this.mail + " " + this.gsm;
	}
}
