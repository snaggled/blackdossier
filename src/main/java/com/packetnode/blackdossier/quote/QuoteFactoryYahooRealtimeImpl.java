package com.packetnode.blackdossier.quote;

import java.io.*;
import java.text.ParseException;
import java.util.*;
import org.apache.http.client.*;
import org.apache.http.impl.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.*;

//http://www.gummy-stuff.org/Yahoo-data.htm
public class QuoteFactoryYahooRealtimeImpl extends QuoteFactoryYahooImpl
implements QuoteFactory
{		
	protected Quote quoteFromRealtime(String realtime)
	throws QuoteFactoryException
	{
		// "MO",20.82,"3/10/2010","4:00pm",+0.05,20.80,20.86,20.70,15479673
		// ticker, price, date, last trade, change, open, high, low, volume

			// ugly, but quick
			final int TICKER = 0;
			final int PRICE = 1;
			final int DATE = 2;
			final int LASTTRADE = 3;
			final int CHANGE = 4;
			final int OPEN = 5;
			final int HIGH = 6;
			final int LOW = 7;
			final int VOLUME = 8;

			String[] params = realtime.split(",");

			String ticker = trim(params[TICKER]);
			Float price = new Float(params[PRICE]);
			Date date = null;
			
			try
			{
				date = Quote.dateFormat.parse(trim(params[DATE]));
			} catch (ParseException e)
			{
				throw new QuoteFactoryException(e);
			}

			// date = new Date(params[DATE]);
			String lastTrade = trim(params[LASTTRADE]);
			Float change = new Float(params[CHANGE]);
			Float open = new Float(params[OPEN]);
			Float  high = new Float(params[HIGH]);
			Float low = new Float(params[LOW]);
			Long volume = new Long(params[VOLUME]);
			return new Quote(ticker, price, date, change, open, high, low, volume);
	}
	
	public Quote getQuote(String ticker)
	throws QuoteFactoryException
	{	
		StringBuffer sb = new StringBuffer("http://download.finance.yahoo.com/d/quotes.csv?s=");
		sb.append(ticker);
		sb.append("&f=sl1d1t1c1ohgv&e=.csv");
		String request = sb.toString();
		String response = null;
		
		try
		{
			HttpResponse httpResponse = getRequest(request);
			InputStream instream = httpResponse.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(instream));
			response = reader.readLine();
			reader.close();
		}
		catch (IOException e)
		{
			throw new QuoteFactoryException(e);
		}
		
		Quote quote = quoteFromRealtime(response);
		return quote;
	}
	
	public List<Quote> getQuotes(String[] tickers) throws QuoteFactoryException
	{
		StringBuffer sb = new StringBuffer("http://download.finance.yahoo.com/d/quotes.csv?s=");
		for (int i = 0; i < tickers.length ; i++)
		{
			sb.append(tickers[i]);
			sb.append(",");
		}
		
		sb.append("&f=sl1d1t1c1ohgv&e=.csv");
		String request = sb.toString();
		System.out.println(request);
		String response = null;
		ArrayList<Quote> quotes = new ArrayList<Quote>();
		
		try
		{
			HttpResponse httpResponse = getRequest(request);
			InputStream instream = httpResponse.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(instream));
			
			while ((response = reader.readLine()) != null)
				quotes.add(quoteFromRealtime(response));
			reader.close();
		}
		catch (IOException e)
		{
			throw new QuoteFactoryException(e);
		}
		
		return quotes;
	}
}
