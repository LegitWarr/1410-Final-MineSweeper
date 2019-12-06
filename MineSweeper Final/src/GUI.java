//imports
import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class GUI extends JFrame
{
	//variable declaration
	//Checking to reset game or not
	public boolean resetter = false;
	//Time aspect //timer
	Date startDate = new Date();
	public int timeX = 1100;
	public int timeY = 5;
	public int sec = 0;
	
	//The board variables
	int spacing =5;
	int difficulty = 15;
	int neighs = 0;
	
	//Clicked X and Y position
	public int mx = -100;
	public int my = -100;

	//Window size XY
	public int winX = 1286;
	public int winY = 829;
	
	//Smile
	public int smileX = winX/2;
	public int smileY = 0;
	public int smileCenterX = smileX + 35;
	public int smileCenterY = smileY + 35;
	
	//attempting image // DNF
	public BufferedImage SMILE;
	
	//Change difficulty
	public int minusX = 250;
	public int minusY = 10;
	public int plusX = 330;
	public int plusY = 10;
	public int difficultyX = 90;
	public int difficultyY = 10;
	
	//Printing victory message
	public int victoryX = 730;
	public int victoryY = -50;
	String victoryMessage = "Nothing";
	
	//Victory and smiley status.
	public boolean happyFace = true;
	public boolean victory = false;
	public boolean defeat = false;
	
	//Mines, neighbor mines, revealed boxes, flagged boxes
	int[][] mines = new int[16][9];
	int[][] Neighbors = new int [16][9];
	boolean[][] revealed = new boolean[16][9];
	
	Random rand = new Random();
	
	
	Scanner sc = new Scanner(System.in);
	
	public GUI()
	{
		//Settings the frame
		this.setTitle("Minesweeper");
		this.setSize(winX, winY);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setResizable(false);
		
		
		//Setting each box to have a mine or not.
		for(int x=0; x<16; x++)
		{
			for(int y=0; y < 9; y++)
			{
				if(rand.nextInt(100) <= difficulty)
				{
					mines[x][y] = 1;
				}
				else
				{
					mines[x][y] = 0;
				}
				revealed[x][y] = false;
			}
		}
		//Checking each box and setting neighbors
		for(int x=0; x<16; x++)
		{
			for(int y=0; y<9; y++)
			{
				neighs = 0;
				for(int z=0; z<16; z++)
				{
					for(int i=0; i<9; i++)
					{
						if(!(z == x && i == y))
						{
							//Adding neighbors 
							if(isN(x,y,z,i) == true)
							{
								neighs++;
							}
						}
					}
				}
				Neighbors[x][y] = neighs;
			}
		}
		
		Board board = new Board();
		this.setContentPane(board);
		
		Move move = new Move();
		this.addMouseMotionListener(move);
		
		Click click = new Click();
		this.addMouseListener(click);
	}
	
	//Board
	public class Board extends JPanel
	{
		public void paintComponent(Graphics g)
		{
			g.setColor(Color.BLACK);
			//Filling entire panel black
			g.fillRect(0, 0, 1280, 800);
			//Setting up the grid. X and Y per square. each square is 80 pixels.
			for(int x=0; x<16; x++)
			{
				for(int y=0; y < 9; y++)
				{
					//sets box color
					g.setColor(Color.DARK_GRAY);
					
					//Always print mines
					if(mines[x][y] == 1)
					{
						//g.setColor(Color.yellow);
					}
					
					//Color once clicked
					if(revealed[x][y] ==true)
					{
						g.setColor(Color.LIGHT_GRAY);
						if(mines[x][y]==1)
						{
							g.setColor(Color.RED);
						}
					}
					
					//Sets color of the squares/boxes
					if( mx >= spacing+x*80 && mx < x*80+80-2*spacing && my >= spacing+y*80+80+26 && my < spacing+y*80+26+80+80-2*spacing)
					{
						g.setColor(Color.LIGHT_GRAY);
					}
					g.fillRect(spacing+x*80, spacing+y*80+80, 80-2*spacing, 80-2*spacing);
					
					if(revealed[x][y] ==true)
					{
						//Color of mine when clicked. //Colors depending on amount of neighboring mines.
						g.setColor(Color.BLACK);
						if(mines[x][y]==0 && Neighbors[x][y] != 0)
						{
							if(Neighbors[x][y] == 1)
							{ 
								g.setColor(Color.BLUE);
							}
							else if(Neighbors[x][y] == 2)
							{
								g.setColor(Color.GREEN);
							}
							else if(Neighbors[x][y] == 3)
							{
								g.setColor(Color.RED);
							}
							else if(Neighbors[x][y] == 4)
							{
								g.setColor(new Color(70,3,166));
							}
							else if(Neighbors[x][y] == 5)
							{
								g.setColor(new Color(70,44,80));
							}
							else if(Neighbors[x][y] == 6)
							{
								g.setColor(new Color(72,209,204));
							}
							else if(Neighbors[x][y] == 7)
							{
								g.setColor(Color.BLACK);
							}
							else if(Neighbors[x][y] == 8)
							{
								g.setColor(Color.DARK_GRAY);
							}
							g.setFont(new Font("tahoma",Font.BOLD, 40));
							g.drawString(Integer.toString(Neighbors[x][y]), x*80+27, y*80+80+55);
						}
						else if (mines[x][y]==1)
						{
							//Prints bomb when clicked // add picture instead of g.fill. Which I did not finish.
							g.fillRect(x*80+10+20, y*80+80+20, 20, 40);
							g.fillRect(x*80+20, y*80+80+10+20, 40, 20);
							g.fillRect(x*80+5+20, y*80+80+5+20, 30, 30);
							g.fillRect(x*80+38, y*80+80+15, 4, 50);
							g.fillRect(x*80+15, y*80+80+38, 50, 4);
						}
					}
				}
			}
			//Changing the difficulty buttons
			//Minus button
			g.setColor(Color.DARK_GRAY);
			g.fillRect(difficultyX, difficultyY, 300, 60);
			g.setColor(Color.black);
			g.fillRect(minusX+3, minusY+3, 54, 54);
			
			//Printing the difficulty
			g.fillRect(plusX+3, plusY+3, 54, 54);
			g.setColor(Color.RED);
			g.setFont(new Font("Tahoma", Font.PLAIN, 26));
			g.drawString("Difficulty : " + difficulty, difficultyX+3, difficultyY + 40);
			
			//Plus button
			g.setColor(Color.WHITE);
			g.fillRect(minusX+20, minusY+27, 20, 6);
			g.fillRect(plusX+20, plusY+27, 20, 6);
			g.fillRect(plusX+27, plusY+20, 6, 20);
			
			//Smile painting
				g.setColor(Color.YELLOW);
				g.fillOval(smileX, smileY, 70, 70);
				g.setColor(Color.black);
				g.fillOval(smileX+15, smileY+15, 10, 10);
				g.fillOval(smileX+45, smileY+15, 10, 10);
				
				//Changes to red when loses
				if(defeat == true)
				{
					g.setColor(Color.RED);
					g.fillOval(smileX, smileY, 70, 70);
					g.setColor(Color.black);
					g.fillOval(smileX+15, smileY+15, 10, 10);
					g.fillOval(smileX+45, smileY+15, 10, 10);
				}
				//Changes to green when win
				if(victory == true)
				{
					g.setColor(Color.GREEN);
					g.fillOval(smileX, smileY, 70, 70);
					g.setColor(Color.black);
					g.fillOval(smileX+15, smileY+15, 10, 10);
					g.fillOval(smileX+45, smileY+15, 10, 10);
				}
			
			if(happyFace == true)
			{
				//s1.paint(g);
				g.fillRect(smileX+20, smileY+50, 30, 5);
				g.fillRect(smileX+17, smileY+45, 5, 5);
				g.fillRect(smileX+48, smileY+45, 5, 5);
				
			}
			else
			{
				g.fillRect(smileX+20, smileY+50, 30, 5);
				g.fillRect(smileX+17, smileY+55, 5, 5);
				g.fillRect(smileX+48, smileY+55, 5, 5);
				
			}
			
			//time counter painting
			g.setColor(Color.WHITE);
			g.fillRect(timeX, timeY, 170, 75);
			//Stops time if win or lose. Or keeps time going as long as you don't win or lose.
			if(defeat == false && victory == false)
			{
				sec = (int)((new Date().getTime()-startDate.getTime()) / 1000);
			}
			g.setColor(Color.BLACK);
			//Changes to green if win
			if(victory == true)
			{
				g.setColor(Color.GREEN);
			}
			//Changes to red if loss.
			if(defeat == true)
			{
				g.setColor(Color.RED);
			}
			
			//Sets the font of the timer.
			g.setFont(new Font("Tahoma", Font.PLAIN,80));
			g.drawString(Integer.toString(sec), timeX, timeY+65);
			
			//Painting victory message.
			//Moving message and changing to green if you win.
			if(victory == true)
			{
				g.setColor(Color.GREEN);
				victoryMessage = "YOU'RE A WINNER";
				victoryY = 65;
			}
			//Moving message and changing to red if you lose.
			else if(defeat == true)
			{
				g.setColor(Color.RED);
				victoryMessage = "YOU'RE A LOSER";
				victoryY = 65;
				for(int x = 0; x < 16; x++)
				{
					for(int y = 0; y<9; y++)
					{
						revealed[x][y] = true;
					}
				}
			}
			
			//Drawing image once you win or lose.
			if(victory == true || defeat == true)
			{
				g.setFont(new Font("Tahoma", Font.PLAIN,45));
				g.drawString(victoryMessage, victoryX, victoryY);
			}
		}
	}
	
	public class Move implements MouseMotionListener
	{
		//Useless stuffs
		@Override
		public void mouseDragged(MouseEvent e)
		{  }	@Override
		public void mouseMoved(MouseEvent e) 
		{  }
	}
	//Mouse listener
	public class Click implements MouseListener
	{
		@Override
		public void mouseClicked(MouseEvent e) 
		{
			//Position that was clicked
			mx = e.getX();
			my = e.getY();
			
			if (inBoxX() != -1 && inBoxY() != -1) 
			{
				revealed[inBoxX()][inBoxY()] = true;
			}
			/*//This was printing which box was chosen and how many neighboring mines.
			if(inBoxX() != -1 && inBoxY() != -1)
			{
				System.out.println("The mouse is in the [" +  inBoxX() + "," + inBoxY() + "], Number of mine nieghs: " + Neighbors[inBoxX()][inBoxY()]);
			}
			else
			{
				System.out.println("The point is not inside a box");
			}
			*/
			if(inSmiley() == true)
			{
				restartGame();
				
			}
			//If the pointer is in the minus button for difficulty
			if (mx >= minusX+20 && mx < minusX+60 && my >= minusY+20 && my < minusY+60)
			{
				difficulty--;
				if(difficulty < 5)
				{
					difficulty = 5;
				}
				
			}
				//if the pointer is in the plus button for difficulty
				else if(mx >= plusX+20 && mx < plusX+60 && my >= plusY+20 && my < plusY+60)
				{
					difficulty++;
					if(difficulty > 60)
					{
						difficulty = 60;
					}
				}
			}
		//Things that must be added for the mouse listener.
		@Override
		public void mousePressed(MouseEvent e) 
		{	}	@Override
		public void mouseReleased(MouseEvent e)
		{	}   @Override
		public void mouseEntered(MouseEvent e)
		{	}	@Override
		public void mouseExited(MouseEvent e) 
		{	}
	}
	
	//Checking if you won
	public void checkVictoryStatus()
	{
		for(int x=0; x<16; x++)
		{
			for(int y=0; y < 9; y++)
			{
				if(revealed[x][y] == true && mines[x][y] == 1)
				{
					defeat = true;
					happyFace = false;
				}
			}
		}
		//if you reveal the amount of boxes minus the amount of mines, you win.
		if(totalBoxesRevealed() == 144 - totalMines())
		{
			victory = true;
		}
	}
	
	//Getting the total amount of mines.
	public int totalMines()
	{
		int total = 0;
		for(int x=0; x<16; x++)
		{
			for(int y=0; y < 9; y++)
			{
				if(mines[x][y] == 1)
				{
					total++;
				}
			}
		}
		return total;
	}
	
	//Getting the total amount of boxes that you have clicked on and revealed.
	public int totalBoxesRevealed()
	{
		int total = 0;
		for(int x=0; x<16; x++)
		{
			for(int y=0; y < 9; y++)
			{
				if(revealed[x][y] == true)
				{
					total++;
				}
			}
		}
		return total;
	}
	
	//If you click the smiley face at the top of the screen, it resets the game with all new mines.
	public void restartGame()
	{
		resetter = true;
		
		startDate = new Date();
		
		victoryY = -50;
		victoryMessage = "Nothing yet!";
		
		happyFace = true;
		victory = false;
		defeat = false;
		for(int x=0; x<16; x++)
		{
			for(int y=0; y < 9; y++)
			{
				if(rand.nextInt(100) +1 < difficulty)
				{
					mines[x][y] = 1;
				}
				else
				{
					mines[x][y] = 0;
				}
				revealed[x][y] = false;
			}
		}
		
		for(int x=0; x<16; x++)
		{
			for(int y=0; y<9; y++)
			{
				neighs = 0;
				for(int z=0; z<16; z++)
				{
					for(int i=0; i<9; i++)
					{
						if(!(z == x && i == y))
						{
							if(isN(x,y,z,i) == true)
							{
								neighs++;
							}
						}
					}
				}
				Neighbors[x][y] = neighs;
			}
		}
		resetter = false;
	}
	
	//Checking to see if you click in smiley.
	public boolean inSmiley()
	{
		int dif = (int) Math.sqrt(Math.abs(mx-smileCenterX)*Math.abs(mx-smileCenterX)+Math.abs(my-smileCenterY)*Math.abs(my-smileCenterY));
		if(dif <= 35)
		{
			return true;
		}
		return false;
		
		//Ethan helped me attempt to fix my code, didn't work.
		/*
		if(smileCenterX - 35 == mx - (mx%70) && smileCenterY - 35 == my - (my%70))
		{
			return true;
		}
		return false;
		*/
	}
	
	//Gets the X values for the boxes
	public int inBoxX()
	{
		for(int x=0; x<16; x++)
		{
			for(int y=0; y < 9; y++)
			{
				if( mx >= spacing+x*80 && mx < x*80+80-2*spacing && my >= spacing+y*80+80+26 && my < spacing+y*80+26+80+80-2*spacing)
				{
					return x;
				}
			}
		}
		return -1;
	}
	
	//Gets the Y values for the boxes
	public int inBoxY()
	{
		for(int x=0; x<16; x++)
		{
			for(int y=0; y < 9; y++)
			{
				if( mx >= spacing+x*80 && mx < x*80+80-2*spacing && my >= spacing+y*80+80+26 && my < spacing+y*80+26+80+80-2*spacing)
				{
					return y;
				}
			}
		}
		return -1;
	}
	
	//Is Neighbor mine. Checking to see if there are mines nearby
	//mX/mY is Mine X and Y
	//cX and cY is clicked X and Y
	public boolean isN(int mX, int mY, int cX, int cY)
	{
		if(mX - cX < 2 && mX - cX > -2 && mY - cY < 2 && mY - cY > -2 && mines[cX][cY] == 1)
		{
			return true;
		}
		return false;
	}
}
