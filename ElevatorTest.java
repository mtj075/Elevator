import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.Border;

//	main�������ڵ��࣬�������ڴ�����ʼ������
public class ElevatorTest {

	public static void main(String[] args) {
		ElevatorFrame frame = new ElevatorFrame("Elevator"); //��ʼ������
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.show(); //��ʾ����
	}
}



