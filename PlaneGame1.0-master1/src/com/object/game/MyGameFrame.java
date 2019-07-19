package com.object.game;

import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.Date;


/**
 * 
 * 游戏主窗口
 * 游戏开始前，关闭输入法
 * w前，s后，a左，d右+shift（可以加速）
 * 
 *
 */
public class MyGameFrame extends Frame {
	private Image plane_img;//飞机图片
	private Image bg;//背景图片
	private Image supply_img;//加血包
	private Image supBullet_img;//子弹包
	private Image sup_img;//毁灭包
	private Image sup1_img;//金币加分包
	
	private MyPlane plane;//我的飞机
	private Image army_images[];//敌机图片
	private Plane armys[];//敌机
	private Image offScreenImage;//用于双缓冲
	private int score;//所获分数
	private int coin;//所获金币
	private SupplyPacket supply;//医疗包
	private SupplyPacket2 supBullet;//空投包
	private SupplyPacket3 sup;//雷电毁灭包
	private SupplyPacket4 sup1;//金币加分包
	
	/*用于画窗口的线程*/
	private Thread paintThread;//重画窗口线程
	private Thread planefire;//我方飞机发射子弹的线程
	private Thread armysfire[];//敌方飞机发射子弹的线程
	private Thread supplyThread;//补给出现的线程
	private Thread supBulletThread;//子弹包路线
	private Thread supThread;//毁灭包路线
	private Thread sup1Thread;//金币路线
	
	private Image bg0;//背景图片
	private Image bg1;//背景图片
	private Image bg2;//背景图片
	private Image bg3;//背景图片
	private Image bg4;//背景图片
	private Image bg5;//背景图片
	private Image bg6;//背景图片
	private Image bg7;//背景图片
	private Image bg8;//背景图片
	boolean kaiguan =false;//进入游戏开关-进入提示页
	boolean kaiguan2 =false;//进入游戏开关
	//计时-游戏时间
	Date startTime;
	long endTime;
	int period;//游戏持续时间
	//游戏背景音乐
	URL url1 = getClass().getResource("media.io_game_music.au");
	AudioClip clip1 = java.applet.Applet.newAudioClip(url1);
	//敌机死亡炸毁音效
	URL url3 = getClass().getResource("media.io_enemy3_down.au");
	AudioClip clip3 = java.applet.Applet.newAudioClip(url3);
	//我方飞机炸毁音效
	URL url2 = getClass().getResource("media.io_game_over.au");
	AudioClip clip2 = java.applet.Applet.newAudioClip(url2);
	//获得补给音效
	URL url4 = getClass().getResource("media.io_game_achievement.au");
	AudioClip clip4 = java.applet.Applet.newAudioClip(url4);
	//我方受到攻击音效
	URL url5 = getClass().getResource("media.io_enemy5_down.au");
	AudioClip clip5 = java.applet.Applet.newAudioClip(url5);
	/**
	 * 用于加载图片
	 */
	public void load(){
		/*加载飞机图片*/
		plane_img=GameUtil.getImage("images/plane.png");
		/*加载背景图片*/
		bg =GameUtil.getImage("images/bg.jpg");
		bg0 =GameUtil.getImage("images/bg0.jpg");
		bg1=GameUtil.getImage("images/bg1.jpg");
		bg2=GameUtil.getImage("images/bg2.jpg");
		bg3=GameUtil.getImage("images/bg3.jpg");
		bg4=GameUtil.getImage("images/bg4.jpg");
		bg5=GameUtil.getImage("images/bg5.jpg");
		bg6=GameUtil.getImage("images/bg6.jpg");
		bg7=GameUtil.getImage("images/bg7.jpg");
		bg8=GameUtil.getImage("images/bg8.jpg");
		
		/*加载敌机图片*/
		army_images=new Image[6];
		for(int i=0;i<army_images.length;i++){
			army_images[i] = GameUtil.getImage("images/army/army"+(i+1)+".png");
            army_images[i].getWidth(null);
		}
		/*加载补给包图片*/
		supply_img=GameUtil.getImage("images/supply.png");
		supBullet_img=GameUtil.getImage("images/supBullet.png");
		sup_img=GameUtil.getImage("images/sup.png");
		sup1_img=GameUtil.getImage("images/sup1.png");
//		clip1.loop();//游戏背影音乐
	}
	/**
	 * 用于初始化窗口
	 */
	public void init(){
		setTitle("PlaneGame1.0");
		setVisible(true);
		setSize(Constant.GAME_WIDTH, Constant.GAME_HEIGHT);
		setLocation(400, 30);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		addKeyListener(new KeyMonitors());
		plane=new MyPlane(plane_img,288,460);
		supply=new SupplyPacket(supply_img);
		supBullet=new SupplyPacket2(supBullet_img);
		sup=new SupplyPacket3(sup_img);
		sup1=new SupplyPacket4(sup1_img);
		
		armys=new Plane[6];
		for(int i=0;i<armys.length;i++){
			armys[i]=new Plane(army_images[i]);
		}
		
		paintThread=new PaintThread();
		planefire=new Thread(plane);
		armysfire=new Thread[armys.length]; //创建敌方飞机发射子弹的线程
		for(int i=0;i<armys.length;i++){//敌方飞机发射子弹的线程
			armysfire[i]=new Thread(armys[i]);
		}
		supplyThread=new Thread(supply);
		supBulletThread=new Thread(supBullet);
		supThread=new Thread(sup);
		sup1Thread=new Thread(sup1);
		startTime = new Date();
	}
	/**
	 * 用于开启线程
	 */
	public void start(){
		paintThread.start();
		if(kaiguan==true&&kaiguan2==true) {
			planefire.start();
			for(int i=0;i<armys.length;i++){
				armysfire[i].start();
			}
			supplyThread.start();
			supBulletThread.start();
			supThread.start();
			sup1Thread.start();
		}
	}
	/**
	 * 用于关闭线程
	 */
	public void stop(){
		paintThread.stop();
		planefire.stop();
		supplyThread.stop();
		supBulletThread.stop();
		supThread.stop();
		sup1Thread.stop();
		clip1.stop();
	}
	
	
	/**
	 * 内部的一个线程类，用于重画窗口
	 */
	class PaintThread extends Thread{
		public void run() {
			while(true){
				repaint();
				try{
					Thread.sleep(40);//1秒画25次
				}catch(InterruptedException e){
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 内部类，监听键盘
	 */
	class KeyMonitors extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			plane.addDirection(e);
		}
		@Override
		public void keyReleased(KeyEvent e) {
			plane.minusDirection(e);
		}
	}
	/**
	 * 进入游戏？
	 */
	public void gogo() {
		this.kaiguan = true;
	}
	public void gogo2() {
		this.kaiguan2 = true;
	}
	//监听鼠标--1.开局点击鼠标进入游戏
	public void act() {
		MouseAdapter lm = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
//				MyGameFrame f=new MyGameFrame();
				gogo();
				load();
				init();
				start();
				act2();
				stop();
//				f.start();
//				f.action();
			}
		};
		this.addMouseListener(lm);
	}
	//监听鼠标--1.1开局点击鼠标进入游戏
		public void act2() {
			MouseAdapter lm2 = new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					MyGameFrame f=new MyGameFrame();
					f.gogo();
					f.gogo2();
					f.load();
					f.init();
					f.start();
					f.action();
					clip1.loop();//游戏背影音乐
				}
			};
			this.addMouseListener(lm2);
		}
	//监听鼠标--2.结束时点击鼠标重新开始
	public void action() {
		MouseAdapter l = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(!plane.isLive()) {
					score=0;
					coin=0;
					stop();
					load();
					init();
					start();
					
				}
			}
		};
		this.addMouseListener(l);
	}
	@Override
	/**
	 * 用于画窗口
	 */
	public void paint(Graphics g) {
		g.drawImage(bg, 0, 0, null);//游戏开始前的背景
		if(kaiguan==true) {
			g.drawImage(bg0, 0, 0, null);//游戏开始前的背景--提示页
			if(kaiguan2==true) {
				//根据分数来切换背景图
				if(score>=0&&score<=100){
					g.drawImage(bg1, 0, 0, null);
				}else if(score>100&&score<=250){
					g.drawImage(bg2, 0, 0, null);
				}else if(score>250&&score<=400){
					g.drawImage(bg3, 0, 0, null);
				}else if(score>400&&score<=550){
					g.drawImage(bg4, 0, 0, null);
				}else if(score>550&&score<=650){
					g.drawImage(bg5, 0, 0, null);
				}else if(score>650&&score<=750){
					g.drawImage(bg6, 0, 0, null);
				}else if(score>750&&score<=850){
					g.drawImage(bg7, 0, 0, null);
				}else {
					g.drawImage(bg8, 0, 0, null);
				}
				drawScore(g);
				supply.drawSelf(g);
				supBullet.drawSelf(g);
				sup.drawSelf(g);
				sup1.drawSelf(g);
				plane.drawSelf(g);
				for(int i=0;i<plane.bullets.length;i++){
					plane.bullets[i].drawSelf(g);
				}
		
				for(int i=0;i<armys.length;i++){
					armys[i].drawSelf(g);
				}
				for(int i=0;i<armys.length;i++){
					for(int j=0;j<armys[i].bullets.length;j++){
						armys[i].bullets[j].drawSelf(g);
					}
				}
				plane.blood.draw(g);
				if(plane.isLive()) {
					isCollide();//判断是否发现碰撞
				}
			}
		}
	}
	/**
	 * 用于双缓冲
	 * 内存中重新开辟新空间，作为后台图像，然后把本来要在界面上进行的清理和重绘在后台处理好，
	 * 生成一幅新的画面，最后再显示在界面上。
	 * 解决：每循环一次界面就要重绘一次，导致屏幕频繁的闪烁。
	 */
	public void update(Graphics g) {
	    if(offScreenImage == null)
	        offScreenImage = this.createImage(Constant.GAME_WIDTH,Constant.GAME_HEIGHT);//这是游戏窗口的宽度和高度
	    Graphics gOff = offScreenImage.getGraphics();
	    paint(gOff);
	    g.drawImage(offScreenImage, 0, 0, null);
	}
	/**
	 * 用于判断是否发生碰撞
	 */
	//判断敌方飞机是否被击中
	public void isCollide(){
		for(int i=0;i<armys.length;i++){
			boolean peng=false;
			for(int j=0;j<plane.bullets.length;j++){
				if(plane.bullets[j].isLive()&&
						(armys[i].getRect().intersects
								(plane.bullets[j].getRect()))){
					peng=true;
					plane.bullets[j].setLive(false);
					score+=10;
					break;
				}
				else{
					continue;
				}
			}
			if(peng){
				armys[i].setCollide(true);
				if(armys[i].explode1.count==0){
					armys[i].explode1.setLocation(armys[i].getX(), armys[i].getY());
					armys[i].ex1=true;
				}
				else{
					armys[i].explode2.setLocation(armys[i].getX(), armys[i].getY());
					armys[i].ex2=true;
				}
					armys[i].setLive(false);
					//敌机死亡炸毁音效
					clip3.play();
			}
			//判断我方飞机是否被击中
			for(int j=0;j<armys[i].bullets.length;j++){
				if(armys[i].bullets[j].isLive()&&(armys[i].bullets[j].getRect().intersects
						(plane.getRect()))){
					plane.blood.minusBlood();
					armys[i].bullets[j].setLive(false);
					//我方受损音效
					clip5.play();
				}
			}
			if(plane.blood.isFlag()==true) {//没血了，飞机GG
				plane.explode3.setLocation(plane.getX(), plane.getY());
				plane.setLive(false);
			}
		}
//		if(!plane.isLive()) {//如果开启判断语句，则页面现实的战斗时间只显示0和最后时刻。
			//计时器结束
			endTime = System.currentTimeMillis();
			period = (int)((endTime-startTime.getTime())/1000);
			//我方飞机炸毁音效
			clip2.play();
//			return;
//		}
		
		/**
		 * 判断我方飞机是否获得补给
		 */
		//+血包
		if(supply.isLive()&&plane.isLive()
				&&plane.getRect()
				.intersects(supply.getRect())){
			plane.blood.addBlood();
			supply.setLive(false);
			//获得补给音效
			clip4.play();
		}
		//空投=+分数（+5）
		if(supBullet.isLive()&&plane.isLive()
				&&plane.getRect()
				.intersects(supBullet.getRect())){
//			plane.change();//换子弹功能未实现
//			plane=new MyPlane(plane_img,288,460);
//			planefire.stop();
//			planefire=new Thread(plane);
//			planefire.start();
			score+=5;
			supBullet.setLive(false);
			//获得补给音效
			clip4.play();
		}
		//金币包+1
		if(sup1.isLive()&&plane.isLive()
				&&plane.getRect()
				.intersects(sup1.getRect())){
			coin+=1;
			sup1.setLive(false);
			//获得补给音效
			clip4.play();
		}
		//+吃到大补给/现有敌机自爆
		if(sup.isLive()&&plane.isLive()
				&&plane.getRect()
				.intersects(sup.getRect())){
			for(int i=0;i<armys.length;i++){
				armys[i].setCollide(true);
				if(armys[i].explode1.count==0){
					armys[i].explode1.setLocation(armys[i].getX(), armys[i].getY());
					armys[i].ex1=true;
				}
				else{
					armys[i].explode2.setLocation(armys[i].getX(), armys[i].getY());
					armys[i].ex2=true;
				}
					armys[i].setLive(false);
					//敌机死亡炸毁音效
					clip3.play();
			}
			sup.setLive(false);
			//获得补给音效
			clip4.play();
		}
	}
	/**
	 * 用于画分数
	 */
	public void drawScore(Graphics g){
		Color c=g.getColor();
		g.setColor(Color.WHITE);
		g.setFont(new Font("宋体", Font.BOLD, 15));
		g.drawString("得分："+score, 15, 50);
		g.drawString("金币："+coin, 100, 50);
		g.drawString("战斗时间："+period+"秒",170,50);//游戏持续时间
		g.setColor(c);
	}
	public static void main(String[] args) {
		MyGameFrame f=new MyGameFrame();
		f.load();
		f.init();
		f.act();
	}
}
