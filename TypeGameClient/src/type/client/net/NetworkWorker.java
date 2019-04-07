package type.client.net;

public abstract class NetworkWorker {
	public abstract boolean isSucceed();
	public abstract void closeConnection();
	public abstract void login(String id, String pw, boolean register, Runnable succCallback, Runnable failCallback);
	public abstract void singleToggle(boolean single);
	public abstract void anonymousLogin();
	public abstract void setChatCallback(ChatCallback ccb);
	public abstract void chat(String msg);
}
