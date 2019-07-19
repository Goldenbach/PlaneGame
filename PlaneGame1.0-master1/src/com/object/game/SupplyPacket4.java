package com.object.game;

import java.awt.Graphics;
import java.awt.Image;

public class SupplyPacket4 extends GameObject implements Runnable{
	private boolean live;
	public SupplyPacket4(Image img) {
		setImg(img);
		setPosition((Math.random()*6*64+64), -37);
		setHeight(img.getHeight(null));
		setWidth(img.getWidth(null));
	}
	public void drawSelf(Graphics g) {
		if(live){
			g.drawImage(getImg(), (int)getX(), (int)getY(), null);
			move();
		}
		checkLocation();
	}

	
	public void checkLocation() {
		if(!live||getY()>Constant.GAME_HEIGHT){
			setPosition((Math.random()*6*64+64), -37);
			live=false;
		}
	}

	public void move() {
		moveY(getY()+2);
	}
	public void run() {
		while(true){
			try{
				Thread.sleep(36500);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
			live=true;
		}
	}
	public boolean isLive(){
		return live;
	}
	public void setLive(boolean live){
		this.live=live;
	}

}
