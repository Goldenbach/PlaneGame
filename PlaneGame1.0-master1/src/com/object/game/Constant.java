package com.object.game;
/**
 * 
 * 此类存放常量
 *
 */
public final class Constant {
	private Constant(){}
	/**
	 * 游戏窗口大小
	 */
	public static final int GAME_WIDTH=512;
	public static final int GAME_HEIGHT=680;
	/**
	 * 飞机步长
	 */
	public static final double PLANE_STEP=3.0;
	public static final double ARMY_STEP=2.0;
	/**
	 * 子弹步长
	 */
	public static final int BULLET_STEP=4;
	/**
	 * 默认子弹数目
	 */
	public static final int DEFAULT_PLANE_BC=50;//我方飞机子弹
	public static final int DEFAULT_ARMY_BC=5;//敌方飞机子弹
}
