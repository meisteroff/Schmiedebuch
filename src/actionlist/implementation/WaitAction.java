package actionlist.implementation;

/**
 * @author meisteroff
 */
public class WaitAction extends Action {

	private int time;

	public WaitAction(int time) {
		super(true);
		this.time = time;
	}

	@Override
	public void prepare() {
	}

	@Override
	public void cleanup() {
	}

	@Override
	public void update() {
		time -= 1;
	}

	@Override
	public boolean finished() {
		return time <= 0;
	}

}
