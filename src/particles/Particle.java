package particles;

import com.badlogic.gdx.math.Vector2;

/**
 * @author meisteroff
 */
public class Particle {

	float positionX, positionY;
	float velocityX, velocityY;
	float accelerationX, accelerationY;
	
	float rotation;
	float angularVelocity;
	float angularAcceleration;

	float size;
	float red, green, blue, alpha;

	int age, lifetime;
	
	Vector2 parent;

}
