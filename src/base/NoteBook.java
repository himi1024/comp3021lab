package base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class NoteBook {
	private ArrayList<Folder> folders;

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
}