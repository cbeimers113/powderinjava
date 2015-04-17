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

import java.util.Random;


public class VelocityMap{
	
	private Random rand;
	
	public double[][]vx;
	public double[][]vy;
	public int width;
	public int height;
	
	public VelocityMap(int width, int height){
		this.width=width;
		this.height=height;
		vx=new double[width][height];
		vy=new double[width][height];
		rand=new Random();
		for(int y=0;y<height;y++)
			for(int x=0;x<width;x++)
				vx[x][y]=vy[x][y]=0.0;
	}
	
	public void update(){
		for(int y=0;y<height;y++){
			for(int x=0;x<width;x++){
				if(vx[x][y]!=0&&rand.nextInt(100)<=10)vx[x][y]+=vx[x][y]<0?1:-1;
				if(vy[x][y]!=0&&rand.nextInt(100)<=10)vy[x][y]+=vy[x][y]<0?1:-1;
			}
		}
	}
}
