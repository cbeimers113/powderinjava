/**
 * @Copyright 2015 firefreak11
 *
 *            This file is part of PowderInJava.
 *
 *            PowderInJava is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *            PowderInJava is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 *            You should have received a copy of the GNU General Public License along with PowderInJava. If not, see <http://www.gnu.org/licenses/>.
 **/

package powderinjava;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import powderinjava.elements.Element;

public class Powder extends Canvas implements KeyListener,MouseListener,MouseMotionListener,MouseWheelListener,Runnable{

	/**
	 * Powder In Java, a Java recreation of The Powder Toy
	 * 
	 * @link http://powdertoy.co.uk
	 * @author firefreak11
	 * @date April 12, 2015
	 */

	// Main Simulation Mechanics

	private static final long serialVersionUID=1L;

	public static final int WIDTH=650;
	public static final int HEIGHT=475;

	public static int xMarginLeft=10;
	public static int xMarginRight=WIDTH-2*xMarginLeft;
	public static int yMarginTop=20;
	public static int yMarginBottom=HEIGHT-4*yMarginTop;
	public static int mx;
	public static int my;
	private static int cursorRadius;

	public static boolean fancyGraphics;
	public static boolean tempGraphics;
	public static boolean presGraphics;
	public static boolean paused;
	private static boolean spawning;
	private static boolean erasing;

	private static String[] welcomeMessage=new String[]{
			"Welcome to Powder In Java!",
			"This is a project by firefreak11, author of the Unrealistic Science Mod Series!",
			"Click to spawn elements into the simulation and watch them interact!",
			"Press \"G\" to cycle through graphics modes!"
	};

	public static JFrame frame;
	public static String title;
	public static Thread thread;
	public static BufferedImage img;
	public static String fpsOutput;
	public static Menu menu;
	public static Element spawnType;
	public static Color text;

	protected static int welcomeTimer;
	protected static int welcomeWait;

	public static boolean resizable;
	public static boolean fullscreen;
	public static boolean tickMaps;
	public static boolean isCloseRequested;

	public static String version="1.0.1";		//<major> . <minor> . <maintenance>

	public static Particle[][] pmap=new Particle[WIDTH][HEIGHT];
	public static Wall[][]bmap=new Wall[WIDTH/2][HEIGHT/2];
	public static float[][] pv=new float[WIDTH][HEIGHT];
	public static float[][] tv=new float[WIDTH][HEIGHT];
	public static float[][] vx=new float[WIDTH][HEIGHT];
	public static float[][] vy=new float[WIDTH][HEIGHT];

	public static float maxTemp=10_000.0f;
	public static float minTemp=-maxTemp;
	public static float maxPres=256.0f;
	public static float minPres=-maxPres;
	public static float presConst=0.04f;
	public static int defaultBurnRate=50;
	public static int parts;

	public static int[] heatMapPos=new int[]{
			0x0000ff,
			0x0020ff,
			0x0040ff,
			0x0060ff,
			0x0080ff,
			0x00c0ff,
			0x00ffff,
			0x00ffc0,
			0x00ff80,
			0x00ff60,
			0x00ff40,
			0x00ff20,
			0x00ff00,
			0x20ff00,
			0x40ff00,
			0x60ff00,
			0x80ff00,
			0xc0ff00,
			0xffff00,
			0xffc000,
			0xff8000,
			0xff6000,
			0xff4000,
			0xff2000,
			0xff0000,
			0xff0020,
			0xff0040,
			0xff0060,
			0xff0080,
			0xff00c0,
			0xff00ff,
			0xc000ff,
			0x8000ff
	};

	public static int[] heatMapNeg=new int[]{
			0x0000ff,
			0x0000e0,
			0x0000c0,
			0x0000a0,
			0x000080,
			0x000070,
			0x000060,
			0x000050,
			0x000040,
			0x000030,
			0x000020,
			0x000010,
			0x000000
	};

	public static int[] pressureMapPos=new int[]{
			0x000000,
			0x100000,
			0x200000,
			0x300000,
			0x400000,
			0x500000,
			0x600000,
			0x800000,
			0xa00000,
			0xc00000,
			0xe00000,
			0xffffff

	};

	public static int[] pressureMapNeg=new int[]{
			0x000000,
			0x000010,
			0x000020,
			0x000030,
			0x000040,
			0x000050,
			0x000060,
			0x000070,
			0x000080,
			0x0000a0,
			0x0000c0,
			0x0000e0,
			0x0000ff,
	};

	public static BufferedImage pause;
	public static BufferedImage play;
	public static BufferedImage page;
	public static BufferedImage pauseH;
	public static BufferedImage playH;
	public static BufferedImage pageH;

	static{
		try{
			pause=ImageIO.read(Powder.class.getResource("/assets/pause.png"));
			play=ImageIO.read(Powder.class.getResource("/assets/play.png"));
			page=ImageIO.read(Powder.class.getResource("/assets/new.png"));
			pauseH=ImageIO.read(Powder.class.getResource("/assets/pauseHover.png"));
			playH=ImageIO.read(Powder.class.getResource("/assets/playHover.png"));
			pageH=ImageIO.read(Powder.class.getResource("/assets/newHover.png"));
		}catch(IOException e){
			e.printStackTrace();
			System.exit(1);
		}
	}

	public Powder(){
		title="Powder In Java";
		resizable=false;
		fullscreen=false;
		fpsOutput="";
		welcomeWait=300;
		text=new Color(0x06739E);
		initFrame();
	}

	private synchronized void initFrame(){
		frame=new JFrame(title);
		frame.setPreferredSize(fullscreen?Toolkit.getDefaultToolkit().getScreenSize():new Dimension(WIDTH,HEIGHT));
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
		frame.add(this);
		frame.addKeyListener(this);
		frame.addMouseListener(this);
		frame.addMouseMotionListener(this);
		frame.addMouseWheelListener(this);
		frame.addWindowListener(new WindowAdapter(){

			public void windowClosing(WindowEvent e){
				isCloseRequested=true;
			}
		});
		frame.addComponentListener(new ComponentAdapter(){

			public void componentResized(ComponentEvent evt){

			}
		});
		try{
			frame.setIconImage(ImageIO.read(Powder.class.getResource("/assets/icon.png")));
		}catch(Exception e){
			System.out.println("No Custom Icon Found For /assets/icon.png");
		}
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(resizable);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		refresh();
		thread=new Thread(this,title);
		thread.start();
	}

	public void initPowder(){
		menu=new Menu();
		spawnType=Element.WATR;
		fancyGraphics=true;
		for(int y=0;y<HEIGHT;y++){
			for(int x=0;x<WIDTH;x++){
				pmap[x][y]=null;
				pv[x][y]=0.0f;
				vx[x][y]=0.0f;
				vy[x][y]=0.0f;
				tv[x][y]=22.0f;
			}
		}
		for(int y=0;y<HEIGHT/2;y++){
			for(int x=0;x<WIDTH/2;x++){
				bmap[x][y]=null;
			}
		}
	}

	private static void refresh(){
		img=new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_ARGB);
	}

	private synchronized void stop(){
		if(!isCloseRequested) return;
		System.exit(0);
	}

	public void run(){
		initPowder();
		MapManager.start();
		long timer=System.currentTimeMillis();
		int frames=0;
		int rFrames=frames;
		while(!isCloseRequested){
			update();
			frames++;
			if(System.currentTimeMillis()-timer>1000){
				timer+=1000;
				rFrames=frames;
				frames=0;
			}
			fpsOutput=("P-I-J "+version+"|FPS:"+rFrames+", Parts: "+parts+", "+(fancyGraphics?"Fancy Display":tempGraphics?"Temperature Display":presGraphics?"Pressure Display":"Normal Display"));
		}
		stop();
	}

	private void update(){
		BufferStrategy bs=getBufferStrategy();
		if(bs==null){
			createBufferStrategy(3);
			return;
		}
		Graphics g=bs.getDrawGraphics();
		g.setColor(Color.black);
		g.fillRect(0,0,WIDTH,HEIGHT);
		menu.render(g);
		if(spawning&&!erasing) fillCursor(cursorRadius);
		if(!spawning&&erasing) eraseCursor(cursorRadius);
		List<Particle>counted=new ArrayList<Particle>();
		for(int y=0;y<HEIGHT;y++){
			for(int x=0;x<WIDTH;x++){
				try{
					tv[x][y]++;
					tv[x][y]--;
				}catch(ArrayIndexOutOfBoundsException e){
					return;
				}
				tickMaps=false;
				Particle p=pmap[x][y];
				if(x>Powder.xMarginLeft&&x<Powder.xMarginRight&&y>Powder.yMarginTop&&y<Powder.yMarginBottom){
					if(Powder.tempGraphics){
						boolean ambient=p==null;
						img.setRGB(x,y,getTempColour(ambient?tv[x][y]:p.temp).getRGB());
					}
					if(Powder.presGraphics) img.setRGB(x,y,getPresColour(pv[x][y]).getRGB());
				}
				// Update Particles
				if(p==null) continue;
				if(p.removeQueue){
					pmap[x][y]=null;
					continue;
				}else{
					if(!paused) p.update();
					if(!counted.contains(p)){
						counted.add(p);
					}
				}
				tickMaps=true;
				if(Powder.fancyGraphics){
					Color glow=new Color(p.extraColour.getRed(),p.extraColour.getGreen(),p.extraColour.getBlue(),p.extraColour.getAlpha());
					if(p.type.state.equals(State.LIQUID)){
						for(int i=0;i<4;i++){
							int ax=i==0||i==2?0:i==1?1:-1;
							int ay=i==0?-1:i==1||i==3?0:1;
							img.setRGB(p.x+ax,p.y+ay,glow.getRGB());
						}
						img.setRGB(p.x,p.y,p.type.colour.getRGB());
					}else if(p.type.state.equals(State.GAS)){
						Color pixel=new Color(glow.getRed(),glow.getGreen(),glow.getBlue(),glow.getAlpha()/3);
						g.setColor(pixel);
						g.fillOval(p.x-3/2,p.y-3/2,3,3);
					}else img.setRGB(p.x,p.y,p.type.colour.getRGB());
				}
				if(!Powder.fancyGraphics&&!Powder.tempGraphics) try{
					img.setRGB(p.x,p.y,p.type.colour.getRGB());
				}catch(ArrayIndexOutOfBoundsException e){
				}
			}
		}
		parts=counted.size();
		//draw walls
//		if(bmap[x][y])
		drawCursor(mx,my,cursorRadius);
		g.drawImage(img,0,0,null);
		FontMetrics fm;
		g.setFont(new Font("Arial",0,14));
		fm=g.getFontMetrics();
		g.setColor(new Color(0xffcd34));
		if(++welcomeTimer<welcomeWait){
			for(int i=0;i<welcomeMessage.length;i++)
				g.drawString(welcomeMessage[i],WIDTH/2-fm.stringWidth(welcomeMessage[i])/2,40+20*i);
		}
		g.setFont(new Font("Arial",0,10));
		fm=g.getFontMetrics();
		g.setColor(text);
		g.drawString(fpsOutput,xMarginLeft,15);
		String cursorData="x:"+mx+", y:"+my;
		String particleData;
		Particle p=Particle.particleAt(mx,my);
		if(p!=null)
			particleData=p.type.name+", Temp: "+rounded(p.temp)+"\u00b0C, Pressure: "+rounded(pv[mx][my])+" N/m\u00b2";
		else particleData=" Temp: "+rounded(tv[mx][my])+"\u00b0C"+" Pressure: "+rounded(pv[mx][my])+" N/m\u00b2";
		g.drawString(particleData,xMarginRight-5-fm.stringWidth(particleData),15);
		g.drawString(cursorData,xMarginRight-5-fm.stringWidth(cursorData),30);
		refresh();
		g.dispose();
		bs.show();
	}

	public static void spawnParticle(int x,int y,Element spawnType){
		if(Particle.particleAt(x,y)!=null||x<=xMarginLeft||x>=xMarginRight||y<=yMarginTop||y>=yMarginBottom) return;
		new Particle(x,y,spawnType);
	}

	private static float rounded(float f){
		return (float)Math.round(f*100)/100;
	}

	public static BufferedImage zoom(BufferedImage originalImage){
		BufferedImage resizedImage=new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_ARGB);
		Graphics2D g=resizedImage.createGraphics();
		g.drawImage(originalImage,0,0,WIDTH,HEIGHT,null);
		g.dispose();
		return resizedImage;
	}

	private static Color getTempColour(float t){
		try{
			if(t>0)
				return new Color(heatMapPos[(int)(t*(heatMapPos.length-1)/maxTemp)]);
			else return new Color(heatMapNeg[(int)(-t*(heatMapNeg.length-1)/maxTemp)]);
		}catch(ArrayIndexOutOfBoundsException e){
			if(t>0)
				return new Color(heatMapPos[heatMapPos.length-1]);
			else return new Color(heatMapNeg[heatMapNeg.length-1]);
		}
	}

	private Color getPresColour(float p){
		try{
			if(p>0)
				return new Color(pressureMapPos[(int)(p*(pressureMapPos.length-1)/maxPres)]);
			else return new Color(pressureMapNeg[(int)(-p*(pressureMapNeg.length-1)/maxPres)]);
		}catch(ArrayIndexOutOfBoundsException e){
			if(p>0)
				return new Color(pressureMapPos[pressureMapPos.length-1]);
			else return new Color(pressureMapNeg[pressureMapNeg.length-1]);
		}
	}

	public static int getBurnRate(Element e) throws Exception{
		if(!e.flammable) return 0;
		if(e==Element.OXGN||e==Element.HYGN){
			return 10;
		}else if(e==Element.WOOD){
			return 50;
		}else if(e==Element.COAL){
			return 150;
		}else if(e==Element.C4){
			return 1;
		}else{
			throw new Exception("You forgot to specify the burn rate of some flammable elements!");
		}
	}

	public void keyPressed(KeyEvent e){
		welcomeTimer=welcomeWait;
		switch(e.getKeyCode()){
			case KeyEvent.VK_SPACE:
				paused=!paused;
				break;
			case KeyEvent.VK_G:
				if(!fancyGraphics&&!tempGraphics&&!presGraphics)
					fancyGraphics=true;
				else if(fancyGraphics&&!tempGraphics&&!presGraphics){
					tempGraphics=true;
					fancyGraphics=false;
				}else if(!fancyGraphics&&tempGraphics&&!presGraphics){
					presGraphics=true;
					tempGraphics=false;
				}else if(!fancyGraphics&&!tempGraphics&&presGraphics){
					presGraphics=false;
				}
				break;
			case 91:
				if(cursorRadius>0) cursorRadius--;
				break;
			case 93:
				cursorRadius++;
				break;
		}
	}

	public void keyReleased(KeyEvent e){

	}

	public void keyTyped(KeyEvent e){

	}

	public void mouseClicked(MouseEvent e){

	}

	public void mouseEntered(MouseEvent e){

	}

	public void mouseExited(MouseEvent e){

	}

	public void mousePressed(MouseEvent e){
		welcomeTimer=welcomeWait;
		if(e.getButton()==MouseEvent.BUTTON1){
			spawning=true;
			erasing=false;
		}else if(e.getButton()==MouseEvent.BUTTON3){
			erasing=true;
			spawning=false;
		}
		if(mx>=0&&mx<=WIDTH&&my>=0&&my<=HEIGHT) menu.grabMouse(mx,my,true);
	}

	public void mouseReleased(MouseEvent e){
		spawning=false;
		erasing=false;
		if(mx>=0&&mx<=WIDTH&&my>=0&&my<=HEIGHT) menu.grabMouse(mx,my,false);
	}

	public void mouseDragged(MouseEvent e){
		if(mx>=0&&mx<=WIDTH&&my>=0&&my<=HEIGHT){
			mx=e.getX();
			my=e.getY();
			menu.grabMouse(mx,my,true);
		}
	}

	public void mouseMoved(MouseEvent e){
		if(mx>=0&&mx<=WIDTH&&my>=0&&my<=HEIGHT){
			mx=e.getX();
			my=e.getY();
			try{
				menu.grabMouse(mx,my,false);
			}catch(NullPointerException e2){
				return;
			}
		}
	}

	public void mouseWheelMoved(MouseWheelEvent e){
		int n=e.getWheelRotation();
		if(n<0&&cursorRadius<50) cursorRadius++;
		if(n>0&&cursorRadius>0) cursorRadius--;
	}

	public static void fillCursor(int radius){
		if(radius==0){
			spawnParticle(mx,my,spawnType);
			return;
		}
		int x=radius;
		int y=0;
		int radiusError=1-x;

		while(x>=y){
			spawnParticle(x+mx,y+my,spawnType);
			spawnParticle(y+mx,x+my,spawnType);
			spawnParticle(-x+mx,y+my,spawnType);
			spawnParticle(-y+mx,x+my,spawnType);
			spawnParticle(-x+mx,-y+my,spawnType);
			spawnParticle(-y+mx,-x+my,spawnType);
			spawnParticle(x+mx,-y+my,spawnType);
			spawnParticle(y+mx,-x+my,spawnType);
			y++;
			if(radiusError<0){
				radiusError+=2*y+1;
			}else{
				x--;
				radiusError+=2*(y-x)+1;
			}
		}
		fillCursor(--radius);
	}

	public static void eraseCursor(int radius){
		if(radius<=0) try{
			Particle.particleAt(mx,my).remove();
			return;
		}catch(NullPointerException e){
			return;
		}
		int x=radius;
		int y=0;
		int radiusError=1-x;
		while(x>=y){
			try{
				Particle.particleAt(x+mx,y+my).remove();
			}catch(NullPointerException e){

			}
			try{
				Particle.particleAt(y+mx,x+my).remove();
			}catch(NullPointerException e){

			}
			try{
				Particle.particleAt(-x+mx,y+my).remove();
			}catch(NullPointerException e){

			}
			try{
				Particle.particleAt(-y+mx,x+my).remove();
			}catch(NullPointerException e){

			}
			try{
				Particle.particleAt(-x+mx,-y+my).remove();
			}catch(NullPointerException e){

			}
			try{
				Particle.particleAt(-y+mx,-x+my).remove();
			}catch(NullPointerException e){

			}
			try{
				Particle.particleAt(x+mx,-y+my).remove();
			}catch(NullPointerException e){

			}
			try{
				Particle.particleAt(y+mx,-x+my).remove();
			}catch(NullPointerException e){

			}
			y++;
			if(radiusError<0){
				radiusError+=2*y+1;
			}else{
				x--;
				radiusError+=2*(y-x)+1;
			}
		}
		eraseCursor(--radius);
	}

	public static void drawCursor(int x0,int y0,int radius){
		int x=radius;
		int y=0;
		int radiusError=1-x;

		while(x>=y){
			drawPixel(x+x0,y+y0,0xffaaaaaa);
			drawPixel(y+x0,x+y0,0xffaaaaaa);
			drawPixel(-x+x0,y+y0,0xffaaaaaa);
			drawPixel(-y+x0,x+y0,0xffaaaaaa);
			drawPixel(-x+x0,-y+y0,0xffaaaaaa);
			drawPixel(-y+x0,-x+y0,0xffaaaaaa);
			drawPixel(x+x0,-y+y0,0xffaaaaaa);
			drawPixel(y+x0,-x+y0,0xffaaaaaa);
			y++;
			if(radiusError<0){
				radiusError+=2*y+1;
			}else{
				x--;
				radiusError+=2*(y-x)+1;
			}
		}
	}

	public static void drawPixel(int x,int y,int colour){
		try{
			img.setRGB(x,y,colour);
		}catch(ArrayIndexOutOfBoundsException e){
			return;
		}
	}

	public static void newSim(){
		for(int y=0;y<HEIGHT;y++){
			for(int x=0;x<WIDTH;x++){
				pv[x][y]=0.0f;
				tv[x][y]=22.0f;
				vx[x][y]=0.0f;
				vy[x][y]=0.0f;
				pmap[x][y]=null;
			}
		}
	}
}
