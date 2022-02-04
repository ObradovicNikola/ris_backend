/*
 *  Primer programa koji racuna proizvod po zadatoj formuli:
 *  P = (1 + 0,1^2) (1 + 0,2^2) ... (1 + 10^2).
 */
#include <stdio.h>
#include <math.h>

int main()
{
  double P = 1;
  double i;

  for (i = 0.1; i <= 10; i += 0.1)
  {
    P *= (1 + pow(i, 2));
  }
  printf("%g\n", P);
}
