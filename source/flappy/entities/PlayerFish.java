package flappy.entities;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
public class PlayerFish extends Entity
{
	private int jumping;
	private BufferedImage texture;
	public PlayerFish(int x,int y)
	{
		super("Flappy Fish",x,y);
		try
		{
			this.texture=ImageIO.read(this.getClass().getResourceAsStream("/assets/textures/fish.png"));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public void jump()
	{
		if(this.jumping==0)
		{
			this.vy=-14;
			this.jumping=20;
		}
	}
	public void tick()
	{
		if(this.jumping>0)
		{
			this.jumping--;
		}
	}
	public void render(Graphics2D g)
	{
		g.drawImage(this.texture,this.x-16,this.y-16,32,32,null);
	}
}