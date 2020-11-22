#include <stdio.h>
#include <string.h>
#include <math.h>
#include "mpi.h"

double PI(int i) {
	float j, suma = 0;;
	for (j = 1; j <= i; j++) {
		suma += (pow(-1, j - 1)) / (2 * j - 1);
	}
	return suma * 4;
}

int main(int argc, char** argv)
{
	int liczba_procesu, numer_procesu;
	int tag = 20;
	double wynik;
	MPI_Status status;
	MPI_Init(&argc, &argv);
	MPI_Comm_rank(MPI_COMM_WORLD, &numer_procesu);
	MPI_Comm_size(MPI_COMM_WORLD, &liczba_procesu);
	if (numer_procesu == liczba_procesu - 1)
	{
		printf("\nNumer procesu = %d\n", numer_procesu);
		printf("\n PI= %lf\n", wynik);
		//wysylamy zmiennych do procesu ostatniego
		MPI_Send(&wynik, 1, MPI_DOUBLE, numer_procesu - 1, tag, MPI_COMM_WORLD);
	}
	//gdy proces ma numer wiekszy niz 0 i mniejszy niz proces ostatni
	if (numer_procesu > 0 && numer_procesu < liczba_procesu - 1)
	{
		//odbieramy zmienne a i suma od kolejnego
		MPI_Recv(&wynik, 1, MPI_DOUBLE, numer_procesu + 1, tag, MPI_COMM_WORLD, &status);
		wynik = PI(numer_procesu);
		printf("\n proces = %d\n", numer_procesu);
		printf("\n PI= %lf\n", wynik);
		//przeslanie zmiennych do poprzedniego procesu
		MPI_Send(&wynik, 1, MPI_DOUBLE, numer_procesu - 1, tag, MPI_COMM_WORLD);
	}
	if (numer_procesu == 0)
	{
		//pobieramy a i suma od kolejnego procesu
		MPI_Recv(&wynik, 1, MPI_DOUBLE, numer_procesu + 1, tag, MPI_COMM_WORLD, &status);
		printf("\n proces = %d\n", numer_procesu);
		printf("\n PI= %lf\n", wynik);
	}
	MPI_Finalize();
}