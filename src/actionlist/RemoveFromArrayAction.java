package actionlist;

import actionlist.implementation.NonBlockingAction;

import com.badlogic.gdx.utils.Array;

public class RemoveFromArrayAction<T> extends NonBlockingAction {

	private final Array<T> array;
	private final T value;

	public RemoveFromArrayAction(Array<T> array, T value) {
		this.array = array;
		this.value = value;
	}

	@Override
	public void prepare() {
	}

	@Override
	public void cleanup() {
		array.removeValue(value, true);
	}

	@Override
	public void update() {
	}

	@Override
	public boolean finished() {
		return true;
	}

}
