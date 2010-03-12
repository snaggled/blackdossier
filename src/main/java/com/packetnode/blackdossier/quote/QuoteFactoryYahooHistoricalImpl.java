package com.packetnode.blackdossier.quote;

import java.io.BufferedReader;
import java.math.BigDecimal;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;

public class QuoteFactoryYahooHistoricalImpl extends QuoteFactoryYahooImpl
		implements QuoteFactory
{
	static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	protected Quote quoteFromHistorical(String ticker, String historical)
	throws QuoteFactoryException
	{
		//Date,Open,High,Low,Close,Volume,Adj Close
		//2010-03-09,126.27,126.29,125.20,125.55,7528800,125.55

			// ugly, but quick
			final int DATE = 0;
			final int OPEN = 1;
			final int HIGH = 2;
			final int LOW = 3;
			final int CLOSE = 4;
			final int VOLUME = 5;
			final int ADJCLOSE = 6;

			String[] params = historical.split(",");

			BigDecimal close = new BigDecimal(params[CLOSE]);
			Date date = null;
			
			try
			{
				date = dateFormat.parse(trim(params[DATE]));
			} catch (ParseException e)
			{
				throw new QuoteFactoryException(e);
			}

			BigDecimal open = new BigDecimal(params[OPEN]);
			BigDecimal  high = new BigDecimal(params[HIGH]);
			BigDecimal low = new BigDecimal(params[LOW]);
			Long volume = new Long(params[VOLUME]);
			BigDecimal adjustedClose = new BigDecimal(params[ADJCLOSE]);
			return new Quote(trim(ticker), date, open, high, low, close, volume, adjustedClose);
		}
	
	public Quote getQuote(String ticker, Date date) throws QuoteFactoryException
	{
		throw new UnsupportedOperationException();	
	}
	
	public List<Quote> getQuotes(String ticker, Date startDate, Date stopDate) throws QuoteFactoryException
	{
		// yahoo appears to barf if we request less than 7 days worth of data
		
		// http://ichart.finance.yahoo.com/table.csv?s=MO&a=00&b=2&c=1970&d=02&e=11&f=2010&g=d&ignore=.csv
		StringBuffer sb = new StringBuffer(
				"http://ichart.finance.yahoo.com/table.csv?s=");
		Calendar cal = Calendar.getInstance();
		sb.append(ticker);

		// startDate
		cal.setTime(startDate);
				
		sb.append("&a=");
		sb.append(cal.get(Calendar.DAY_OF_MONTH));
		sb.append("&b=");
		sb.append(cal.get(Calendar.MONTH));
		sb.append("&c=");
		// hack - we'll get a years worth to be sure
		sb.append(cal.get(Calendar.YEAR) -1);
		
		/// stopDate
		cal.setTime(stopDate);
		sb.append("&d=");
		sb.append(cal.get(Calendar.DAY_OF_MONTH));
		sb.append("&e=");
		sb.append(cal.get(Calendar.MONTH));
		sb.append("&f=");
		sb.append(cal.get(Calendar.YEAR));
		sb.append("&g=d&ignore=.csv");
		
		String request = sb.toString();

		System.out.println(request);
		String response = null;
		ArrayList<Quote> quotes = new ArrayList<Quote>();

		try
		{
			HttpResponse httpResponse = getRequest(request);
			InputStream instream = httpResponse.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					instream));

			// ignore the first one
			reader.readLine();
			
			while ((response = reader.readLine()) != null)
				quotes.add(quoteFromHistorical(ticker, response));
			reader.close();
		} 
		catch (IOException e)
		{
			throw new QuoteFactoryException(e);
		}

		return quotes;
	}
	
	public List<Quote> getAllQuotes(String ticker) throws QuoteFactoryException
	{
		// http://ichart.finance.yahoo.com/table.csv?s=MO&a=00&b=2&c=1970&d=02&e=11&f=2010&g=d&ignore=.csv
		StringBuffer sb = new StringBuffer(
				"http://ichart.finance.yahoo.com/table.csv?s=");
		sb.append(ticker);
		sb.append("&a=00&b=2&c=1970&d=02&e=11&f=2010&g=d&ignore=.csv");
		String request = sb.toString();

		System.out.println(request);
		String response = null;
		ArrayList<Quote> quotes = new ArrayList<Quote>();

		try
		{
			HttpResponse httpResponse = getRequest(request);
			InputStream instream = httpResponse.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					instream));

			// ignore the first one
			reader.readLine();
			
			while ((response = reader.readLine()) != null)
				quotes.add(quoteFromHistorical(ticker, response));
			reader.close();
		} 
		catch (IOException e)
		{
			throw new QuoteFactoryException(e);
		}

		return quotes;
	}
}
