/**
**	James Bilbrey
**	CMSC 441 - Algorithms
**	Spring 2017
**	Project 2 part 2
**
**	JimFactor.java
**	To build me,	$ javac JimFactor.java
**	To run me,		$ java JimFactor
**/

import java.math.BigInteger;
import java.util.Random;

public class JimFactor
{
	private static final Random rand = new Random();
	
	public static void main(String... args)
	{
		if (args.length > 0)
		{
			pollardRho(new BigInteger(args[0]));
		}
		System.out.println("You should pass the number to factor as the argument.");
	}
	
	/**
		From CLRS 31.9
		POLLARD-RHO(n)
		1	i = 1
		2	x = RANDOM(0, n - 1)
		3	y = x1
		4	k = 2
		5	while TRUE
		6		i = i + 1
		7		x = (x^2 - 1) mod n
		8		d = gcd(y - xi, n)
		9		if d != 1 && d != n
		10			print d
		11		if i == k
		12			y = x
		13			k = 2k
	*/
	public static void pollardRho(BigInteger n)
	{
		long i = 1;
		BigInteger x;
		System.out.println("Attempting to get x between 0 and n-1...");
		do {
			x = new BigInteger(n.bitLength(), rand);
		} while (x.compareTo(n) > 0 || x.compareTo(BigInteger.ZERO) < 0);
		BigInteger y = x;
		long k = 2;
		BigInteger d, b;
		System.out.println("Okay, got our random starting x, beginning the loop...");
		
		while (true)
		{
			i = i + 1;
			x = x.pow(2).subtract(BigInteger.ONE).mod(n);
			d = n.gcd(y.subtract(x));
			if (!d.equals(BigInteger.ONE) && !d.equals(n)) {
				System.out.println("FACTOR: " + d);
				b = n.divide(d);
				System.out.println(n + " / " + d + " = " + b);
				System.out.println(d + " * " + b + " = " + d.multiply(b));
			}
			if (i == k) {
				y = x;
				System.out.println(k + ": " + y);
				k = 2 * k;
			}
		}
	}
}
