package com.packetnode.blackdossier.quote;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.math.BigDecimal;

public class Quote
{
	private long id;
	private String ticker;
	private BigDecimal close;
	private Date date;
	private BigDecimal priceChange;
	private BigDecimal percentChange;
	private BigDecimal open;
	private BigDecimal high;
	private BigDecimal low;
	
	@Override
	public int hashCode()
	{
		return toString().hashCode();
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		return toString().equals(obj.toString());
	}

	private long volume;
	private BigDecimal adjustedClose;

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
				 BigDecimal open,
				 BigDecimal high,
				 BigDecimal low,
				 BigDecimal close, 
				 long volume, 
				 BigDecimal adjustedClose)
	{
		this.ticker = ticker;
		this.date = date;
		this.open = open;
		this.high = high;
		this.low = low;
		this.close = close;
		this.volume = volume;
		this.adjustedClose = adjustedClose; 
		//this.priceChange = close - open;
		//this.percentChange = (priceChange/open)*100;
	}
	
	public Quote(String ticker, 
				 BigDecimal close, 
				 Date date, 
				 BigDecimal priceChange, 
				 BigDecimal open, 
				 BigDecimal high, 
				 BigDecimal low, 
				 long volume)
	{
		this.ticker = ticker;
		this.date = date;
		this.open = open;
		this.priceChange = priceChange;
		this.high = high;
		this.low = low;
		//this.priceChange = priceChange;
		this.volume = volume; 
		//this.percentChange = (priceChange/open)*100;
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

	public BigDecimal getAdjustedClose()
	{
		return adjustedClose;
	}

	public void setAdjustedClose(BigDecimal adjustedClose)
	{
		this.adjustedClose = adjustedClose;
	}
	
	public BigDecimal getClose()
	{
		return close;
	}

	public void setClose(BigDecimal close)
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

	public BigDecimal getPriceChange()
	{
		return priceChange;
	}

	public void setPriceChange(BigDecimal change)
	{
		this.priceChange = change;
	}
	
	public BigDecimal getPercentChange()
	{
		return percentChange;
	}

	public void setPercentChange(BigDecimal change)
	{
		this.percentChange = change;
	}
	
	public BigDecimal getOpen()
	{
		return open;
	}

	public void setOpen(BigDecimal open)
	{
		this.open = open;
	}

	public BigDecimal getHigh()
	{
		return high;
	}

	public void setHigh(BigDecimal high)
	{
		this.high = high;
	}

	public BigDecimal getLow()
	{
		return low;
	}

	public void setLow(BigDecimal low)
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
