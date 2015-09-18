import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

//	控制一部的电梯的SubPanel类，负责一部电梯的界面布局，管理一部电梯的运行情况
public class SubPanel extends JPanel implements Runnable {

	// 一个状态数组，表示某一时刻，这部电梯在各个楼层是否要停的情况
	// 0 表示 不停，1 表示 要停
	int[] FloorStop = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0 };

	Thread thread; // 电梯运行的线程

	int CurrentFloor; // 当前电梯所在地楼层

	boolean goFlag; // 电梯是否在“向上”或“向下”
	boolean runFlag; // 电梯是否处于工作状态
	int CurrentState; // 电梯当前所处的状态 0 停着； 1 正在向上；2 正在向下

	// JLabel label;
	JComboBox DestFloorCombo;
	String strName;

	// 用20个黑色按钮模拟大楼的各个楼层
	// 变红时，表示电梯在该楼层（门关着）
	// 变蓝时，表示电梯在该楼层（门开着）
	private JButton[] dispButton;

	private JLabel dispCurrentFloorLabel; // 显示当前电梯在哪个楼层的标签
	private JButton[] operatorButtons; // 模拟在电梯内部的操作按钮 （1 到 20楼）
	private JLabel dispOpenClose; // 显示当前电梯是关着门，还是开着门

	public String toString() {
		return strName;
	}

	SubPanel(String str) {

		strName = "Elevator " + str;
		thread = new Thread(this);
		thread.setDaemon(true);

		Border b = BorderFactory.createEtchedBorder(); // 加边框
		Border titled = BorderFactory.createTitledBorder(b, str);
		setBorder(titled); // 加边框标题

		setLayout(new BorderLayout()); // 设置布局管理器，采用 BorderLayout的布局管理

		JPanel panelControl = new JPanel(); // 一部电梯的控制区，控制电梯是否处于工作状态

		// label = new JLabel("1");
		// panelControl.add(label);

		CurrentFloor = 1; // 初始化时，电梯在第一层
		CurrentState = 0; // 初始化时，电梯停着
		runFlag = false; // 初始化时，电梯处于不工作状态
		goFlag = false; // 初始化时，电梯停着，没有向上，也没有向下

		final JButton startButton = new JButton("start"); // 启动电梯，使电梯进入工作状态
		final JButton stopButton = new JButton("stop"); // 关闭电梯，使电梯进入不工作状态

		// 提供启动电梯按钮的监听器类
		ActionListener startL = new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				runFlag = true;
				startButton.setEnabled(false);
				stopButton.setEnabled(true);
				for (int i = 0; i < operatorButtons.length; ++i) {
					operatorButtons[i].setEnabled(true);
				}
			}
		};
		startButton.addActionListener(startL);
		panelControl.add(startButton);

		// 提供关闭电梯按钮的监听器类
		stopButton.setEnabled(false);
		ActionListener stopL = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (goFlag == false) {

					runFlag = false;
					startButton.setEnabled(true);
					stopButton.setEnabled(false);
					for (int i = 0; i < operatorButtons.length; ++i) {
						operatorButtons[i].setEnabled(false);
					}
				}
			}
		};
		stopButton.addActionListener(stopL);
		panelControl.add(stopButton);

		add(panelControl, BorderLayout.SOUTH); // 把电梯的控制区的panel,加入一部电梯的panel

		// 电梯内部的操作区，提供20个按钮，表示20个楼层
		// 若按下某按钮，则电梯会在相应楼层停下
		JPanel panelCtrlInElevator = new JPanel();

		panelCtrlInElevator.setLayout(new BorderLayout());
		// 设置布局管理器，采用 BorderLayout的布局管理

		dispCurrentFloorLabel = new JLabel("               " + "1");
		// 代表一部电梯panel的上方，是显示当前电梯在第几楼的label,初始化在1楼
		panelCtrlInElevator.add(dispCurrentFloorLabel, BorderLayout.NORTH);

		dispOpenClose = new JLabel("        CLOSED"); // 初始化时，门是关着的
		// 把电梯内部的操作区的panel，加入一部电梯的panel
		panelCtrlInElevator.add(dispOpenClose, BorderLayout.SOUTH);

		// 实例化操作区的panel
		JPanel panelButtonsInEvelator = new JPanel();

		panelButtonsInEvelator.setLayout(new GridLayout(19, 2));
		// 设置布局管理器，采用 BorderLayout的布局管理

		for (int i = 0; i < 4; ++i) {
			panelButtonsInEvelator.add(new JLabel(""));
		}

		operatorButtons = new JButton[20]; // 代表 20 层的20 个按钮
		for (int i = 0; i < 20; ++i) {
			operatorButtons[i] = new JButton("" + (i + 1));
			operatorButtons[i].setEnabled(false);
			panelButtonsInEvelator.add(operatorButtons[i]);
			addAction(operatorButtons[i]);
		}

		// 把管理20个按钮的panel，加入电梯内部操作区的panel
		panelCtrlInElevator.add(panelButtonsInEvelator, BorderLayout.CENTER);

		add(panelCtrlInElevator, BorderLayout.CENTER);

		// 模拟楼层的panel，也用20个panel显示
		JPanel panelElevator = new JPanel();
		panelElevator.setLayout(new GridLayout(20, 1));
		dispButton = new JButton[20];
		for (int i = 0; i < 20; ++i) {
			dispButton[i] = new JButton("      ");
			dispButton[i].setBackground(Color.black); // 初始化的颜色是黑色
			dispButton[i].setEnabled(false);

			panelElevator.add(dispButton[i]);
		}
		dispButton[19].setBackground(Color.red); // 一楼在初始化时 颜色是红色，表示电梯在一楼

		add(panelElevator, BorderLayout.WEST);

		thread.start(); // 线程启动

	}

	// 在电梯内部，接受到哪一层楼的指令的监听器类
	private void addAction(final JButton button) {
		ActionListener bL = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int num = Integer.parseInt(button.getText().trim());

				if (runFlag != false) {
					if (ProcessInput(FloorStop, num, CurrentFloor, CurrentState)) {
						button.setBackground(Color.blue);
						goFlag = true;
						synchronized (thread) {
							thread.notify();
						}

					}
				}

			}
		};

		button.addActionListener(bL);
	}

	// 当某一部电梯有新的任务时，设置该部电梯要停靠的楼层的数组
	public void setTask(int floorNo) {
		FloorStop[floorNo] = 1;
		if (CurrentState == 0) {
			goFlag = true;
			synchronized (thread) {
				thread.notify();
			}
		}

	}

	// 返回该部电梯是否处于工作状态
	public boolean getRunnabel() {
		return runFlag;
	}

	// 返回该部电梯是否处于停着
	public boolean isStoped() {
		if (CurrentState == 0) {
			return true;
		} else {
			return false;
		}
	}

	// 返回该部电梯所处的状态：0 停着； 1 正在向上； 2 正在向下
	public int getCurrentState() {
		return CurrentState;
	}

	// 返回该部电梯所处的楼层
	public int getCurrentFloor() {
		return CurrentFloor;
	}

	// 此函数处理是否接受指令
	// 当电梯在向上时，只接受高于当前楼层的停靠指令
	// 当电梯在向下时，只接受低于当前楼层的停靠指令
	private boolean ProcessInput(int[] FloorStop, int num, int CurrentFloor,
			int CurrentState) {
		if (CurrentState == 1) {
			if (num > CurrentFloor) {
				FloorStop[num] = 1;
				return true;
			}
		} else if (CurrentState == 2) {
			if (num < CurrentFloor) {
				FloorStop[num] = 1;
				return true;
			}
		} else {
			FloorStop[num] = 1;
			return true;

		}
		return false;

	}

	private boolean isTheSameFloor(int[] FloorStop) {
		if (FloorStop[CurrentFloor] == 1) {
			return true;
		}
		return false;
	}

	private boolean isUP(int[] FloorStop) {
		int i;
		for (i = 1; i <= 20; ++i) {
			if (FloorStop[i] == 1) {
				break;
			}
		}
		if (CurrentFloor < i) {
			return true;
		} else {
			return false;
		}

	}

	// 判断该部电梯，是否还要继续向上运行
	private boolean isStillUP(int[] FloorStop, int Current) {
		int i;
		for (i = Current + 1; i <= 20; ++i) {
			if (FloorStop[i] == 1) {
				return true;
			}
		}
		return false;
	}

	// 判断该部电梯，是否还要继续向下运行
	private boolean isStillDOWN(int[] FloorStop, int Current) {
		int i;
		for (i = Current - 1; i > 0; --i) {
			if (FloorStop[i] == 1) {
				return true;
			}
		}
		return false;
	}

	// 判断该部电梯，是否要在某个楼层停靠
	private boolean isStoped(int[] FloorStop, int Current) {
		if (FloorStop[Current] == 1) {
			return true;
		}
		return false;

	}

	// 处理该电梯到了某个目的楼层后的处理工作
	private void arrivalDisp(int CurrentFloor) // 电梯内的灯暗掉
	{
		System.out.println("arrive" + " " + CurrentFloor);
		operatorButtons[CurrentFloor - 1].setBackground(Color.lightGray);
	}

	// 当电梯在运行时，处理到了某个中途楼层后的显示工作
	private void updateDisp(int CurrentFloor) {
		for (int i = 0; i < dispButton.length; ++i) {
			if ((i + CurrentFloor) == 20) {
				dispButton[i].setBackground(Color.red);
			} else {
				dispButton[i].setBackground(Color.black);
			}
		}
		dispCurrentFloorLabel.setText("               " + CurrentFloor);
	}

	// 线程运行的函数
	public void run() {
		while (true) {

			try {
				// 线程阻塞
				synchronized (thread) {

					if (!runFlag || !goFlag) {
						thread.wait();
					}
				}

				if (isTheSameFloor(FloorStop)) { // 在当前楼层响应指令，开门

					System.out.println(CurrentFloor);
					updateDisp(CurrentFloor);
					FloorStop[CurrentFloor] = 0;
					Thread.sleep(500);
					arrivalDisp(CurrentFloor);
					System.out.print(this);
					System.out.println(CurrentFloor);
					dispButton[20 - CurrentFloor].setBackground(Color.blue);
					dispOpenClose.setText("        OPENED");
					Thread.sleep(2500);
					dispButton[20 - CurrentFloor].setBackground(Color.red);
					dispOpenClose.setText("        CLOSED");
				} else if (isUP(FloorStop)) { // 在该电梯向上运行时的处理
					CurrentState = 1; // 设置电梯状态

					while (isStillUP(FloorStop, CurrentFloor)) {
						// 判断是否继续向上
						++CurrentFloor;

						// label.setText("" + CurrentFloor);
						updateDisp(CurrentFloor);

						if (isStoped(FloorStop, CurrentFloor)) { // 到了某一个目的楼层
							Thread.sleep(1000);
							// 显示开门
							dispButton[20 - CurrentFloor]
									.setBackground(Color.blue);

							dispOpenClose.setText("        OPENED");
							FloorStop[CurrentFloor] = 0;
							// updateDisp(CurrentFloor);
							arrivalDisp(CurrentFloor);
							System.out.print(this);
							System.out.println(CurrentFloor);
							Thread.sleep(1000); // 停一下，线程睡觉
						}

						// 显示关门
						dispButton[20 - CurrentFloor].setBackground(Color.red);
						dispOpenClose.setText("        CLOSED");
						Thread.sleep(1000);
					}

				} else { // 在该电梯向下运行时的处理
					CurrentState = 2; // 设置电梯状态
					while (isStillDOWN(FloorStop, CurrentFloor)) {
						// 判断是否继续向下
						--CurrentFloor;
						// label.setText("" + CurrentFloor);
						updateDisp(CurrentFloor);

						if (isStoped(FloorStop, CurrentFloor)) {// 到了某一个目的楼层

							Thread.sleep(1000);
							dispButton[20 - CurrentFloor]
									.setBackground(Color.blue);

							dispOpenClose.setText("        OPENED");
							FloorStop[CurrentFloor] = 0;

							arrivalDisp(CurrentFloor);
							System.out.print(this);
							System.out.println(CurrentFloor);
							Thread.sleep(1000);

						}

						dispButton[20 - CurrentFloor].setBackground(Color.red);

						dispOpenClose.setText("        CLOSED");
						Thread.sleep(1000);
					}
				}
				// 运行完毕后，设置停止的状态
				goFlag = false;
				CurrentState = 0;

				for (int i = 1; i <= 20; ++i) {
					System.out.print(FloorStop[i] + " ");
				}
				System.out.println();

			} catch (InterruptedException e) {
			}
		}
	}
}
