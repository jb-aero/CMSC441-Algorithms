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

public class JimFactor
{
	private static final Random rand = new Random();
	
	public static void main(String... args)
	{
		
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
		unsigned int i = 1;
		BigInteger x;
		do {
			x = new BigInteger(n.bitLength(), rand);
		} while (x.compare(n) > 0 || x.compare(BigInteger.ZERO) < 0);
		BigInteger y = x;
		unsigned int k = 2;
		while (true)
		{
			i = i + 1;
			x = x.pow(2).subtract(BigInteger.ONE).mod(n);
			d = n.gcd(y.subtract(x));
			if (d != 1 && d != n) {
				System.out.println(d);
			}
			if (i == k) {
				y = x;
				k = 2 * k;
			}
		}
	}
}
