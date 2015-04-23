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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import powderinjava.elements.Element;

/**
 * A Specific Particle On Screen
 */

public class Particle{

	public static List<Particle> particles=new ArrayList<Particle>();
	
	public Element type;
	public Element ctype;
	public Color extraColour;
	private Random rand;

	private int t;
	public int x;
	public int y;
	public int life;
	public int combust;

	public float vx;
	public float vy;
	public float tempInit;
	public float temp;

	public boolean removeQueue;
	public boolean burning;

	public Particle(int x,int y,Element type,Element...ctype){
		this.x=x;
		this.y=y;
		this.type=type;
		try{
			this.ctype=ctype[0];
		}catch(ArrayIndexOutOfBoundsException e){
			this.ctype=Element.NONE;
		}
		rand=new Random();
		switch(type.state){
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
			case PLASMA:
				vx=rand.nextInt(3)-1;
				vy=rand.nextInt(2)-1;
				break;
		}
		extraColour=type.colour;
		type.onSpawn(this);
		tempInit=temp;
		Physics.pmap[x][y]=this;
		particles.add(this);
	}

	public synchronized void update(){
		if(!type.state.equals(State.SOLID)) switch(type.state){
			case GAS:
				displace((int)(vx*(rand.nextInt(3)-1)),(int)(vy*(rand.nextInt(3)-1)));
				vx=rand.nextInt(3)-1;
				vy=rand.nextInt(3)-1;
				break;
			case LIQUID:
				displace((int)(vx*(rand.nextInt(5)-2)),(int)(vy*rand.nextInt(2)));
				vx=rand.nextInt(3)-1;
				break;
			case POWDER:
				displace((int)(vx*(rand.nextInt(3)-1)),(int)vy);
				break;
			case QUANTUM:
				displace((int)vx,(int)vy);
				break;
			case PLASMA:
				displace((int)(vx*(rand.nextInt(3)-1)),(int)(vy*(rand.nextInt(2)+1)));
				vx=rand.nextInt(3)-1;
				vy=rand.nextInt(2)-2;
				break;
			default:
				break;
		}
		if(temp>Physics.maxTemp)temp=Physics.maxTemp;
		if(temp<Physics.minTemp)temp=Physics.minTemp;
		for(int i=0;i<8;i++){
			int ax=i<3?x-1:i<4?x:x+1;
			int ay=i==0||i==3||i==5?y+1:i==1||i==6?y:y-1;
			try{
				type.doPhysics(ax,ay,this);
			}catch(Exception e){
				e.printStackTrace();
				System.exit(1);
			}
			if(type.update(ax,ay,this)==1) break;
		}
		if(x<Powder.xMarginLeft||y<Powder.yMarginTop||x>Powder.xMarginRight()||y>Powder.yMarginBottom()||type.equals(Element.NONE))
			remove();
		else{
			if(Physics.pmap[x][y]==null) Physics.pmap[x][y]=this;
		}
	}

	public void displace(int xDest,int yDest){
		if(x+xDest==Powder.xMarginLeft||y+yDest==Powder.yMarginTop||x+xDest==Powder.xMarginRight()||y+yDest==Powder.yMarginBottom()){// Collide with the borders
			return;
		}
		Particle check=particleAt(x+xDest,y+yDest);
		if(check!=null){

			if(check.type.mass>=type.mass){
				if(check.type.equals(type)){
					if(type.stacks) return;
					int nx=rand.nextInt(2)==0?1:-1;
					if(type.mass>=5&&particleAt(x+nx,y+1)==null){
						displace(nx,1);
						return;
					}
				}
				return;
			}
			if(check!=this) 
				check.displace(rand.nextInt(2)==0?1:-1,-1);
		}
		if(++t%((type.mass>>5)+1)==0){
			int px=x;
			int py=y;
			x+=xDest;
			y+=yDest;
			Physics.pmap[px][py]=null;
			t=0;
		}
	}

	public static Particle particleAt(int x,int y){
		return Physics.pmap[x][y];
	}

	public void remove(){
		removeQueue=true;
	}
}
