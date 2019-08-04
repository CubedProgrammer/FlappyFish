package flappy.entities;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
public class WormHole extends Entity
{
	public static final BufferedImage space;
	static
	{
		BufferedImage s=null;
		try
		{
			s=ImageIO.read(WormHole.class.getResourceAsStream("/assets/textures/space.png"));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			space=s;
		}
	}
	public WormHole exit;
	public WormHole(WormHole exit,int x,int y)
	{
		super("wormy",x,y);
		this.vx=-1;
		if(exit!=null)
		{
			(this.exit=exit).exit=this;
		}
	}
	public void tick()
	{
		
	}
	public void render(Graphics2D g)
	{
		BufferedImage b=new BufferedImage(64,64,2);
		int d=0;
		int h=0;
		for(int i=0;i<64;i++)
		{
			d=(int)Math.sqrt(1024-(h=32-i)*h);
			for(int j=32-d;j<32+d;j++)
			{
				b.setRGB(j,i,this.x+j>=0&&this.x+j<WormHole.space.getWidth()&&this.y+i>=0&&this.y+i<WormHole.space.getHeight()?WormHole.space.getRGB(this.x+j,this.y+i):0xFF000000);
			}
		}
		g.drawImage(b,this.x-32,this.y-32,null);
	}
}