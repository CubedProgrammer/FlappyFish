package flappy.main;
import java.awt.event.*;
public class KeyHandler extends KeyAdapter
{
	public boolean up;
	public boolean enter;
	public KeyHandler(){}
	public void keyPressed(KeyEvent e)
	{
		if(e.getKeyCode()==32)
		{
			this.up=true;
		}
		else if(e.getKeyCode()==10)
		{
			this.enter=true;
		}
	}
	public void keyReleased(KeyEvent e)
	{
		if(e.getKeyCode()==32)
		{
			this.up=false;
		}
		else if(e.getKeyCode()==10)
		{
			this.enter=false;
		}
	}
}