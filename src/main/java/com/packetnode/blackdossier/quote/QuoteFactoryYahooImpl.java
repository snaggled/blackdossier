package com.packetnode.blackdossier.quote;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class QuoteFactoryYahooImpl implements QuoteFactory
{
	protected HttpClient httpClient;
	
	private List<QuoteFactory> factories = new ArrayList<QuoteFactory>();
	private static QuoteFactory singletonFactory = null;
	
	public static QuoteFactory getFactory()
	{
		if (singletonFactory == null)
		{	
			singletonFactory = new QuoteFactoryYahooImpl(true);
		}
		return singletonFactory;
	}
	
	protected QuoteFactoryYahooImpl()
	{		
		httpClient = new DefaultHttpClient();
	}
	
	protected QuoteFactoryYahooImpl(boolean withFactories)
	{
		this();
		// the order actually matters, which probably breaks the 
		// design pattern
		if (withFactories)
		{
			addFactory(new QuoteFactoryCachedYahooHistoricalImpl());
			addFactory(new QuoteFactoryYahooHistoricalImpl());
			addFactory(new QuoteFactoryYahooRealtimeImpl());
		}
	}	

	// we should really have a delete too
	protected void addFactory(QuoteFactory factory)
	{
		this.factories.add(factory);
	}

	protected String trim(String value)
	{
		if (value == null)
			return value;

		value = value.trim();
		if (value.startsWith("\"") && value.endsWith("\""))
			return value.substring(1, value.length() - 1);

		return value;
	}
	
	protected HttpResponse getRequest(String requestString)
	throws QuoteFactoryException
	{
		HttpGet get = new HttpGet(requestString);
		HttpResponse response = null;
		try
		{
			response = httpClient.execute(get);
		} 
		catch (ClientProtocolException e)
		{
			get.abort();
			throw new QuoteFactoryException(e);
		} 
		catch (IOException e)
		{
			get.abort();
			throw new QuoteFactoryException(e);
		} 
		catch (RuntimeException e)
		{
			get.abort();
			throw new QuoteFactoryException(e);
		} 
		finally
		{
			//httpClient.getConnectionManager().shutdown();
		}
		return response;
	}
	
	// might move this into a destructor or something
	public void finished()
	{
		httpClient.getConnectionManager().shutdown();
	}

	public List<String> getAllTickers()
	throws QuoteFactoryException
	{
		return Arrays.asList(QuoteFactory.DJIA);
	}
	
	public List<Quote> getAllQuotes(String ticker) throws QuoteFactoryException
	{
		for (QuoteFactory f : this.factories)
		{
			try
			{
				return f.getAllQuotes(ticker);
			}
			catch (UnsupportedOperationException e)
			{	
			}
		}
		throw new UnsupportedOperationException();
	}

	public Quote getQuote(String ticker) throws QuoteFactoryException
	{
		for (QuoteFactory f : this.factories)
		{
			try
			{
				return f.getQuote(ticker);
			}
			catch (UnsupportedOperationException e)
			{	
			}
		}
		throw new UnsupportedOperationException();
	}

	public Quote getQuote(String ticker, Date date)
			throws QuoteFactoryException
	{
		for (QuoteFactory f : this.factories)
		{
			try
			{
				return f.getQuote(ticker, date);
			}
			catch (UnsupportedOperationException e)
			{	
			}
		}
		throw new UnsupportedOperationException();	
	}

	public List<Quote> getQuotes(String ticker, Date startDate, Date stopDate)
			throws QuoteFactoryException
	{
		for (QuoteFactory f : this.factories)
		{
			try
			{
				return f.getQuotes(ticker, startDate, stopDate);
			}
			catch (UnsupportedOperationException e)
			{	
			}
		}
		throw new UnsupportedOperationException();	
	}

	public List<Quote> getQuotes(String[] tickers) throws QuoteFactoryException
	{
		for (QuoteFactory f : this.factories)
		{
			try
			{
				return f.getQuotes(tickers);
			}
			catch (UnsupportedOperationException e)
			{	
			}
		}
		throw new UnsupportedOperationException();	
	}

	public List<String> getTickers(String regex) throws QuoteFactoryException
	{
		for (QuoteFactory f : this.factories)
		{
			try
			{
				return f.getTickers(regex);
			}
			catch (UnsupportedOperationException e)
			{	
			}
		}
		throw new UnsupportedOperationException();	
	}
}
