package com.packetnode.blackdossier.probability;

import java.util.Arrays;
import java.util.Collection;

public class GaussianMixtureDistribution implements Distribution 
{
	private GaussianDistribution[] distributions; 
	private double[] weights;
	
	public double probability(Double value) 
	{
		double result = 0.0;
		
		for (int i = 0; i < this.distributions.length; i++)
		{
			result += distributions[i].probability(value) * weights[i];
		}
		
		return result;
	}
	
	public GaussianMixtureDistribution(int numberGaussians, double[] values)
	{
		
	//	double[][] delta = getDelta(o);
	//	double[] newMixingProportions = 
	//		computeNewMixingProportions(delta, o, weights);
	//	double[] newMeans = computeNewMeans(delta, o, weights);
	//	double[] newVariances = computeNewVariances(delta, o, weights);
		
	//	distribution = new GaussianMixtureDistribution(newMeans, newVariances,
	//			newMixingProportions);
		distributions = new GaussianDistribution[numberGaussians];
		weights = new double[numberGaussians];
		
		//double[] v = {0.88, 0.89, 0.90};

		for(int i = 0; i < numberGaussians; i++)
			distributions[i] = new GaussianDistribution(values);

		// tbd - add constructor with weights
		for(int i = 0; i < numberGaussians; i++)
			weights[i] = 1.0;
		
		double[][] deltas = getDeltas(numberGaussians, values);
		double[] newWeights = getWeights(numberGaussians, deltas, values);
		double[] means = getMeans(numberGaussians, deltas, values);
		weights = newWeights;
		
		for (double w : means)
			System.out.println("mean: " + w);
		
	}
	
	protected double[] getWeights(int numberGaussians, 
			double[][] deltas, double[] values)
	{	
		double[] n = new double[numberGaussians];
		double sum = 0.0;
		
		for (int i = 0; i < numberGaussians; i++)
		{
			for (int j = 0; j < values.length; j++) 
			{
				n[i] += weights[i] * deltas[i][j];
				sum += weights[i] * deltas[i][j];
			}
		}
		
		double[] newWeights = new double[numberGaussians];
		for (int i = 0; i < numberGaussians; i++) 
			newWeights[i] = n[i]/sum;
		
		return newWeights;
	}
	
	protected double[][] getDeltas(int numberGaussians, double[] values)
	{
		// array of gaussians * number of values
		double[][] d = new double[numberGaussians][values.length];
		                                           
		for (int i = 0; i < numberGaussians; i++) 
		{			
			for (int j = 0; j < values.length; j++)
			{
				// the calculation here is basically: the weight of a given distribution
				// * the output probability of that distribution / the output probability 
				// of the overall mixture
				d[i][j] = weights[i] * distributions[i].probability(values[j] / 
						probability(values[j]));
			}
		}
			
		return d;
	}
	
	public double[] getMeans(int numberGaussians, double[][] delta, double[] values)
	{
		double[] num = new double[numberGaussians];
		double[] sum = new double[numberGaussians];
		
		for (int i = 0; i < numberGaussians; i++)
			for (int t = 0; t < values.length; t++) {
				num[i] += weights[i] * delta[i][t] * values[t];
				sum[i] += weights[i] * delta[i][t];
			}
		
		double[] newMeans = new double[numberGaussians];
		for (int i = 0; i < numberGaussians; i++)
			newMeans[i] = num[i] / sum[i];
		
		return newMeans;
	}

	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < distributions.length; i ++)
			sb.append("gaussian distribution(" + i + "), mean: " 
					+ distributions[i].getMean() + ", variance: "
					+ distributions[i].getVariance() 
					+ ", standard deviation: " 
					+ distributions[i].getStandardDeviation()
					+"\n");
		return sb.toString();
	}
	
	public static void main(String[] args)
	{
		double[] v = {1, 2, 3};
		Distribution gmd = new GaussianMixtureDistribution(3, v);
		Distribution gd = new GaussianDistribution(v);
		System.out.println(gd);
		System.out.println(gmd);
		System.out.println(gd.probability(2.0));

		System.out.println(gmd.probability(2.0));
	}
	
}
