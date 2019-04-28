package type.client.net;

public abstract class NetworkWorker {
	public abstract boolean isSucceed();
	public abstract void closeConnection();
	public abstract void login(String id, String pw, boolean register, Runnable succCallback, Runnable failCallback);
	public abstract void singleToggle(boolean single);
	public abstract void anonymousLogin();
	public abstract void setChatCallback(ChatCallback ccb);
	public abstract void chat(String msg);
	public abstract void user_matchmake_start();
	public abstract void user_matchmake_cancel();
	public abstract void setMatchCallback(MatchCallback mcb);
	public abstract void play_score(int score);
}
