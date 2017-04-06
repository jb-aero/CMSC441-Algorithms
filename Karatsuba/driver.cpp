#include <iostream>
#include <cstdlib>
#include "posint.h"
using namespace std;

void work();

int main() {

  // Set base to 16^3
  // This causes numbers to display in hex.  
  // However, base must be no greater than 2^15 when
  // using 32-bit integers, so we can only fit 3 hex 
  // digits (or 12 bits) per 32-bit int.  It is more
  // space-efficient to use the default, but then the
  // numbers will print in binary.
  
  PosInt::setBase(10,1);
  work();

  PosInt::setBase(16,1);
  //work();

  PosInt::setBase(16,3);
  //work();
  
  int three, two, one, zero, b, c, d, a = 42585;
  b = a % (16*16*16);
  three = a - b;
  c = b % (16*16);
  two = b - c;
  d = c % 16;
  one = c - d;
  zero = d;
  cout << "a = " << a << endl;
  cout << "3 = " << three << endl;
  cout << "2 = " << two << endl;
  cout << "1 = " << one << endl;
  cout << "0 = " << zero << endl;
}

void work()
{
  PosInt t(16);
  PosInt v(3);
  t.pow(v);
  cout << "t = 16^3  = " << t << endl;
  cout << "t =       = ";
  t.print_array(cout);
  cout << endl << endl;
  t.add(PosInt(16 * 16 * 16 - 1));
  cout << "t =       = " << t << endl;
  cout << "t =       = ";
  t.print_array(cout);
  cout << endl << endl;

  PosInt u(167);
  cout << "u =  167  = " << u << endl;
  cout << "u         = ";
  u.print_array(cout);
  cout << endl << endl;

  PosInt w(255);
  cout << "w =  255  = " << w << endl;
  cout << "w         = ";
  w.print_array(cout);
  cout << endl << endl;

  w.mul(u);
  cout << "w = 255*167 = " << w << endl;
  cout << "w           = ";
  w.print_array(cout);
  cout << endl << endl;
  
  PosInt a(167);
  PosInt b(255);
  b.fastMul(a);
  cout << "b = 255*167 = " << b << endl;
  cout << "b           = ";
  b.print_array(cout);
  cout << endl << endl;
  
  PosInt j(6);
  PosInt k(7);
  j.fastMul(k);
  cout << "j = 6*7 = " << j << endl;
  cout << "j       = ";
  j.print_array(cout);
  cout << endl << endl;
  
 
  /*
  // PosInt::setBase(10, 4);

  // x = 2^128
  PosInt x(2);
  PosInt y(128);
  x.pow(y);

  // z = random number between 0 and 2^128 - 1
  //  srand(0);
  srand(time(NULL));
  PosInt z;
  z.rand(x);

  cout << "x = 2^128 = " << x << endl;
  cout << "x         = ";
  x.print_array(cout);
  cout << endl << endl;

  cout << "z = rand(x) = " << z << endl;
  cout << "z           = ";
  z.print_array(cout);
  cout << endl << endl;

  // z = z^2
  z.mul(z);

  cout << "z^2 = " << z << endl;

  // z = z mod x
  z.mod(x);

  cout << "z^2 mod x = " << z << endl;*/
  cout << "########################################" << endl << endl;
}
  
