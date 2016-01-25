package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import robot.MyRobotLego;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

public class RobotPlayer extends JFrame implements Runnable {

	private JPanel contentPane;
	private MyRobotLego robot;
	private Trabalho04JFrame main;
	private boolean recording, playing, inverse;
	
	private ArrayList<String> commands;
	
	public RobotPlayer(MyRobotLego robot, Trabalho04JFrame main) {
		
		this.robot = robot;
		this.main = main;
		
		commands = new ArrayList<String>();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 214, 253);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnSaveConfig = new JButton("Guardar Configura\u00E7\u00E3o");
		btnSaveConfig.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				saveRobotConfig();
			}
		});
		btnSaveConfig.setBounds(10, 11, 175, 23);
		contentPane.add(btnSaveConfig);
		
		JButton btnLoadConfig = new JButton("Carregar Configura\u00E7\u00E3o");
		btnLoadConfig.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				loadRobotConfig();
			}
		});
		btnLoadConfig.setBounds(10, 45, 175, 23);
		contentPane.add(btnLoadConfig);
		
		JButton btnSavePath = new JButton("Gravar Trajet\u00F3ria");
		btnSavePath.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				recording = true;
				if(playing) playing = false;
				main.log("Recording: " + recording);
			}
		});
		btnSavePath.setBounds(10, 79, 175, 23);
		contentPane.add(btnSavePath);
		
		JButton btnLoadPath = new JButton("Reproduzir Trajet\u00F3ria");
		btnLoadPath.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(recording) recording = false;
				playing = true;
				loadPathFromFile();
				main.log("Playing: " + playing);
			}
		});
		btnLoadPath.setBounds(10, 113, 175, 23);
		contentPane.add(btnLoadPath);
		
		JButton btnLoadInversePath = new JButton("Reproduzir Trajet\u00F3ria Inversa");
		btnLoadInversePath.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				inverse = true;
				playing = true;
				if(recording) recording = false;
				main.log("Playing inverse: " + playing);
			}
		});
		btnLoadInversePath.setBounds(10, 147, 175, 23);
		contentPane.add(btnLoadInversePath);
		
		JButton btnStop = new JButton("Parar Grava\u00E7\u00E3o/Reprodu\u00E7\u00E3o");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(recording) {
					recording = false;
					savePathToFile();
				}
				playing = false;
				inverse = false;
				main.log("Stopped all");
			}
			
		});
		btnStop.setBounds(10, 181, 175, 23);
		contentPane.add(btnStop);
	}

	public boolean isRecording() { return recording; }
	
	public boolean isPlaying() { return playing; }
	
	public boolean isPlayModeInverse() { return inverse; }
	
	public void addCommand(String cmd) { commands.add(cmd); }
	
	private void saveRobotConfig() {
		String[] config = main.getRobotConfig().split(":");
		String robotName = main.getRobotName();
		File file = new File(robotName+".txt");
		try {
			file.createNewFile();			
			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);
			//offset esquerda + offset direita
			bw.write(config[0]+":"+config[1]);
			bw.close();
			
		} catch (IOException e) { e.printStackTrace(); }
		main.log("Config for robot " + robotName + " saved: "+config[0]+":"+config[1]);
	}

	private void loadRobotConfig() {
		File file = new File(main.getRobotName()+".txt");
		String config = null;
		if (file.exists()) {
			try {
				
				FileReader fr = new FileReader(file);
				BufferedReader br = new BufferedReader(fr);
				config = br.readLine();
				br.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		main.setRobotConfig(config);
		
	}
	
	private void savePathToFile() {
		File file = new File("path.txt");
		try {
			
			file.createNewFile();			
			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);
			for(String cmd : commands) {
				bw.write(cmd+"\r\n");
			}
			bw.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void loadPathFromFile() {
		File file = new File("path.txt");
		commands.clear();
		if(file.exists()) {
			try {
				FileReader fr = new FileReader(file);
				BufferedReader br = new BufferedReader(fr);
				String cmd;
				while((cmd = br.readLine()) != null) commands.add(cmd);
				br.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void executeCommand(String cmd) {
		String[] s = cmd.split(":");
		switch(s[0]) {
			case "0":
				robot.Reta(Integer.parseInt(s[1]));
				break;	
			case "1":	
				robot.CurvarDireita(Integer.parseInt(s[1]), Integer.parseInt(s[2]));
				break;
			case "2":
				robot.CurvarEsquerda(Integer.parseInt(s[1]), Integer.parseInt(s[2]));
				break;
			case "3":
				robot.Parar(false);
				break;
		}
	}
	
	@Override
	public void run() {
		while(true) {
			
		}
		
	}
}