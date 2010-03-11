package com.packetnode.blackdossier.quote;

import java.util.List;
import com.packetnode.blackdossier.hibernate.*;

import org.hibernate.*;
import com.packetnode.blackdossier.quote.*;
import java.util.*;

public class QuoteFactoryCachedYahooHistoricalImpl extends QuoteFactoryYahooHistoricalImpl 
implements QuoteFactory 
{
	private QuoteFactory yahooRealtime = new QuoteFactoryYahooRealtimeImpl();
	private QuoteFactory yahooHistorical = new QuoteFactoryYahooHistoricalImpl();
	
	protected void store(List<Quote> quotes)
	{
		Session session = HibernateManager.getSessionFactory().getCurrentSession(); 
	    session.beginTransaction(); 

		Iterator it = quotes.iterator();
		while (it.hasNext()) 
		{
			Quote q = (Quote) it.next();
			session.save(q);
		}
		System.out.println("commit");
	    session.getTransaction().commit(); 
	}
	
	public List<Quote> getAllQuotes(String ticker) 
	throws QuoteFactoryException
	{	
		// we should:
		// get the realtime quote, extra the latest date from it
		// get the latest record we have by date
		// if we don't have the latest, request all records between now and what we have
		// create and store
		
		// pull all records and return
		Quote latest = yahooRealtime.getQuote(ticker);
        System.out.println("The latest realtime date is: " + dateFormat.format(latest.getDate()));

		Session session = HibernateManager.getSessionFactory().getCurrentSession(); 
        session.beginTransaction(); 
        
		Query q = session.createQuery("from Quote q where q.ticker = '" + ticker + "' order by q.date desc");
		q.setMaxResults(1);
        Quote result = (Quote) q.uniqueResult();
        session.getTransaction().commit(); 
       
        // nothing in the db means we might as well fetch the lot
        if (result == null)
        {
        	System.out.println("Theres nothing in the db for this");
    		store(yahooHistorical.getAllQuotes(ticker));
        }
        else
        {
            System.out.println("The latest in the db is: " + result.getDate());
            if (result.getDate().before(latest.getDate()))
            {
            	System.out.println("fetching records from " + result.getDate() + " to " + latest.getDate());
            	// cache the records we are missing
            }
            else
            {
            	System.out.println("db appears to be upto date");
            }
        }
        
		session = HibernateManager.getSessionFactory().getCurrentSession(); 
        session.beginTransaction(); 
        
		q = session.createQuery("from Quote q where q.ticker = '" + ticker + "' order by q.date desc");
        List<Quote> results = q.list();
        session.getTransaction().commit(); 	
        return results;
	}

}
