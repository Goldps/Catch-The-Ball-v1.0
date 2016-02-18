import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

/*
 * to do:
 * come up with a way to select next color for when making a ball instead of picking a random color
 * come up with game modes
 * come up with game difficulty
 */
@SuppressWarnings("serial")
public class Game extends JFrame implements ActionListener, MouseListener {
	//Window, JFrame and JPanel stuff
	private int windowWidth = 700;
	private int windowHeight = 600;
	private CardLayout cl = new CardLayout();
	private JPanel cards = new JPanel(cl);
	private JButton jpMenuStartButton = new JButton("Start");
	private JButton jpMenuScoresButton = new JButton("Scores");
	private JButton jpMenuQuitButton = new JButton("Quit");
	private JButton jpScoresExitButton = new JButton("Back to Menu");
	private JButton jpPauseMenuButton = new JButton("Save / Back to Menu");
	private JButton jpPauseGameButton = new JButton("Back to Game");

	private GamePanel jpGame = new GamePanel();
	private MenuPanel jpMenu = new MenuPanel();
	private ScoresPanel jpScores = new ScoresPanel();	
	private PausePanel jpPause = new PausePanel();
	private Image imgMenuScreen = null;
	
	//Save stuff
	private Object currentSave = null;
	private int numberOfSaves = 0;
	
	//Game stuff
	private int gameMode = 0;
	private boolean running = false;
	private boolean paused = false;
	private int fps = 60;
	private int frameCount = 0;
	private int score = 0;
	private int round = -1;
	private int intSec = 0;
	private int intMin = 0;
	private int intHr = 0;
	private String strSec = "";
	private String strMin = "";
	private String strHr = "";
	private String strTime = "00:00:00";
	private ArrayList<Ball> arrBall = new ArrayList<>();
	private Random rnd = new Random();

	public Game() {
		//Code to set up the jframe for window
		super("Catch The Ball!");
		setSize(windowWidth, windowHeight);
		setResizable(false);
		setFocusable(true);
		requestFocus();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Setup jpGame
		jpGame.addMouseListener(this);
		jpGame.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "escape");
        jpGame.getActionMap().put("escape", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cl.show(cards, "Pause");
                running = false;
            }
        });
        
		//Setup jpPause
        jpPauseGameButton.addActionListener(this);
		jpPauseMenuButton.addActionListener(this);

		jpPauseMenuButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "escape");
		jpPauseMenuButton.getActionMap().put("escape", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	cl.show(cards, "Game");
    			running = true;
    			runGameLoop();
            }
        });
		
		//Setup jpScores
		jpScoresExitButton.addActionListener(this);
		
		//Setup jpMenu
		jpMenuStartButton.addActionListener(this);
		jpMenuScoresButton.addActionListener(this);
		jpMenuQuitButton.addActionListener(this); 

		try{
			imgMenuScreen = ImageIO.read(new File("MenuScreen.png"));
		} catch(Exception e) {
			
		}
	
		//Adds all the panels to cardlayout
		cards.add(jpMenu, "Menu");
		cards.add(jpGame, "Game");
		cards.add(jpScores, "Scores");
		cards.add(jpPause, "Pause");
		
		//Adds cardlayout to jframe
		add(cards);
		
		//Shows menu to player at start
		cl.show(cards, "Menu");
		
	}

	public static void main(String[] args) {
		Game game = new Game();
		game.setVisible(true);

	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object s = e.getSource();

		if(s == jpMenuStartButton) {
			cl.show(cards, "Game");
			running = true;
			runGameLoop();
		} else if(s == jpMenuScoresButton) {
			cl.show(cards, "Scores");
		} else if(s == jpMenuQuitButton) {
			System.exit(0);
		} else if(s == jpScoresExitButton) {
			cl.show(cards, "Menu");
		} else if(s == jpPauseGameButton){
			cl.show(cards, "Game");
			running = true;
			runGameLoop();
		} else if(s == jpPauseMenuButton) {
			cl.show(cards, "Menu");
		}
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		boolean DidHitABall = false;
		for(int i = 0; i <= arrBall.size() - 1; i++) {
			if(arrBall.get(i).DidGetClicked(e.getX(), e.getY())) {
				if(!arrBall.get(i).isDead) {
					DidHitABall = true;
					score += 25;
					arrBall.get(i).drawX -= arrBall.get(i).radius / 2;
					arrBall.get(i).isDead = true;
				}
			}
		}
		if(DidHitABall == false) {
			score -= 50;
		}
	}
	
	public void newRound() {
		if(round == 0) {
			Ball ball = new Ball(windowWidth / 2, windowHeight / 2);
			arrBall.add(ball);
		} else {
			for(int i = 0; i <= round * 3 - 1; i++) {
				Ball ball = new Ball(rnd.nextInt(windowWidth - 20) + 10, rnd.nextInt(windowHeight - 20) + 10);
				arrBall.add(ball);
			}
		}
	}

	private void updateGame() {
		jpGame.update();
	}
   
	private void drawGame() {
      	jpGame.repaint();
	}
	
	private class PausePanel extends JPanel {
		public PausePanel() { 
			
            setLayout(new GridBagLayout());

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;

            gbc.fill = GridBagConstraints.VERTICAL;
            gbc.gridy++;
    		add(jpPauseGameButton, gbc);
            gbc.gridy++;
    		add(jpPauseMenuButton, gbc);
    		
        }
		
		public void paintComponent(Graphics g) {
			
			g.setColor(Color.WHITE.darker());
			g.fillRect(0, 0, windowWidth, windowHeight);
			
			//Draws each ball on screen with ball color
			for(int i = 0; i <= arrBall.size() - 1; i++) {
				arrBall.get(i).DrawDarker(g);
			} 
			
			g.setColor(Color.BLACK);
			g.drawString("FPS: " + fps, 5, 10);
			g.drawString("Score: " + score, 60, 10);
			g.drawString("Round: " + round, 140, 10);
			g.drawString("Time: " + strTime, 220, 10);
		}
	}
	
	private class ScoresPanel extends JPanel {
		public ScoresPanel() { 
			
            setLayout(new GridBagLayout());

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;

            gbc.fill = GridBagConstraints.VERTICAL;
            gbc.gridy++;
            add(jpScoresExitButton, gbc);

        }
		
		public void paintComponent(Graphics g) {
			
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, windowWidth, windowHeight);
			 
		}
	}
	
	private class MenuPanel extends JPanel {
		public MenuPanel() { 
			
            setLayout(new GridBagLayout());

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;

            gbc.fill = GridBagConstraints.VERTICAL;
            gbc.gridy++;
            add(jpMenuStartButton, gbc);
            gbc.gridy++;
            add(jpMenuScoresButton, gbc);
            gbc.gridy++;
            add(jpMenuQuitButton, gbc);
        }
		 public void paintComponent(Graphics g) {
			   g.drawImage(imgMenuScreen, 0, 0, windowWidth, windowHeight, null);
		 }
	}
	
	private class GamePanel extends JPanel {
	   public void update() {
		   if(arrBall.size() == 0) {
			   round++;
			   newRound();
		   }
		   
		   for(int i = 0; i <= arrBall.size() - 1; i++) {
			   if(arrBall.get(i).isDead) {
				   if(arrBall.get(i).secsDead == 60) {
					   arrBall.remove(i);
				   } else {
					   arrBall.get(i).secsDead++;
				   }
			   }
		   }
		   
		   if(round != 0) {
			   
			   for(int i = 0; i <= arrBall.size() - 1; i++) {
				   arrBall.get(i).CheckIfHitWall(windowWidth, windowHeight);
				   if(!arrBall.get(i).isDead){
					   double d = Math.random();
					   if (d > 0.9769){
						   arrBall.get(i).Randomize();
					   }
					   arrBall.get(i).Move();
				   }
			   }	
		   }
	   }
      
	   public void paintComponent(Graphics g) {
		   g.setColor(Color.WHITE);
		   g.fillRect(0, 0, windowWidth, windowHeight);
		   
		   //Draws each ball on screen with ball color
		   for(int i = 0; i <= arrBall.size() - 1; i++) {
			   arrBall.get(i).Draw(g);
		   }
		   
		   g.setColor(Color.BLACK);
		   g.drawString("FPS: " + fps, 5, 10);
		   g.drawString("Score: " + score, 60, 10);
		   g.drawString("Round: " + round, 140, 10);
		   g.drawString("Time: " + strTime, 220, 10);
         
		   frameCount++;
	   }
	}
	
	//Starts a new thread and runs the game loop in it.
	public void runGameLoop() {
		Thread loop = new Thread() {
			public void run() {
				gameLoop();
			}
		};
		loop.start();
	}
	   
	//Only run this in another Thread!
	private void gameLoop() {
		//This value would probably be stored elsewhere.
		final double GAME_HERTZ = 30.0;
		//Calculate how many ns each frame should take for our target game hertz.
		final double TIME_BETWEEN_UPDATES = 1000000000 / GAME_HERTZ;
		//At the very most we will update the game this many times before a new render.
		//If you're worried about visual hitches more than perfect timing, set this to 1.
		final int MAX_UPDATES_BEFORE_RENDER = 5;
	   	//We will need the last update time.
		double lastUpdateTime = System.nanoTime();
		//Store the last time we rendered.
		double lastRenderTime = System.nanoTime();
	      
		//If we are able to get as high as this FPS, don't render again.
		final double TARGET_FPS = 60;
		final double TARGET_TIME_BETWEEN_RENDERS = 1000000000 / TARGET_FPS;
	      
		//Simple way of finding FPS.
		int lastSecondTime = (int) (lastUpdateTime / 1000000000);
	      
		while (running) {
			double now = System.nanoTime();
			int updateCount = 0;
	         
			if (!paused) {
				//Do as many game updates as we need to, potentially playing catchup.
				while( now - lastUpdateTime > TIME_BETWEEN_UPDATES && updateCount < MAX_UPDATES_BEFORE_RENDER ) {
					updateGame();
					lastUpdateTime += TIME_BETWEEN_UPDATES;
					updateCount++;
				}
	   
				//If for some reason an update takes forever, we don't want to do an insane number of catchups.
				//If you were doing some sort of game that needed to keep EXACT time, you would get rid of this.
				if ( now - lastUpdateTime > TIME_BETWEEN_UPDATES) {
					lastUpdateTime = now - TIME_BETWEEN_UPDATES;
				}
	         
				//Render.
				drawGame();
	           	lastRenderTime = now;
	         
	           	//Update the frames we got.
	           	int thisSecond = (int) (lastUpdateTime / 1000000000);
	           	if (thisSecond > lastSecondTime) {
	           		intSec++;
	           		if(intSec == 60) {intSec = 0; intMin++;}
	           		if(intMin == 60) {intMin = 0; intHr++;}
	           		if(intSec <= 9) {strSec = "0" + intSec;} else strSec = "" + intSec;
	           		if(intMin <= 9) {strMin = "0" + intMin;} else strMin = "" + intMin;
	           		if(intHr <= 9) {strHr = "0" + intHr;} else strHr = "" + intHr;
	           		strTime = strHr + ":" + strMin + ":" + strSec;
	           		fps = frameCount;
	            	frameCount = 0;
	            	lastSecondTime = thisSecond;
	            }
	         
	            //Yield until it has been at least the target time between renders. This saves the CPU from hogging.
	            while ( now - lastRenderTime < TARGET_TIME_BETWEEN_RENDERS && now - lastUpdateTime < TIME_BETWEEN_UPDATES) {
	            	Thread.yield();
	            
	            	//This stops the app from consuming all your CPU. It makes this slightly less accurate, but is worth it.
	            	//You can remove this line and it will still work (better), your CPU just climbs on certain OSes.
	            	//FYI on some OS's this can cause pretty bad stuttering. Scroll down and have a look at different peoples' solutions to this.
	            	try {Thread.sleep(1);} catch(Exception e) {} 
	            
	            	now = System.nanoTime();
	            }
			}
		}
	}   
	
	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}
}