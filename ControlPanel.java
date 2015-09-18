import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.border.Border;

// 一个panel类，这个panel模拟在每个楼面发出“向上”、“向下”命令的显示区
public class ControlPanel extends JPanel {
	JComboBox currentFloorCombo; //选择楼层的下拉列表框
	MainPanel mainPanel; //显示四部电梯的区域的panel的引用，方便消息传递

	// ControlPanel类的构造，对这个显示区的布局
	public ControlPanel(MainPanel mainPanel) {
		this.mainPanel = mainPanel;
		Border b = BorderFactory.createEtchedBorder(); //边框

		setBorder(b); //为这个显示区加上边框

		addComboBox(); //自定义函数，加入下拉列表框

		addButton(); //自定义函数，加入操作按钮：向上、向下按钮
	}

	//	自定义函数，加入操作按钮：向上、向下按钮
	private void addButton() {
		JButton upButton = new JButton("UP");
		JButton downButton = new JButton("DOWN");

		// 为upButton按钮提供监听器类
		ActionListener UpL = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					//从下拉列表框中，读出在电梯外，哪一层有按钮命令
	String InputStr = (String) currentFloorCombo.getSelectedItem();

				int floorNo = checkInput(InputStr);
				if (floorNo != -1) {
					//	若往电梯的控制类mainPanel，发出命令，	floorNo表示在哪一层，1 表示要向上	
					mainPanel.addCommand(floorNo, 1);
				} else {
				}

			}
		};
		upButton.addActionListener(UpL);

		//	为downButton按钮提供监听器类
		ActionListener DownL = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					//	从下拉列表框中，读出在电梯外，哪一层有按钮命令
	String InputStr = (String) currentFloorCombo.getSelectedItem();
				int floorNo = checkInput(InputStr);
				if (floorNo != -1) {

					//	若往电梯的控制类mainPanel，发出命令，	floorNo表示在哪一层，1 表示要向上	

					mainPanel.addCommand(floorNo, 2);
				} else {
				}

			}
		};
		downButton.addActionListener(DownL);
		//	往panel中加入按钮
		add(upButton);
		add(downButton);
	}

	// 下拉列表框允许直接输入楼层，来模拟在不同的楼层按下按钮，
	// 此函数是对输入值进行检验, 不满足要求的输入值将不被接受
	private int checkInput(String InputStr) {
		String str = InputStr.trim();
		if (str != null) {
			try {
				int floorNo = Integer.parseInt(str);
				if (floorNo >= 0 && floorNo <= 20) {
					System.out.println(floorNo);
					return floorNo;
				} else {
					System.out.println("illegal num!");
					currentFloorCombo.setSelectedItem(new String(""));
				}
			} catch (Exception e) {
				System.out.println("Input error!");
				currentFloorCombo.setSelectedItem(new String(""));
			}
		} else {
			System.out.println("No input!");
			currentFloorCombo.setSelectedItem(new String(""));
		}
		return -1;
	}

	//	自定义函数，加入下拉列表框，模拟在不同的楼层按下“向上”、”向下“按钮
	private void addComboBox() {

		currentFloorCombo = new JComboBox();
		currentFloorCombo.setEditable(true);
		for (int i = 1; i <= 20; ++i) {
			currentFloorCombo.addItem("" + i);
			//往下拉列表框加入1 到 20 模拟在不同的楼层按下“向上”、”向下“按钮
		}

		currentFloorCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {

			}
		});

		add(currentFloorCombo); //	往panel中加入下拉列表框

	}
}
