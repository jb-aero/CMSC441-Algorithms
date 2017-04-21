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
import java.util.Random;

public class JimRSA
{
	private static final String MESSAGE = "I deserve an A";
	private static final Random rand = new Random();
	
	public static void main(String... args)
	{
		int modSize = 1024;
		
		if (args.length >= 1)
		{
			modSize = Integer.valueOf(args[0]);
		}
		
		System.out.println(MESSAGE);
		
		BigInteger a = BigInteger.valueOf(54);
		BigInteger b = BigInteger.valueOf(63);
		Triple<BigInteger> result = extendedEuclid(a, b);
		
		System.out.println("GCD of " + a + " and " + b + ": " + result.L());
		System.out.println("d = ax + by | " + result.L() + " = (" + a + ")("
				+ result.M() + ") + (" + b + ")(" + result.R() + ")");

		BigInteger p = new BigInteger(modSize, rand);
		BigInteger q = new BigInteger(modSize, rand);
		
		// Ensure p and q are different
		while (q.equals(p))
			q = new BigInteger(modSize, rand);
		
		// Ensure odd numbers
		if (!p.testBit(0))
			p = p.setBit(0);
		if (!q.testBit(0))
			q = q.setBit(0);
		
		// Loop calling Miller-Rabin goes here to attempt to make p and q prime
		
		BigInteger n = p.multiply(q);
		BigInteger phin = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
		
		BigInteger e = phin;
		//while (!phin.gcd(e).equals(BigInteger.ONE))
		
		BigInteger d = BigInteger.valueOf(2).multiply(phin).add(BigInteger.ONE).divide(e);
		
		System.out.println("n: " + n);
		System.out.println("e: " + e);
		System.out.println("d: " + d);
	}
	
	/**
	**	From 31.2 of CLRS
	**/
	public static Triple<BigInteger> extendedEuclid(BigInteger a, BigInteger b)
	{
		if (b.equals(BigInteger.ZERO))
		{
			return new Triple<BigInteger>(a, BigInteger.ONE, BigInteger.ZERO);
		}
		else
		{
			Triple<BigInteger> tprime = extendedEuclid(b, a.mod(b));
			return new Triple<BigInteger>(tprime.L(), tprime.R(), tprime.M().subtract(a.divide(b).multiply(tprime.R())));
		}
	}
	
	/**
	**	For sanity following CLRS. Uses built in method as allowed.
	**/
	public static BigInteger modularExponentiation(BigInteger a, BigInteger b, BigInteger n)
	{
		return a.modPow(b, n);
	}
	
	/**
	**	isComposite
	**/
	public static boolean millerRabin()
	{
		return true;
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
