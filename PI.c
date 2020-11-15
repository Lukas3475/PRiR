#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>

float PI(int i){
    float N, j, suma = 0;;
    N = ((rand()%50) * 100) + i;
    for(j = 1; j <= N; j++){
        suma += (pow(-1,j-1))/(2*j-1);
    }
    return suma * 4;
}

int main()
{
    pid_t pid;
    int i, liczba_procesow;

    printf("Podaj liczbe procesów: ");
    scanf("%d", &liczba_procesow);

    /* wypisuje identyfikator procesu */
    printf("Moj PID = %d\n", getpid());

    /* tworzy nowe procesy */
    for (i = 1; i <= liczba_procesow; i++) {

        switch (pid = fork()) {
        case 0: /* proces potomny */
            printf("Wartość PI = %f , w procesie potomnym o PID = %d\n", PI(i), getpid());
            printf("Jestem procesem potomnym. Wartosc przekazana przez fork() = \%d\n", pid);
            return 0;


        default: /* proces macierzysty */
            printf("Wartość PI = %f , w procesie macierzystym o PID = %d\n", PI(i), getpid());
            printf("Jestem procesem macierzystym. Wartosc przekazana przez fork() =\%d\n", pid);

        } /*switch*/
    }

    return 0;

}
