import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

//	程序的主panel，在此进行界面布局
public class ElevatorPanel extends JPanel {
	//	程序的主panel的构造函数
	public ElevatorPanel() {
		setLayout(new BorderLayout()); //设置布局管理器，采用BorderLayout的布局管理

		JLabel label =
			new JLabel("                                                                                                         Elevator Test");

		add(label, BorderLayout.NORTH); //窗口的上方是显示“Elevator Test“的标签

		MainPanel mainPanel = new MainPanel();
		add(mainPanel); //窗口的中间是显示五部电梯的区域，用一个MainPanel类来管理
		validate();
		ControlPanel controlPanel = new ControlPanel(mainPanel);
		add(controlPanel, BorderLayout.SOUTH); //窗口的上方是模拟在每个楼面发出“向上”、“向下”命令的显示区

	}
}