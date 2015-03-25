# ActionList

An ActionList is a queue of *actions* or *events*. What distinguishes them from a simple FIFO (=First In First Out) queue is the idea of **blocking** and **non-blocking** actions.

Here is the data required for an Action and ActionList:
```
Action {
	blocking: boolean,
	finished: boolean,
	update: function()
}
```
```
ActionList {
	queue: List[Action]
}
```

To see why the *blocking* is relevant one should consider the following:
```
ActionList.update() {
	for (action in queue) {
		action.update();
		if (action.finished) {
			queue.remove(action);
		}		
		if (action.blocking) {
			break;
		}
	}
}
```

This simple ***break*** is what makes the use of an ActionList so powerful. Imagine a game where you want to issue commands to your units. Every command should be registered as they come but only executed one after the other. It might look something like this:

> 0. MoveTo 10,10
> 0. ThrowGrenadeTo to 11,10
> 0. MoveTo 10,15
> 0. ThrowGrenadeTo to 9,15
> 0. ...

The same effect can of course be achieved with a FIFO queue where only the front Action is updated.

However, consider this example:

> 0. LookForEnemy (non-blocking)
> 0. MoveTo 10,10 (blocking)
> 0. ThrowGrenadeTo 11,10 (blocking)
> 0. LookForEnemy (non-blocking)
> 0. MoveTo 10,15 (blocking)
> 0. ThrowGrenadeTo 9,15 (blocking)
> 0. ...

Now the unit always will look for an enemy while moving and can react when one is spotted.

Another consideration should be bundling these dependant actions together:
```
MoveToAction (blocking):
	1. lookForEnemy,
	2. prepareNextWeapon,
	3. move
```

To allow this bundling in code a neat trick can be used:
```
ActionList extends Action {
	queue: List[Action]
}
```

Note the added *extends Action*. Now one can put lists within lists, both blocking and non-blocking ones.

For more information I highly recommend watching ***[Action! Lists](https://www.youtube.com/watch?v=o6CaB-hmqoE)***