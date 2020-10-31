import java.util.Random;

class Lodz extends Thread {
    static int PORT = 1;
    static int START = 2;
    static int PLYNIECIE = 3;
    static int DOKOWANIE = 4;
    static int KATASTROFA = 5;
    static int TANKUJ = 1000;
    static int REZERWA = 500;
    int numer;
    int paliwo;
    int stan;
    Port l;
    Random rand;

    public Lodz(int numer, int paliwo, Port l) {
        this.numer = numer;
        this.paliwo = paliwo;
        this.stan = PLYNIECIE;
        this.l = l;
        rand = new Random();
    }

    public void run() {
        while (true) {
            if (stan == PORT) {
                if (rand.nextInt(2) == 1) {
                    stan = START;
                    paliwo = TANKUJ;
                    System.out.println("W porcie prosze o pozwolenie na odpływ, łódź " + numer);
                    stan = l.odplyw(numer);
                } else {
                    System.out.println("Postoje sobie jeszcze troche");
                }
            } else if (stan == START) {
                System.out.println("Odpłynąłem, łódź " + numer);
                stan = PLYNIECIE;
            } else if (stan == PLYNIECIE) {
                paliwo -= rand.nextInt(500);
                System.out.println("Łódź " + numer + " w trasie");
                if (paliwo <= REZERWA) {
                    stan = DOKOWANIE;
                } else try {
                    sleep(rand.nextInt(1000));
                } catch (Exception e) {
                }
            } else if (stan == DOKOWANIE) {
                System.out.println("Prosze o pozowolenie na dokowanie " + numer + " ilosc paliwa " + paliwo);
                stan = l.dokuj();
                if (stan == DOKOWANIE) {
                    paliwo -= rand.nextInt(500);
                    System.out.println("REZERWA " + paliwo);
                    if (paliwo <= 0) stan = KATASTROFA;
                }
            } else if (stan == KATASTROFA) {
                System.out.println("KATASTROFA łodzi " + numer);
                l.zniszcz();
            }
        }
    }
}

class Port {
    static int PORT = 1;
    static int START = 2;
    static int PLYNIECIE = 3;
    static int DOKOWANIE = 4;
    static int KATASTROFA = 5;
    int ilosc_dokowisk;
    int ilosc_zajetych;
    int ilosc_lodek;

    Port(int ilosc_dokowisk, int ilosc_lodek) {
        this.ilosc_dokowisk = ilosc_dokowisk;
        this.ilosc_lodek = ilosc_lodek;
        this.ilosc_zajetych = 0;
    }

    synchronized int odplyw(int numer) {
        ilosc_zajetych--;
        System.out.println("Pozwolenie na odpłynięcie łodzi " + numer);
        return START;
    }

    synchronized int dokuj() {
        try {
            Thread.currentThread().sleep(1000);
        } catch (Exception ie) {
        }
        if (ilosc_zajetych < ilosc_dokowisk) {
            ilosc_zajetych++;
            System.out.println("Pozwolenie dokowanie łodzi " + ilosc_zajetych);
            return PORT;
        } else {
            return DOKOWANIE;
        }
    }

    synchronized void zniszcz() {
        ilosc_lodek--;
        System.out.println("ZNISZCZYŁEM");
        if (ilosc_lodek == ilosc_dokowisk) System.out.println("Ilosc łodzi taka sama jak dokowisk");
    }
}

public class Main {
    static int ilosc_lodzi = 10;
    static int ilosc_dokowisk = 5;
    static Port port;


    public static void main(String[] args) {
        port = new Port(ilosc_dokowisk, ilosc_lodzi);
        for (int i = 0; i < ilosc_lodzi; i++)
            new Lodz(i, 2000, port).start();
    }
}