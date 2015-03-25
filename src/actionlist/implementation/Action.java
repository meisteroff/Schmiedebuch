package actionlist.implementation;

public abstract class Action {

	protected final boolean blocking;
	
	public Action(boolean blocking) {
		this.blocking = blocking;
	}
	
	public abstract void prepare();

	public abstract void cleanup();

	public abstract void update();

	public abstract boolean finished();
	
}
