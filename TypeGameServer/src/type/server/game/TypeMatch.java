package type.server.game;

import java.util.ArrayList;

public class TypeMatch {
	private static final int MAX_USERS = 200;
	long makeTime = System.currentTimeMillis();

	public boolean isWaiting() {
		return (System.currentTimeMillis() - makeTime) < 60000;
	}

	public boolean canJoin() {
		return isWaiting() && users.size() < MAX_USERS;
	}

	private ArrayList<MatchUserData> users = new ArrayList<>();

	public TypeMatch() {

	}

	public void addUser(MatchUserData mud) {
		users.add(mud);
	}

	public String hashCodeString() {
		return Integer.toHexString(hashCode());
	}
}
