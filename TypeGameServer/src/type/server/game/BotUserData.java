package type.server.game;

import type.common.work.Utils;

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

	private final int randomInterval = (int) (Math.random() * 1500) + 500;
	private final int minimumTime = (int) (Math.random() * 1500) + 1500;

	@Override
	public void playSignal(double h) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				Utils.sleep((int) (Math.random() * randomInterval) + minimumTime);
				while (health > 0) {
//					Utils.l.fine("BotUserData", "Adding score. Health: " + health);
					worker.score((int) (Math.random() * 12) + 4);
					Utils.sleep((int) (Math.random() * randomInterval) + minimumTime);
				}
			}
		}).start();
	}

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
