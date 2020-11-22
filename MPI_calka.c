#include <stdio.h>
#include <string.h>
#include <math.h>
#include "mpi.h"

double y(double x){
	return pow(x,2);
}

int main(int argc, char** argv)
{
	int liczba_procesu, numer_procesu;
	int tag = 20, a = 1, b = 4, n;
	double calka = 0, s, h;
	MPI_Status status;
	MPI_Init(&argc, &argv);
	MPI_Comm_rank(MPI_COMM_WORLD, &numer_procesu);
	MPI_Comm_size(MPI_COMM_WORLD, &liczba_procesu);
	if (numer_procesu == liczba_procesu - 1){
		printf("\nNumer procesu = %d\n", numer_procesu);
		printf("Calka = %lf\n", calka);
		//wysylamy zmiennych do procesu ostatniego
		MPI_Send(&calka, 1, MPI_DOUBLE, numer_procesu - 1, tag, MPI_COMM_WORLD);
	}
		//gdy proces ma numer wiekszy niz 0 i mniejszy niz proces ostatni
	if (numer_procesu > 0 && numer_procesu < liczba_procesu - 1){
			//odbieramy zmienne a i suma od kolejnego
			MPI_Recv(&calka, 1, MPI_DOUBLE, numer_procesu + 1, tag, MPI_COMM_WORLD, &status);
			n = numer_procesu;
			h = (b - a) / n;
			s = y(a) + y(b);
			for (int i = 1; i < n; i++) {
				s += 2 * y(a + i * h);
			}
			calka = (h / 2) * s;
			printf("\n proces = %d\n", numer_procesu);
			printf("Calka = %lf\n", calka);
			//przeslanie zmiennych do poprzedniego procesu
			MPI_Send(&calka, 1, MPI_DOUBLE, numer_procesu - 1, tag, MPI_COMM_WORLD);
		}
	if (numer_procesu == 0){
		//pobieramy a i suma od kolejnego procesu
		MPI_Recv(&calka, 1, MPI_DOUBLE, numer_procesu + 1, tag, MPI_COMM_WORLD, &status);
		printf("\n proces = %d\n", numer_procesu);
		printf("Calka = %lf\n", calka);
	}
	MPI_Finalize();
}