/**
**	James Bilbrey
**	CMSC 441 - Algorithms
**	Spring 2017
**	Project 2 part 1a
**
**	JimRSA.java
**	To build me,	$ javac JimRSA.java
**	To run me,		$ java JimRSA
**/

import java.math.BigInteger;
import java.text.MessageFormat;
import java.util.Random;

public class JimRSA
{
	private static final String MESSAGE = "I deserve an A";
	private static final Random rand = new Random();
	
	public static void main(String... args)
	{
		int modSize = 1024;
		
		// Miller-Rabin S
		int MRS = 100;
		
		boolean debug = false;
		
		if (args.length >= 1)
		{
			modSize = Integer.valueOf(args[0]);
		}
		
		if (debug)
		{
			BigInteger a = BigInteger.valueOf(54);
			BigInteger b = BigInteger.valueOf(63);
			Triple<BigInteger> result = extendedEuclid(a, b);
			
			// Verify that EEA appears to work
			System.out.println("GCD of " + a + " and " + b + ": " + result.L());
			MessageFormat ex2 = new MessageFormat("d = ax + by | {0} = ({1})({2}) + ({3})({4})");
			Object[] ex2args = new Object[]{result.L(), a, result.M(), b, result.R()};
			System.out.println(ex2.format(ex2args));
			
			// Verify that encoding an mesage as an int works
			System.out.println("a = " + stringToInt("a"));
			System.out.println("aa = " + stringToInt("aa"));
			System.out.println("97 << 8 = " + (97 << 8));
			
		
			System.out.println(MESSAGE);
			System.out.println("Message: " + stringToInt(MESSAGE));
			System.out.println("Message: " + intToString(stringToInt(MESSAGE)));
			System.out.println("Message: " + betterStringToInt(MESSAGE));
			System.out.println("Message: " + betterIntToString(betterStringToInt(MESSAGE)));
		}

		BigInteger p = new BigInteger(modSize, rand);
		
		// Ensure p is prime
		while (millerRabin(p, MRS))
			p = new BigInteger(modSize, rand);
		
		BigInteger q = new BigInteger(modSize, rand);
		
		// Ensure p and q are different
		while (q.equals(p))
			q = new BigInteger(modSize, rand);
		
		// Ensure q is prime
		while (millerRabin(q, MRS))
			q = new BigInteger(modSize, rand);
		
		// n = p*q
		BigInteger n = p.multiply(q);
		
		// Phi(n) = (p-1)*(q-1)
		BigInteger phin = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
		
		// e - a small odd integer relatively prime to Phi(n)
		// 
		BigInteger e = BigInteger.valueOf(65537);
		Triple<BigInteger> eer = extendedEuclid(e, phin);
		while (!eer.L().equals(BigInteger.ONE))
		{
			e = e.add(BigInteger.valueOf(2));
			eer = extendedEuclid(e, phin);
		}
		
		// As described by 31.4 CLRS
		BigInteger d = eer.M();
		
		// As described by Khan Academy
		//BigInteger d = BigInteger.valueOf(2).multiply(phin).add(BigInteger.ONE).divide(e);
		
		System.out.println("p: " + p);
		System.out.println("q: " + q);
		System.out.println();
		System.out.println("n: " + n);
		System.out.println("e: " + e);
		System.out.println("d: " + d);
		System.out.println();
		
		System.out.println(MESSAGE);
		BigInteger encoded = betterStringToInt(MESSAGE);
		System.out.println(encoded);
		BigInteger encrypted = modularExponentiation(encoded, e, n);
		System.out.println(encrypted);
		BigInteger decrypted = modularExponentiation(encrypted, d, n);
		System.out.println(decrypted);
		System.out.println(betterIntToString(decrypted));
	}
	
	/**
	**	Encodes a string as a BigInteger
	**	Adapted from the python example on the project page
	**/
	public static BigInteger stringToInt(String message)
	{
		BigInteger result = BigInteger.ZERO;
		for (char c : message.toCharArray())
		{
			result = result.shiftLeft(8);
			result = result.xor(BigInteger.valueOf(c));
		}
		return result;
	}
	
	/**
	**	For fun/debug
	**	Wanted to see if the message was actually readable
	**/
	public static String intToString(BigInteger message)
	{
		byte[] bytes = message.toByteArray();
		char[] result = new char[bytes.length];
		for (int i = 0; i < bytes.length; i++)
		{
			result[i] = (char)bytes[i];
		}
		return String.valueOf(result);
	}
	
	/**
	**	This version sould be more memory efficient
	**/
	public static BigInteger betterStringToInt(String message)
	{
		return new BigInteger(message.getBytes());
	}
	
	/**
	**	This version sould be more memory efficient
	**/
	public static String betterIntToString(BigInteger message)
	{
		return new String(message.toByteArray());
	}
	
	/**
	**	The standard Euclidean algorithm for determining GCD
	**/
	public static BigInteger gcdEuclid(BigInteger a, BigInteger b)
	{
		if (b.equals(BigInteger.ZERO))
			return a;
		return gcdEuclid(b, a.mod(b));
	}
	
	/**
	**	Extended Euclidean Algorithm from 31.2 of CLRS
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
	**	Returns whether a is a witness to the composite-ness of n
	**/
	public static boolean witness(BigInteger a, BigInteger n)
	{
		BigInteger nminusone = n.subtract(BigInteger.ONE);
		int t = 0;
		BigInteger u = nminusone;
		do
		{
			u = u.divide(BigInteger.valueOf((int) Math.pow(2, ++t)));
		} while(!u.testBit(0));
		
		BigInteger prev = modularExponentiation(a, u, n);
		BigInteger curr = BigInteger.ZERO;
		for (int i = 0; i < t; i++)
		{
			curr = prev.pow(2).mod(n);
			if (curr.equals(BigInteger.ONE) && !prev.equals(BigInteger.ONE) && !prev.equals(nminusone))
				return true;
			prev = curr;
		}
		if (!curr.equals(BigInteger.ONE))
			return true;
		return false;
	}
	
	/**
	**	isComposite
	**	Note: true means *definitely* composite, false only means *probably* prime
	**/
	public static boolean millerRabin(BigInteger n, int s)
	{
		for (int i = 0; i < s; i++)
			if (witness(new BigInteger(n.bitLength(), rand), n))
				return true;
		return false;
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
