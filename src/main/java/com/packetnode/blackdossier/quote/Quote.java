package com.packetnode.blackdossier.quote;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Quote
{
	private long id;
	private String ticker;
	private Float close;
	private Date date;
	private Float priceChange;
	private Float percentChange;
	private Float open;
	private Float high;
	private Float low;
	private long volume;
	private Float adjustedClose;

	static final DateFormat dateFormat = new SimpleDateFormat("M/d/yyyy");

	public Quote()
	{
	}
	
	private String inQuotes(String value)
	{
		StringBuffer sb = new StringBuffer();
		sb.append('"').append(value).append('"');
		return sb.toString();
	}

	public Quote(String ticker, 
				 Date date, 
				 Float open,
				 Float high,
				 Float low,
				 Float close, 
				 long volume, 
				 Float adjustedClose)
	{
		this.ticker = ticker;
		this.date = date;
		this.open = open;
		this.high = high;
		this.low = low;
		this.close = close;
		this.volume = volume;
		this.adjustedClose = adjustedClose; 
		this.priceChange = close - open;
		this.percentChange = (priceChange/open)*100;
	}
	
	public Quote(String ticker, 
				 Float close, 
				 Date date, 
				 Float priceChange, 
				 Float open, 
				 Float high, 
				 Float low, 
				 long volume)
	{
		this.ticker = ticker;
		this.date = date;
		this.open = open;
		this.priceChange = priceChange;
		this.high = high;
		this.low = low;
		this.priceChange = priceChange;
		this.volume = volume; 
		this.percentChange = (priceChange/open)*100;
	}

	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(inQuotes(ticker));
		sb.append(",");
		sb.append(close);
		sb.append(",");
		sb.append(inQuotes(dateFormat.format(date)));
		sb.append(",");
		/*sb.append(inQuotes(lastTrade));
		sb.append(",");
		sb.append(change);
		sb.append(",");*/
		sb.append(open);
		sb.append(",");
		sb.append(high);
		sb.append(",");
		sb.append(low);
		sb.append(",");
		sb.append(volume);
		return sb.toString();
	}

	public long getId()
	{
		return id;
	}

	
	private void setId(long id) 
	{ 
		this.id = id; 
	}
	 
	public String getTicker()
	{
		return ticker;
	}

	public void setTicker(String ticker)
	{
		this.ticker = ticker;
	}

	public Float getAdjustedClose()
	{
		return adjustedClose;
	}

	public void setAdjustedClose(Float adjustedClose)
	{
		this.adjustedClose = adjustedClose;
	}
	
	public Float getClose()
	{
		return close;
	}

	public void setClose(Float close)
	{
		this.close = close;
	}

	public Date getDate()
	{
		return date;
	}

	public void setDate(Date date)
	{
		this.date = date;
	}

	public Float getPriceChange()
	{
		return priceChange;
	}

	public void setPriceChange(Float change)
	{
		this.priceChange = change;
	}
	
	public Float getPercentChange()
	{
		return percentChange;
	}

	public void setPercentChange(Float change)
	{
		this.percentChange = change;
	}
	
	public Float getOpen()
	{
		return open;
	}

	public void setOpen(Float open)
	{
		this.open = open;
	}

	public Float getHigh()
	{
		return high;
	}

	public void setHigh(Float high)
	{
		this.high = high;
	}

	public Float getLow()
	{
		return low;
	}

	public void setLow(Float low)
	{
		this.low = low;
	}

	public long getVolume()
	{
		return volume;
	}

	public void setVolume(long volume)
	{
		this.volume = volume;
	}
}
