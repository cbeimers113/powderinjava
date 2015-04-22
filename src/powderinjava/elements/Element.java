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

package powderinjava.elements;

import java.awt.Color;
import java.util.Random;
import powderinjava.Main;
import powderinjava.Particle;
import powderinjava.Physics;
import powderinjava.State;

/**
 * An Element Type, defines elemental behaviour
 */

public abstract class Element{

	public static final Element NONE=new NONE();
	public static final Element WATR=new WATR();
	public static final Element FIRE=new FIRE();
	public static final Element STNE=new STNE();
	public static final Element SALT=new SALT();
	public static final Element OXGN=new OXGN();
	public static final Element HYGN=new HYGN();
	public static final Element WOOD=new WOOD();
	public static final Element COAL=new COAL();
	public static final Element SMKE=new SMKE();
	public static final Element C4=new C4();

	public String name;
	public State state;
	public Color colour;
	protected Random rand;

	public int mass;
	
	public float heat;

	public boolean flammable;
	public boolean melts;
	public boolean boils;
	public boolean stacks;
	public boolean glows;

	public Element(String name,State state,int colour,int mass,float heat,boolean flammable,boolean melts,boolean boils){
		try{
			this.name=name.substring(0,4);
		}catch(StringIndexOutOfBoundsException fourChar){
			try{
				this.name=name.substring(0,3);
			}catch(StringIndexOutOfBoundsException threeChar){
				try{
					this.name=name.substring(0,2);
				}catch(StringIndexOutOfBoundsException twoChar){
					try{
						this.name=name.substring(0,1);
					}catch(StringIndexOutOfBoundsException oneChar){
						this.name="NULL";
					}
				}
			}
		}
		this.state=state;
		this.colour=new Color(colour);
		this.mass=state.equals(State.SOLID)?100:state.equals(State.GAS)?0:mass<=0?5:mass>=100?95:mass; // GAS:0, LIQUID:50-80, POWDER: 80-99, SOLID: 0, Less than 50 = VERY light elements
		this.heat=heat;
		this.flammable=flammable;
		this.melts=state.equals(State.SOLID)?melts:false;
		this.boils=state.equals(State.LIQUID)?boils:false;
		/*
		 * switch(state){ case SOLID: Main.powder.menu.solids.add(this); break; case LIQUID: Main.powder.menu.liquids.add(this); break; case GAS: Main.powder.menu.gasses.add(this); break; case POWDER: Main.powder.menu.powders.add(this); break; case SPECIAL: Main.powder.menu.hidden.add(this); break; case QUANTUM: Main.powder.menu.quantum.add(this); break; }
		 */
		rand=new Random();
		Main.powder.menu.liquids.add(this);
	}

	/**
	 * Behaviour for adjacent particles
	 * 
	 * @return: 0 if the particle remains unchanged, but must return 1 if the particle changes. Ie: water+salt=saltwater. DO NOT remove 'return 0' at the end of the update function. Particle p is the particle on screen with this element.
	 */
	public abstract int update(int x,int y,Particle p);

	/** DO NOT OVERRIDE unless you are an idiot or you have an excellent reason. */
	public void doPhysics(int x,int y,Particle p){
		Particle adj=Particle.particleAt(x,y);
		if(adj==null){
			if(p.temp>Physics.tv[x][y]){
				if(p.temp>Physics.minTemp)p.temp-=this.heat*Physics.tv[x][y];
				if(Physics.tv[x][y]<Physics.maxTemp)Physics.tv[x][y]+=NONE.heat*p.temp;
			}else if(p.temp<Physics.tv[x][y]){
				if(p.temp<Physics.maxTemp)p.temp+=this.heat*Physics.tv[x][y];
				if(Physics.tv[x][y]>Physics.minTemp)Physics.tv[x][y]-=NONE.heat*p.temp;
			}
			return;
		}
		if(p.temp>adj.temp){
			p.temp-=this.heat*adj.temp;
			adj.temp+=adj.type.heat*p.temp;
		}else if(p.temp<adj.temp){
			p.temp+=this.heat*adj.temp;
			adj.temp-=adj.type.heat*p.temp;
		}
	}

	/** Again, DO NOT override or mess up. */
	public void onSpawn(Particle p){
		if(p.type==FIRE){
			p.life=rand.nextInt(100)+300;
			p.temp=rand.nextInt(25)+400.0f;
		}else if(p.type==SMKE){
			p.life=rand.nextInt(1000)+500;
			p.temp=rand.nextInt(25)+100.0f;
		}else{
			p.temp=22.0f;
		}
	}

	/** Makes a particle with element e at x and y */
	public static void createPart(int x,int y,Element e){
		if(Particle.particleAt(x,y)==null) Main.powder.spawnParticle(x,y,e);
	}

	/** Changes element of particle at x and y */
	public static void changeType(Particle p,Element e){
		p.type=e;
		p.extraColour=e.colour;
		e.onSpawn(p);
	}

	/** Returns element of a specific particle */
	public static Element elementAt(int x,int y){
		try{
			return Particle.particleAt(x,y).type;
		}catch(NullPointerException e){
			return NONE;
		}
	}
}
