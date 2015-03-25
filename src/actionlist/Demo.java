package actionlist;

import helper.Adapter;
import helper.Helper;
import actionlist.implementation.ActionList;
import actionlist.implementation.NonBlockingActionList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Demo for different types of actions:<br><br>
 *
 * 1. WaypointAction (LEFT-click)<br> 
 * 2. MoveAction (bundled, automatically)<br> 
 * 3. RemoveFromArrayAction (bundled, automatically)<br>
 * 
 * @author meisteroff
 */
public class Demo extends Adapter {

	private SpriteBatch batch;
	private Sprite pixel;

	private final Color sharedColor = new Color();

	private ActionList unitActions;
	private Vector2 unit;
	/* only used to draw the targets of the unit (=move queue) */
	private Array<Vector2> targets;

	@Override
	public void create() {
		batch = new SpriteBatch();
		pixel = Helper.makePixel();

		unitActions = new NonBlockingActionList();
		unit = new Vector2();
		targets = new Array<Vector2>();

		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render() {
		update();
		draw();
	}

	private void update() {
		unitActions.update();
	}

	private void draw() {
		Gdx.gl.glClearColor(.2f, .84f, .3f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();

		for (int i = 0; i < targets.size; i++) {
			Vector2 target = targets.get(i);

			float percentage = i / (float) targets.size;
			sharedColor.r = 1;
			sharedColor.g = .8f * percentage;
			sharedColor.b = .4f * percentage;
			sharedColor.a = 1;

			rectangle(target.x - 4, target.y - 4, 8, 8, 4, 4, 0, sharedColor);
		}
		rectangle(unit.x - 8, unit.y - 8, 16, 16, 8, 8, 0, Color.BLACK);

		batch.end();
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (button == Input.Buttons.LEFT) {
			Vector2 source = unit;
			Vector2 target = new Vector2(Helper.mouseX(), Helper.mouseY());
			float speed = 2;

			targets.add(target);

			unitActions.add(new WaypointAction(source, target, speed, targets));
		}

		return false;
	}

	private void rectangle(float x, float y, float width, float height, float originX, float originY, float rotation, Color color) {
		pixel.setPosition(x, y);
		pixel.setOrigin(originX, originY);
		pixel.setRotation(rotation);
		pixel.setSize(width, height);
		pixel.setColor(color);
		pixel.draw(batch);
	}

	public static void main(String[] args) {
		new LwjglApplication(new Demo(), Helper.configuration("Particles"));
	}

}
