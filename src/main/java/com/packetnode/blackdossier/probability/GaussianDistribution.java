package com.packetnode.blackdossier.probability;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;

import org.apache.commons.logging.*;
public class GaussianDistribution implements Distribution 
{
	private double mean;
	private double variance;
	private double standardDeviation;
	
	public GaussianDistribution(double mean, double deviance)
	{
		this.mean = mean;
		this.variance = deviance;
		this.standardDeviation = Math.sqrt(deviance);
	}
	
	public GaussianDistribution(double[] values)
	{		
		// 1. mean
		double sum = 0;
		for (double v : values) sum += v;
		this.mean = sum/values.length;

		// 2. variance
		double[] deltas = values.clone();
		for (int i = 0; i < values.length ; i++)
		{
			double diff = values[i] - this.mean;
			deltas[i] = diff * diff;
		}
		
		sum = 0;
		for (double v : deltas) sum += v;

		this.variance = sum/(values.length - 1);
		
		// 3. standard deviation
		this.standardDeviation = Math.sqrt(this.variance);
	}
	
	public double probability(Double value) 
	{
		double expArg = -.5 * (value - mean) * (value - mean) / variance;
		return Math.pow(2. * Math.PI * variance, -.5) * Math.exp(expArg);
	}
	
	public String toString()
	{
		return "gaussian distribution, mean: " + mean + ", variance: "+ variance + ", standard deviation: " + standardDeviation;
	}
	
	public static void main(String[] args)
	{
		Logger logger = Logger.getLogger("file");
		logger.log(Level.INFO, "main()");
		GaussianDistribution gd = new GaussianDistribution(1., 9.);
		logger.log(Level.INFO, gd.toString());
		double[] values = {1, 2, 3};
		gd = new GaussianDistribution(values);
		logger.log(Level.INFO, gd.toString());
		System.out.println(gd.probability(2.00));
	}
}
