package base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.Serializable;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class NoteBook implements Serializable {

	private ArrayList<Folder> folders;	
	private static final long serialVersionUID = 1L;
	
	public NoteBook(){
		folders = new ArrayList<Folder>();
	}

	public boolean createTextNote(String folderName, String title) {
		TextNote note = new TextNote(title);
		return insertNote(folderName, note);
	}

	public boolean createTextNote(String folderName, String title, String content){
		TextNote note = new TextNote(title, content);
		return insertNote(folderName, note);
	}

	public boolean createImageNote(String folderName, String title) {
		ImageNote note = new ImageNote(title);
		return insertNote(folderName, note);
	}

	public ArrayList<Folder> getFolders() {
		return folders;
	}

	public boolean insertNote(String folderName, Note note) {
		Folder f = null;
		boolean overlap = false;
		for(Folder f1:folders) {
			if( folderName == f1.getName()) { //Folder already exists
				f = f1;
				overlap = true;
				break;
			}
		}
		if(overlap == false) {
			f = new Folder(folderName);
			folders.add(f);
		}

		
		for(Note n:f.getNotes()) {
			if(n.equals(note)) {
				System.out.println("Creating note " + note.getTitle() + " under folder " + folderName + " failed");
				return false;
			}
		}
		f.addNote(note);
		return true;
	}

	public void sortFolders(){
		for (Folder f : this.folders) {
			f.sortNotes(); 
		}
		Collections.sort(this.folders); 

	}

	public List<Note> searchNotes(String keywords){
		List<Note> notes = new ArrayList<Note>();
		for (Folder f : folders) {
			List<Note> result = f.searchNotes(keywords);
			if(!result.isEmpty()){ 
				notes.addAll(result); 
			}
		}
		return notes;
	}
	
	public boolean save(String file) {
		FileOutputStream fos = null;
		ObjectOutputStream out =null;
		try {
			fos = new FileOutputStream("file.ser");
			out = new ObjectOutputStream(fos);
			NoteBook n = this;
			out.writeObject(n);
			out.close();
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public NoteBook(String file) {
		FileInputStream fis = null;
		ObjectInputStream in = null;
		try {
			fis = new FileInputStream("file.ser");
			in = new ObjectInputStream(fis);
			NoteBook n = (NoteBook) in.readObject();
			folders = n.getFolders();
			in.close();
		}catch(Exception e) {
			e.printStackTrace();
			}
	}
	
}