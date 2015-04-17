package powderinjava;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import powderinjava.elements.Element;

public class Menu{

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

	private boolean b;

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
		elSize=30;
		elHeight=elSize/3;
		menu=MLIQUIDS;
	}

	public void grabMouse(int x,int y,boolean b){
		this.x=x;
		this.y=y;
		this.b=b;
	}

	public void render(Graphics g){
		g.setColor(Color.white);
		g.drawLine(0,Powder.yMarginBottom(),Main.powder.width,Powder.yMarginBottom());
		g.setFont(new Font("Arial",0,9));
		FontMetrics fm=g.getFontMetrics();
		List<Element> list=toScan();
		for(Element el:list){
			int x=Main.powder.width-(2+list.indexOf(el))*elSize;
			int y=30*Main.powder.height/32-elHeight-1;
			g.setColor(el.colour);
			g.fillRect(x,y,elSize,elHeight);
			g.setColor(Main.powder.spawnType.equals(el)?Color.green:Color.white);
			g.drawString(el.name,x+elSize/2-fm.stringWidth(el.name)/2,y+elHeight-1);
			g.drawRect(x,y,elSize,elHeight);
			if(b&&this.x>=x&&this.y>=y&&this.x<=x+elSize&&this.y<=y+elHeight) Main.powder.spawnType=el;
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
