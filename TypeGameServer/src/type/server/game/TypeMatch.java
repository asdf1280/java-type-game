package type.server.game;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

import type.common.work.Utils;
import type.server.main.Server;

public class TypeMatch {
	private static final int MAX_USERS_PER_MATCH = 200;
	private static final int WAIT_SECONDS = 40;
	private static final int ADD_AI_UNTIL = 50;
	private static final int COUNTDOWN_MIN_PLAYERS = 5;
	private static final int PLAYER_GOAL = 150;
	private static final int TICKS_PER_SECOND = 20;
	private static final int TICKRATE = 1000 / TICKS_PER_SECOND;
	long makeTime = System.currentTimeMillis();
	long cStartTime = System.currentTimeMillis();

	public boolean isWaiting() {
		return (System.currentTimeMillis() - cStartTime) < WAIT_SECONDS * 1000;
	}

	public boolean canJoin() {
		return (isWaiting() && users.size() < MAX_USERS_PER_MATCH);
	}

	private boolean ended = false;

	public boolean ended() {
		return ended;
	}

	private ArrayList<PlayerData> users = new ArrayList<>();

	public TypeMatch() {
		Runnable gameTick = new Runnable() {
			long ticks = 0;
			long gameStartTicks = 0;
			boolean healthChanged = false;
			int lastUsers = 0;
			long lastRun = System.currentTimeMillis();
			int lastInterval = 0;
			int preferredSleepTime = TICKRATE;

			GameState gs = GameState.WAITING;

			@Override
			public void run() {
				lastInterval = (int) Math.max(0l, (System.currentTimeMillis() - lastRun) - preferredSleepTime);
				lastRun = System.currentTimeMillis();
				ticks++;
				for (int i = 0; i < users.size(); i++) {
					PlayerData mud = users.get(i);
					if (!mud.isOnline()) {
						i--;
						users.remove(mud);
						Utils.l.fine("TypeMatch", "User logged out");
					}
				}

				switch (gs) {
				case WAITING:
					cStartTime = System.currentTimeMillis();
					if (users.size() >= COUNTDOWN_MIN_PLAYERS || System.currentTimeMillis() - makeTime > 10000) {
						gs = GameState.MATCHING;
						ticks = 0;
					} else if (System.currentTimeMillis() - makeTime > 3000
							&& users.size() + 2 < COUNTDOWN_MIN_PLAYERS) {
						users.add(new BotUserData());
					}
					for (PlayerData mud : users) {
						mud.countDown(-1, users.size());
					}
					break;
				case MATCHING:
					boolean dd = ticks % 20 == 0;
					if (isWaiting() && ticks % 8 == 0 && users.size() < ADD_AI_UNTIL) {
						users.add(new BotUserData());
					}
					if (isWaiting() & dd) {
						int leftTime = (int) (WAIT_SECONDS - ((System.currentTimeMillis() - cStartTime) / 1000));
						lastUsers = leftTime;
						for (PlayerData mud : users) {
							mud.countDown((int) leftTime, users.size());
						}
						if (leftTime % 20 == 0)
							Utils.l.info("Matchmaking",
									"Left joining time: " + leftTime + " sec. Users: " + users.size());
					} else if (dd) {
						// Prepare game for all users
						{
							int players = 0;
							for (PlayerData mud : users) {
								if (mud instanceof MatchUserData)
									players++;
							}
							if (players == 0) {
								Utils.l.fine("TypeMatch", "No real player found. Aborting game.");
								ended = true;
//								return;
							}
						}
						int botsAdded = 0;
						while (users.size() < ADD_AI_UNTIL) {
							addUser(new BotUserData());
							botsAdded++;
						}
						Utils.l.fine("TypeMatch", "Added " + botsAdded + " bot" + (botsAdded == 1 ? "" : "s") + ".");
						gs = GameState.LOADING;
						for (PlayerData mud : users) {
							mud.message("Preparing game...");
						}
					}
					break;
				case LOADING:
					if (System.currentTimeMillis() - makeTime <= WAIT_SECONDS * 1000 + 2000) {
						break;
					}
					final int mh = users.size() * PLAYER_GOAL;
					for (PlayerData mud : users) {
						mud.health = mh;
						mud.worker = new MatchWorker(TypeMatch.this) {

							@Override
							public int getMaxHealth() {
								return mh;
							}

							@Override
							public void score(int score) {
								// Damage all players
								// This code is very buggy when players die at the same moment
								if (ended) { // Game already ended
									// ignore packet
									return;
								}
								System.out.println(mud + ": " + score);
								mud.scores += score;
								mud.damages = 0;
								mud.health = Math.min(mh, mud.health + score * (users.size() / 3));
								mud.setHealth(mud.health);
								// 플레이어 랜덤으로 한명 씩 꺼내면서 (남은 인원 5명 이하이고 처음 죽은 사람이 나오면) 또는 (죽은 사람이 있으면)
								HashSet<Integer> s = new HashSet<>();
								for (int i = 0; (users.size() > 3 || i == 0) && !users.isEmpty(); i++) {
									int idx = (int) (Math.random() * users.size());
									while (s.contains(idx)) {
										idx = (int) (Math.random() * users.size());
									}
									s.add(idx);
									PlayerData pd = users.get(idx);
									double damage = score * (1 + pd.damages / 50);
									if (pd.health - damage <= 0) {
										pd.health = 0;
										killUser(pd, users.size());
									} else {
										pd.health -= damage;
										pd.setHealth(pd.health);
										mud.damages++;
									}
								}
								healthChanged = true;
							}
						};
						mud.playSignal(mh);
					}

					System.out.println("Starting match!!!!!===================================");
					maxUsers = users.size();
					gameStartTicks = ticks;
					gs = GameState.PLAYING;
					lastUsers = 0;
					break;
				case PLAYING:
//					System.out.println("PLAY TICK!!!!!!!");
					if (users.size() == 1) {
						users.get(0).health = 0;
						killUser(users.get(0), 1);
						Utils.l.fine("TypeMatch", "A GAME ENDED!");
						ended = true;
						return;
					} else if (users.size() <= 0) {
						ended = true;
					}
					long df = 280 - (ticks % 280) - 1;
					if ((df == 200 || df == 100 || (df <= 60 && df >= 20)) && ticks - gameStartTicks > 280
							&& ticks % 20 == 0) {
						for (PlayerData mud : users) {
							mud.message("The lowest score will die in " + (df / 20) + " seconds!");
						}
					} else if (df <= 0 && ticks - gameStartTicks > 280 && ticks % TICKS_PER_SECOND == 0) {
						double lows = -1;
						PlayerData lowp = null;
						lowp = users.get(0);
						lows = lowp.scores;
						for (PlayerData mud : users) {
							if (lows > mud.scores) {
								lowp = mud;
								lows = mud.scores;
							}
						}
						for (PlayerData mud : users) {
							mud.message("The lowest score died!");
						}
						lowp.health = 0;
						killUser(lowp, users.size());
					}
					

					if (ticks % 20 == 0 && healthChanged) {
						double sum = 0;
						double min = users.get(0).health;
						double max = 0;
						for (PlayerData mud : users) {
							sum += mud.health;
							min = Math.min(min, mud.health);
							max = Math.max(max, mud.health);
						}
						sum /= users.size();
						for (PlayerData mud : users) {
							mud.userStatus(max, sum, min);
						}
					}
					if (users.size() != lastUsers) {
						lastUsers = users.size();
						for (int i = 0; i < users.size(); i++) {
							users.get(i).leftUsers(users.size());
						}
					}
					//per bot damage
					if(ticks % 20 == 0) {
						for(int i=0;i<users.size();i++) {
							if(users.get(i) instanceof BotUserData) {
								users.get(i).worker.score(1);
							}
						}
					}
					break;
				}
				end();
			}

			private void end() {
				if (!ended()) {
					long dlyMs = lastRun + TICKRATE - System.currentTimeMillis() - lastInterval;
					if (dlyMs < 0) {
						Utils.l.warning("TypeMatch",
								"Can't keep up! Tick took too long and tick rate is extremely low!");
					}
					Server.gameGroup.schedule(this, Math.max(dlyMs, 0), TimeUnit.MILLISECONDS);
				} else {
					return;
				}
			}
		};
		Server.gameGroup.schedule(gameTick, 0, TimeUnit.SECONDS);
	}

	public void addUser(PlayerData mud) {
		// Prepare user join
		mud.initCon();

		users.add(mud);
	}

	int maxUsers = 0;

	public void killUser(PlayerData mud, int rank) {
		users.remove(mud);
		mud.gameOver(rank, maxUsers);
	}

	public String hashCodeString() {
		return Integer.toHexString(hashCode());
	}
}
