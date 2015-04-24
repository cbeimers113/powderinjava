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

import static powderinjava.Powder.pv;
import static powderinjava.Powder.tv;
import static powderinjava.Powder.maxTemp;
import static powderinjava.Powder.getBurnRate;
import java.awt.Color;
import java.util.Random;
import powderinjava.Menu;
import powderinjava.Particle;
import powderinjava.Powder;
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
	public static final Element WTRV=new WTRV();
	public static final Element WOOD=new WOOD();
	public static final Element COAL=new COAL();
	public static final Element SMKE=new SMKE();
	public static final Element C4=new C4();
	public static final Element PLSM=new PLSM();
	public static final Element VENT=new VENT();
	public static final Element VACU=new VACU();

	public String name;
	public String desc;
	public State state;
	public Color colour;
	protected Random rand;

	public int mass;

	public float heat;

	public boolean flammable;
	public boolean stacks;
	private boolean exp;

	public Element(Class<?>sClass,State state,int colour,int mass,float heat,boolean flammable,boolean exp,String desc){
		String name=sClass.getSimpleName();
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
		this.exp=flammable&exp;
		this.desc=desc;
		/*
		 * switch(state){ case SOLID: Main.powder.menu.solids.add(this); break; case LIQUID: Main.powder.menu.liquids.add(this); break; case GAS: Main.powder.menu.gasses.add(this); break; case POWDER: Main.powder.menu.powders.add(this); break; case SPECIAL: Main.powder.menu.hidden.add(this); break; case QUANTUM: Main.powder.menu.quantum.add(this); break; }
		 */
		rand=new Random();
		Menu.liquids.add(this);
	}

	/**
	 * Behaviour for adjacent particles
	 * 
	 * @return: 0 if the particle remains unchanged, but must return 1 if the particle changes. Ie: water+salt=saltwater. DO NOT remove 'return 0' at the end of the update function. Particle p is the particle on screen with this element.
	 */
	public abstract int update(int x,int y,Particle p);

	/**
	 * DO NOT OVERRIDE unless you are an idiot or you have an excellent reason.
	 * 
	 * @throws Exception
	 */
	public void doPhysics(int x,int y,Particle p) throws Exception{
		try{
			tv[x][y]++;
			tv[x][y]--;
		}catch(ArrayIndexOutOfBoundsException e){
			return;
		}
		Particle adj=Particle.particleAt(x,y);
		float delta;
		float transfer=rand.nextFloat();
		boolean transferPart;
		boolean transferAdj;
		if(adj==null){
			transferPart=transfer<=heat;
			transferAdj=transfer<=Element.NONE.heat;
			delta=tv[x][y]-p.temp;
			if(delta<0) delta=-delta;
			if(p.temp<tv[x][y]){
				if(transferPart) p.temp+=delta;
				if(transferAdj) tv[x][y]-=delta;
			}else if(p.temp>tv[x][y]){
				if(transferPart) p.temp-=delta;
				if(transferAdj) tv[x][y]+=delta;
			}
		}else{
			transferPart=transfer<=heat;
			transferAdj=transfer<=adj.type.heat;
			delta=adj.temp-p.temp;
			if(delta<0) delta=-delta;
			if(p.temp<adj.temp){
				if(transferPart) p.temp+=delta;
				if(transferAdj) adj.temp-=delta;
			}else if(p.temp>adj.temp){
				if(transferPart) p.temp-=delta;
				if(transferAdj) adj.temp+=delta;
			}
		}
		if(flammable){
			float heatPres=0.0f;	//Percentage of maxPres to be added to simulation while burning.
			if(!p.burning){
				float ignition;
				if(this==WOOD){
					ignition=150.0f;
					heatPres=0.005f;
				}else if(this==COAL){
					ignition=349.0f;
					heatPres=0.01f;
				}else if(this==OXGN||this==HYGN){
					ignition=500.0f;
					heatPres=0.25f;
				}else if(this==C4){
					ignition=maxTemp;
					heatPres=0.5f;
				}else throw new Exception("You forgot to specify combustion data of some flammable elements!");
				if(p.temp>=ignition&&++p.combust%getBurnRate(this)==0) p.burning=true;
			}else{
				if(++p.combust%getBurnRate(this)==0){
					changeType(p,FIRE);
					if(exp){
						pv[p.x][p.y]+=p.temp/(heatPres*maxTemp);
						for(int i=0;i<rand.nextInt(50*((int)(heatPres*10)+1));i++){
							createPart(p.x+rand.nextInt(50)-25,p.y+rand.nextInt(50)-25,FIRE);
						}
					}
					p.combust=0;
					return;
				}
			}
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
		}else if(p.type==PLSM){
			p.life=rand.nextInt(100)+300;
			p.temp=maxTemp;
		}else if(p.type==WTRV){
			p.temp=rand.nextInt(50)+100;
		}
		else{
			p.temp=22.0f;
		}
	}

	/** Makes a particle with element e at x and y */
	public static void createPart(int x,int y,Element e){
		if(Particle.particleAt(x,y)==null) Powder.spawnParticle(x,y,e);
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
