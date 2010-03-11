package com.packetnode.blackdossier.hibernate; 

import org.hibernate.SessionFactory; 
import org.hibernate.cfg.Configuration; 

public class HibernateManager 
{ 
    private static final SessionFactory sessionFactory = buildSessionFactory(); 

    private static SessionFactory buildSessionFactory() 
	{ 
        try 
		{ 
            // Create the SessionFactory from hibernate.cfg.xml 
            return new Configuration().configure().buildSessionFactory(); 
        } 
        catch (Throwable ex) 
		{ 
        	System.err.println("Initial SessionFactory creation failed." + ex); 
            throw new ExceptionInInitializerError(ex); 
		} 
   	} 
			    	
	public static SessionFactory getSessionFactory() 
	{ 
		return sessionFactory; 
	} 
} 
			