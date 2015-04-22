/**
 *	@Copyright 2015 firefreak11
 *
 *	This file is part of PowderInJava.
 *
 *	PowderInJava is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.
 *
 *	PowderInJava is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU General Public License for more details.
 *
 *	You should have received a copy of the GNU General Public License
 *	along with PowderInJava.  If not, see <http://www.gnu.org/licenses/>.
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
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

public abstract class Engine extends Canvas implements KeyListener,MouseListener,MouseMotionListener,MouseWheelListener,Runnable{

	public static final long serialVersionUID=1L;

	public JFrame frame;
	public String title;
	public Thread thread;
	public BufferedImage img;
	public String fpsOutput;

	public int width;
	public int height;
	public int[] pixels;

	public boolean resizable;
	public boolean fullscreen;
	public boolean isCloseRequested;

	public Engine(String title,int width,int height,boolean resizable,boolean fullscreen){
		this.title=title;
		this.width=width;
		this.height=height;
		this.resizable=resizable;
		this.fullscreen=fullscreen;
		fpsOutput="";
		initialize();
	}

	private synchronized void initialize(){
		frame=new JFrame(title);
		frame.setPreferredSize(fullscreen?Toolkit.getDefaultToolkit().getScreenSize():new Dimension(width,height));
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
			frame.setIconImage(ImageIO.read(Engine.class.getResource("/assets/icon.png")));
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

	private void refresh(){
		img=new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
		pixels=((DataBufferInt)img.getRaster().getDataBuffer()).getData();
	}

	private synchronized void stop(){
		if(!isCloseRequested) return;
		save();
		System.exit(0);
	}

	public void run(){
		Main.powder.init();
		long timer=System.currentTimeMillis();
		int frames=0;
		int rFrames=frames;
		while(!isCloseRequested){
			coreRender();
			frames++;
			if(System.currentTimeMillis()-timer>1000){
				timer+=1000;
				rFrames=frames;
				frames=0;
			}
			setFpsOutput("FPS:"+rFrames+", Parts: "+Particle.particles.size()+", "+(Main.powder.fancyGraphics?"Fancy Mode":Main.powder.tempGraphics?"Temperature Mode":"Normal Mode"));
		}
		stop();
	}

	private void setFpsOutput(String fpsOutput){
		this.fpsOutput=fpsOutput;
	}

	private void coreRender(){
		BufferStrategy bs=getBufferStrategy();
		if(bs==null){
			createBufferStrategy(3);
			return;
		}
		Graphics g=bs.getDrawGraphics();
		g.setColor(Color.black);
		g.fillRect(0,0,width,height);
		Main.powder.menu.render(g);
		render(g);
		g.drawImage(img,0,0,null);
		g.setFont(new Font("Arial",0,10));
		FontMetrics fm=g.getFontMetrics();
		g.setColor(new Color(0x06739E));
		g.drawString(fpsOutput,5,15);
		String particleData="x:"+Main.powder.mx+", y:"+Main.powder.my;
		Particle p=Particle.particleAt(Main.powder.mx,Main.powder.my);
		if(p!=null) particleData=p.type.name+", Temp: "+rounded(p.temp)+"\u00b0C, life: "+p.life+", "+particleData;
		else particleData+=" Temp: "+rounded(Physics.tv[Main.powder.mx][Main.powder.my])+"\u00b0C"+" Pressure: "+rounded(Physics.pv[Main.powder.mx][Main.powder.my]);
		g.drawString(particleData,Powder.xMarginRight()-fm.stringWidth(particleData),15);
		refresh();
		g.dispose();
		bs.show();
	}

	private float rounded(float f){
		return (float)Math.round(f*100)/100;
	}

	public BufferedImage zoom(BufferedImage originalImage){
		BufferedImage resizedImage=new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
		Graphics2D g=resizedImage.createGraphics();
		g.drawImage(originalImage,0,0,width,height,null);
		g.dispose();
		return resizedImage;
	}

	public abstract void save();

	public abstract void render(Graphics g);
}
