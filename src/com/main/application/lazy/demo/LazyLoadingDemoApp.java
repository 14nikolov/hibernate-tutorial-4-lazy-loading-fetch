package com.main.application.lazy.demo;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.tables.entities.Course;
import com.tables.entities.Instructor;
import com.tables.entities.InstructorDetail;

public class LazyLoadingDemoApp {

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
			System.out.println(" \n\n\nluv2code:INSTRUCTOR NUMBER OF COURSES: " + instructor.getCourses().size());
			// print a list of all of the courses of this Instructor
			// we will print the courses' toString methods
			System.out.println("\n\n\nluv2code: INSTRUCTOR COURSES: " + instructor.getCourses());
			
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
