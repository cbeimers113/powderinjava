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

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import powderinjava.Particle;

public abstract class Engine extends Canvas implements KeyListener,
		MouseListener, MouseMotionListener, MouseWheelListener, Runnable {

	public static final long serialVersionUID = 1L;

	public JFrame frame;
	public String title;
	public Thread thread;
	public BufferedImage img;
	public String fpsOutput;

	public int width;
	public int height;
	public int[] pixels;

	public boolean resizable;
	public boolean fullscreen;
	public boolean isCloseRequested;

	public Engine(String title, int width, int height, boolean resizable,
			boolean fullscreen) {
		this.title = title;
		this.width = width;
		this.height = height;
		this.resizable = resizable;
		this.fullscreen = fullscreen;
		fpsOutput = "";
		initialize();
	}

	private synchronized void initialize() {
		frame = new JFrame(title);
		frame.setPreferredSize(fullscreen ? Toolkit.getDefaultToolkit()
				.getScreenSize() : new Dimension(width, height));
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
		frame.add(this);
		frame.addKeyListener(this);
		frame.addMouseListener(this);
		frame.addMouseMotionListener(this);
		frame.addMouseWheelListener(this);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				isCloseRequested = true;
			}
		});
		try {
			frame.setIconImage(ImageIO.read(Engine.class
					.getResource("/assets/icon.png")));
		} catch (Exception e) {
			System.out.println("No Custom Icon Found For /assets/icon.png");
		}
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(resizable);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		instantiate();
		thread = new Thread(this, title);
		thread.start();
	}

	private void instantiate() {
		img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
	}

	private synchronized void stop() {
		if (!isCloseRequested)
			return;
		save();
		System.exit(0);
	}

	public void run() {
		long timer = System.currentTimeMillis();
		int frames = 0;
		while (!isCloseRequested) {
			update();
			coreRender();
			frames++;
			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				setFpsOutput("FPS:" + frames + ", Parts: "
						+ Particle.particles.size());
				frames = 0;
			}
		}
		stop();
	}

	private void setFpsOutput(String fpsOutput) {
		this.fpsOutput = fpsOutput;
	}

	private void coreRender() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		render();
		g.drawImage(img, 0, 0, null);
		g.setColor(Color.cyan);
		g.drawString(fpsOutput, 5, 15);
		g.dispose();
		bs.show();
	}

	public abstract void update();

	public abstract void render();

	public abstract void save();
}
