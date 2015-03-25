package actionlist;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import actionlist.implementation.BlockingActionList;

/**
 * A bundled action consisting of a MoveAction and RemoveFromArrayAction.
 * Once the unit reaches its destination the RemoveFromArrayAction will take care of
 * removing the target from the waypoint drawing list.
 * 
 * @author meisteroff
 */
public class WaypointAction extends BlockingActionList {

	public WaypointAction(Vector2 source, Vector2 target, float speed, Array<Vector2> targets) {
		add(new MoveToAction(source, target, speed));
		add(new RemoveFromArrayAction<Vector2>(targets, target));
	}

}
