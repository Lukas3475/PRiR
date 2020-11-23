#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <time.h>
#include "mpi.h"
#define REZERWA 500
#define PORT 1
#define START 2
#define ODPLYNIECIE 3
#define PRZYPLYNIECIE 4
#define KATASTROFA 5
#define TANKUJ 5000
int paliwo = 5000;
int DOKUJ = 1, NIE_DOKUJ = 0;
int liczba_procesow;
int nr_procesu;
int ilosc_statkow;
int ilosc_dokow = 4;
int ilosc_zajetych_dokow = 0;
int tag = 1;
int wyslij[2];
int odbierz[2];
MPI_Status mpi_status;
void Wyslij(int nr_statku, int stan) 
{
	wyslij[0] = nr_statku;
	wyslij[1] = stan;
	MPI_Send(&wyslij, 2, MPI_INT, 0, tag, MPI_COMM_WORLD);
	sleep(1);
}

void Port(int liczba_procesow) {
	int nr_statku, status;
	ilosc_statkow = liczba_procesow - 1;
	printf("Halo, Witam serdecznie, tu wieża kontrolna portu \n");
	if (rand() % 2 == 1) {
		printf("Mamy piekna pogode sprzyjajaca morskim podbojom\n");
	}
	else {
		printf("Niestety pogoda nie sprzyja dzisiejszym rejsom\n");
	}
	printf("Zyczymy Panstwu, przyjemnej podrozy \n \n \n");
	printf("Dysponujemy %d dokami\n", ilosc_dokow);
	sleep(2);
	while (ilosc_dokow <= ilosc_statkow) {
		MPI_Recv(&odbierz, 2, MPI_INT, MPI_ANY_SOURCE, tag, MPI_COMM_WORLD, &mpi_status);
		nr_statku = odbierz[0];
		status = odbierz[1];
		if (status == 1) {
			printf("Statek %d stoi w porcie, przynajmniej nie bedzie katastrofy\n", nr_statku);
		}
		if (status == 2) {
			printf("Statek %d pozwolenie na odpływ z doku nr %d\n", nr_statku, ilosc_zajetych_dokow);
			ilosc_zajetych_dokow--;
		}
		if (status == 3) {
			printf("Statek %d, płynie\n", nr_statku);
		}
		if (status == 4) {
			if (ilosc_zajetych_dokow < ilosc_dokow) {
				ilosc_zajetych_dokow++;
				MPI_Send(&DOKUJ, 1, MPI_INT, nr_statku, tag, MPI_COMM_WORLD);
			}
			else {
				MPI_Send(&NIE_DOKUJ, 1, MPI_INT, nr_statku, tag, MPI_COMM_WORLD);
			}
		}
			if (status == 5) {
				ilosc_statkow--;
				printf("Ilosc statków %d\n", ilosc_statkow);
			}
	} 
	printf("Program zakonczyl dzialanie:)\n");
}
void Statek() {
	int stan, suma, i;
	stan = ODPLYNIECIE; 
	while (1) {
		if (stan == 1) {
			if (rand() % 2 == 1) {
				stan = START;
				paliwo = TANKUJ;
				printf("Prosze o pozwolenie na odpływ, statek nr %d\n", nr_procesu);
				Wyslij(nr_procesu, stan);
			}
			else {
				Wyslij(nr_procesu, stan);
			}
		}
		else if (stan == 2) {
			printf("Wypłynąłem, statek %d\n", nr_procesu);
			stan = ODPLYNIECIE;
			Wyslij(nr_procesu, stan);
		}
		else if (stan == 3) {
			paliwo -= rand() % 500;
			if (paliwo <= REZERWA) {
				stan = PRZYPLYNIECIE;
				printf("prosze o pozwolenie na dokowanie\n");
				Wyslij(nr_procesu, stan);
			}
			else {
				for (i = 0; rand() % 10000;i++);
			}
		}
		else if (stan == 4) {
			int temp;
			MPI_Recv(&temp, 1, MPI_INT, 0, tag, MPI_COMM_WORLD, &mpi_status);
			if (temp == DOKUJ) {
				stan = PORT;
				printf("Zadokowałem, statek %d\n", nr_procesu);
			}
			else
			{
				paliwo -= rand() % 500;
				if (paliwo > 0) {
					Wyslij(nr_procesu, stan);
				}
				else {
					stan = KATASTROFA;
					printf("zatonąłem\n");
					Wyslij(nr_procesu, stan);
					return;
				}
			}
		}
	}
}
int main(int argc, char* argv[])
{
	MPI_Init(&argc, &argv);
	MPI_Comm_rank(MPI_COMM_WORLD, &nr_procesu);
	MPI_Comm_size(MPI_COMM_WORLD, &liczba_procesow);
	srand(time(NULL));
	if (nr_procesu == 0)
		Port(liczba_procesow + 1);
	else 
		Statek();
	MPI_Finalize();
	return 0;
}