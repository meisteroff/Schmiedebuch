# Particles

Particles give life to every game and are quickly implemented.

Here is the data needed for an accelerating particle:
```
Particle {
	position: [x,y],
	velocity: [x,y],
	acceleration: [x,y]
}
```

Basic physics tells us how the velocity and position will be updated every iteration:
```
Particle.update() {
	velocity.x += acceleration.x;
	velocity.y += acceleration.y;
	position.x += velocity.x;
	position.y += velocity.y;
}
```

This is everything a basic particle system requires.

Some additional useful data to store within a particle:

- **texture:** drawing more than just basic shapes should definitely be considered
- **age/lifetime:** can be used for triggers like particle death (=removal)
- **color:** more variety for particle effects, for example alpha value based on age/lifetime
- **size:** more variety for particle effects
- **rotation:** more variety for particle effects
- **angularVelocity/angularAcceleration:** more variety for particle effects
- **parent:** reference for relative positioning, like moving particle emitters

These should cover most basic effects. When a more sophisticated system is needed
3rd party libraries should probably be considered.