package type.server.game;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import io.netty.channel.EventLoopGroup;
import type.common.work.Utils;

public class MatchMaking {
	public static final LinkedList<MatchUserData> queue = new LinkedList<>();
	public static void addQueue(MatchUserData mud) {
		queue.add(mud);
		Utils.l.info("Matchmaking", mud.handle.getName() + ": Matchmaking started");
	}

	public static void startThread(EventLoopGroup group) {
		Runnable matchMaker = new Runnable() {
			private ArrayList<TypeMatch> joinable = new ArrayList<>();

			public void run() {
//				Utils.l.info("Matchmaking", "Joinable matches: " + joinable.size());
//				if(joinable.isEmpty()) {
//					Utils.l.info("Matchmaking", "Initializing first match");
//					joinable.add(new TypeMatch());
//				}
				int matched = 0;
				while (matched++ < 100 && !queue.isEmpty()) {
					MatchUserData mud = queue.removeFirst();
					TypeMatch tojoin = null;
					for (int i = 0; i < joinable.size(); i++) {
						TypeMatch jointest = joinable.get(i);
						Utils.l.info("Matchmaking", "Match " + jointest.hashCodeString() + ": Waiting state info: " + jointest.isWaiting());
						if (!jointest.isWaiting()) {
							joinable.remove(jointest);
							i--;
							Utils.l.info("Matchmaking", "Match " + jointest.hashCodeString() + ": Matching finished");
						} else if (jointest.canJoin()) {
							tojoin = jointest;
							break;
						}
					}
					if (tojoin == null) {
						TypeMatch newMatch = new TypeMatch();
						Utils.l.info("Matchmaking", "Created new match: " + newMatch.hashCodeString());
						joinable.add(newMatch);
						tojoin = newMatch;
					}

					tojoin.addUser(mud);
					Utils.l.info("Matchmaking", mud.handle.getName() + ": Matchmaking completed");
				}
			}
		};
		group.scheduleAtFixedRate(matchMaker, 0, 5, TimeUnit.SECONDS);
		Utils.l.info("Matchmaking", "Scheduled matchmaking!");
	}
}
