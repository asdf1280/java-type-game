package type.server.game;

public abstract class MatchWorker {
	protected TypeMatch tym;

	public MatchWorker(TypeMatch tm) {
		tym = tm;
	}
	
	public abstract void score(int score);
	
	public abstract int getMaxHealth();
}
