package base;

import java.util.Date;

public class Note implements Comparable<Note>{
	/** parameter in superclass*/
	private Date date;
	private String title;

	public Note(String title){
		this.title = title;
		this.date = new Date(System.currentTimeMillis());
	}

	public String getTitle(){
		return title;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		Note other = (Note) obj;
		if (!title.equals(other.title))
			return false;
		return true;
	}


	public int compareTo(Note o){
		if(this.date.compareTo(o.date) > 0)
			return -1;
		else if(this.date.compareTo(o.date) < 0)
			return 1;
		else
			return 0;
	}

	public String toString(){
		return date.toString() + "\t" + title;
	}
}