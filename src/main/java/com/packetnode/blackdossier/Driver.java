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
import com.packetnode.blackdossier.probability.*;

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
			String ticker = qf.getTicker("AA");
			Quote quote = qf.getQuote(ticker);
			List<Quote> bt = qf.getQuotes(ticker, 3);
			System.out.println("Found " + bt.size() + " records for " + ticker);   
			
			double[] values = new double[bt.size()];
			int i = 0;
			Iterator<Quote> it = bt.iterator();
			while (it.hasNext())
			{
				Quote q = it.next();
				values[i++] = q.getAdjustedClose().doubleValue();
			}
			GaussianMixtureDistribution dg = new GaussianMixtureDistribution(3, values);
			System.out.println(values.length);
			System.out.println(dg);
			System.out.println(dg.probability(.891));
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
