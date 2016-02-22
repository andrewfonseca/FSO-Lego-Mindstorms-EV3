package Robot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class MyRobotLegoRecorder extends MyRobotLego {
	private boolean isPlaying = false;
	private boolean isRecording = false;
	
	private ArrayList<String> commands = new ArrayList<String>();
	private String filePath = "path.txt";
	
	public boolean isRecording() {
		return isRecording;
	}
	
	public boolean isPlaying() {
		return isPlaying;
	}
	
	public void toggleRecorder() throws IOException {
		if(isPlaying && !isRecording) {
			isPlaying = false;
		}
		
		if(isRecording) {
			savePath();
		}
		
		isRecording = !isRecording;
	}
	
	public void togglePlayer() {
		if(!isPlaying && isRecording) {
			isRecording = false;
		}
		
		isPlaying = !isPlaying;
	}
	
	public void saveConfig() throws IOException {	
		File file = new File(getName() + ".txt");
		BufferedWriter bw = new BufferedWriter(new FileWriter(file));
		
		file.createNewFile();

		int[] robotOffsets = getOffsets();
		bw.write(robotOffsets[0] + ":" + robotOffsets[1]);
		
		bw.close();
	}
	
	public void loadConfig(String robotName) throws FileNotFoundException, IOException {	
		File file = new File(robotName + ".txt");
		
		BufferedReader br = new BufferedReader(new FileReader(file));
		
		String[] config = br.readLine().split(":");
		br.close();
		
		offsets[0] = Integer.parseInt(config[0]);
		offsets[1] = Integer.parseInt(config[1]);
	}
	
	private void saveCommand(String cmd, int[] units) {	
		if(isRecording) {
			String args = "";
			for(int unit : units) {
				args += ":" + Integer.toString(unit);
			}
			
			commands.add(cmd + args);
		}
	}
	
	private void operateRobot(String cmd, boolean inverse) {
		String[] s = cmd.split(":");
		int[] c = new int[s.length];
		
		for(int i = 0; i < s.length; i++) {
			c[i] = Integer.parseInt(s[i]);
		}
		
		SetRelativeSpeed(Integer.parseInt(s[0]));
		
		if(inverse) {			
			c[0] = (c[0] == 1 ? 2 : c[0] == 2 ? 1 : c[0]);
		}
		
		switch(c[0]) {
			case 0:
				Reta(Integer.parseInt(s[1]), false);
				sleep(calculateMovementDelay(c[0], c[1]));
				break;
			case 1:	
				CurvarDireita(Integer.parseInt(s[1]), Integer.parseInt(s[2]), false);
				sleep(calculateMovementDelay(c[0], c[1], c[2]));
				break;
			case 2:
				CurvarEsquerda(Integer.parseInt(s[1]), Integer.parseInt(s[2]), false);
				sleep(calculateMovementDelay(c[0], c[1], c[2]));
				break;
			case 3:
				Parar(false);
				break;
		}
	}
	
	public void replay(boolean inverse) throws FileNotFoundException, IOException {
		loadPath();
		
		if(inverse) {
			CurvarEsquerda(180, 0, true);
		}
		
		for(String c : commands) {
			operateRobot(c, inverse);
		}
	}
	
	public void savePath() throws IOException {
		File file = new File(filePath);
			
		file.createNewFile();			
		FileWriter fw = new FileWriter(file);
		BufferedWriter bw = new BufferedWriter(fw);
		for(String cmd : commands) {
			bw.write(cmd + "\r\n");
		}
		bw.close();
	}
	
	private void loadPath() throws FileNotFoundException, IOException {
		File file = new File(filePath);
		commands.clear();
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		
		for(String cmd = br.readLine(); cmd != null; cmd = br.readLine()) {
			commands.add(cmd);
		}
		
		br.close();
	}	
	
	@Override
	public void Reta(int units, boolean stop) {
		saveCommand("0", new int[] { getRelativeSpeed(), units });
		super.Reta(units, stop);
	}
	
	@Override
	public void CurvarDireita(int radius, int angle, boolean stop) {
		saveCommand("1", new int[] { getRelativeSpeed(), radius, angle });
		super.CurvarDireita(radius, angle, stop);
	}
	
	@Override
	public void CurvarEsquerda(int radius, int angle, boolean stop) {
		saveCommand("2", new int[] { getRelativeSpeed(), radius, angle });
		super.CurvarEsquerda(radius, angle, stop);
	}
	
	@Override
	public void Parar(boolean trueStop) {
		saveCommand("3", new int[] { });
		super.Parar(trueStop);
	}
}