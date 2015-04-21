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

import powderinjava.elements.Element;

public class Physics{

	public static Particle[][] pmap=new Particle[Main.powder.width][Main.powder.height];

	private static int defaultBurnRate;
	private static float defaultHeatCapacity;
	public static float maxTemp;
	static{
		defaultBurnRate=50;
		defaultHeatCapacity=0.5f;
		maxTemp=10000.0f;
	}

	public float[][] pv;
	public int width;
	public int height;

	public Physics(int width,int height){
		this.width=width;
		this.height=height;
		pv=new float[width][height];
	}

	public void update(){
		for(int y=0;y<height;y++){
			for(int x=0;x<width;x++){
				// Average pressure
				float pAv=0.0f;
				for(int i=0;i<8;i++){
					int ax=i==0||i==6||i==7?-1:i==1||i==5?0:1;
					int ay=i==0||i==1||i==2?-1:i==3||i==7?0:1;
					try{
						pAv+=pv[x+ax][y+ay];
					}catch(ArrayIndexOutOfBoundsException e){
						continue;
					}
				}
				pv[x][y]=pAv/8;
			}
		}
	}

	public void addAir(int x,int y,float pressure){
		pv[x][y]+=pressure;
	}

	public static int getBurnRate(Element e){
		if(!e.flammable) return 0;
		return e.equals(Element.WOOD)?defaultBurnRate:e.equals(Element.OXGN)||e.equals(Element.HYGN)?5:e.equals(Element.COAL)?150:e.equals(Element.C4)?1:defaultBurnRate;
	}

	public static float getHeatCapacity(Element e){
		if(e==Element.COAL||e==Element.STNE){
			return 0.17f;
		}else if(e==Element.HYGN||e==Element.OXGN){
			return 3.42f;
		}else if(e==Element.FIRE||e==Element.SMKE){
			return 0.49f;
		}else if(e==Element.SALT){
			return 0.21f;
		}else if(e==Element.WOOD){
			return 0.41f;
		}else if(e==Element.WATR){
			return 1.0f;
		}else if(e==Element.NONE){
			return 0.0f;
		}else{
			return defaultHeatCapacity;
		}
	}
}
