/**
 *	@Copyright 2015 Chris Beimers/firefreak11
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
import java.util.ArrayList;
import java.util.List;

import powderinjava.State;

/**
 * An Element Type
 */

public abstract class Element {

	public static List<Element> elements = new ArrayList<Element>();
	public static final Element AIR=new Air();
	
	public String name;
	public State state;
	public Color colour;

	public int mass;

	public boolean flammable;
	public boolean melts;
	public boolean boils;

	public Element(String name, State state, Color colour, int mass,
			boolean flammable, boolean melts, boolean boils) {
		this.name = name.substring(0, 3);
		this.state = state;
		this.colour = colour;
		this.mass = state.equals(State.SOLID) ? 100
				: state.equals(State.GAS) ? 0 : mass <= 0 ? 5
						: mass >= 100 ? 95 : mass;
		this.flammable = flammable;
		this.melts = state.equals(State.SOLID) ? melts : false;
		this.boils = state.equals(State.LIQUID) ? boils : false;
		elements.add(this);
	}
	
	/**Behaviour for adjacent particles*/
	public abstract void update(int x, int y);
}
