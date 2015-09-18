import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.border.Border;

// һ��panel�࣬���panelģ����ÿ��¥�淢�������ϡ��������¡��������ʾ��
public class ControlPanel extends JPanel {
	JComboBox currentFloorCombo; //ѡ��¥��������б��
	MainPanel mainPanel; //��ʾ�Ĳ����ݵ������panel�����ã�������Ϣ����

	// ControlPanel��Ĺ��죬�������ʾ���Ĳ���
	public ControlPanel(MainPanel mainPanel) {
		this.mainPanel = mainPanel;
		Border b = BorderFactory.createEtchedBorder(); //�߿�

		setBorder(b); //Ϊ�����ʾ�����ϱ߿�

		addComboBox(); //�Զ��庯�������������б��

		addButton(); //�Զ��庯�������������ť�����ϡ����°�ť
	}

	//	�Զ��庯�������������ť�����ϡ����°�ť
	private void addButton() {
		JButton upButton = new JButton("UP");
		JButton downButton = new JButton("DOWN");

		// ΪupButton��ť�ṩ��������
		ActionListener UpL = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					//�������б���У������ڵ����⣬��һ���а�ť����
	String InputStr = (String) currentFloorCombo.getSelectedItem();

				int floorNo = checkInput(InputStr);
				if (floorNo != -1) {
					//	�������ݵĿ�����mainPanel���������	floorNo��ʾ����һ�㣬1 ��ʾҪ����	
					mainPanel.addCommand(floorNo, 1);
				} else {
				}

			}
		};
		upButton.addActionListener(UpL);

		//	ΪdownButton��ť�ṩ��������
		ActionListener DownL = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					//	�������б���У������ڵ����⣬��һ���а�ť����
	String InputStr = (String) currentFloorCombo.getSelectedItem();
				int floorNo = checkInput(InputStr);
				if (floorNo != -1) {

					//	�������ݵĿ�����mainPanel���������	floorNo��ʾ����һ�㣬1 ��ʾҪ����	

					mainPanel.addCommand(floorNo, 2);
				} else {
				}

			}
		};
		downButton.addActionListener(DownL);
		//	��panel�м��밴ť
		add(upButton);
		add(downButton);
	}

	// �����б������ֱ������¥�㣬��ģ���ڲ�ͬ��¥�㰴�°�ť��
	// �˺����Ƕ�����ֵ���м���, ������Ҫ�������ֵ����������
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

	//	�Զ��庯�������������б��ģ���ڲ�ͬ��¥�㰴�¡����ϡ��������¡���ť
	private void addComboBox() {

		currentFloorCombo = new JComboBox();
		currentFloorCombo.setEditable(true);
		for (int i = 1; i <= 20; ++i) {
			currentFloorCombo.addItem("" + i);
			//�������б�����1 �� 20 ģ���ڲ�ͬ��¥�㰴�¡����ϡ��������¡���ť
		}

		currentFloorCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {

			}
		});

		add(currentFloorCombo); //	��panel�м��������б��

	}
}
