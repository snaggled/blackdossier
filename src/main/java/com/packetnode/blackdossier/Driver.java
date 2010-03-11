package com.packetnode.blackdossier;

import java.util.*;

import org.hibernate.Session;
import com.packetnode.blackdossier.hibernate.HibernateManager;

import com.packetnode.blackdossier.quote.Quote;
import com.packetnode.blackdossier.quote.QuoteFactory;
import com.packetnode.blackdossier.quote.QuoteFactoryException;
import com.packetnode.blackdossier.quote.QuoteFactoryYahooHistoricalImpl;
import com.packetnode.blackdossier.quote.QuoteFactoryYahooImpl;
import com.packetnode.blackdossier.quote.QuoteFactoryYahooRealtimeImpl;

public class Driver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Driver init");
		QuoteFactory qf = QuoteFactoryYahooImpl.getFactory();
	    Session session = null; 
	
		try 
		{
			Quote quote = qf.getQuote("OPWV");
			System.out.println(quote);
			
			List<Quote> quotes = qf.getQuotes(QuoteFactory.DJIA);
			Iterator<Quote> it = quotes.iterator();
			while (it.hasNext()) System.out.println(it.next());
			qf.finished();
			
			List<String> tickers = qf.getAllTickers();
			Iterator<String> tit = tickers.iterator();
			while (tit.hasNext())
			{
				String ticker = tit.next();
				quotes = qf.getAllQuotes(ticker);
				System.out.println("Found " + quotes.size() + " records for " + ticker);   
			}
		}
		catch (QuoteFactoryException e)
		{
			e.printStackTrace();
		}
	}

}
