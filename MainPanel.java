import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.Timer;

// �岿���ݵĿ�����MainPanel����������ʾ�岿���ݵ�����
public class MainPanel extends JPanel {

	SubPanel[] panel; //��ʾ�岿���ݵ�����
	private Timer timer; //��ʱ��

	int[] UpWardArray = new int[21]; //���ܿ����У�ά���š����ϡ����������
	int[] DownWardArray = new int[21]; //���ܿ����У�ά���š����¡����������

	//	��ʱ���࣬����ÿ��һ��ʱ�䣬��̬�أ����ŵذѸ�¥��ָ���ɷ�һ��
	private class TimerListener implements ActionListener {

		public void actionPerformed(ActionEvent event) {

			if (hasUpTask() || hasDownTask()) {

				dispatchCommand(); //�ɷ���¥��ָ��ĺ���
			}
		}
	}
	//	�жϸ���¥����û�����ϵ�ָ��
	private boolean hasUpTask() {
		for (int i = 0; i < UpWardArray.length; ++i) {
			if (UpWardArray[i] == 1) {
				return true;
			}
		}
		return false;
	}
	//	�жϸ���¥����û�����µ�ָ��
	private boolean hasDownTask() {

		for (int i = 0; i < DownWardArray.length; ++i) {
			if (DownWardArray[i] == 2) {
				return true;
			}
		}
		return false;
	}

	// �����岿���ݵĿ�����MainPanel�Ĺ��캯����Ϊ�岿���ݽ��н��沼��
	public MainPanel() {

		setLayout(new GridLayout(1, 4)); //���ò��ֹ����������� GridLayout�Ĳ��ֹ���1��5�У�

		panel = new SubPanel[5]; //��ʼ���岿����
		panel[0] = new SubPanel("one"); //ÿ�����ݼ�����ʾ��ʶ
		panel[1] = new SubPanel("two");
		panel[2] = new SubPanel("three");
		panel[3] = new SubPanel("four");
		panel[4] = new SubPanel("five");

		add(panel[0]); //���岿���ݵ���ʾ����MainPanel
		add(panel[1]);
		add(panel[2]);
		add(panel[3]);
		//add(panel[4]);

		for (int i = 0; i <= 20; ++i) {
			DownWardArray[i] = 0; //��ʼ����¥�������ָ�0 ����ĳ��¥��û������ָ��
		}
		for (int i = 0; i <= 20; ++i) {
			UpWardArray[i] = 0; //��ʼ����¥�������ָ�0 ����ĳ��¥��û������ָ��
		}

		ActionListener timerlistener = new TimerListener(); //��ʼ����ʱ��
		timer = new Timer(1000, timerlistener);
		//ÿ��1�룬��̬�أ����ŵ��ɷ�һ�θ�¥�㡰���ϡ��������¡�ָ��
		timer.start(); //������ʱ��

	}

	//	���á����ϡ��������¡�ָ�����飬�ɷ�ָ��
	public void addCommand(int floorNo, int direction) {

		if (direction == 1) { //�� 1 ��������ָ��
			UpWardArray[floorNo] = 1;
		} else if (direction == 2) { //�� 2 ��������ָ��
			DownWardArray[floorNo] = 2;
		}

		dispatchCommand(); //�ɷ�ָ��

	}

	// ������֧���岿���ݲ�ȫ���е�������˺����õ��������еĵ�������
	private ArrayList getRunnableElevator() {
		ArrayList runnableElevator = new ArrayList();
		for (int i = 0; i < 5; ++i) {
			if (panel[i].getRunnabel()) {
				runnableElevator.add(panel[i]);
			}

		}
		return runnableElevator;

	}

	//	�ɷ�����ĺ���
	public void dispatchCommand() {
		ArrayList runnableElevators = getRunnableElevator(); //�õ��������еĵ���

		if (runnableElevators.size() == 0) { //��û���������еĵ��ݣ���ֱ�ӷ���
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

		if (hasUpTask()) { //	�����¥���С����ϡ�ָ�����ô������ϡ�ָ��ĺ���
			ProccessUpTask(runnableElevators);

		} else if (hasDownTask()) { //	�����¥���С����¡�ָ�����ô������¡�ָ��ĺ���
			ProccessDownTask(runnableElevators);
		}

	}

	//	���е����������ϣ���ĳ¥���С����ϡ�ָ�
	//	���ҷ��������ϡ�ָ���¥������������ϵ��ݵĵ�ǰ¥�㣬���ɷ���Щָ��
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

	//	���е����������£���ĳ¥���С����¡�ָ�
	//	���ҷ��������¡�ָ���¥������������µ��ݵĵ�ǰ¥�㣬���ɷ���Щָ��
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

	//	��ͣ�ŵĵ��ݣ����С����¡�ָ����ɷ���ѡ������ص�����ִ��ָ��
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

	//	��ͣ�ŵĵ��ݣ����С����ϡ�ָ����ɷ���ѡ������ص�����ִ��ָ��
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

	//	�����¥��ġ����ϡ�ָ��
	private void ProccessUpTask(ArrayList runnableElevators) {

		// ������¥��ġ����ϡ�ָ�����飬����Ƿ��С����ϡ�ָ��
		for (int i = 0; i < UpWardArray.length; ++i) {
			if (UpWardArray[i] == 1) { //ĳ¥���С����ϡ�ָ��
				//	����ָ���ܷ��ɷ����������ϵĵ���
				isAssignedToUpWardElevator(runnableElevators);
				//	����ָ���ܷ��ɷ���ͣ�ŵĵ���
				isAssignedToStopElevatorUp(runnableElevators);
			}
		}

	}

	//	�����¥��ġ����¡�ָ��
	private void ProccessDownTask(ArrayList runnableElevators) {
		for (int i = 0; i < DownWardArray.length; ++i) {
			if (DownWardArray[i] == 2) { //ĳ¥���С����¡�ָ��
				//	����ָ���ܷ��ɷ����������µĵ���
				isAssignedToDownWardElevator(runnableElevators);
				//	����ָ���ܷ��ɷ���ͣ�ŵĵ���
				isAssignedToStopElevatorDown(runnableElevators);

			}
		}
	}

	//	�ж����е����Ƿ�ͣ��
	private boolean isAllStoped(ArrayList runnableElevators) {

		for (int i = 0; i < runnableElevators.size(); ++i) {
			if (!((SubPanel) runnableElevators.get(i)).isStoped()) {
				return false;
			}
		}
		return true;

	}

}
