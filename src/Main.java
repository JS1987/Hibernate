import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.lang.Long;

public class Main {

	Session session;

	public static void main(String[] args) {
		Main main = new Main();
		//main.addNewData();
		main.addTeachers();
		//main.executeQueries();
		//main.updateSchool();
		main.printSchools();
		main.close();
	}

	public Main() {
		session = HibernateUtil.getSessionFactory().openSession();
	}

	public void close() {
		session.close();
		HibernateUtil.shutdown();
	}

	private void printSchools() {
		Criteria crit = session.createCriteria(School.class);
		List<School> schools = crit.list();

		System.out.println("### Schools and classes");
		for (School s : schools) {
			System.out.println(s);
			List<SchoolClass> schoolClasses = new ArrayList<SchoolClass>(s.getSchoolClasses());
			for (SchoolClass sc : schoolClasses) {
				System.out.println(sc);
				System.out.println("\t> Students:");
				List<Student> students = new ArrayList<Student>(sc.getStudents());
				for (Student st : students) {
					System.out.println(st);
				}
			}
		}
	}
	
	private void addTeachers() {
		Teacher teacher = new Teacher();
		teacher.setName("W³adek");
		teacher.setSubject("Biochemistry");
		Teacher teacher2 = new Teacher();
		teacher2.setName("Romek");
		teacher2.setSubject("Engineering");
		
		SchoolClass schoolClass = new SchoolClass();
		schoolClass.setProfile("whatever");
		SchoolClass schoolClass2 = new SchoolClass();
		schoolClass2.setProfile("whatever2");
		Set<SchoolClass> schoolClasses = new HashSet<SchoolClass>();
		schoolClasses.add(schoolClass);
		schoolClasses.add(schoolClass2);
		
		teacher.setSchoolClasses(schoolClasses);
		teacher2.setSchoolClasses(schoolClasses);
		
		Transaction transaction = session.beginTransaction();
		session.save(teacher);
		session.save(teacher2);
		transaction.commit();
	}
	
	private void addNewData() {
		School school = new School();
		school.setName("UJ");
		school.setAddress("ul. Gronostajowa 2");
		
		SchoolClass schoolClass1 = new SchoolClass();
		SchoolClass schoolClass2 = new SchoolClass();
		schoolClass1.setProfile("biol-chem");
		schoolClass1.setStartYear(1999);
		schoolClass1.setCurrentYear(15);
		schoolClass2.setProfile("europejski");
		schoolClass2.setStartYear(2006);
		schoolClass2.setCurrentYear(4);
		
		Student student1 = new Student();
		Student student2 = new Student();
		Student student3 = new Student();
		Student student4 = new Student();
		student1.setName("Andrzej");
		student1.setSurname("Jakiœ");
		student1.setPesel("12345678901");
		student2.setName("Zbigniew");
		student2.setSurname("Jarzyna");
		student2.setPesel("98765432109");
		student3.setName("Magdalena");
		student3.setSurname("Nowak");
		student3.setPesel("11111111111");
		student4.setName("Nikola");
		student4.setSurname("Jakaœ");
		student4.setPesel("99999999999");
		
		HashSet<Student> students1 = new HashSet<Student>();
		students1.add(student1);
		students1.add(student2);
		HashSet<Student> students2 = new HashSet<Student>();
		students2.add(student3);
		students2.add(student4);
		schoolClass1.setStudents(students1);
		schoolClass2.setStudents(students2);
		
		HashSet<SchoolClass> schoolClasses = new HashSet<SchoolClass>();
		schoolClasses.add(schoolClass1);
		schoolClasses.add(schoolClass2);
		school.setSchoolClasses(schoolClasses);
		
		Transaction transaction = session.beginTransaction();
		session.save(school);
		transaction.commit();
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
	
	private void updateSchool() {
		Transaction transaction = session.beginTransaction();

		Query query = session.createQuery("UPDATE School s set s.address = :address WHERE s.id= :id").setString("address","ul. ZmienionaZmieniona").setLong("id", 3);
		query.executeUpdate();
		
		transaction.commit();
	}
	
	private void query0() {
		String hql = "FROM School";
		Query query = session.createQuery(hql);
		List results = query.list();
		System.out.println(results);
	}
	
	private void query1() {
		String hql = "FROM School WHERE name='AE'";
        Query query = session.createQuery(hql);
        List results = query.list();
        System.out.println(results);
	}
	
	private void query2() {
        String hql = "SELECT id FROM School WHERE name='AE'";
        Query query = session.createQuery(hql);
        List<Long> results = query.list();
        for (Long id : results) {
        		System.out.println(id);
        		School school = (School)session.load(School.class, id);
        		System.out.println(school);
        		session.delete(school);
		}
	}
	
	private void query3() {
		String hql = "SELECT COUNT(*) FROM School)";
		Query query = session.createQuery(hql);
		List results = query.list();
		System.out.println(results);
	}
	
	private void query4() {
		String hql = "SELECT COUNT(*) FROM Student";
		Query query = session.createQuery(hql);
		List results = query.list();
		System.out.println(results);
	}
	
	private void query5() {
		String hql = "FROM School s WHERE s.schoolClasses.size>=2";
		Query query = session.createQuery(hql);
		List results = query.list();
		System.out.println(results);
	}
	
	private void query6() {
		String hql ="SELECT s FROM School s INNER JOIN s.schoolClasses schoolClasses WHERE schoolClasses.profile = 'mat-fiz' AND schoolClasses.currentYear>=2";
		Query query = session.createQuery(hql);
		List results = query.list();
		System.out.println(results);
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