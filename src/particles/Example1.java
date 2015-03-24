package particles;

import helper.ExampleAdapter;
import helper.Helper;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Demo for two different types of particles:
 * 1a. explosion (LEFT-click)
 * 1b. explosion (RIGHT-hold)
 * 2. rain drops (continous)
 * 
 * @author meisteroff
 */
public class Example1 extends ExampleAdapter {

	private SpriteBatch batch;
	private Sprite pixel;

	private Array<Particle> particles;

	@Override
	public void create() {
		batch = new SpriteBatch();
		pixel = Helper.makePixel();
		particles = new Array<Particle>();

		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render() {
		update();
		draw();
	}

	private void update() {
		spawnRainDrop();

		if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
			spawnExplosion(Helper.mouseX(), Helper.mouseY());
		}

		for (Iterator<Particle> it = particles.iterator(); it.hasNext();) {
			Particle particle = it.next();
			particle.age += 1;

			particle.velX += particle.accX;
			particle.velY += particle.accY;
			particle.x += particle.velX;
			particle.y += particle.velY;

			float progress = (particle.age / (float) particle.lifetime);
			particle.a = 1f - progress;

			if (particle.age > particle.lifetime) {
				it.remove();
			}
		}
	}

	private void draw() {
		Gdx.gl.glClearColor(.2f, .84f, .3f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();

		Color color = new Color();
		for (Particle particle : particles) {
			float x = particle.x;
			float y = particle.y;
			float width = particle.size;
			float height = particle.size;
			color.set(particle.r, particle.g, particle.b, particle.a);

			drawRectangle(x, y, width, height, color);
		}

		batch.end();
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (button == Input.Buttons.LEFT) {
			spawnExplosion(Helper.mouseX(), Helper.mouseY());
		}

		return false;
	}

	private void spawnRainDrop() {
		Particle particle = new Particle();

		particle.x = MathUtils.random(640);
		particle.y = 360;
		particle.size = MathUtils.random(1f, 3f);

		particle.velX = MathUtils.random(.01f, .02f) * MathUtils.randomSign();
		particle.velY = -MathUtils.random(2.4f, 3.6f);

		particle.accY = MathUtils.random(.01f, .02f) * Math.signum(particle.velY);

		particle.lifetime = MathUtils.random(130, 180);

		particle.r = MathUtils.random(.2f);
		particle.g = MathUtils.random(.3f);
		particle.b = MathUtils.random(.4f, .9f);
		particle.a = 1;

		particles.add(particle);
	}

	private void spawnExplosion(float x, float y) {
		Vector2 vector = new Vector2();
		for (int i = 0; i < 100; i++) {
			Particle particle = new Particle();

			particle.x = x;
			particle.y = y;
			particle.size = MathUtils.random(2f, 4f);

			vector.set(1, 0).rotate(MathUtils.random(360f)).scl(MathUtils.random(1.2f, 3.4f));
			particle.velX = vector.x;
			particle.velY = vector.y;

			particle.accX = MathUtils.random(.02f, .04f) * Math.signum(-particle.velX);
			particle.accY = MathUtils.random(.02f, .04f) * Math.signum(-particle.velY);

			particle.lifetime = MathUtils.random(40, 60);

			particle.r = MathUtils.random(.2f, .8f);
			particle.g = MathUtils.random(.2f, .8f);
			particle.b = MathUtils.random(.3f);
			particle.a = 1;

			particles.add(particle);
		}
	}

	public void drawRectangle(float x, float y, float width, float height, Color color) {
		/* draw centered around x,y */
		pixel.setPosition(x - width / 2f, y - height / 2f);
		pixel.setSize(width, height);
		pixel.setColor(color);
		pixel.draw(batch);
	}

	public static void main(String[] args) {
		new LwjglApplication(new Example1(), Helper.configuration());
	}

}
