package com.packetnode.blackdossier.quote;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

			Float close = new Float(params[CLOSE]);
			Date date = null;
			
			try
			{
				date = dateFormat.parse(trim(params[DATE]));
			} catch (ParseException e)
			{
				throw new QuoteFactoryException(e);
			}

			Float open = new Float(params[OPEN]);
			Float  high = new Float(params[HIGH]);
			Float low = new Float(params[LOW]);
			Long volume = new Long(params[VOLUME]);
			Float adjustedClose = new Float(params[ADJCLOSE]);
			return new Quote(trim(ticker), date, open, high, low, close, volume, adjustedClose);
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
