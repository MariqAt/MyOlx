package Users;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Message implements Comparable<Message> {
	private User sender;
	private User receiver;
	private LocalDateTime time;
	private String content;
	private int iD;
	private static int uniqueID = 1;

	private Message(User sender, User receiver, String content) {
		super();
		this.sender = sender;
		this.receiver = receiver;
		this.time = LocalDateTime.now();
		this.content = content;
		this.iD = uniqueID;
		uniqueID++;
	}

	static Message createMessage(User sender, User receiver, String content) {
		Message message = new Message(sender, receiver, content);

		return message;
	}

	LocalDateTime getTime() {
		return this.time;
	}

	int getID() {
		return this.iD;
	}

	@Override

	public String toString() {
		return "Sent at " + this.time + " Sender: " + sender.getName() + " Receiver: " + receiver.getName() + " "
				+ content;
	}

	@Override
	public int compareTo(Message o) {

		return this.getID() - o.getID();
	}
}
