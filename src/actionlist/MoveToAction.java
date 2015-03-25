package actionlist;

import com.badlogic.gdx.math.Vector2;

import actionlist.implementation.BlockingAction;


/**
 * @author meisteroff
 */
public class MoveToAction extends BlockingAction {

	private final Vector2 source;
	private final Vector2 target;

	private final float speed;
	private final Vector2 vector = new Vector2();
	private boolean targetReached;

	public MoveToAction(Vector2 source, Vector2 target, float speed) {
		this.source = source;
		this.target = target;
		this.speed = speed;
	}

	@Override
	public void prepare() {
	}

	@Override
	public void cleanup() {
	}

	@Override
	public void update() {
		vector.set(target).sub(source);

		float distance = vector.len();

		if (distance <= speed) {
			targetReached = true;
			vector.nor().scl(distance);
		} else {
			vector.nor().scl(speed);
		}

		source.add(vector);
	}

	@Override
	public boolean finished() {
		return targetReached;
	}

}
