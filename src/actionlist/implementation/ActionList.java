package actionlist.implementation;

import java.util.Iterator;

import com.badlogic.gdx.utils.Array;

public class ActionList extends Action {

	private final Array<Action> actions = new Array<Action>();
	private final Array<Action> previous = new Array<Action>();

	public ActionList(boolean blocking) {
		super(blocking);
	}

	@Override
	public void prepare() {
	}

	@Override
	public void cleanup() {
	}

	@Override
	public void update() {
		for (Iterator<Action> it = actions.iterator(); it.hasNext();) {
			Action action = it.next();

			attemptPrepare(action);
			action.update();

			if (action.finished()) {
				action.cleanup();
				previous.removeValue(action, true);
				it.remove();
			}

			if (action.blocking) {
				break;
			}
		}
	}

	@Override
	public boolean finished() {
		return actions.size == 0;
	}

	public void add(Action action) {
		actions.add(action);
	}

	public boolean isFirst(Action action) {
		if (actions.size == 0) {
			return false;
		}

		return actions.get(0) == action;
	}

	private void attemptPrepare(Action action) {
		for (Action a : previous) {
			if (a == action) {
				return;
			}
		}

		action.prepare();
		previous.add(action);
	}

}
