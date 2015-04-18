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
	public int vx;
	public int vy;
	public int t;

	public boolean removeQueue;

	public Particle(int x,int y,Element element){
		this.x=x;
		this.y=y;
		this.element=element;
		rand=new Random();
		switch(element.state){
			case SOLID:
				vx=vy=0;
				break;
			case LIQUID:
				vx=rand.nextInt(3)-1;
				vy=1;
				break;
			case GAS:
				vx=rand.nextInt(3)-1;
				vy=rand.nextInt(3)-1;
				break;
			case POWDER:
				vx=0;
				vy=1;
				break;
			case SPECIAL:
				vx=vy=0;
				break;
			case QUANTUM:
				vx=rand.nextInt(2)==0?1:-1;
				vy=rand.nextInt(2)==0?1:-1;
				break;
		}
		particles.add(this);
	}

	public synchronized void update(){
		if(removeQueue)return;
		if(!element.state.equals(State.SOLID))if(++t%1/*(25-element.mass/4)*/==0){
			switch(element.state){
				case GAS:
					displace(vx*(rand.nextInt(3)-1),vy*(rand.nextInt(3)-1));
					vx=rand.nextInt(3)-1;
					vy=rand.nextInt(3)-1;
					break;
				case LIQUID:
					displace(vx*(rand.nextInt(5)-2),vy*rand.nextInt(2));
					vx=rand.nextInt(3)-1;
					break;
				case POWDER:
					displace(vx*(rand.nextInt(3)-1),vy);
					break;
				case QUANTUM:
					displace(vx,vy);
					break;
				default:
					break; // Don't need to update movement for solids.
			}
			t=0;
		}
		for(int i=0;i<8;i++){
			int ax=i<3?x-1:i<4?x:x+1;
			int ay=i==0||i==3||i==5?y+1:i==1||i==6?y:y-1;
			element.update(ax,ay,this); // Checks and updates for elemental behaviour surrounding this particle
		}
		for(Particle p:particles){
			if(p==null)continue;
			if(p==this)continue;
			if(p.element.equals(Element.NONE))continue;
			if(x==p.x&&y==p.y)y--;
		}
		if(x<0||y<0||x>Main.powder.width||y>Main.powder.height) remove();
	}

	public void displace(int xDest,int yDest){
		if(x+xDest==Powder.xMarginLeft||y+yDest==Powder.yMarginTop||x+xDest==Powder.xMarginRight()||y+yDest==Powder.yMarginBottom()){// Collide with the borders
			return;
		}
		if(x+xDest<Powder.xMarginLeft||y+yDest<Powder.yMarginTop||x+xDest>Powder.xMarginRight()||y+yDest>Powder.yMarginBottom()){// If it is past the borders, remove it.
			 remove();
			return;
		}
		Particle check=particleAt(x+xDest,y+yDest);
		if(check!=null){
			
			if(check.element.mass>=element.mass){
				if(check.element.equals(element)){
					if(element.stacks) return;
					int nx=rand.nextInt(2)==0?1:-1;
					if(!element.state.equals(State.GAS)&&particleAt(x+nx,y+1)==null){
						displace(nx,1);
						return;
					}
				}
				return;
			}
			 check.displace(rand.nextInt(2)==0?1:-1,-1);
		}
		int px=x;
		int py=y;
		x+=xDest+Main.powder.v.vx[px][py];
		y+=yDest+Main.powder.v.vy[px][py];
		if(rand.nextInt(100)<=25)Main.powder.v.vx[px][py]+=xDest;
		if(rand.nextInt(100)<=25)Main.powder.v.vy[px][py]+=yDest;
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
