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

//	����һ���ĵ��ݵ�SubPanel�࣬����һ�����ݵĽ��沼�֣�����һ�����ݵ��������
public class SubPanel extends JPanel implements Runnable {

	// һ��״̬���飬��ʾĳһʱ�̣��ⲿ�����ڸ���¥���Ƿ�Ҫͣ�����
	// 0 ��ʾ ��ͣ��1 ��ʾ Ҫͣ
	int[] FloorStop = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0 };

	Thread thread; // �������е��߳�

	int CurrentFloor; // ��ǰ�������ڵ�¥��

	boolean goFlag; // �����Ƿ��ڡ����ϡ������¡�
	boolean runFlag; // �����Ƿ��ڹ���״̬
	int CurrentState; // ���ݵ�ǰ������״̬ 0 ͣ�ţ� 1 �������ϣ�2 ��������

	// JLabel label;
	JComboBox DestFloorCombo;
	String strName;

	// ��20����ɫ��ťģ���¥�ĸ���¥��
	// ���ʱ����ʾ�����ڸ�¥�㣨�Ź��ţ�
	// ����ʱ����ʾ�����ڸ�¥�㣨�ſ��ţ�
	private JButton[] dispButton;

	private JLabel dispCurrentFloorLabel; // ��ʾ��ǰ�������ĸ�¥��ı�ǩ
	private JButton[] operatorButtons; // ģ���ڵ����ڲ��Ĳ�����ť ��1 �� 20¥��
	private JLabel dispOpenClose; // ��ʾ��ǰ�����ǹ����ţ����ǿ�����

	public String toString() {
		return strName;
	}

	SubPanel(String str) {

		strName = "Elevator " + str;
		thread = new Thread(this);
		thread.setDaemon(true);

		Border b = BorderFactory.createEtchedBorder(); // �ӱ߿�
		Border titled = BorderFactory.createTitledBorder(b, str);
		setBorder(titled); // �ӱ߿����

		setLayout(new BorderLayout()); // ���ò��ֹ����������� BorderLayout�Ĳ��ֹ���

		JPanel panelControl = new JPanel(); // һ�����ݵĿ����������Ƶ����Ƿ��ڹ���״̬

		// label = new JLabel("1");
		// panelControl.add(label);

		CurrentFloor = 1; // ��ʼ��ʱ�������ڵ�һ��
		CurrentState = 0; // ��ʼ��ʱ������ͣ��
		runFlag = false; // ��ʼ��ʱ�����ݴ��ڲ�����״̬
		goFlag = false; // ��ʼ��ʱ������ͣ�ţ�û�����ϣ�Ҳû������

		final JButton startButton = new JButton("start"); // �������ݣ�ʹ���ݽ��빤��״̬
		final JButton stopButton = new JButton("stop"); // �رյ��ݣ�ʹ���ݽ��벻����״̬

		// �ṩ�������ݰ�ť�ļ�������
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

		// �ṩ�رյ��ݰ�ť�ļ�������
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

		add(panelControl, BorderLayout.SOUTH); // �ѵ��ݵĿ�������panel,����һ�����ݵ�panel

		// �����ڲ��Ĳ��������ṩ20����ť����ʾ20��¥��
		// ������ĳ��ť������ݻ�����Ӧ¥��ͣ��
		JPanel panelCtrlInElevator = new JPanel();

		panelCtrlInElevator.setLayout(new BorderLayout());
		// ���ò��ֹ����������� BorderLayout�Ĳ��ֹ���

		dispCurrentFloorLabel = new JLabel("               " + "1");
		// ����һ������panel���Ϸ�������ʾ��ǰ�����ڵڼ�¥��label,��ʼ����1¥
		panelCtrlInElevator.add(dispCurrentFloorLabel, BorderLayout.NORTH);

		dispOpenClose = new JLabel("        CLOSED"); // ��ʼ��ʱ�����ǹ��ŵ�
		// �ѵ����ڲ��Ĳ�������panel������һ�����ݵ�panel
		panelCtrlInElevator.add(dispOpenClose, BorderLayout.SOUTH);

		// ʵ������������panel
		JPanel panelButtonsInEvelator = new JPanel();

		panelButtonsInEvelator.setLayout(new GridLayout(19, 2));
		// ���ò��ֹ����������� BorderLayout�Ĳ��ֹ���

		for (int i = 0; i < 4; ++i) {
			panelButtonsInEvelator.add(new JLabel(""));
		}

		operatorButtons = new JButton[20]; // ���� 20 ���20 ����ť
		for (int i = 0; i < 20; ++i) {
			operatorButtons[i] = new JButton("" + (i + 1));
			operatorButtons[i].setEnabled(false);
			panelButtonsInEvelator.add(operatorButtons[i]);
			addAction(operatorButtons[i]);
		}

		// �ѹ���20����ť��panel����������ڲ���������panel
		panelCtrlInElevator.add(panelButtonsInEvelator, BorderLayout.CENTER);

		add(panelCtrlInElevator, BorderLayout.CENTER);

		// ģ��¥���panel��Ҳ��20��panel��ʾ
		JPanel panelElevator = new JPanel();
		panelElevator.setLayout(new GridLayout(20, 1));
		dispButton = new JButton[20];
		for (int i = 0; i < 20; ++i) {
			dispButton[i] = new JButton("      ");
			dispButton[i].setBackground(Color.black); // ��ʼ������ɫ�Ǻ�ɫ
			dispButton[i].setEnabled(false);

			panelElevator.add(dispButton[i]);
		}
		dispButton[19].setBackground(Color.red); // һ¥�ڳ�ʼ��ʱ ��ɫ�Ǻ�ɫ����ʾ������һ¥

		add(panelElevator, BorderLayout.WEST);

		thread.start(); // �߳�����

	}

	// �ڵ����ڲ������ܵ���һ��¥��ָ��ļ�������
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

	// ��ĳһ���������µ�����ʱ�����øò�����Ҫͣ����¥�������
	public void setTask(int floorNo) {
		FloorStop[floorNo] = 1;
		if (CurrentState == 0) {
			goFlag = true;
			synchronized (thread) {
				thread.notify();
			}
		}

	}

	// ���ظò������Ƿ��ڹ���״̬
	public boolean getRunnabel() {
		return runFlag;
	}

	// ���ظò������Ƿ���ͣ��
	public boolean isStoped() {
		if (CurrentState == 0) {
			return true;
		} else {
			return false;
		}
	}

	// ���ظò�����������״̬��0 ͣ�ţ� 1 �������ϣ� 2 ��������
	public int getCurrentState() {
		return CurrentState;
	}

	// ���ظò�����������¥��
	public int getCurrentFloor() {
		return CurrentFloor;
	}

	// �˺��������Ƿ����ָ��
	// ������������ʱ��ֻ���ܸ��ڵ�ǰ¥���ͣ��ָ��
	// ������������ʱ��ֻ���ܵ��ڵ�ǰ¥���ͣ��ָ��
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

	// �жϸò����ݣ��Ƿ�Ҫ������������
	private boolean isStillUP(int[] FloorStop, int Current) {
		int i;
		for (i = Current + 1; i <= 20; ++i) {
			if (FloorStop[i] == 1) {
				return true;
			}
		}
		return false;
	}

	// �жϸò����ݣ��Ƿ�Ҫ������������
	private boolean isStillDOWN(int[] FloorStop, int Current) {
		int i;
		for (i = Current - 1; i > 0; --i) {
			if (FloorStop[i] == 1) {
				return true;
			}
		}
		return false;
	}

	// �жϸò����ݣ��Ƿ�Ҫ��ĳ��¥��ͣ��
	private boolean isStoped(int[] FloorStop, int Current) {
		if (FloorStop[Current] == 1) {
			return true;
		}
		return false;

	}

	// ����õ��ݵ���ĳ��Ŀ��¥���Ĵ�����
	private void arrivalDisp(int CurrentFloor) // �����ڵĵư���
	{
		System.out.println("arrive" + " " + CurrentFloor);
		operatorButtons[CurrentFloor - 1].setBackground(Color.lightGray);
	}

	// ������������ʱ��������ĳ����;¥������ʾ����
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

	// �߳����еĺ���
	public void run() {
		while (true) {

			try {
				// �߳�����
				synchronized (thread) {

					if (!runFlag || !goFlag) {
						thread.wait();
					}
				}

				if (isTheSameFloor(FloorStop)) { // �ڵ�ǰ¥����Ӧָ�����

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
				} else if (isUP(FloorStop)) { // �ڸõ�����������ʱ�Ĵ���
					CurrentState = 1; // ���õ���״̬

					while (isStillUP(FloorStop, CurrentFloor)) {
						// �ж��Ƿ��������
						++CurrentFloor;

						// label.setText("" + CurrentFloor);
						updateDisp(CurrentFloor);

						if (isStoped(FloorStop, CurrentFloor)) { // ����ĳһ��Ŀ��¥��
							Thread.sleep(1000);
							// ��ʾ����
							dispButton[20 - CurrentFloor]
									.setBackground(Color.blue);

							dispOpenClose.setText("        OPENED");
							FloorStop[CurrentFloor] = 0;
							// updateDisp(CurrentFloor);
							arrivalDisp(CurrentFloor);
							System.out.print(this);
							System.out.println(CurrentFloor);
							Thread.sleep(1000); // ͣһ�£��߳�˯��
						}

						// ��ʾ����
						dispButton[20 - CurrentFloor].setBackground(Color.red);
						dispOpenClose.setText("        CLOSED");
						Thread.sleep(1000);
					}

				} else { // �ڸõ�����������ʱ�Ĵ���
					CurrentState = 2; // ���õ���״̬
					while (isStillDOWN(FloorStop, CurrentFloor)) {
						// �ж��Ƿ��������
						--CurrentFloor;
						// label.setText("" + CurrentFloor);
						updateDisp(CurrentFloor);

						if (isStoped(FloorStop, CurrentFloor)) {// ����ĳһ��Ŀ��¥��

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
				// ������Ϻ�����ֹͣ��״̬
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
