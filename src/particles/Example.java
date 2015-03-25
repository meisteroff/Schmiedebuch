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
 * Demo for different types of particles:
 * 1a. explosion (LEFT-click)
 * 1b. explosion (RIGHT-hold)
 * 2. rain drops (continuous)
 * 3. moving object with trail also known as particle emitter (continuous)
 * 
 * @author meisteroff
 */
public class Example extends ExampleAdapter {

	private SpriteBatch batch;
	private Sprite pixel;
	private final Color color = new Color();

	private int clock;
	private Array<Particle> particles;
	private final Vector2 particleEmitter = new Vector2();

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
		clock += 1;

		spawnRainDrop();

		if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
			spawnExplosion(10, Helper.mouseX(), Helper.mouseY());
		}

		particleEmitter.x = 640 / 2 + 640 / 2 * MathUtils.sin(.1f * clock / MathUtils.PI2);
		particleEmitter.y = 360 / 2 + 360 / 2 * MathUtils.sin(.2f * clock / MathUtils.PI2);

		spawnTrail(5, particleEmitter.x, particleEmitter.y);

		for (Iterator<Particle> it = particles.iterator(); it.hasNext();) {
			Particle particle = it.next();
			particle.age += 1;

			particle.velocityX += particle.accelerationX;
			particle.velocityY += particle.accelerationY;
			particle.x += particle.velocityX;
			particle.y += particle.velocityY;

			particle.angularVelocity += particle.angularAcceleration;
			particle.rotation += particle.angularVelocity;

			float progress = (particle.age / (float) particle.lifetime);
			particle.alpha = 1f - progress;

			if (particle.age > particle.lifetime) {
				it.remove();
			}
		}
	}

	private void draw() {
		Gdx.gl.glClearColor(.2f, .84f, .3f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();

		for (Particle particle : particles) {
			drawParticle(particle);
		}
		drawParticleEmitter(particleEmitter);

		batch.end();
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (button == Input.Buttons.LEFT) {
			spawnExplosion(500, Helper.mouseX(), Helper.mouseY());
		}

		return false;
	}

	private void spawnRainDrop() {
		Particle particle = new Particle();

		particle.x = MathUtils.random(640);
		particle.y = 360;
		particle.size = MathUtils.random(1f, 3f);

		particle.velocityX = MathUtils.random(.01f, .02f) * MathUtils.randomSign();
		particle.velocityY = -MathUtils.random(2.4f, 3.6f);

		particle.accelerationY = MathUtils.random(.01f, .02f) * Math.signum(particle.velocityY);

		particle.lifetime = MathUtils.random(130, 180);

		particle.red = MathUtils.random(.2f);
		particle.green = MathUtils.random(.3f);
		particle.blue = MathUtils.random(.4f, .9f);
		particle.alpha = 1;

		particles.add(particle);
	}

	private void spawnExplosion(int amount, float x, float y) {
		Vector2 vector = new Vector2();
		for (int i = 0; i < amount; i++) {
			Particle particle = new Particle();

			particle.x = x;
			particle.y = y;
			particle.size = MathUtils.random(2f, 6f);

			vector.set(1, 0).rotate(MathUtils.random(360f)).scl(MathUtils.random(1.2f, 3.4f));
			particle.velocityX = vector.x;
			particle.velocityY = vector.y;

			particle.accelerationX = MathUtils.random(.02f, .04f) * Math.signum(-particle.velocityX);
			particle.accelerationY = MathUtils.random(.02f, .04f) * Math.signum(-particle.velocityY);

			particle.lifetime = MathUtils.random(30, 80);

			particle.red = MathUtils.random(.2f, .8f);
			particle.green = MathUtils.random(.2f, .8f);
			particle.blue = MathUtils.random(.3f);
			particle.alpha = 1;

			particle.angularVelocity = MathUtils.random(3.5f) * MathUtils.randomSign();
			particle.angularAcceleration = MathUtils.random(.5f) * MathUtils.randomSign();

			particles.add(particle);
		}
	}

	private void spawnTrail(int amount, float x, float y) {
		Vector2 vector = new Vector2();
		for (int i = 0; i < amount; i++) {
			Particle particle = new Particle();

			particle.x = x;
			particle.y = y;
			particle.size = MathUtils.random(2f, 6f);

			vector.set(1, 0).rotate(MathUtils.random(360f)).scl(MathUtils.random(.2f, .4f));
			particle.velocityX = vector.x;
			particle.velocityY = vector.y;

			particle.lifetime = MathUtils.random(10, 20);

			particle.red = MathUtils.random(.8f, .9f);
			particle.green = MathUtils.random(.8f, .9f);
			particle.blue = MathUtils.random(.8f, .9f);
			particle.alpha = 1;

			particle.angularVelocity = MathUtils.random(3.5f) * MathUtils.randomSign();
			particle.angularAcceleration = MathUtils.random(.2f) * MathUtils.randomSign();

			particles.add(particle);
		}
	}

	private void drawParticle(Particle particle) {
		float width = particle.size;
		float height = particle.size;

		/* draw centered around x,y */
		float x = particle.x - width / 2f;
		float y = particle.y - height / 2f;

		float rotation = particle.rotation;

		color.set(particle.red, particle.green, particle.blue, particle.alpha);

		/* rotate around center */
		float originX = width / 2f;
		float originY = height / 2f;

		rectangle(x, y, width, height, originX, originY, rotation, color);
	}

	private void drawParticleEmitter(Vector2 emitter) {
		float width = 16;
		float height = 16;

		/* draw centered around x,y */
		float x = emitter.x - width / 2f;
		float y = emitter.y - height / 2f;

		color.set(1, 1, 1, 1);

		rectangle(x, y, width, height, 0, 0, 0, color);
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
		new LwjglApplication(new Example(), Helper.configuration("Particles"));
	}

}
