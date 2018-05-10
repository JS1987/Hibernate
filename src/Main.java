import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class Main {

	Session session;

	public static void main(String[] args) {
		Main main = new Main();
		//main.addNewData();
		//main.addTeachers();
		main.printSchools();
		main.executeQueries();
		//main.updatingObject();
		main.close();
	}

	public Main() {
		session = HibernateUtil.getSessionFactory().openSession();
	}

	public void close() {
		session.close();
		HibernateUtil.shutdown();
	}

	private void executeQueries() {
		Transaction transaction = session.beginTransaction();
		
		query0();
		query1();
		query2();
		query3();
		query4();
		query5();
		query6();
		transaction.commit();
	}

	private void query0() {
		String hql = "FROM School";
		Query query = session.createQuery(hql);
		List results = query.list();
		System.out.println(results);
	}

	private void query1() {
		String hql = "FROM School S WHERE S.name='UE'";
		Query query = session.createQuery(hql);
		List results = query.list();
		System.out.println(results);
	}

	private void query2() {
		String hql = "FROM School S WHERE S.name='UE'";
		Query query = session.createQuery(hql);
		List<School> results = query.list();
		Transaction transaction = session.beginTransaction();
		for (School s : results) {
			session.delete(s);
		}
		transaction.commit();
	}

	private void query3() {
		String hql = "SELECT COUNT(S) FROM School S";
		Query query = session.createQuery(hql);
		Integer schoolsCount = (Integer) query.uniqueResult();
		System.out.println("Schools count: " + schoolsCount);
	}

	private void query4() {
		String hql = "SELECT COUNT(S) FROM Student S";
		Query query = session.createQuery(hql);
		Integer schoolsCount = (Integer) query.uniqueResult();
		System.out.println("Students count: " + schoolsCount);
	}

	private void query5() {
		String hql = "SELECT COUNT(S) FROM School S WHERE size(S.classes)>=2";
		Query query = session.createQuery(hql);
		Integer schoolsCount = (Integer) query.uniqueResult();
		System.out.println("Schools count: " + schoolsCount);
	}

	private void query6() {
		String hql = "SELECT s FROM School s INNER JOIN s.classes classes WHERE classes.profile = 'mat-fiz' AND classes.currentYear>=2";
		Query query = session.createQuery(hql);
		List results = query.list();
		System.out.println(results);
	}

	private void updatingObject() {
		Query query = session.createQuery("from School where id= :id");
		query.setLong("id", 3);
		School school = (School) query.uniqueResult();
		school.setAddress("Krakow");
		Transaction transaction = session.beginTransaction();
		session.save(school);
		transaction.commit();
	}
	
	private void addNewData() {
		Set<Student> newStudents = new HashSet<Student>();
		Student newStudent = new Student();
		newStudent.setName("Adam");
		newStudent.setSurname("Barszcz");
		//newStudent.setPesel(841223451);
		newStudents.add(newStudent);	
		
		Set<Student> newStudents2 = new HashSet<Student>();
		Student newStudent2 = new Student();
		newStudent2.setName("Mikolaj");
		newStudent2.setSurname("Ziemba");
		//newStudent2.setPesel(847798123);
		newStudents2.add(newStudent2);	
		
		Set<Teacher> newTeachers = new HashSet<Teacher>();
		Teacher newTeacher = new Teacher();
		newTeacher.setName("Franciszek");
		newTeacher.setSurname("Smuta");
		newTeacher.setSubject("Biologia");
		newTeachers.add(newTeacher);
		
		School newSchool = new School();
		newSchool.setName("Uniwersytet Warszawski");
		newSchool.setAddress("ul. Kasztanowa 15, Warszawa");
		
		SchoolClass newClass = new SchoolClass();
		newClass.setProfile("mat-fiz");
		newClass.setStartYear(2015);
		newClass.setCurrentYear(2);
		newClass.setStudents(newStudents);
		//newClasses.add(newClass);
		
		//Student newStudent = new Student();
		newStudent.setName("Ja�");
		newStudent.setSurname("Tr�balski");
		newStudent.setPesel("12345678");
		
		//newClass.addStudent(newStudent);
	//	newSchool.addClass(newClass);
		
		Teacher newTeacher3 = new Teacher();
		newTeacher3.setName("Maksymilian");
		newTeacher3.setSurname("Jarosz");
		newTeacher3.setSubject("matematyka");
		//newTeacher3.setSchoolClass(newClass);
		session.save(newTeacher3);
		
		
		Transaction transaction = session.beginTransaction();
		session.save(newSchool);
		transaction.commit();
	}

/*	private void addTeachers() { //przeniesione do addNewData
		Teacher teacher = new Teacher();
		teacher.setName("Stanislaw");
		teacher.setSubject("jezyk polski");
		Teacher teacher2 = new Teacher();
		teacher2.setName("Wieslaw");
		teacher2.setSubject("Matematyka");
		
		SchoolClass schoolClass = new SchoolClass();
		schoolClass.setProfile("fiz-mat");
		SchoolClass schoolClass2 = new SchoolClass();
		schoolClass2.setProfile("biol-chem");
		Set<SchoolClass> schoolClasses = new HashSet<SchoolClass>();
		schoolClasses.add(schoolClass);
		schoolClasses.add(schoolClass2);
		
		teacher.setSchoolClasses(schoolClasses);
		teacher2.setSchoolClasses(schoolClasses);
		
		Transaction transaction = session.beginTransaction();
		session.save(teacher);
		session.save(teacher2);
		transaction.commit();
	}*/
	
	private void printSchools() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Criteria crit = session.createCriteria(School.class);
		List<School> schools = crit.list();

		System.out.println("### Schools and classes");
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy");
		for (School s : schools) {
			System.out.println(s);
			for (SchoolClass schoolClass : s.getClasses()) {
				System.out.println("   " + schoolClass);
				System.out.println("Students: ");
				for (Student student : schoolClass.getStudents()) {
					System.out.print("            " + student.getName());
					System.out.print(" " + student.getSurname());
					System.out.println(" (" + student.getPesel() + ")");
				}
			}
		}

		session.close();
		HibernateUtil.shutdown();
	}

	
	
	private void hibernateTest() {

		Session session = HibernateUtil.getSessionFactory().openSession();

		Criteria crit = session.createCriteria(School.class);
		List<School> schools = crit.list();

		for (School s : schools) {
			System.out.println(s.getName());
		}

		Criteria crit2 = session.createCriteria(SchoolClass.class);
		List<SchoolClass> classes = crit2.list();

		for (SchoolClass sClass : classes) {
			System.out.println(sClass.getProfile() + " " + sClass.getStartYear() + " " + sClass.getCurrentYear());
		}

		session.close();
		HibernateUtil.shutdown();

	}

	private void jdbcTest() {
		Connection conn = null;
		Statement stmt = null;
		try {
			// STEP 2: Register JDBC driver
			Class.forName("org.sqlite.JDBC");

			// STEP 3: Open a connection
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection("jdbc:sqlite:school.db", "", "");

			// STEP 4: Execute a query
			System.out.println("Creating statement...");
			stmt = conn.createStatement();
			String sql;
			sql = "SELECT * FROM schools";
			ResultSet rs = stmt.executeQuery(sql);

			// STEP 5: Extract data from result set
			while (rs.next()) {
				// Retrieve by column name
				String name = rs.getString("name");
				String address = rs.getString("address");

				// Display values
				System.out.println("Name: " + name);
				System.out.println(" address: " + address);
			}
			// STEP 6: Clean-up environment
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			} // nothing we can do
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			} // end finally try
		} // end try
		System.out.println("Goodbye!");
	}// end jdbcTest

}
