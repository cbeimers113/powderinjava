/**
 * @Copyright 2015 firefreak11
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 **/

package powderinjava.elements;

import java.awt.Color;
import powderinjava.Particle;
import powderinjava.State;

public class FIRE extends Element{

	public FIRE(){
		super("FIRE",State.GAS,0xff0000,0,0.05f,false,false);
	}

	public int update(int x,int y,Particle p){
		try{
			p.extraColour=new Color(255,(int)(p.temp/p.tempInit)*255,0,colour.getAlpha());
		}catch(IllegalArgumentException e){
			p.extraColour=colour;
		}
		if(elementAt(x,y)==WATR||elementAt(x,y)==WTRV){
			changeType(p,SMKE);
			return 1;
		}
		if(p.temp<=100.0f){
			changeType(p,SMKE);
			return 1;
		}else if(p.temp>=1000.0f){
			changeType(p,PLSM);
			return 1;
		}
		return 0;
	}
}
