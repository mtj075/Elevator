import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

//	�������panel���ڴ˽��н��沼��
public class ElevatorPanel extends JPanel {
	//	�������panel�Ĺ��캯��
	public ElevatorPanel() {
		setLayout(new BorderLayout()); //���ò��ֹ�����������BorderLayout�Ĳ��ֹ���

		JLabel label =
			new JLabel("                                                                                                         Elevator Test");

		add(label, BorderLayout.NORTH); //���ڵ��Ϸ�����ʾ��Elevator Test���ı�ǩ

		MainPanel mainPanel = new MainPanel();
		add(mainPanel); //���ڵ��м�����ʾ�岿���ݵ�������һ��MainPanel��������
		validate();
		ControlPanel controlPanel = new ControlPanel(mainPanel);
		add(controlPanel, BorderLayout.SOUTH); //���ڵ��Ϸ���ģ����ÿ��¥�淢�������ϡ��������¡��������ʾ��

	}
}