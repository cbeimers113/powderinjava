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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import powderinjava.elements.Element;

/**
 * A Specific Particle On Screen
 */

public class Particle{

	public static List<Particle> particles=new ArrayList<Particle>();

	public Element element;
	private Random rand;

	public int x;
	public int y;
	public int dx;
	public int dy;
	public int life;
	
	public float temp;

	public boolean removeQueue;

	public Particle(int x,int y,Element element){
		this.x=x;
		this.y=y;
		this.element=element;
		rand=new Random();
		switch(element.state){
			case SOLID:
				dx=dy=0;
				break;
			case LIQUID:
				dx=rand.nextInt(3)-1;
				dy=1;
				break;
			case GAS:
				dx=rand.nextInt(3)-1;
				dy=rand.nextInt(3)-1;
				break;
			case POWDER:
				dx=0;
				dy=1;
				break;
			case SPECIAL:
				dx=dy=0;
				break;
			case QUANTUM:
				dx=rand.nextInt(2)==0?1:-1;
				dy=rand.nextInt(2)==0?1:-1;
				break;
			case PLASMA:
				dx=rand.nextInt(3)-1;
				dy=rand.nextInt(2)-1;
				break;
		}
		element.onSpawn(this);
		particles.add(this);
	}

	public synchronized void update(){
		if(!element.state.equals(State.SOLID))
			switch(element.state){
				case GAS:
					displace(dx*(rand.nextInt(3)-1),dy*(rand.nextInt(3)-1));
					dx=rand.nextInt(3)-1;
					dy=rand.nextInt(3)-1;
					break;
				case LIQUID:
					displace(dx*(rand.nextInt(5)-2),dy*rand.nextInt(2));
					dx=rand.nextInt(3)-1;
					break;
				case POWDER:
					displace(dx*(rand.nextInt(3)-1),dy);
					break;
				case QUANTUM:
					displace(dx,dy);
					break;
				case PLASMA:
					displace(dx*(rand.nextInt(3)-1),dy*(rand.nextInt(2)+1));
					dx=rand.nextInt(3)-1;
					dy=rand.nextInt(2)-2;
				default:
					break;
			}
		for(int i=0;i<8;i++){
			int ax=i<3?x-1:i<4?x:x+1;
			int ay=i==0||i==3||i==5?y+1:i==1||i==6?y:y-1;
			if(element.update(ax,ay,this)!=0) break; // Checks and updates for elemental behaviour surrounding this particle
		}
		for(Particle p:particles){
			if(p==null) continue;
			if(p==this) continue;
			if(p.element.equals(Element.NONE)) continue;
			if(x==p.x&&y==p.y) y--;
		}
		if(x<Powder.xMarginLeft||y<Powder.yMarginTop||x>Powder.xMarginRight()||y>Powder.yMarginBottom()||element.equals(Element.NONE)) remove();
	}

	public void displace(int xDest,int yDest){
		if(x+xDest==Powder.xMarginLeft||y+yDest==Powder.yMarginTop||x+xDest==Powder.xMarginRight()||y+yDest==Powder.yMarginBottom()){// Collide with the borders
			return;
		}
		Particle check=particleAt(x+xDest,y+yDest);
		if(check!=null){

			if(check.element.mass>=element.mass){
				if(check.element.equals(element)){
					if(element.stacks) return;
					int nx=rand.nextInt(2)==0?1:-1;
					if(element.mass>=5&&particleAt(x+nx,y+1)==null){
						displace(nx,1);
						return;
					}
				}
				return;
			}
			if(check!=this)check.displace(rand.nextInt(2)==0?1:-1,-1);
		}
		int px=x;
		int py=y;
		x+=xDest*(Main.powder.physics.vx[px][py]+1);
		y+=yDest*(Main.powder.physics.vy[px][py]+1);
		
	}

	public static Particle particleAt(int x,int y){
		for(Iterator<Particle> iterator=particles.iterator();iterator.hasNext();){
			Particle particle=iterator.next();
			if(particle.x==x&&particle.y==y) return particle;
		}
		return null;
	}

	public void remove(){
		removeQueue=true;
	}
}
