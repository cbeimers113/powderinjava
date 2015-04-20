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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import powderinjava.elements.Element;

public class Powder extends Engine{

	/**
	 * Powder In Java, a Java recreation of The Powder Toy
	 * 
	 * @link http://powdertoy.co.uk
	 * @author firefreak11
	 * @date April 12, 2015
	 */

	// Main Simulation Mechanics

	private static final long serialVersionUID=1L;

	public static int xMarginLeft=10;

	public static int xMarginRight(){
		try{
			return Main.powder.getWidth()-xMarginLeft;
		}catch(NullPointerException e){
			return 0;
		}
	}

	public static int yMarginTop=20;

	public static int yMarginBottom(){
		try{
			return Main.powder.height-(yMarginTop<<2)-1;
		}catch(NullPointerException e){
			return 0;
		}
	}

	public Menu menu;
	public Physics physics;
	public Element spawnType;

	public int mx;
	public int my;
	private int cursorRadius;

	public boolean fancyGraphics;
	private boolean spawning;
	private boolean erasing;
	private boolean paused;

	public Powder(){
		super("Powder In Java",650,475,false,false);
	}

	public void render(Graphics g){
		try{
			for(Iterator<Particle> iterator=Particle.particles.iterator();iterator.hasNext();){
				Particle p=iterator.next();
				if(p.removeQueue){
					iterator.remove();
					continue;
				}
				if(Main.powder.fancyGraphics){
					Color glow=new Color(p.extraColour.getRed(),p.extraColour.getGreen(),p.extraColour.getBlue(),p.extraColour.getAlpha());
					if(p.type.state.equals(State.LIQUID)){
						for(int i=0;i<4;i++){
							int ax=i==0||i==2?0:i==1?1:-1;
							int ay=i==0?-1:i==1||i==3?0:1;
							img.setRGB(p.x+ax,p.y+ay,glow.getRGB());
						}
						img.setRGB(p.x,p.y,p.type.colour.getRGB());
					}else if(p.type.state.equals(State.GAS)||p.type.state.equals(State.PLASMA)){
						Color pixel=new Color(glow.getRed(),glow.getGreen(),glow.getBlue(),glow.getAlpha()/3);
						g.setColor(pixel);
						g.fillOval(p.x-3/2,p.y-3/2,3,3);
					}else img.setRGB(p.x,p.y,p.type.colour.getRGB());
				}else img.setRGB(p.x,p.y,p.type.colour.getRGB());
				if(!paused) p.update();
			}
		}catch(ConcurrentModificationException e){
		}
		if(spawning&&!erasing) fillCursor(cursorRadius);
		if(!spawning&&erasing) eraseCursor(cursorRadius);
		if(!paused) physics.update();
		drawCursor(mx,my,cursorRadius);
	}

	public void spawnParticle(int x,int y,Element spawnType){
		if(Particle.particleAt(x,y)!=null||x<=xMarginLeft||x>=xMarginRight()||y<=yMarginTop||y>=yMarginBottom()) return;
		new Particle(x,y,spawnType);
	}

	public void save(){

	}

	public void keyPressed(KeyEvent e){
		switch(e.getKeyCode()){
			case KeyEvent.VK_SPACE:
				paused=!paused;
				break;
			case KeyEvent.VK_1:
				fancyGraphics=!fancyGraphics;
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
		if(e.getButton()==MouseEvent.BUTTON1){
			spawning=true;
			erasing=false;
		}else if(e.getButton()==MouseEvent.BUTTON3){
			erasing=true;
			spawning=false;
		}
		menu.grabMouse(mx,my,true);
	}

	public void mouseReleased(MouseEvent e){
		spawning=false;
		erasing=false;
		menu.grabMouse(mx,my,false);
	}

	public void mouseDragged(MouseEvent e){
		mx=e.getX();
		my=e.getY();
		menu.grabMouse(mx,my,true);
	}

	public void mouseMoved(MouseEvent e){
		mx=e.getX();
		my=e.getY();
		menu.grabMouse(mx,my,false);
	}

	public void mouseWheelMoved(MouseWheelEvent e){
		int n=e.getWheelRotation();
		if(n<0&&cursorRadius<50) cursorRadius++;
		if(n>0&&cursorRadius>0) cursorRadius--;
	}

	public void fillCursor(int radius){
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

	public void eraseCursor(int radius){
		if(radius<=0) try{
			Particle.particleAt(mx,my).remove();
			return;
		}catch(NullPointerException e){
			return;
		}
		int x=radius;
		int y=0;
		int radiusError=1-x;
		int mx=this.mx;
		int my=this.my;
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

	public void drawCursor(int x0,int y0,int radius){
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

	public void drawPixel(int x,int y,int colour){
		try{
			img.setRGB(x,y,colour);
		}catch(ArrayIndexOutOfBoundsException e){
			return;
		}
	}
}
