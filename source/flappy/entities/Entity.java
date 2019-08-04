package flappy.entities;
import java.awt.Graphics2D;
public abstract class Entity
{
	protected int x,y;
	protected int vx,vy;
	protected String name;
	public Entity(String name,int x,int y)
	{
		this.name=name;
		this.x=x;
		this.y=y;
	}
	public int getX()
	{
		return this.x;
	}
	public int getY()
	{
		return this.y;
	}
	public int getVelocityX()
	{
		return this.vx;
	}
	public int getVelocityY()
	{
		return this.vy;
	}
	public String name()
	{
		return this.name;
	}
	public void setX(int x)
	{
		this.x=x;
	}
	public void setY(int y)
	{
		this.y=y;
	}
	public void setVelocityX(int vx)
	{
		this.vx=vx;
	}
	public void setVelocityY(int vy)
	{
		this.vy=vy;
	}
	public abstract void tick();
	public abstract void render(Graphics2D g);
	public void run()
	{
		this.x+=vx;
		this.y+=vy;
		this.tick();
	}
	public int distanceBetweenSquared(Entity e)
	{
		int x=e.x-this.x,y=e.y-this.y;
		return x*x+y*y;
	}
}