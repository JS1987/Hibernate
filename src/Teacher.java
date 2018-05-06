import java.util.Set;

public class Teacher {

	private long id;
	private String name;
	private String surname;
	private String pesel;
	private String subject;
	private Set<SchoolClass> schoolClasses;
	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getPesel() {
		return pesel;
	}

	public void setPesel(String pesel) {
		this.pesel = pesel;
	}
	
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public Set<SchoolClass> getSchoolClasses() {
		return schoolClasses;
	}
	
	public void setSchoolClasses(Set<SchoolClass> schoolClasses) {
		this.schoolClasses = schoolClasses;
	}

	public String toString() {
		return "Teacher " + name + " (Subject: "+ subject +")";
	}
}
