package com.packetnode.blackdossier.quote;

import java.util.List;
import com.packetnode.blackdossier.hibernate.*;
import java.math.BigDecimal;


import org.hibernate.*;
import org.slf4j.Logger;

import com.packetnode.blackdossier.quote.*;
import java.util.*;

public class QuoteFactoryCachedYahooHistoricalImpl extends
		QuoteFactoryYahooHistoricalImpl implements QuoteFactory
{
	private QuoteFactory yahooRealtime = new QuoteFactoryYahooRealtimeImpl();
	private QuoteFactory yahooHistorical = new QuoteFactoryYahooHistoricalImpl();

	protected void store(List<Quote> quotes)
	{
		System.out.println("begin");
		Session session = HibernateManager.getSessionFactory()
				.getCurrentSession();

		session.beginTransaction();

		// 3 tranactions here:
		// a) select all the matching tickers in the db
		// b) if not selected in (a), save to db
		// c) select all again, calculate price changes

		// tbd - assuming these are all the same ticker - possibly not the case
		Query lookup = session.createQuery("from Quote q where q.ticker = '"
				+ quotes.listIterator().next().getTicker()
				+ "' order by event_date asc");
		List<Quote> results = lookup.list();
		// session.getTransaction().commit();

		// session = HibernateManager.getSessionFactory().getCurrentSession();
		// session.beginTransaction();

		Iterator it = quotes.iterator();
		while (it.hasNext())
		{
			Quote q = (Quote) it.next();
			if (!results.contains(q))
				session.save(q);
		}

		// session.getTransaction().commit();

		// session = HibernateManager.getSessionFactory().getCurrentSession();
		// session.beginTransaction();

		List<Quote> complete = lookup.list();
		Iterator<Quote> qit = complete.iterator();
		Quote previous = qit.next();
		while (qit.hasNext())
		{
			Quote current = qit.next();
			if (current.getPriceChange() == null)
			{
				current.setPriceChange(new BigDecimal(current.getAdjustedClose().doubleValue()
						- previous.getAdjustedClose().doubleValue()));
				
				current.setPercentChange(new BigDecimal((current.getPriceChange().doubleValue()/
						previous.getAdjustedClose().doubleValue())*100));
				
				session.save(current);
			}
			previous = current;
		}

		session.getTransaction().commit();
		System.out.println("commit");

	}
	
	public List<Quote> getQuotes(String ticker, int maxQuotes) throws QuoteFactoryException
	{
		// we should:
		// get the realtime quote, extra the latest date from it
		// get the latest record we have by date
		// if we don't have the latest, request all records between now and what
		// we have
		// create and store

		// pull all records and return
		Quote latest = yahooRealtime.getQuote(ticker);
		System.out.println("The latest realtime date is: "
				+ dateFormat.format(latest.getDate()));

		Session session = HibernateManager.getSessionFactory()
				.getCurrentSession();
		session.beginTransaction();

		Query q = session.createQuery("from Quote q where q.ticker = '"
				+ ticker + "' order by q.date desc");
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
			long diff = (latest.getDate().getTime() - result.getDate()
					.getTime())
					/ (1000 * 60 * 60 * 24);

			System.out.println("db data is " + diff + " days out of date");

			// yahoo weirdness - barfs at less than 7 days
			if (diff > 7)
			{
				System.out.println("requesting records from "
						+ result.getDate() + " to "
						+ dateFormat.format(latest.getDate()));
				// cache the records we are missing
				store(yahooHistorical.getQuotes(ticker, result.getDate(),
						latest.getDate()));
			} else
			{
				System.out
						.println("Will not fetch any existing data, using db only");
			}
		}

		session = HibernateManager.getSessionFactory().getCurrentSession();
		session.beginTransaction();

		q = session.createQuery("from Quote q where q.ticker = '" + ticker
				+ "' order by q.date asc");
		if (maxQuotes > 0) q.setMaxResults(maxQuotes);
		
		List<Quote> results = q.list();
		session.getTransaction().commit();
		return results;
	}

	public List<Quote> getAllQuotes(String ticker) throws QuoteFactoryException
	{
		return this.getQuotes(ticker, 0);
	}


}
