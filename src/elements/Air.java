/**
 *	@Copyright 2015 Chris Beimers/firefreak11
 *
 *	This file is part of BeanPowder.
 *
 *	BeanPowder is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.
 *
 *	BeanPowder is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU General Public License for more details.
 *
 *	You should have received a copy of the GNU General Public License
 *	along with BeanPowder.  If not, see <http://www.gnu.org/licenses/>.
 **/

package net.codarch.bpowder.elements;

import java.awt.Color;

import net.codarch.bpowder.State;

public class Air extends Element {

	public Air() {
		super("AIR", State.GAS, new Color(0xff,0,0), 0, false, false, false);
	}
}
