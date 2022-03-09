package base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Folder implements Comparable<Folder>{
	private ArrayList<Note> notes;
	private String name;

	public Folder(String name){
		notes = new ArrayList<Note>();
		this.name = name;
	}

	public void addNote(Note note){
		notes.add(note);
	}

	public String getName(){
		return name;
	}

	public ArrayList <Note> getNotes(){
		return notes;
	}


	public String toString() {
		int nText = 0;
		int nImage = 0;

		for (Note note : this.getNotes()){
			if (note instanceof TextNote){
				nText++;
			}

			else{
				nImage++;
			}
		}

		return name + ":" + nText + ":" + nImage;
	}



	public int compareTo(Folder o){
		if(this.name.compareTo(o.name) > 0) {
			return 1;
		}
		else if(this.name.compareTo(o.name) < 0) {
			return -1;
		}
		else
			return 0;
	}
	
	public void sortNotes(){
		Collections.sort(this.notes); 
	}

	public List<Note> searchNotes(String keywords){
		List<Note> note = new ArrayList<Note>(); //result
		keywords = keywords.toUpperCase(); //solve case-sensitive
		
		String[] keyToFind = keywords.split(" "); //split all the keyword into an array.
		
		List<String> wordlist = new ArrayList<String>(); // empty array for key requirement.(specified "OR" case)
		
		int i = 0;
		
		//All keyword criteria
		while (i < keyToFind.length)
		{
			if (keyToFind[i+1].equals("OR"))
			{
				wordlist.add(keyToFind[i]+" "+keyToFind[i+2]); // OR
				i = i + 3;
			}
			else
			{
				wordlist.add(keyToFind[i]); // AND
				i = i + 1;
			}
		}
		
		//Searching
		for (Note temp : notes)
		{		
			boolean existWord = true;
			for(String word:wordlist) {
				//OR
				if(word.contains(" ")){
					String word1 = word.substring(0,word.indexOf(" ")); // "word1" OR word2
					String word2 = word.substring(word.indexOf(" ")+1); // word1 OR "word2"
					if(temp instanceof TextNote) {
						if(temp.getTitle().toUpperCase().contains(word1) == true || temp.getTitle().toUpperCase().contains(word2) == true 
								|| ((TextNote)temp).content.toUpperCase().contains(word1) == true || ((TextNote)temp).content.toUpperCase().contains(word2) == true){
							if(word.compareTo(wordlist.get(wordlist.size()-1)) == 0) 
								note.add(temp);		
						}
						else
							break;

					}
					else 
						if(temp.getTitle().toUpperCase().contains(word1) == true || temp.getTitle().toUpperCase().contains(word2) == true){
							if(word.compareTo(wordlist.get(wordlist.size()-1)) == 0)
								note.add(temp);
						}
						else
							break;

				}
				
				//AND
				else {
						if(temp instanceof TextNote) {
							if(temp.getTitle().toUpperCase( ).contains(word) == true || ((TextNote)temp).content.toUpperCase().contains(word) == true){
								if(word.compareTo(wordlist.get(wordlist.size()-1)) == 0)
									note.add(temp);
							}
							else
								break;

						}
						else {
							if(temp.getTitle().toUpperCase().contains(word) == true){
								if(word.compareTo(wordlist.get(wordlist.size()-1)) == 0)
									note.add(temp);
							}
							else
								break;
						}
				}
			}
		}
		return note;
	}
}
