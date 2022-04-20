package base;

import java.io.Serializable;
/** allow the use of ArrayList*/
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.List;

/** a class with its own name and an ArrayList of Note object*/
public class Folder implements Comparable<Folder>,Serializable{
	private ArrayList<Note> notes;
	private String name;

	/** construct a Folder object with name and
	 *  in process create a new empty ArrayList of Note*/
	public Folder(String name){
		notes = new ArrayList<Note>();
		this.name = name;
	}

	/** add a Note object to Folder's ArrayList*/
	public void addNote(Note note){
		notes.add(note);
	}

	/** return caller's (Folder) name*/
	public String getName(){
		return name;
	}

	/** return caller's (Folder) ArrayList of Note*/
	public ArrayList <Note> getNotes(){
		return notes;
	}

	/** hashCode note placeholder*/
	@Override
	public int hashCode(){
		return Objects.hash(name);
	}

	/** toString overriding method modified from Source based on lab notes,
	 *   count number of TextNote and ImageNote in Folder,
	 *   then print those number along with folder name*/
	@Override
	public String toString() {
		int nText = 0;
		int nImage = 0;
		// TODO

		for (Note note : this.getNotes()){
			/** instanceof is for finding out if note is TextNote subclass or not*/
			if (note instanceof TextNote){
				nText++;
			}

			else{
				nImage++;
			}
		}

		return name + ":" + nText + ":" + nImage;
	}

	/** equals overriding method from Source*/
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Folder other = (Folder) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	/** return name comparison of notes (String.compareTo())*/
	@Override
	public int compareTo(Folder o){
		return this.getName().compareTo(o.getName());
	}
	/** sort notes with time order, since notes created at same time, resulting with different than sample output*/
	public void sortNotes(){
		Collections.sort(this.notes); // type casting the array list to list, changes made to array list
	}

	/** search notes that has keywords meeting requirements*/
	public List<Note> searchNotes(String keywords){
		List<Note> notes = new ArrayList<Note>(); // list of notes has keywords requirement
		String insenKeyword = keywords.toLowerCase(); // case insensitive keyword
		String[] testArray = insenKeyword.split(" "); // split keyword with space

		ArrayList<String> target = new ArrayList<String>(); // for initialization, whole list is AND
		// which an element is separated by or, in other words, enforcing key1 AND ( key2 OR key3 ) AND key4
		int j = 0;

		boolean lab3 = false; // lab 3 which has or keyword
		for(String i : testArray){
			if (i.equals("or")){ // keyword has or
				lab3 = true; // next step do one in lab3
			}
		}
		if(lab3){ // have or keyword
			for (String i : testArray){
				if (i.equals("or")){ // when meeting or (operation) in keywords
					target.add(testArray[j - 1] + " " + testArray[j + 1]); // put "or" key in a element
				}// key OR key
				++j; // go to another element, representing AND (element AND element)

			}
		}
		else{ // no or keyword
			for (String i : testArray){
				target.add(i); // add all string directly
			}

		}

		for(Note e : this.notes){ // go through all notes
			boolean set = false; // default: not set note as list's element
			for(String i : target){ // go through all AND
				String[] pair = i.split(" "); // split pair of keys
				for(String half : pair){ // for a key in OR
					if(e instanceof TextNote){ // TextNote
						if(e.getTitle().toLowerCase().indexOf(half)!= -1 ||
								((TextNote)e).content.toLowerCase().indexOf(half)!= -1 ){ // find key in title and content
							set = true; // found key, can set note as result
							break; // go to another pair separated by AND
						}
					}
					else{ // ImageNote
						if(e.getTitle().toLowerCase().indexOf(half)!= -1){ // just find keyword in title
							set = true;
							break;
						}
					}
					set = false; // this half not in note, set back to false
				}

			}
			if(set){ // can set note as result
				notes.add(e); // add that note to result
			}
			set = false; // back to default
		}
		return notes; // return result
	}

	public boolean removeNotes(String currentNote) {
		// TODO Auto-generated method stub
			for (int i = 0; i < notes.size();i++) {
				if (notes.get(i).getTitle().equals(currentNote)) {
					notes.remove(i);
					return true;
				}
			}
		return false;
	}
	

}