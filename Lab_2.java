class M_prostokat extends Thread{

    double  ai, bi, dx, calka;
    int dokladnosc;

    M_prostokat(double ai, double bi, int dokladnosc){
        this.ai = ai;
        this.bi = bi;
        this.dokladnosc = dokladnosc;
    }

    private static double func(double x) {
        return x*x+3;
    }

    public void run(){
        dx = (bi - ai) / (double)dokladnosc;

        calka = 0;
        for (int i=1; i<=dokladnosc; i++) {
            calka += func(ai + i * dx);
        }
        calka *= dx;
        System.out.println("Wynik prostokatow: " + calka);
    }
}

class M_trapezow extends Thread{
    double ai, bi, dx, calka;
    int dokladnosc;

    M_trapezow(double ai, double bi, int dokladnosc){
        this.ai = ai;
        this.bi = bi;
        this.dokladnosc = dokladnosc;
    }

    private static double func(double x) {
        return x*x+3;
    }

    public void run(){
        dx = (bi - ai) / (double)dokladnosc;

        calka = 0;
        for (int i=1; i<dokladnosc; i++) {
            calka += func(ai + i * dx);
        }
        calka += (func(ai) + func(bi)) / 2;
        calka *= dx;
        System.out.println("Wynik trapezow: " + calka);
    }
}


class M_simpsona extends Thread{

    double ai, bi, dx, calka, s, x;
    int dokladnosc;

    M_simpsona(double ai, double bi, int dokladnosc){
        this.ai = ai;
        this.bi = bi;
        this.dokladnosc = dokladnosc;
    }

    private static double func(double x) {
        return x*x+3;
    }

    public void run(){
        dx = (bi - ai) / (double)dokladnosc;

        calka = 0;
        s = 0;
        for (int i=1; i<dokladnosc; i++) {
            x = ai + i*dx;
            s += func(x - dx / 2);
            calka += func(x);
        }
        s += func(bi - dx / 2);
        calka = (dx/6) * (func(ai) + func(bi) + 2*calka + 4*s);

        System.out.println("Wynik Simpsona: " + calka);
    }
}

public class Lab_2{

    public static void main(String[] args){
        int dokladnosc = 10;
        M_prostokat mp = new M_prostokat(1,3,dokladnosc);
        M_trapezow mt = new M_trapezow(1,3,dokladnosc);
        M_simpsona sp = new M_simpsona(1,3,dokladnosc);

        mp.start();
        mt.start();
        sp.start();

        while (mp.isAlive() && mt.isAlive() && sp.isAlive()){
            try{
                Thread.sleep(10);
            }
            catch (InterruptedException e){

            }
            System.out.println(mp.calka + mt.calka + sp.calka);
        }
    }
}