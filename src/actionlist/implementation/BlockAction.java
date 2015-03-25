package actionlist.implementation;

public class BlockAction extends Action {

	private final ActionList actionList;
	
	public BlockAction(ActionList actionList) {
		super(true);
		this.actionList = actionList;
	}

	@Override
	public void prepare() {
	}

	@Override
	public void cleanup() {
	}

	@Override
	public void update() {
	}

	@Override
	public boolean finished() {
		return actionList.isFirst(this);
	}

}
