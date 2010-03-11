package com.packetnode.blackdossier.quote;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public abstract class QuoteFactoryYahooImpl implements QuoteFactory
{
	protected HttpClient httpClient;

	public QuoteFactoryYahooImpl()
	{		
		httpClient = new DefaultHttpClient();
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
		throw new UnsupportedOperationException();
	}

	public Quote getQuote(String ticker) throws QuoteFactoryException
	{
		throw new UnsupportedOperationException();
	}

	public Quote getQuote(String ticker, Date date)
			throws QuoteFactoryException
	{
		throw new UnsupportedOperationException();
	}

	public List<Quote> getQuotes(String ticker, Date startDate, Date stopDate)
			throws QuoteFactoryException
	{
		throw new UnsupportedOperationException();
	}

	public List<Quote> getQuotes(String[] tickers) throws QuoteFactoryException
	{
		throw new UnsupportedOperationException();	

	}

	public List<String> getTickers(String regex) throws QuoteFactoryException
	{
		throw new UnsupportedOperationException();
	}

}
