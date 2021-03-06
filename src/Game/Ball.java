package Game;
import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;
import java.util.Random;

@SuppressWarnings("serial")
public class Ball implements Serializable{

	public int drawX = 0;
	public int drawY = 0;
	public int centerX = 0;
	public int centerY = 0;
	public int radius = 0;
	public int speed = 0;
	public String dir = "";
	public Object color = null;
	public int secsDead = 0;
	public boolean isDead = false;
	public long lastChangeTime = System.currentTimeMillis();
	public Random rnd = new Random();
	
	public Ball(int _mouseX, int _mouseY, int diff) {
		//Random size
		switch(rnd.nextInt(4)) {
			case 0: radius = 25; break;
			case 1: radius = 30; break;
			case 2: radius = 35; break;
			case 3: radius = 40; break;
		}
		
		if(diff == 3 || diff == 4) {
			//Random speed
			switch(rnd.nextInt(3)) {
				case 0: speed = 7; break;
				case 1: speed = 10; break;
				case 2: speed = 13; break;
			}
		} else {
			switch(rnd.nextInt(3)) {
				case 0: speed = 5; break;
				case 1: speed = 6; break;
				case 2: speed = 7; break;
			}
		}
		
		//Random dir
		switch(rnd.nextInt(8)) {
			case 0: dir = "up"; break;
			case 1: dir = "down"; break;	
			case 2: dir = "left"; break;
			case 3: dir = "right"; break;
			case 4: dir = "upleft"; break;
			case 5: dir = "upright"; break;
			case 6: dir = "downleft"; break;
			case 7: dir = "downright"; break;
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
			case "up": if(Ymax <= 0) { centerY = windowHeight + radius; drawY = windowHeight; answer = !answer;} break;
			case "down": if(Ymin >= windowHeight) { centerY = 0 - radius; drawY = 0 - radius * 2; answer = !answer;} break;
			case "left": if(Xmax <= 0) { centerX = windowWidth + radius; drawX = windowWidth; answer = !answer;} break;
			case "right": if(Xmin >= windowWidth) { centerX = 0 - radius; drawX = 0 - radius * 2; answer = !answer;} break;
			case "upleft": {
				if(Ymax <= 0) { 
					centerY = windowHeight + radius; drawY = windowHeight; answer = !answer;
				} else if(Xmax <= 0) { 
					centerX = windowWidth + radius; drawX = windowWidth; answer = !answer;
				}
				break;
			}
			case "upright": {
				if(Ymax <= 0) { 
					centerY = windowHeight + radius; drawY = windowHeight; answer = !answer;
				} else if(Xmin >= windowWidth) { 
					centerX = 0 - radius; drawX = 0 - radius * 2; answer = !answer;
				}
				break;
			}
			case "downleft": {
				if(Ymin >= windowHeight) { 
					centerY = 0 - radius; drawY = 0 - radius * 2; answer = !answer;
				} else if(Xmax <= 0) { 
					centerX = windowWidth + radius; drawX = windowWidth; answer = !answer;
				}
				break;
			}
			case "downright": {
				if(Ymin >= windowHeight) { 
					centerY = 0 - radius; drawY = 0 - radius * 2; answer = !answer;
				} else if(Xmin >= windowWidth) { 
					centerX = 0 - radius; drawX = 0 - radius * 2; answer = !answer;
				} 
				break;
			}
		}
		return answer;
	}
	
	public void Move() {
		switch(dir) {
			case "up": centerY -= speed; drawY -= speed; break;
			case "down": centerY += speed; drawY += speed; break;
			case "left": centerX -= speed; drawX -= speed; break;
			case "right": centerX += speed; drawX += speed; break;
			case "upleft": centerY -= speed; drawY -= speed; centerX -= speed; drawX -= speed; break;
			case "upright": centerY -= speed; drawY -= speed; centerX += speed; drawX += speed; break;
			case "downleft": centerY += speed; drawY += speed; centerX -= speed; drawX -= speed; break;
			case "downright": centerY += speed; drawY += speed; centerX += speed; drawX += speed; break;
		}
	}
	
	public void Draw(Graphics g) { 
		Color newColor = (Color) color;
		if(isDead) {
			g.setColor(brighter(newColor, secsDead));
			g.fillOval(drawX, drawY, radius * 3, radius * 2);
		} else {
			g.setColor((Color) color);
			g.fillOval(drawX, drawY, radius * 2, radius * 2);
		}
	} 
	
	public Color brighter(Color color, int sec) {
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        int alpha = color.getAlpha();
        double redFactor = 4;
        double greenFactor = 4;
        double blueFactor = 4;
        
        if(color.getRed() >= 128) {
        	redFactor = 1.2;
        }
        if(color.getGreen() >= 128) {
        	greenFactor = 1.2;
        }
        if(color.getBlue() >= 128) {
        	blueFactor = 1.2;
        }
        
        if(sec != 0) {
        	return new Color (Math.min((int)(r + sec * redFactor), 255),
                    Math.min((int)(g + sec * greenFactor), 255),
                    Math.min((int)(b + sec * blueFactor), 255),
                    alpha);
        } else {
        	return new Color(r, g, b, alpha);
        }
    }
	
	public void DrawDarker(Graphics g) {
		Color newColor = (Color) color;
		if(isDead) {
			g.setColor(newColor.darker());
			g.fillRect(drawX, drawY, radius * 2, radius * 2);
		} else {
			g.setColor(newColor.darker());
			g.fillOval(drawX, drawY, radius * 2, radius * 2);
		}
	}
	
	public void MediumRandomize() {
		if((System.currentTimeMillis() - lastChangeTime) > 200) {
			double d = Math.random();

			if(d > 0.95) {
				switch(rnd.nextInt(8)) {
					case 0: dir = "up"; break;
					case 1: dir = "down"; break;	
					case 2: dir = "left"; break;
					case 3: dir = "right"; break;
					case 4: dir = "upleft"; break;
					case 5: dir = "upright"; break;
					case 6: dir = "downleft"; break;
					case 7: dir = "downright"; break;
				}
				switch(rnd.nextInt(3)) {
					case 0: speed = 5; break;
					case 1: speed = 6; break;
					case 2: speed = 7; break;
				}
			}
			lastChangeTime = System.currentTimeMillis();
		}
	}
	
	public void HardRandomize() {	
		if((System.currentTimeMillis() - lastChangeTime) > 50) {
			double d = Math.random();

			if(d > 0.95) {
				switch(rnd.nextInt(8)) {
					case 0: dir = "up"; break;
					case 1: dir = "down"; break;	
					case 2: dir = "left"; break;
					case 3: dir = "right"; break;
					case 4: dir = "upleft"; break;
					case 5: dir = "upright"; break;
					case 6: dir = "downleft"; break;
					case 7: dir = "downright"; break;
				}
				switch(rnd.nextInt(3)) {
					case 0: speed = 7; break;
					case 1: speed = 10; break;
					case 2: speed = 13; break;
				}
			}
			lastChangeTime = System.currentTimeMillis();
		}
	}
	
	public void ExtremeRandomize(int windowWidth, int windowHeight) {	
		if((System.currentTimeMillis() - lastChangeTime) > 50) {
			double d = Math.random();

			if(d > 0.95) {
				switch(rnd.nextInt(8)) {
					case 0: dir = "up"; break;
					case 1: dir = "down"; break;	
					case 2: dir = "left"; break;
					case 3: dir = "right"; break;
					case 4: dir = "upleft"; break;
					case 5: dir = "upright"; break;
					case 6: dir = "downleft"; break;
					case 7: dir = "downright"; break;
				}
				switch(rnd.nextInt(3)) {
					case 0: speed = 7; break;
					case 1: speed = 10; break;
					case 2: speed = 13; break;
				}
			}
			d = Math.random();
			if(d > 0.995) {
				d = Math.random();
				if(d < .5) {
					d = Math.random();
					if(d < .5){
						if(centerX > 75 || centerX < windowWidth - 75) {
							int num = rnd.nextInt(80) + 30;
							centerX -= num;
							drawX = num - radius;
						}
					} else {
						if(centerY > 75 || centerY < windowHeight - 75) {
							int num = rnd.nextInt(80) + 30;
							centerY -= num;
							drawY = num - radius;
						}
					}
				} else {
					d = Math.random();
					if(d < .5){
						if(centerX > 75 || centerX < windowWidth - 75) {
							int num = rnd.nextInt(80) + 30;
							centerX += num;
							drawX = num - radius;
						}
					} else {
						if(centerY > 75 || centerY < windowHeight - 75) {
							int num = rnd.nextInt(80) + 30;
							centerY += num;
							drawY = num - radius;
						}
					}
				}
			}
			lastChangeTime = System.currentTimeMillis();
		}
	}
}
