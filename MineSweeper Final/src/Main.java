import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/*
 * Ashton Heit
 * Final project for 1410
 * Minesweeper
 */


/*
 * I met my goals by making the buttons, hiding bombs, losing when you hit a bomb, choosing difficulty, revealing field once you lose, 
 * and displaying the number of bombs nearby the button you click.
 * -----------------------------------------------------------------------------------------------------------------------------------
 * I was unable to add a flag mechanism, play sound in the file, and clear all mines that don't contain a neighbor.
 * -----------------------------------------------------------------------------------------------------------------------------------
 * 
 */

public class Main implements Runnable 
{
	//Attempting image. //Did not finish. Apparently I'm still really bad at images. 
	StationaryObject s1 = null;
	
	//Making new GUI object.
	GUI gui = new GUI();
	
	//Figured out from youtube video, claimed it was a better way to start GUI? 
	public static void main(String[] args) 
	{
			new Thread(new Main()).start();
	}

	@Override
	public void run() 
	{
		while(true)
		{
			//Paints the gui
			gui.repaint();
			//Checks victory status and reset value
			if(gui.resetter == false)
			{
			gui.checkVictoryStatus();
			//System.out.println("Victory: " + gui.victory + ", Defeat: " + gui.defeat);
			}
		}
	}
	
	//Attempting image for smiley which I absolutely suck at and gave up.
	public void face()
	{
		try
		{
			BufferedImage SMILE = ImageIO.read(new File("Smiley.png"));
			s1 = new StationaryObject(0,0,SMILE,70,70);
		}
		catch(IOException e)
		{
			System.out.println("Error");
		}
	}

}
