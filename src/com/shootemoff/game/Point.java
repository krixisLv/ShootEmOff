package com.shootemoff.game;

public class Point {
	private double x;
	private double y;
	
	public Point(double new_x, double new_y)
	{
		x = new_x;
		y = new_y;
	}
	
	public double GetX()
	{
		return x;
	}
	
	public double GetY()
	{
		return y;
	}
	
	public void ChangeX(double new_x)
	{
		x = new_x;
	}
	
	public void ChangeY(double new_y)
	{
		y = new_y;
	}
}
