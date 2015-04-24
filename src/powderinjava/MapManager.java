/**
 * @Copyright 2015 firefreak11
 *
 *            This file is part of PowderInJava.
 *
 *            PowderInJava is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *            PowderInJava is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 *            You should have received a copy of the GNU General Public License along with PowderInJava. If not, see <http://www.gnu.org/licenses/>.
 **/

package powderinjava;

import static powderinjava.Powder.pv;
import static powderinjava.Powder.tv;
import static powderinjava.Powder.maxTemp;
import static powderinjava.Powder.minTemp;
import static powderinjava.Powder.maxPres;
import static powderinjava.Powder.minPres;
import static powderinjava.Powder.presConst;
import static powderinjava.Powder.vx;
import static powderinjava.Powder.vy;

public class MapManager{

	private static Thread thread;

	public static void start(){
		thread=new Thread(){

			public void run(){
				while(!Powder.isCloseRequested)
					update();
			}
		};
		thread.start();
	}

	private static void update(){
		for(int y=0;y<Powder.HEIGHT;y++){
			for(int x=0;x<Powder.WIDTH;x++){
				if(!Powder.paused){
					float pAv=0.0f;
					float tAv=0.0f;
					float success=0.0f;
					for(int i=0;i<8;i++){
						int ax=i==0||i==6||i==7?-1:i==1||i==5?0:1;
						int ay=i==0||i==1||i==2?-1:i==3||i==7?0:1;
						if(x+ax>Powder.xMarginLeft&&x+ax<Powder.xMarginRight&&y+ay>Powder.yMarginTop&&y+ay<Powder.yMarginBottom){
							pAv+=pv[x+ax][y+ay];
							tAv+=tv[x+ax][y+ay];
							success++;
						}
						if(x-1>=Powder.xMarginLeft&&x+1<=Powder.xMarginRight&&y-1>=Powder.yMarginTop&&y+1<=Powder.yMarginBottom){
							float dp=0.0f;
							float dx=0.0f;
							float dy=0.0f;
							dp+=vx[x-1][y]-vx[x][y];
							dp+=vy[x][y-1]-vy[x][y];
							pv[x][y]*=0.9999f;
							pv[x][y]+=dp*0.3f;
							dx+=pv[x][y]-pv[x+1][y];
							dy+=pv[x][y]-pv[x][y+1];
							vx[x][y]*=0.999f;
							vy[x][y]*=0.999f;
							vx[x][y]+=dx*0.4f;
							vy[x][y]+=dy*0.4f;
						}
					}
					pv[x][y]=pAv/success;
					tv[x][y]=tAv/success;
					if(tv[x][y]>maxTemp) tv[x][y]=maxTemp;
					if(tv[x][y]<minTemp) tv[x][y]=minTemp;
					if(pv[x][y]>maxPres) pv[x][y]=maxPres;
					if(pv[x][y]<minPres) pv[x][y]=minPres;
					pv[x][y]+=presConst*(tv[x][y]/maxTemp);
					if(x>Powder.xMarginLeft&&x<Powder.xMarginRight&&y>Powder.yMarginTop&&y<Powder.yMarginBottom){
						float a=pv[x+1][y];
						float b=pv[x-1][y];
						float c=pv[x][y-1];
						float d=pv[x][y+1];
						vx[x][y]=b-a;
						vy[x][y]=c-d;
					}
				}
			}
		}
	}
}
