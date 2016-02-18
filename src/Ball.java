import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Ball {
	public int drawX = 0;
	public int drawY = 0;
	public int centerX = 0;
	public int centerY = 0;
	
	public int radius = 0;
	public int speed = 0;
	public String dir = "";
	public Object color = null;
	
	public Random rnd = new Random();
	
	public Ball(int _mouseX, int _mouseY) {
		//Random size
		switch(rnd.nextInt(4)) {
			case 0: radius = 25; break;
			case 1: radius = 30; break;
			case 2: radius = 35; break;
			case 3: radius = 40; break;
		}
		
		//Random speed
		switch(rnd.nextInt(3)) {
			case 0: speed = 3; break;
			case 1: speed = 5; break;
			case 2: speed = 7; break;
		}
		
		//Random dir
		switch(rnd.nextInt(4)) {
			case 0: dir = "up"; break;
			case 1: dir = "down"; break;	
			case 2: dir = "left"; break;
			case 3: dir = "right"; break;
		}
		
		//Random color
		switch(rnd.nextInt(7)) {
			case 0: color = Color.BLACK; break;
			case 1: color = Color.BLUE; break;
			case 2: color = Color.RED; break;
			case 3: color = Color.YELLOW; break;
			case 4: color = Color.ORANGE; break;
			case 5: color = Color.GREEN; break;
			case 6: color = new Color(219, 0, 219); break;
		}
		centerX = _mouseX;
		centerY = _mouseY;
		drawX = _mouseX - radius;
		drawY = _mouseY - radius;
	}
	
	public boolean DidGetClicked(int mouseX, int mouseY) {
		boolean answer = false;
		int Xmax = centerX + radius;
		int Xmin = centerX - radius;
		int Ymax = centerY + radius;
		int Ymin = centerY - radius;
		
		if((mouseX <= Xmax && mouseX >= Xmin) && (mouseY <= Ymax && mouseY >= Ymin)) {
			answer = true;
		}
		
		return answer;
	}
	
	public boolean CheckIfHitWall(int windowWidth, int windowHeight) {
		boolean answer = false;
		int Xmax = centerX + radius;
		int Xmin = centerX - radius;
		int Ymax = centerY + radius;
		int Ymin = centerY - radius;
		
		switch(dir) {
			case "up": if(Ymax <= 0) { centerX = centerX; centerY = windowHeight + radius; drawX = drawX; drawY = windowHeight; answer = !answer;} break;
			case "down": if(Ymin >= windowHeight) { centerX = centerX; centerY = 0 - radius; drawX = drawX; drawY = 0 - radius * 2; answer = !answer;} break;
			case "left": if(Xmax <= 0) { centerX = windowWidth + radius; centerY = centerY; drawX = windowWidth; drawY = drawY; answer = !answer;} break;
			case "right": if(Xmin >= windowWidth) { centerX = 0 - radius; centerY = centerY; drawX = 0 - radius * 2; drawY = drawY; answer = !answer;} break;
		}
		return answer;
	}
	
	public void Move() {
		switch(dir) {
			case "up": centerY -= speed; drawY -= speed; break;
			case "down": centerY += speed; drawY += speed; break;
			case "left": centerX -= speed; drawX -= speed; break;
			case "right": centerX += speed; drawX += speed; break;
		}
	}
	
	public void Draw(Graphics g) { 
		g.setColor((Color) color);
		g.fillOval(drawX, drawY, radius * 2, radius * 2);
	} 
	
	public void Randomize() {
		//Random size
		switch(rnd.nextInt(4)) {
			case 0: radius = 25; break;
			case 1: radius = 30; break;
			case 2: radius = 35; break;
			case 3: radius = 40; break;
		}
				
		//Random speed
		switch(rnd.nextInt(3)) {
			case 0: speed = 3; break;
			case 1: speed = 5; break;
			case 2: speed = 7; break;
		}
				
		//Random dir
		switch(rnd.nextInt(4)) {
			case 0: dir = "up"; break;
			case 1: dir = "down"; break;	
			case 2: dir = "left"; break;
			case 3: dir = "right"; break;
		}
				
		//Random color
		switch(rnd.nextInt(7)) {
			case 0: color = Color.BLACK; break;
			case 1: color = Color.BLUE; break;
			case 2: color = Color.RED; break;
			case 3: color = Color.YELLOW; break;
			case 4: color = Color.ORANGE; break;
			case 5: color = Color.GREEN; break;
			case 6: color = new Color(219, 0, 219); break;
		}
	}
}
