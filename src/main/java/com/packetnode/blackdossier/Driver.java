package com.packetnode.blackdossier;

import java.util.*;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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
			//String ticker = qf.getTicker("^FTSE");
			//Quote quote = qf.getQuote(ticker);
			//List<Quote> bt = qf.getAllQuotes(ticker);
			//System.out.println("Found " + bt.size() + " records for " + ticker);   

			List<String> tickers = qf.getAllTickers();
			Iterator<String> tit = tickers.iterator();
			while (tit.hasNext())
			{
				String ticker = qf.getTicker(tit.next());
				List<Quote>quotes = qf.getAllQuotes(ticker);
				System.out.println("Found " + quotes.size() + " records for " + ticker);   
			}
		}
		catch (QuoteFactoryException e)
		{
			e.printStackTrace();
		} 
		finally
		{
			qf.finished();
		}
	}

}
