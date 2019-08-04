package flappy.main;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.io.*;
import java.util.Vector;
import javax.swing.*;
import flappy.entities.*;
public class FlappyFish extends Canvas
{
	private static final long serialVersionUID=-179455871123455671L;
	public static final FlappyFish flappy=new FlappyFish();
	public static final int modulus(int a,int b)
	{
		return(a%=b)<0?a+b:a;
	}
	private Thread commands;
	private boolean running;
	private PlayerFish fish;
	private Vector<Entity>entities;
	private int[][]pipes;
	private boolean alive;
	private boolean playing;
	private int score;
	private BufferedReader reader;
	public FlappyFish()
	{
		super();
		this.setSize(1014,566);
		this.setPreferredSize(this.getSize());
		this.setBackground(new Color(0));
		this.addKeyListener(new KeyHandler());
		this.fish=new PlayerFish(512,288);
		this.pipes=new int[][]{{1024,300},{1300,100},{1576,200},{1852,300}};
		this.entities=new Vector<Entity>();
		this.running=true;
		this.commands=new Thread(this::parse);
		this.reader=new BufferedReader(new InputStreamReader(System.in));
		this.makeWormHole(2040,240,2340,360);
	}
	public void makeWormHole(int w,int x,int y,int z)
	{
		WormHole a=new WormHole(null,w,x),b=new WormHole(a,y,z);
		this.entities.add(a);
		this.entities.add(b);
	}
	public void parse()
	{
		try
		{
			String cmd=this.reader.readLine();
			while(cmd!=null)
			{
				String[]args=cmd.split(" ");
				if(args.length>0)
				{
					if("wormhole".equals(args[0]))
					{
						this.makeWormHole(Integer.parseInt(args[1]),Integer.parseInt(args[2]),Integer.parseInt(args[3]),Integer.parseInt(args[4]));
						System.out.println("Created wormholes at specified coordinates. Wormholes at negative coordinates will not spawn.");
					}
					else if("addscore".equals(args[0]))
					{
						this.score+=Integer.parseInt(args[1]);
						System.out.println("Added "+args[1]+" to the score. Score is now "+Integer.toString(this.score)+".");
					}
					else if("subtractscore".equals(args[0]))
					{
						this.score-=Integer.parseInt(args[1]);
						System.out.println("Subtracted "+args[1]+" to the score. Score is now "+Integer.toString(this.score)+".");
					}
					else if("setscore".equals(args[0]))
					{
						this.score=Integer.parseInt(args[1]);
						System.out.println("Set "+args[1]+" as the score. Score is now "+Integer.toString(this.score)+".");
					}
					else if("kill".equals(args[0]))
					{
						this.alive=false;
						System.out.println("Killed yourself!");
					}
					else if("help".equals(args[0]))
					{
						System.out.println("wormhole x1 y1 x2 y2\nCreates a wormhole.\naddscore x\nAdds x to the score.\nsubtractscore x\nSubtracts x from the score.\nsetscore x\nSets the score to x.\nkill\nKill yourself.\nhelp\nShows this list.");
						System.out.println("type quit to quit");
					}
					else if("quit".equals(args[0]))
					{
						this.running=false;
					}
					else
					{
						System.out.println("Unknown command. Type \"help\" for a list of commands.");
					}
				}
				cmd=reader.readLine();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public void paint(Graphics2D g)
	{
		g.setColor(new Color(64));
		g.fillRect(0,0,this.getWidth(),this.getHeight());
		for(int i=0;i<this.entities.size();i++)
		{
			this.entities.get(i).render(g);
		}
		for(int i=0;i<this.pipes.length;i++)
		{
			g.setColor(new Color(16384));
			g.fillRect(this.pipes[i][0],0,80,this.pipes[i][1]-16);
			g.fillRect(this.pipes[i][0],this.pipes[i][1]+208,80,this.getHeight()-(this.pipes[i][1]+208));
			g.fillRect(this.pipes[i][0]-4,this.pipes[i][1]-16,88,16);
			g.fillRect(this.pipes[i][0]-4,this.pipes[i][1]+192,88,16);
			g.setColor(new Color(65286));
			g.fillRect(this.pipes[i][0]+4,0,72,this.pipes[i][1]-16);
			g.fillRect(this.pipes[i][0]+4,this.pipes[i][1]+208,72,this.getHeight()-(this.pipes[i][1]+208));
			g.fillRect(this.pipes[i][0],this.pipes[i][1]-12,80,8);
			g.fillRect(this.pipes[i][0],this.pipes[i][1]+196,80,8);
		}
		this.fish.render(g);
		if(this.playing)
		{
			g.setColor(new Color(-1));
			g.setFont(new Font("Tahoma",0,16));
			g.drawString(Integer.toString(this.score),4,16);
		}
	}
	public void tick()
	{
		KeyHandler k=(KeyHandler)this.getKeyListeners()[0];
		if(this.playing)
		{
			boolean xcollide=false,ycollide=false;
			int distance=0;
			if(k.up&&this.alive)
			{
				this.fish.jump();
			}
			if(this.fish.getVelocityY()<16)
			{
				this.fish.setVelocityY(this.fish.getVelocityY()+1);
			}
			this.fish.run();
			if(this.fish.getY()>560)
			{
				this.fish.setY(560);
			}
			else if(this.fish.getY()<0)
			{
				this.fish.setY(0);
			}
			for(int i=0;i<this.entities.size()&&this.alive;i++)
			{
				this.entities.get(i).run();
				if("wormy".equals(this.entities.get(i).name()))
				{
					if(this.fish.distanceBetweenSquared(this.entities.get(i))<=256)
					{
						this.fish.setY(((WormHole)this.entities.get(i)).exit.getY()+17);
						this.fish.setVelocityY(0);
						this.score+=distance=((WormHole)this.entities.get(i)).exit.getX()-this.fish.getX();
						for(int j=0;j<this.entities.size();j++)
						{
							this.entities.get(j).setX(this.entities.get(j).getX()-distance);
						}
						for(int j=0;j<this.pipes.length;j++)
						{
							this.pipes[j][0]-=distance;
							this.pipes[j][0]=FlappyFish.modulus(this.pipes[j][0]+80,1104)-80;
						}
					}
				}
			}
			for(int i=0;i<this.pipes.length&&this.alive;i++)
			{
				if(pipes[i][0]==-80)
				{
					pipes[i][0]=1024;
					pipes[i][1]=(int)(System.nanoTime()%384);
				}
				else
				{
					pipes[i][0]--;
				}
				xcollide=this.fish.getX()+16>pipes[i][0]&&this.fish.getX()<=pipes[i][0]+96;
				ycollide=this.fish.getY()>pipes[i][1]+176||this.fish.getY()<=pipes[i][1]+16;
				if(xcollide&&ycollide)
				{
					this.alive=false;
				}
			}
			if(this.score%4000==0&&this.score!=0)
			{
				this.makeWormHole(pipes[0][0]+1024,pipes[0][1]+80,pipes[1][0]+1024,500);
			}
			if(this.alive)
			{
				this.score++;
			}
			else if(k.enter)
			{
				this.fish=new PlayerFish(512,288);
				this.pipes=new int[][]{{1024,300},{1300,100},{1576,200},{1852,300}};
				for(int i=0;i<this.entities.size();this.entities.get(i).setX(this.entities.get(i++).getX()+this.score));
				this.score=0;
				this.playing=false;
			}
		}
		else
		{
			if(k.enter)
			{
				this.playing=true;
				this.alive=true;
			}
		}
	}
	public void render()
	{
		BufferStrategy bs=this.getBufferStrategy();
		if(bs==null)
		{
			this.createBufferStrategy(3);
			bs=this.getBufferStrategy();
		}
		Graphics2D g=(Graphics2D)bs.getDrawGraphics();
		this.paint(g);
		g.dispose();
		bs.show();
	}
	public static final void main(String[]args)
	{
		JFrame frame=new JFrame("Flappy Fish");
		frame.add(FlappyFish.flappy);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(3);
		frame.setResizable(false);
		frame.setVisible(true);
		Thread thread=new Thread(()->
		{
			long then=System.nanoTime();
			long elapsed=0;
			long now=0;
			while(FlappyFish.flappy.running)
			{
				now=System.nanoTime();
				elapsed+=now-then;
				then=now;
				if(elapsed>=16666667)
				{
					FlappyFish.flappy.tick();
					FlappyFish.flappy.render();
					elapsed=0;
				}
			}
			Runtime.getRuntime().exit(0);
		});
		thread.start();
		FlappyFish.flappy.commands.start();
	}
}