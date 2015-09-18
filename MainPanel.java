import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.Timer;

// 五部电梯的控制类MainPanel，来管理显示五部电梯的区域
public class MainPanel extends JPanel {

	SubPanel[] panel; //表示五部电梯的数组
	private Timer timer; //定时器

	int[] UpWardArray = new int[21]; //在总控类中，维护着“向上”命令的数组
	int[] DownWardArray = new int[21]; //在总控类中，维护着“向下”命令的数组

	//	定时器类，用于每隔一段时间，动态地，最优地把各楼层指令派发一次
	private class TimerListener implements ActionListener {

		public void actionPerformed(ActionEvent event) {

			if (hasUpTask() || hasDownTask()) {

				dispatchCommand(); //派发各楼层指令的函数
			}
		}
	}
	//	判断各个楼层有没有向上的指令
	private boolean hasUpTask() {
		for (int i = 0; i < UpWardArray.length; ++i) {
			if (UpWardArray[i] == 1) {
				return true;
			}
		}
		return false;
	}
	//	判断各个楼层有没有向下的指令
	private boolean hasDownTask() {

		for (int i = 0; i < DownWardArray.length; ++i) {
			if (DownWardArray[i] == 2) {
				return true;
			}
		}
		return false;
	}

	// 管理五部电梯的控制类MainPanel的构造函数，为五部电梯进行界面布局
	public MainPanel() {

		setLayout(new GridLayout(1, 4)); //设置布局管理器，采用 GridLayout的布局管理（1行5列）

		panel = new SubPanel[5]; //初始化五部电梯
		panel[0] = new SubPanel("one"); //每部电梯加上显示标识
		panel[1] = new SubPanel("two");
		panel[2] = new SubPanel("three");
		panel[3] = new SubPanel("four");
		panel[4] = new SubPanel("five");

		add(panel[0]); //把五部电梯的显示加入MainPanel
		add(panel[1]);
		add(panel[2]);
		add(panel[3]);
		//add(panel[4]);

		for (int i = 0; i <= 20; ++i) {
			DownWardArray[i] = 0; //初始化各楼层的向上指令，0 代表某个楼层没有向上指令
		}
		for (int i = 0; i <= 20; ++i) {
			UpWardArray[i] = 0; //初始化各楼层的向下指令，0 代表某个楼层没有向下指令
		}

		ActionListener timerlistener = new TimerListener(); //初始化定时器
		timer = new Timer(1000, timerlistener);
		//每隔1秒，动态地，最优地派发一次各楼层“向上”、“向下”指令
		timer.start(); //启动定时器

	}

	//	设置“向上”、“向下”指令数组，派发指令
	public void addCommand(int floorNo, int direction) {

		if (direction == 1) { //用 1 代表向上指令
			UpWardArray[floorNo] = 1;
		} else if (direction == 2) { //用 2 代表向下指令
			DownWardArray[floorNo] = 2;
		}

		dispatchCommand(); //派发指令

	}

	// 本程序支持五部电梯不全运行的情况，此函数得到正在运行的电梯数组
	private ArrayList getRunnableElevator() {
		ArrayList runnableElevator = new ArrayList();
		for (int i = 0; i < 5; ++i) {
			if (panel[i].getRunnabel()) {
				runnableElevator.add(panel[i]);
			}

		}
		return runnableElevator;

	}

	//	派发命令的函数
	public void dispatchCommand() {
		ArrayList runnableElevators = getRunnableElevator(); //得到正在运行的电梯

		if (runnableElevators.size() == 0) { //若没有正在运行的电梯，则直接返回
			System.out.println("No");
			for (int i = 0; i < 21; ++i) {
				UpWardArray[i] = 0;
				DownWardArray[i] = 0;
			}
			return;
		}

		/*//////////////////
				ArrayList runnableElevators=new ArrayList();
				runnableElevators.add(panel[0]);
				runnableElevators.add(panel[1]);
				runnableElevators.add(panel[2]);
				runnableElevators.add(panel[3]);
				runnableElevators.add(panel[4]);
				////////////////////////////*/

		if (hasUpTask()) { //	如果各楼层有“向上”指令，则调用处理“向上”指令的函数
			ProccessUpTask(runnableElevators);

		} else if (hasDownTask()) { //	如果各楼层有“向下”指令，则调用处理“向下”指令的函数
			ProccessDownTask(runnableElevators);
		}

	}

	//	当有电梯正在向上，且某楼层有“向上”指令，
	//	并且发出“向上“指令的楼层高于正在向上电梯的当前楼层，则派发这些指令
	private void isAssignedToUpWardElevator(ArrayList runnableElevators) {
		//		for (int i = 0; i < runnableElevators.size(); ++i) {
		//			System.out.println(runnableElevators.get(i));
		//		}
		if (runnableElevators.size() == 0) {
			return;
		}

		for (int i = 0; i < UpWardArray.length; ++i) {
			if (UpWardArray[i] == 1) {
				int nearest = -1;
				int nearestElevator = -1;
				for (int j = 0; j < runnableElevators.size(); ++j) {
					if (((SubPanel) runnableElevators.get(j)).getCurrentState()
						== 1) {
						int temp =
							((SubPanel) runnableElevators.get(j))
								.getCurrentFloor();
						if (temp > nearest && temp < i) {
							nearest =
								((SubPanel) runnableElevators.get(j))
									.getCurrentFloor();
							nearestElevator = j;
						}
					}
				}

				if (nearest != -1) {

					(
						(SubPanel) runnableElevators.get(
							nearestElevator)).setTask(
						i);
					UpWardArray[i] = 0;
					nearest = -1;
					nearestElevator = -1;

				}
			}
		}

	}

	//	当有电梯正在向下，且某楼层有“向下”指令，
	//	并且发出“向下“指令的楼层低于正在向下电梯的当前楼层，则派发这些指令
	private void isAssignedToDownWardElevator(ArrayList runnableElevators) {
		System.out.println("new task ");
		if (runnableElevators.size() == 0) {
			return;
		}
		for (int i = 0; i < DownWardArray.length; ++i) {
			if (DownWardArray[i] == 2) {
				int nearest = 999;
				int nearestElevator = -1;
				for (int j = 0; j < runnableElevators.size(); ++j) {
					if (((SubPanel) runnableElevators.get(j)).getCurrentState()
						== 2) {
						int temp =
							((SubPanel) runnableElevators.get(j))
								.getCurrentFloor();
						if (temp < nearest && temp > i) {
							nearest =
								((SubPanel) runnableElevators.get(j))
									.getCurrentFloor();
							nearestElevator = j;
						}
					}
				}
				if (nearestElevator != -1) {
					(
						(SubPanel) runnableElevators.get(
							nearestElevator)).setTask(
						i);
					DownWardArray[i] = 0;
					nearest = 999;
					nearestElevator = -1;
				}
			}
		}
	}

	//	对停着的电梯，进行“向下”指令的派发，选择最靠近地电梯来执行指令
	private void isAssignedToStopElevatorDown(ArrayList runnableElevators) {
		if (runnableElevators.size() == 0) {
			return;
		}

		for (int i = 0; i < DownWardArray.length; ++i) {
			if (DownWardArray[i] == 2) {
				int nearestDistence = 999;
				int nearestElevator = -1;
				for (int j = 0; j < runnableElevators.size(); ++j) {
					if (((SubPanel) runnableElevators.get(j)).getCurrentState()
						== 0) {
						int temp =
							((SubPanel) runnableElevators.get(j))
								.getCurrentFloor();
						if (Math.abs(i - temp) < nearestDistence) {
							nearestDistence = Math.abs(i - temp);
							nearestElevator = j;
						}
					}
				}
				if (nearestElevator != -1) {
					(
						(SubPanel) runnableElevators.get(
							nearestElevator)).setTask(
						i);
					DownWardArray[i] = 0;
					nearestDistence = 999;
					nearestElevator = -1;
				}
			}
		}
	}

	//	对停着的电梯，进行“向上”指令的派发，选择最靠近地电梯来执行指令
	private void isAssignedToStopElevatorUp(ArrayList runnableElevators) {
		if (runnableElevators.size() == 0) {
			return;
		}

		for (int i = 0; i < UpWardArray.length; ++i) {
			if (UpWardArray[i] == 1) {
				int nearestDistence = 999;
				int nearestElevator = -1;
				for (int j = 0; j < runnableElevators.size(); ++j) {
					if (((SubPanel) runnableElevators.get(j)).getCurrentState()
						== 0) {
						int temp =
							((SubPanel) runnableElevators.get(j))
								.getCurrentFloor();
						if (Math.abs(i - temp) < nearestDistence) {
							nearestDistence = Math.abs(i - temp);
							nearestElevator = j;
						}
					}
				}
				if (nearestElevator != -1) {
					(
						(SubPanel) runnableElevators.get(
							nearestElevator)).setTask(
						i);
					UpWardArray[i] = 0;
					nearestDistence = 999;
					nearestElevator = -1;
				}
			}
		}
	}

	//	处理各楼层的“向上”指令
	private void ProccessUpTask(ArrayList runnableElevators) {

		// 遍历各楼层的“向上”指令数组，检测是否有“向上”指令
		for (int i = 0; i < UpWardArray.length; ++i) {
			if (UpWardArray[i] == 1) { //某楼层有“向上”指令
				//	看此指令能否派发给正在向上的电梯
				isAssignedToUpWardElevator(runnableElevators);
				//	看此指令能否派发给停着的电梯
				isAssignedToStopElevatorUp(runnableElevators);
			}
		}

	}

	//	处理各楼层的“向下”指令
	private void ProccessDownTask(ArrayList runnableElevators) {
		for (int i = 0; i < DownWardArray.length; ++i) {
			if (DownWardArray[i] == 2) { //某楼层有“向下”指令
				//	看此指令能否派发给正在向下的电梯
				isAssignedToDownWardElevator(runnableElevators);
				//	看此指令能否派发给停着的电梯
				isAssignedToStopElevatorDown(runnableElevators);

			}
		}
	}

	//	判断所有电梯是否都停着
	private boolean isAllStoped(ArrayList runnableElevators) {

		for (int i = 0; i < runnableElevators.size(); ++i) {
			if (!((SubPanel) runnableElevators.get(i)).isStoped()) {
				return false;
			}
		}
		return true;

	}

}
