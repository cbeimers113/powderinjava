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

import powderinjava.State;

public class Air extends Element {

	public Air() {
		super("AIR", State.GAS, new Color(0xaa,0xaa,0xaa,0xff), 0, false, false, false);
	}

	public void update(int x, int y) {
		
	}
}
