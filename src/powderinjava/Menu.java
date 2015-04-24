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

package powderinjava;

import static powderinjava.StaticData.pause;
import static powderinjava.StaticData.play;
import static powderinjava.StaticData.page;
import static powderinjava.StaticData.pauseH;
import static powderinjava.StaticData.playH;
import static powderinjava.StaticData.pageH;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import powderinjava.elements.Element;

public class Menu{

	private static int[] drawPImageAt;
	private static int[] drawNewAt;

	public List<Element> liquids;
	public List<Element> gasses;
	public List<Element> powders;
	public List<Element> solids;
	public List<Element> hidden;
	public List<Element> quantum;

	private int menu;
	private int x;
	private int y;
	private int elSize;
	private int elHeight;
	private int MLIQUIDS;
	private int MGASSES;
	private int MPOWDERS;
	private int MSOLIDS;
	private int MHIDDEN;
	private int MQUANTUM;

	public boolean b;

	public Menu(){
		liquids=new ArrayList<Element>();
		gasses=new ArrayList<Element>();
		powders=new ArrayList<Element>();
		solids=new ArrayList<Element>();
		hidden=new ArrayList<Element>();
		quantum=new ArrayList<Element>();
		MLIQUIDS=0;
		MGASSES=1;
		MPOWDERS=2;
		MSOLIDS=3;
		MHIDDEN=4;
		MQUANTUM=5;
		elSize=35;
		elHeight=10;
		menu=MLIQUIDS;
	}

	public void grabMouse(int x,int y,boolean b){
		this.x=x;
		this.y=y;
		this.b=b;
	}

	public static Color getContrastColor(Color color){
		double y=(299*color.getRed()+587*color.getGreen()+114*color.getBlue())/1000;
		return y>=128?Color.black:Color.white;
	}

	public void render(Graphics g){
		g.setColor(Color.white);
		g.drawLine(Powder.xMarginLeft,Powder.yMarginBottom(),Powder.xMarginRight(),Powder.yMarginBottom());
		g.drawLine(Powder.xMarginLeft,Powder.yMarginBottom(),Powder.xMarginLeft,Powder.yMarginTop);
		g.drawLine(Powder.xMarginLeft,Powder.yMarginTop,Powder.xMarginRight(),Powder.yMarginTop);
		g.drawLine(Powder.xMarginRight(),Powder.yMarginTop,Powder.xMarginRight(),Powder.yMarginBottom());
		g.setFont(new Font("Arial",1,9));
		FontMetrics fm=g.getFontMetrics();
		List<Element> list=toScan();
		for(Element e:list){
			int x=Powder.xMarginRight()-(1+list.indexOf(e))*(elSize+2);
			int y=30*Main.powder.height/32-elHeight-1;
			g.setColor(e.colour);
			g.fillRect(x,y,elSize,elHeight);
			g.setColor(getContrastColor(e.colour));
			g.drawString(e.name,x+elSize/2-fm.stringWidth(e.name)/2,y+elHeight-1);
			g.setColor(e.equals(Main.powder.spawnType)?Color.green:Color.white);
			g.drawRect(x,y,elSize,elHeight);
			if(b&&this.x>=x&&this.y>=y&&this.x<=x+elSize&&this.y<=y+elHeight) Main.powder.spawnType=e;
		}
		g.setColor(new Color(0x06739E));
		BufferedImage pImage=Main.powder.paused?play:pause;
		drawPImageAt=new int[]{
				Powder.xMarginRight()-5-pImage.getWidth(),
				Powder.yMarginBottom()+4
		};
		drawNewAt=new int[]{
				Powder.xMarginRight()-5-page.getWidth(),
				Powder.yMarginBottom()+8+page.getHeight()
		};
		if(x>=drawPImageAt[0]&&x<=drawPImageAt[0]+pImage.getWidth()&&y>=drawPImageAt[1]&&y<=drawPImageAt[1]+pImage.getHeight()){
			pImage=Main.powder.paused?playH:pauseH;
			g.drawImage(pImage,drawPImageAt[0],drawPImageAt[1],null);
			String pMsg=(Main.powder.paused?"Plays":"Pauses")+" the simulation.";
			g.drawString(pMsg,Powder.xMarginRight()-5-fm.stringWidth(pMsg),Powder.yMarginBottom()-5);
			if(b){
				Main.powder.paused=!Main.powder.paused;
				b=false;
			}
		}else{
			g.drawImage(pImage,drawPImageAt[0],drawPImageAt[1],null);
		}
		if(x>=drawNewAt[0]&&x<=drawNewAt[0]+page.getWidth()&&y>=drawNewAt[1]&&y<=drawNewAt[1]+page.getHeight()){
			g.drawImage(pageH,drawNewAt[0],drawNewAt[1],null);
			String nMsg="Starts a new simulation.";
			g.drawString(nMsg,Powder.xMarginRight()-5-fm.stringWidth(nMsg),Powder.yMarginBottom()-5);
			if(b){
				Main.powder.newSim();
				b=false;
			}
		}else{
			g.drawImage(page,drawNewAt[0],drawNewAt[1],null);
		}
	}

	public List<Element> toScan(){
		if(menu==MLIQUIDS)
			return liquids;
		else if(menu==MGASSES)
			return gasses;
		else if(menu==MPOWDERS)
			return powders;
		else if(menu==MSOLIDS)
			return solids;
		else if(menu==MHIDDEN)
			return hidden;
		else if(menu==MQUANTUM)
			return quantum;
		else return powders;
	}
}
