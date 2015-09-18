import java.awt.Container;

import javax.swing.JFrame;

//	程序窗口类，在主窗口中，初始化一个主panel
public class ElevatorFrame extends JFrame {
	//	窗口的大小，长1000像素，宽700像素
	public static final int DEFAULT_WIDTH = 1000;
	public static final int DEFAULT_HEIGHT = 700;

	// 程序窗口类的构造函数
	public ElevatorFrame(String str) {
		super(str);
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT); //设置窗口大小
		Container contentPane = getContentPane();
		ElevatorPanel panel = new ElevatorPanel(); //初始化一个主panel
		contentPane.add(panel); //建立关联
	}
}