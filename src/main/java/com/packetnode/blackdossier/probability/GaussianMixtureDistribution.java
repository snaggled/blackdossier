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
		
		double[] v = {0.88, 0.89, 0.90};

		for(int i = 0; i < numberGaussians; i++)
			distributions[i] = new GaussianDistribution(values);

		// tbd - add constructor with weights
		for(int i = 0; i < numberGaussians; i++)
			weights[i] = 1.0;
		
		double[][] deltas = getDeltas(3, values);
		
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
	
	public static void main(int[] args)
	{
		double[] v = {1, 2, 3};
		GaussianDistribution gd = new GaussianDistribution(v);
		System.out.println(gd);
	}
	
}
