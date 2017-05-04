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

public class JimFactor2
{
	private static final Random rand = new Random();
	private static final BigInteger TWO = BigInteger.valueOf(2);
	
	public static void main(String... args)
	{
		if (args.length > 0)
		{
			BigInteger n = new BigInteger(args[0]);
			System.out.println("Attempting to factor modulus of size " + n.bitLength());
			if (args.length > 1) {
				System.out.print("Manually ");
				if ("brent".equals(args[1]))
				{
					System.out.println("using Brent's variant factorization...");
					brent(n);
				}
				else
				{
					System.out.println("using Pollard Rho factorization...");
					pollardRho(n);
				}
			} else {
				System.out.print("Automatically ");
				if (n.bitLength() > 120)
				{
					System.out.println("using Brent's variant factorization...");
					brent(n);
				}
				else
				{
					System.out.println("using Pollard Rho factorization...");
					pollardRho(n);
				}
			}
		} else {
			System.out.println("You should pass the number to factor as the argument.");
		}
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
		8		d = gcd(y - x, n)
		9		if d != 1 && d != n
		10			print d
		11		if i == k
		12			y = x
		13			k = 2k
	*/
	public static void pollardRho(BigInteger n)
	{
		if (n.mod(TWO).equals(BigInteger.ZERO)) {
			System.out.println("Number is even!");
			return;
		}
		long i = 1;
		BigInteger x;
		System.out.println("Attempting to get x between 0 and n-1...");
		do {
			x = new BigInteger(n.bitLength(), rand);
		} while (!(x.compareTo(n) < 0) || x.compareTo(BigInteger.ZERO) < 0);
		System.out.println("Okay, got our random starting x, beginning the loop...");
		BigInteger y = x;
		long k = 2;
		BigInteger d, b;
		
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
				return;
			}
			if (i == k) {
				y = x;
				System.out.println(k + ": " + y);
				k = 2 * k;
			}
		}
	}
	
	/**
	From https://comeoncodeon.wordpress.com/2010/09/18/pollard-rho-brent-integer-factorization/
	def brent(N):
        if N%2==0:
                return 2
        y,c,m = random.randint(1, N-1),random.randint(1, N-1),random.randint(1, N-1)
        g,r,q = 1,1,1
        while g==1:             
                x = y
                for i in range(r):
                        y = ((y*y)%N+c)%N
                k = 0
                while (k<r and g==1):
                        ys = y
                        for i in range(min(m,r-k)):
                                y = ((y*y)%N+c)%N
                                q = q*(abs(x-y))%N
                        g = gcd(q,N)
                        k = k + m
                r = r*2
        if g==N:
                while True:
                        ys = ((ys*ys)%N+c)%N
                        g = gcd(abs(x-ys),N)
                        if g>1:
                                break
         
        return g
	*/
	public static void brent(BigInteger n)
	{
		if (n.mod(TWO).equals(BigInteger.ZERO)) {
			System.out.println("Number is even!");
			return;
		}
		
		BigInteger y, c, m;
		System.out.println("Attempting to get y,c,m between 0 and n-1...");
		do {
			y = new BigInteger(n.bitLength(), rand);
		} while (!(y.compareTo(n) < 0) || y.compareTo(BigInteger.ZERO) < 0);
		do {
			c = new BigInteger(n.bitLength(), rand);
		} while (!(c.compareTo(n) < 0) || c.compareTo(BigInteger.ZERO) < 0);
		do {
			m = new BigInteger(n.bitLength(), rand);
		} while (!(m.compareTo(n) < 0) || m.compareTo(BigInteger.ZERO) < 0);
		System.out.println("Okay, got our random starting y,c,m, beginning the loop...");
		
		BigInteger r, q, g;
		g = BigInteger.ONE;
		q = BigInteger.ONE;
		r = BigInteger.ONE;
		BigInteger ys = y, x = y, k, minMRK, f, i;
		
		while (g.equals(BigInteger.ONE))
		{
			x = y;
			for (i = BigInteger.ZERO; i.compareTo(r) < 0; i = i.add(BigInteger.ONE))
			{
				y = y.pow(2).mod(n).add(c).mod(n);
				//System.out.println("--\nr = " + r + "\ni = " + i);
			}
			k = BigInteger.ZERO;
			while (k.compareTo(r) < 0 && g.equals(BigInteger.ONE))
			{
				//System.out.println("---\nr = " + r + "\nk = " + k + "\ng = " + g);
				ys = y;
				minMRK = m.min(r.subtract(k));
				for (i = BigInteger.ZERO; i.compareTo(minMRK) < 0; i = i.add(BigInteger.ONE))
				{
					y = y.pow(2).mod(n).add(c).mod(n);
					q = q.multiply(x.subtract(y).abs()).mod(n);
				}
				g = q.gcd(n);
				k = k.add(m);
			}
			r = r.multiply(TWO);
		}
		if (g.equals(n))
		{
			while (true)
			{
				ys = ys.pow(2).mod(n).add(c).mod(n);
				g = n.gcd(x.subtract(ys).abs());
				if (!g.equals(BigInteger.ONE))
					break;
			}
		}
		System.out.println("FACTOR: " + g);
		f = n.divide(g);
		System.out.println(n + " / " + g + " = " + f);
		System.out.println(g + " * " + f + " = " + g.multiply(f));
		return;
	}
}
