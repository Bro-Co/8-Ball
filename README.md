# Particle Collision Simulator

A Newtonian simulation of particle collisions in a 2D enviornment, built from the ground up using **Java**.

To increase efficiency, the engine precomputes collisions, which are stored in a priority queue. This avoids having to check for collisions every frame. Outdated projected colllisions are simply discarded when they reach the front of the queue.

![Project Demo](https://github.com/smchase/Physics-Engine/blob/master/demo.gif)
