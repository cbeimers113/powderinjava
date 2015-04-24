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

import powderinjava.Particle;
import powderinjava.Powder;
import powderinjava.State;


public class VENT extends Element{

	public VENT(){
		super(VENT.class,State.SOLID,0xffffff,100,0.2f,false,false,"Vent. Increases surrounding air pressure.");
	}

	public int update(int x,int y,Particle p){
		Powder.pv[p.x][p.y]+=Powder.presConst*p.temp;
		return 0;
	}
}
