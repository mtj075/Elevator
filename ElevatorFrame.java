import java.awt.Container;

import javax.swing.JFrame;

//	���򴰿��࣬���������У���ʼ��һ����panel
public class ElevatorFrame extends JFrame {
	//	���ڵĴ�С����1000���أ���700����
	public static final int DEFAULT_WIDTH = 1000;
	public static final int DEFAULT_HEIGHT = 700;

	// ���򴰿���Ĺ��캯��
	public ElevatorFrame(String str) {
		super(str);
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT); //���ô��ڴ�С
		Container contentPane = getContentPane();
		ElevatorPanel panel = new ElevatorPanel(); //��ʼ��һ����panel
		contentPane.add(panel); //��������
	}
}