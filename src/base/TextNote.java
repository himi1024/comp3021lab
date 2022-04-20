package base;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;

public class TextNote extends Note {
	public String content;

	public TextNote(String title){
		super(title);
	}

	public TextNote(String title, String content){
		super(title);
		this.content = content;
	}
	
	public TextNote(File f) {
		super(f.getName());
		this.content = getTextFromFile(f.getAbsolutePath());
	}
	
	
	public String getTextFromFile(String absolutePath) {
		String line;
		String result = "";
		InputStreamReader isr = null;
		BufferedReader br = null;
		try {
			isr = new InputStreamReader(new FileInputStream(absolutePath));
			br = new BufferedReader(isr);
			while((line = br.readLine()) != null){
				result += line + '\n';
			}
			br.close();
			isr.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public void exportTextToFile(String pathFolder) {	
		if(pathFolder.isEmpty() == true)
			pathFolder = ".";
		File file = new File(pathFolder + File.separator + this.getTitle().replaceAll(" ", "_") + ".txt");
		BufferedWriter bw = null;
		FileWriter fw = null;
		try {
			fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();
			fw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void setContent(String text) {
		// TODO Auto-generated method stub
		this.content = text;
	}

	public String getContent() {
		// TODO Auto-generated method stub
		return content;
	}
}