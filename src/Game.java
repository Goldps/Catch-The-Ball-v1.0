import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

@SuppressWarnings("serial")
public class Game extends JFrame implements ActionListener, MouseListener {
	//Window, JFrame and JPanel stuff
	private int windowWidth = 800;
	private int windowHeight = 700;
	private GamePanel jpGame = new GamePanel();
	private JPanel jpMenu = new JPanel();
	private JPanel jpPause = new JPanel();
	private JPanel jpScores = new JPanel();
	private CardLayout cl = new CardLayout();
	private JPanel cards = new JPanel(cl);
	private JButton jpMenuStartButton = new JButton("Start");
	private JButton jpMenuScoresButton = new JButton("Scores");
	private JButton jpMenuQuitButton = new JButton("Quit");
	private JButton jpScoresExitButton = new JButton("Back to menu");
	private JButton jpPauseMenuButton = new JButton("Back to menu");
	private JButton jpPauseExitButton = new JButton("Back to game");

	//Game stuff
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
		//Default code to set up the jframe for window
		super("Catch The Ball!");
		setSize(windowWidth, windowHeight);
		setFocusable(true);
		requestFocus();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Sets up jpGame
		jpGame.addMouseListener(this);
		jpGame.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "escape");
        jpGame.getActionMap().put("escape", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                paused = !paused;
                cl.show(cards, "Pause");
            }
        });
        
		//Sets up jpPause
		jpPauseExitButton.addActionListener(this);
		jpPauseMenuButton.addActionListener(this);
		jpPause.add(jpPauseMenuButton);
		jpPause.add(jpPauseExitButton);
		
		//Sets up jpScores
		jpScoresExitButton.addActionListener(this);
		
		jpScores.add(jpScoresExitButton);
		
		//Sets up jpMenu
		jpMenuStartButton.addActionListener(this);
		jpMenuScoresButton.addActionListener(this);
		jpMenuQuitButton.addActionListener(this); 
		
		jpMenu.add(jpMenuStartButton);
		jpMenu.add(jpMenuScoresButton);
		jpMenu.add(jpMenuQuitButton);
	
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
			running = !running;
			runGameLoop();
			cl.show(cards, "Game");
		} else if(s == jpMenuScoresButton) {
			cl.show(cards, "Scores");
		} else if(s == jpMenuQuitButton) {
			System.exit(0);
		} else if(s == jpScoresExitButton) {
			cl.show(cards, "Menu");
		} else if(s == jpPauseExitButton){
			paused = !paused;
			cl.show(cards, "Game");
		} else if(s == jpPauseMenuButton) {
			running = !running;
			paused = !paused;
			cl.show(cards, "Menu");
		}
		
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		System.out.println("blah");
		for(int i = 0; i <= arrBall.size() - 1; i++) {
			if(arrBall.get(i).DidGetClicked(e.getX(), e.getY())) {
				arrBall.remove(i);
			} else {
				
			}
		}
	}
	
	public void newRound() {
		if(round == 0) {
			Ball ball = new Ball(rnd.nextInt(windowWidth - 20) + 10, rnd.nextInt(windowHeight - 20) + 10);
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
   
	private void drawGame(float interpolation) {
		jpGame.setInterpolation(interpolation);
      	jpGame.repaint();
	}
   
	private class GamePanel extends JPanel {
	   float interpolation;
      
	   public GamePanel() {

	   }

	   public void setInterpolation(float interp) {
		   interpolation = interp;
	   }
      
	   public void update() {
		   if(arrBall.size() == 0) {
			   round++;
			   newRound();
		   }
		   
		   for(int i = 0; i <= arrBall.size() - 1; i++) {
			   arrBall.get(i).CheckIfHitWall(windowWidth, windowHeight);
			   arrBall.get(i).Move();
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
	         
				//Render. To do so, we need to calculate interpolation for a smooth render.
				float interpolation = Math.min(1.0f, (float) ((now - lastUpdateTime) / TIME_BETWEEN_UPDATES) );
				drawGame(interpolation);
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