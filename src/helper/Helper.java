package helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * @author meisteroff
 */
public class Helper {

	public static Sprite makePixel() {
		Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);

		int white = 0xFFFFFFFF;
		pixmap.drawPixel(0, 0, white);

		return new Sprite(new Texture(pixmap));
	}

	public static LwjglApplicationConfiguration configuration() {
		LwjglApplicationConfiguration configuration = new LwjglApplicationConfiguration();
		configuration.title = "";
		configuration.width = 640;
		configuration.height = 360;
		configuration.resizable = false;
		configuration.vSyncEnabled = false;
		configuration.foregroundFPS = 60;

		return configuration;
	}
	
	public static float mouseX() {
		return Gdx.input.getX();
	}
	
	public static float mouseY() {
		return 360 - Gdx.input.getY();
	}

}
