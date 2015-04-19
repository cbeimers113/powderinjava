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
import powderinjava.Main;
import powderinjava.Particle;
import powderinjava.State;

/**
 * An Element Type, 
 * defines elemental behaviour
 */

public abstract class Element {

	public static final Element WATR=new WATR();
	public static final Element NONE=new NONE();
	public static final Element STNE=new STNE();
	public static final Element OXGN=new OXGN();
	public static final Element HYGN=new HYGN();
	
	public String name;
	public State state;
	public Color colour;

	public int mass;
	public int temp;

	public boolean flammable;
	public boolean melts;
	public boolean boils;
	public boolean stacks;

	public Element(String name, State state, int colour, int mass,
			boolean flammable, boolean melts, boolean boils) {			
		this.name = name.substring(0, 4);
		this.state = state;
		this.colour = new Color(colour);
		this.mass = state.equals(State.SOLID) ? 100
				: state.equals(State.GAS) ? 0 : mass <= 0 ? 5
						: mass >= 100 ? 95 : mass;					//GAS:0, LIQUID:50-80, POWDER: 80-99, SOLID: 0, Less than 50 = VERY light elements
		this.flammable = flammable;		
		this.melts = state.equals(State.SOLID) ? melts : false;
		this.boils = state.equals(State.LIQUID) ? boils : false;
		/*switch(state){
		case SOLID:
			Main.powder.menu.solids.add(this);
			break;
		case LIQUID:
			Main.powder.menu.liquids.add(this);
			break;
		case GAS:
			Main.powder.menu.gasses.add(this);
			break;
		case POWDER:
			Main.powder.menu.powders.add(this);
			break;
		case SPECIAL:
			Main.powder.menu.hidden.add(this);
			break;
		case QUANTUM:
			Main.powder.menu.quantum.add(this);
			break;
		}*/
		Main.powder.menu.liquids.add(this);
	}
	
	/**Behaviour for adjacent particles
	 * @return: 0 if the particle remains unchanged, but must return greater than 0 if the particle changes. Ie: water+salt=saltwater. DO NOT remove 'return 0' at the end of the update function. Particle p is the particle on screen with this element. 
	 */
	public abstract int update(int x, int y, Particle p);
	
	/**Returns element of a specific particle*/
	public static Element elementAt(int x, int y){
		try{
			return Particle.particleAt(x, y).element;
		}catch(NullPointerException e){
			return NONE;
		}
	}
	
	/**Makes a particle with element e at x and y*/
	public static void createPart(int x, int y, Element e){
		if(Particle.particleAt(x,y)==null)
			Main.powder.spawnParticle(x,y,e);
	}
	
	/**Changes element of particle at x and y*/
	public static void changePart(int x, int y, Element e){
		if(Particle.particleAt(x,y)==null)createPart(x,y,e);
		Particle np=Particle.particleAt(x,y);
		np.element=e;
	}
}
