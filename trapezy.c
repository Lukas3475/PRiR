#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>

float y(float x)
{
    // Declaring the function f(x) = 1/(1+x*x) 
    return 1 / (1 + x * x);
}

int main()
{
    pid_t pid;
    int i, liczba_procesow, a, b, n;
    float wynik = 0;
    float s, h;

    printf("Podaj liczbe procesów: ");
    scanf("%d", &liczba_procesow);

    /* wypisuje identyfikator procesu */
    printf("Moj PID = %d\n", getpid());

    /* tworzy nowe procesy */
    for (i = 1; i <= liczba_procesow; i++) {

        switch (pid = fork()) {
        case 0: /* proces potomny */
            b = rand() % 100;
            a = 1;
            n = rand() % 10;
            h = (b - a) / n;
            s = y(a) + y(b);
            for (int i = 1; i < n; i++) {
                s += 2 * y(a + i * h);
            }
            printf("Wynik całki, metodą trapenów: %f\n", (h / 2) * s);
            printf("Jestem procesem potomnym. Moj PID = %d\n", getpid());
            printf("Jestem procesem potomnym. Wartosc przekazana przez fork() = \%d\n", pid);
            return 0;


        default: /* proces macierzysty */
            b = rand() % 100;
            a = 1;
            n = rand() % 10;
            h = (b - a) / n;
            s = y(a) + y(b);
            for (int i = 1; i < n; i++) {
                s += 2 * y(a + i * h);
            }
            printf("Wynik całki, metodą trapenów: %f\n", (h / 2) * s);
            printf("Jestem procesem macierzystym. Moj PID = %d\n", getpid());
            printf("Jestem procesem macierzystym. Wartosc przekazana przez fork() =\%d\n", pid);

        } /*switch*/
    }

    return 0;

}
