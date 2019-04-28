package type.server.game;

public abstract class PlayerData {
	public abstract void initCon();
	public abstract void countDown(int seconds, int joined);
	public abstract void userStatus(double max, double avg, double min);
	public abstract void message(String msg);
	public abstract boolean isOnline();
	public abstract void playSignal(double maxHealth);
	public abstract void setHealth(double h);
	public abstract void gameOver(int rank, int maxUsers);
	public abstract void leftUsers(int count);
	protected double health;
	protected MatchWorker worker = null;
	
	public double damages;
	public double scores = 0;
}
