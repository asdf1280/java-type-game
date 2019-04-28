package type.server.game;

public class BotUserData extends PlayerData {

	@Override
	public void initCon() {
	}

	@Override
	public void message(String msg) {
	}

	@Override
	public boolean isOnline() {
		return true;
	}

//	private final int randomInterval = (int) (Math.random() * 1500) + 500;
//	private final int minimumTime = (int) (Math.random() * 1500) + 1500;

	@Override
	public void playSignal(double h) {
//		g.schedule(new BotscoreRunnable(), (int) (Math.random() * randomInterval) + minimumTime, TimeUnit.MILLISECONDS);
	}
//
//	private static DefaultEventLoopGroup g = new DefaultEventLoopGroup(5);
//
//	private class BotscoreRunnable implements Runnable {
//		@Override
//		public void run() {
//			if (health <= 0)
//				return;
//			worker.score(((int) (Math.random() * 12) + 4) * 15);
//			g.schedule(this, ((int) (Math.random() * randomInterval) + minimumTime) * 15, TimeUnit.MILLISECONDS);
//		}
//	}

	@Override
	public void setHealth(double h) {
		health = h; // UNSAFE, TO PREVENT BUGS THAT NOT CHANGING SCORE WHEN KILL
	}

	@Override
	public void gameOver(int rank, int maxUsers) {
	}

	@Override
	public void leftUsers(int count) {
	}

	@Override
	public void countDown(int seconds, int joined) {
		// nothing
	}

	@Override
	public void userStatus(double max, double avg, double min) {
		// TODO Auto-generated method stub

	}

}
