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

package powderinjava;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import powderinjava.elements.Element;

/**
 * A Specific Particle On Screen
 */

public class Particle {

	public static List<Particle> particles = new ArrayList<Particle>();

	public Element element;
	private Random rand;

	public int x;
	public int y;
	public int dx;
	public int dy;
	public int t;

	public boolean removeQueue;

	public Particle(int x, int y, Element element) {
		this.x = x;
		this.y = y;
		this.element = element;
		rand = new Random();
		switch (element.state) {
		case SOLID:
			dx = dy = 0;
			break;
		case LIQUID:
			dx = rand.nextInt(3) - 1;
			dy = 1;
			break;
		case GAS:
			dx = rand.nextInt(3) - 1;
			dy = rand.nextInt(3) - 1;
			break;
		case POWDER:
			dx = 0;
			dy = 1;
			break;
		}
		this.t = 0;
		particles.add(this);
	}

	public synchronized void update() {
		if (++t % 100 == 0) {
			switch (element.state) {
			case GAS:
				if (particleAt(x + dx, y + dy) == null) {
					dx += rand.nextInt(3) - 1;
					dy += rand.nextInt(3) - element.mass;
					if(dx==0)dx=rand.nextInt(2)==0?1:-1;
					if(dy==0)dy=rand.nextInt(2)==0?1:-1;
					x += dx;
					y -= dy;
				}
				break;
			case LIQUID:
				if (particleAt(x, y + 1) == null)
					y += dy;
				if (particleAt(x + dx, y) == null) {
					x += dx;
					dx += rand.nextInt(3) - 1;
				}
				break;
			case POWDER:
				break;
			case SOLID:
				break;
			}
			for (int i = 0; i < 8; i++) {
				int ax = i < 3 ? x - 1 : i < 4 ? x : x + 1;
				int ay = i == 0 || i == 3 || i == 5 ? y + 1
						: i == 1 || i == 6 ? y : y - 1;
				element.update(ax, ay);
			}
			if (x < 0 || y < 0 || x >= Main.powder.width
					|| y >= Main.powder.height)
				remove();
			while (dx > 1)
				dx--;
			while (dx < -1)
				dx++;
			while (dy > 1)
				dy--;
			while (dy < -1)
				dy++;
			t = 0;
		}
		if (particleAt(x, y) != null)
			if (x < this.x) {
				displace(this.x + 1, this.y);
				if (y < this.y)
					displace(this.x, this.y + 1);
				else if (y > this.y)
					displace(this.x, this.y - 1);
			} else if (x > this.x) {
				displace(this.x - 1, this.y);
				if (y < this.y)
					displace(this.x, this.y + 1);
				else if (y > this.y)
					displace(this.x, this.y - 1);
			}
	}

	public void displace(int dx, int dy) {
		if (particleAt(x + dx, y + dy) == null) {
			x += dx;
			y += dy;
		}
	}

	public static Particle particleAt(int x, int y) {
		for (Iterator<Particle> iterator = particles.iterator(); iterator
				.hasNext();) {
			Particle particle = iterator.next();
			if (particle.x == x && particle.y == y)
				return particle;
		}
		return null;
	}

	public void remove() {
		removeQueue = true;
	}
}
