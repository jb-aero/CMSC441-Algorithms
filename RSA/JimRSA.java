/**
**	James Bilbrey
**	CMSC 441 - Algorithms
**	Spring 2017
**	Project 2 part 1a
**
**	JimRSA.java
** 
**/

import java.math.BigInteger;

public class JimRSA
{
	private static final String MESSAGE = "I deserve an A";
	
	public static void main(String... args)
	{
		System.out.println(MESSAGE);
		
		BigInteger a = new BigInteger("54");
		BigInteger b = new BigInteger("63");
		Triple result = extendedEuclid(a, b);
		
		System.out.println("GCD of " + a + " and " + b + ": " + result.L());
		System.out.println("d = ax + by | " + result.L() + " = (" + a + ")(" + result.M() + ") + (" + b + ")(" + result.R() + ")");
	}
	
	/**
	**	From 31.2 of CLRS
	**/
	public static Triple<BigInteger> extendedEuclid(BigInteger a, BigInteger b)
	{
		if (b.equals(BigInteger.ZERO))
		{
			return new Triple(a, BigInteger.ONE, BigInteger.ZERO);
		}
		else
		{
			Triple<BigInteger> tprime = extendedEuclid(b, a.mod(b));
			return new Triple(tprime.L(), tprime.R(), tprime.M().subtract(a.divide(b).multiply(tprime.R())));
		}
	}
	
	/**
	**	There are libs that add these, but I didn't know if using them is allowed,
	**	and I only needed something simple anyway.
	**/
	public static class Triple<T>
	{
		private T m_L, m_M, m_R;
		
		public Triple(T left, T middle, T right)
		{
			m_L = left;
			m_M = middle;
			m_R = right;
		}
		
		public T L() {
			return m_L;
		}
		
		public T M() {
			return m_M;
		}
		
		public T R() {
			return m_R;
		}
	}
}
