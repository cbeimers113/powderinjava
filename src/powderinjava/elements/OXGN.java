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
import powderinjava.State;

public class OXGN extends Element{

	public OXGN(){
		super(OXGN.class,State.GAS,0x7FE5D7,0,3.42f,true,false,"Oxygen. Creates water with hydrogen.");
	}

	public int update(int x,int y,Particle p){
		if(elementAt(x,y)==HYGN&&(p.temp+Particle.particleAt(x,y).temp)/2>=400.0f){
			changeType(p,WATR);
			changeType(Particle.particleAt(x,y),NONE);
			return 1;
		}
		return 0;
	}
}
