package com.object.game;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;

public class MyPlane extends Plane {
	private boolean up=false;
	private boolean down=false;
	private boolean left=false;
	private boolean right=false;
	private boolean accelerate=false;//加速关闭
	private double step=Constant.PLANE_STEP;
	Blood blood=new Blood();
	//飞机GG后爆炸
	Explode explode3=new Explode();
	//变换子弹开关
	boolean change = false;
	public MyPlane(Image img, double x, double y) {
		super(img, x, y,Constant.DEFAULT_PLANE_BC);
		setPosition(300, 450);
		if(change==false) {
			for(int i=0;i<bullets.length;i++){//我方飞机子弹
				bullets[i]=new PlaneBullet();
			}
		}else {
			for(int i=0;i<bullets.length;i++){//换子弹
				bullets[i]=new PlaneBullet2();
			}
		}
		
	}
	public MyPlane(Image img, double x, double y,int bulletcount) {
		super(img, x, y,bulletcount);
		setBulletCount(bulletcount);
		setPosition(300, 450);
		if(change==false) {
			for(int i=0;i<bullets.length;i++){
				bullets[i]=new PlaneBullet();
			}
		}else {
			for(int i=0;i<bullets.length;i++){//换子弹
				bullets[i]=new PlaneBullet2();
			}
		}
	}
	public boolean change() {
		change = true;
		return change;
	}
	public void drawSelf(Graphics g) {
		if(isLive()){
			g.drawImage(getImg(),(int)getX(),(int)getY(), null);
			move();
		}
		Object imgs1 = GameUtil.getImage("images/letter_cover.png");
		if(!isLive()){
			g.drawImage((Image) imgs1 ,135,330, null);
			exp_anim(g);
		}
		checkLocation();
	}
	/**
	 * 增加方向
	 * @param e
	 */
	public void addDirection(KeyEvent e){
		switch(e.getKeyCode()){
		case KeyEvent.VK_W:
			up=true;
			break;
		case KeyEvent.VK_S:
			down=true;
			break;
		case KeyEvent.VK_A:
			left=true;
			break;
		case KeyEvent.VK_D:
			right=true;
			break;
		case KeyEvent.VK_SHIFT:
			accelerate=true;
			break;
		}
	}
	/**
	 * 取消某键
	 * @param e
	 */
	public void minusDirection(KeyEvent e){
		switch(e.getKeyCode()){
		case KeyEvent.VK_W:
			up=false;
			break;
		case KeyEvent.VK_S:
			down=false;
			break;
		case KeyEvent.VK_A:
			left=false;
			break;
		case KeyEvent.VK_D:
			right=false;
			break;
		case KeyEvent.VK_SHIFT:
			accelerate=false;
			break;
		}
	}
	public void move() {
		if(accelerate){
			step=Constant.PLANE_STEP+2;
		}
		else{
			step=Constant.PLANE_STEP;
		}
		if(up){
			moveY(getY()-step);
		}
		if(down){
			moveY(getY()+step);
		}
		if(right){
			moveX(getX()+step);
		}
		if(left){
			moveX(getX()-step);
		}

	}
	/*
	 * 不需要自己回收子弹，因为子弹每次画自己的时候都会检查自己的状态
	 * 如果飞出框外自动回收，设live为false
	 * 检查自己的位子
	 */
	public void checkLocation(){
		if(getX()<=0)moveX(0);
		if(getX()>=(Constant.GAME_WIDTH-64))moveX(Constant.GAME_WIDTH-64);
		if(getY()<30)moveY(30);
		if(getY()>=(Constant.GAME_HEIGHT-64))moveY(Constant.GAME_HEIGHT-64);
	}
	//当飞机活着的时候才喷射子弹，GG后子弹停止
	public void fire() {
		if(isLive()) {
			for(int i=0;i<bullets.length;i++){
				if(!bullets[i].isLive()){
					bullets[i].setPosition(getX()+25, getY()-20);
					bullets[i].setLive(true);
					break;
				}
			}
		}
	}
	
	public void run() {
		while(true){
			fire();
			try{
				Thread.sleep(500);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
	}
	
	public void exp_anim(Graphics g){
			explode3.draw(g);
	}
}
