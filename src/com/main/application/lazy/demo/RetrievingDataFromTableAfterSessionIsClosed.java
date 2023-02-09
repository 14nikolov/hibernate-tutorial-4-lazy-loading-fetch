// LINE 67 - 79 CODE DOES NOT WORK, THROWS EXCEPTION
// LINE 67 - 79 CODE DOES NOT WORK, THROWS EXCEPTION
// LINE 67 - 79 CODE DOES NOT WORK, THROWS EXCEPTION
// LINE 67 - 79 CODE DOES NOT WORK, THROWS EXCEPTION
// LINE 67 - 79 CODE DOES NOT WORK, THROWS EXCEPTION
// LINE 67 - 79 CODE DOES NOT WORK, THROWS EXCEPTION
// LINE 67 - 79 CODE DOES NOT WORK, THROWS EXCEPTION
// LINE 67 - 79 CODE DOES NOT WORK, THROWS EXCEPTION
// LINE 67 - 79 CODE DOES NOT WORK, THROWS EXCEPTION

package com.main.application.lazy.demo;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import com.tables.entities.Course;
import com.tables.entities.Instructor;
import com.tables.entities.InstructorDetail;

public class RetrievingDataFromTableAfterSessionIsClosed {

	public static void main(String[] args) {
		SessionFactory sessionFactory = new Configuration().
				configure("hibernate.cfg.xml").
				addAnnotatedClass(Instructor.class).
				addAnnotatedClass(InstructorDetail.class).
				addAnnotatedClass(Course.class).
				buildSessionFactory();
		
		Session session = sessionFactory.getCurrentSession();
		
		session.beginTransaction();
		
		try {
			
			// When we execute this line only the Instructor Object/Table Row
			// will be retrieved/loaded, without his child elements (in this case - "courses") 
			// Unless we explicitly request them,
			// because the Instructor Class' variable "courses"
			// has it's Fetch/Loading Type set to LAZY
			// Retrieve Instructor from Database Table
			int theId = 15;
			Instructor instructor = session.get(Instructor.class, theId);
			
			// print the Instructor toString method
			System.out.println("\n\n\nluv2code: INSTRUCTOR:\n" + instructor.toString() + "\n\n\n");
			// print total number of Instructor Courses
			
			// BREAKING THE FLOW OF THE PROGRAM ON PURPOSE
				// Here we are closing the session before
				// retrieving Instructor's Courses.
				// Then down below, we will try to retrieve Instructor Courses,
				// which will result in an Exception, since there is no open session
			// WHY ARE WE BREAKING THE FLOW?
				// in order to showcase what do we do, if we 
				// need to retrieve Parent's Child Elements
				// when the session has ended
			session.getTransaction().commit();
			session.close();
			
			// SOLUTION:
			// start a new session
			// begin a new transaction 
			// create an HQL Query (Hibernate Fetch Strategy)
			
			session = sessionFactory.getCurrentSession();
			
			session.beginTransaction();
		
			// specifying again which course we want to work with
			int id = 15;
			
			Query<Course> query = session.createNativeQuery("select * from Course c "
					+ "where c.instructor_id=:theInstructorId", Course.class);
					 
			query.setParameter("theInstructorId", id);
					 
			List<Course> tempCourses = query.getResultList();
					 
			System.out.println("tempCourses: " + tempCourses);
			
			session.getTransaction().commit();
			
		}catch(Exception e) {
			System.out.println("\n\n\nTransaction Failed");
			System.out.println("Closing Session and SessionFactory\n\n\n");
			session.close();
			sessionFactory.close();
			e.printStackTrace();
		}
	}
	
}
