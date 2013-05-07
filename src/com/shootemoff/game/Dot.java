package com.shootemoff.game;

public class Dot
{
	public enum Type {Enemy, Health, Gunshot}

	public Type type;
	public VectorF speed;
	public float energy; // Max 1.0F
	public VectorF coords;
	public static float maxRadius;
	public double angle_radians; // used only for gun shots
}
