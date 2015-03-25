package particles;

import helper.Adapter;
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
 * Demo for different types of particles:<br><br>
 * 
 * 1a. explosion (LEFT-click)<br>
 * 1b. explosion (RIGHT-hold)<br>
 * 2. rain drops (continuous)<br>
 * 3. moving object with trail (continuous)<br>
 * 4. moving particle emitter as parent (every x ticks)
 * 
 * @author meisteroff
 */
public class Demo extends Adapter {

	private SpriteBatch batch;
	private Sprite pixel;

	private final Color sharedColor = new Color();
	private final Vector2 sharedVector = new Vector2();

	private int clock;
	private Array<Particle> particles;
	private final Vector2 particleEmitterWithTrail = new Vector2();
	private final Vector2 particleEmitterAsParent = new Vector2();

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

		particleEmitterWithTrail.x = 640 / 2 + 640 / 2 * MathUtils.sin(.1f * clock / MathUtils.PI2);
		particleEmitterWithTrail.y = 360 / 2 + 360 / 2 * MathUtils.sin(.2f * clock / MathUtils.PI2);

		particleEmitterAsParent.x = 640 / 2 + 640 / 4 * MathUtils.sin(.2f * clock / MathUtils.PI2);
		particleEmitterAsParent.y = 360 / 2 + 360 / 4 * MathUtils.sin(.1f * clock / MathUtils.PI2);

		spawnTrail(5, particleEmitterWithTrail.x, particleEmitterWithTrail.y);

		if (clock % 60 == 0) {
			spawnExplosionWithParent(50, particleEmitterAsParent);
		}

		for (Iterator<Particle> it = particles.iterator(); it.hasNext();) {
			Particle particle = it.next();
			particle.age += 1;

			particle.velocityX += particle.accelerationX;
			particle.velocityY += particle.accelerationY;
			particle.positionX += particle.velocityX;
			particle.positionY += particle.velocityY;

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
		drawParticleEmitter(particleEmitterWithTrail);
		drawParticleEmitter(particleEmitterAsParent);

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

		particle.positionX = MathUtils.random(640);
		particle.positionY = 360;
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
		for (int i = 0; i < amount; i++) {
			particles.add(makeExplosionParticle(x, y));
		}
	}

	private void spawnExplosionWithParent(int amount, Vector2 parent) {
		for (int i = 0; i < amount; i++) {
			Particle particle = makeExplosionParticle(0, 0);
			particle.parent = parent;
			particles.add(particle);
		}
	}

	private void spawnTrail(int amount, float x, float y) {
		Vector2 vector = new Vector2();
		for (int i = 0; i < amount; i++) {
			Particle particle = new Particle();

			particle.positionX = x;
			particle.positionY = y;
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

	private Particle makeExplosionParticle(float x, float y) {
		Particle particle = new Particle();

		particle.positionX = x;
		particle.positionY = y;
		particle.size = MathUtils.random(2f, 6f);

		sharedVector.set(1, 0).rotate(MathUtils.random(360f)).scl(MathUtils.random(1.2f, 3.4f));
		particle.velocityX = sharedVector.x;
		particle.velocityY = sharedVector.y;

		particle.accelerationX = MathUtils.random(.02f, .04f) * Math.signum(-particle.velocityX);
		particle.accelerationY = MathUtils.random(.02f, .04f) * Math.signum(-particle.velocityY);

		particle.lifetime = MathUtils.random(30, 80);

		particle.red = MathUtils.random(.2f, .8f);
		particle.green = MathUtils.random(.2f, .8f);
		particle.blue = MathUtils.random(.3f);
		particle.alpha = 1;

		particle.angularVelocity = MathUtils.random(3.5f) * MathUtils.randomSign();
		particle.angularAcceleration = MathUtils.random(.5f) * MathUtils.randomSign();

		return particle;
	}

	private void drawParticle(Particle particle) {
		float width = particle.size;
		float height = particle.size;

		/* draw centered around x,y */
		float x = particle.positionX - width / 2f;
		float y = particle.positionY - height / 2f;

		if (particle.parent != null) {
			x += particle.parent.x;
			y += particle.parent.y;
		}

		float rotation = particle.rotation;

		sharedColor.set(particle.red, particle.green, particle.blue, particle.alpha);

		/* rotate around center */
		float originX = width / 2f;
		float originY = height / 2f;

		rectangle(x, y, width, height, originX, originY, rotation, sharedColor);
	}

	private void drawParticleEmitter(Vector2 emitter) {
		float width = 16;
		float height = 16;

		/* draw centered around x,y */
		float x = emitter.x - width / 2f;
		float y = emitter.y - height / 2f;

		sharedColor.set(1, 1, 1, 1);

		rectangle(x, y, width, height, 0, 0, 0, sharedColor);
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
