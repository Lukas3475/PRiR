import javax.swing.*;
import java.util.Scanner;
import java.util.concurrent.Semaphore ;
import java.util.Random;
class Filozof1 extends Thread {
    static int MAX;
    static Semaphore [] widelec  ;
    int mojNum;
    public Filozof1 (int nr) {
        mojNum = nr ;
    }

    public void ustaw(int ilosc){
        MAX = ilosc;
        widelec = new Semaphore [MAX];
    }

    public void run ( ) {
        while ( true ) {
// myslenie
            System.out.println ( "Mysle ¦ " + mojNum) ;
            try {
                Thread.sleep ( ( long ) (7000 * Math.random( ) ) ) ;
            } catch ( InterruptedException e ) {
            }
            widelec [mojNum].acquireUninterruptibly ( ) ; //przechwycenie L widelca
            widelec [ (mojNum+1)%MAX].acquireUninterruptibly ( ) ; //przechwycenie P widelca
// jedzenie
            System.out.println ( "Zaczyna jesc "+mojNum) ;
            try {
                Thread.sleep ( ( long ) (5000 * Math.random( ) ) ) ;
            } catch ( InterruptedException e ) {
            }
            System.out.println ( "Konczy jesc "+mojNum) ;
            widelec [mojNum].release ( ) ; //zwolnienie L widelca
            widelec [ (mojNum+1)%MAX].release ( ) ; //zwolnienie P widelca
        }
    }
    public void rozpocznij() {
        for ( int i =0; i<MAX; i++) {
            widelec [ i ]=new Semaphore ( 1 ) ;
        }
        for ( int i =0; i<MAX; i++) {
            new Filozof1(i).start();
        }
    }
}

class Filozof2 extends Thread {
    static int MAX;
    static Semaphore [] widelec;
    int mojNum;
    public Filozof2 ( int nr ) {
        mojNum=nr ;
    }

    public void ustaw(int ilosc){
        MAX = ilosc;
        widelec = new Semaphore [MAX];
    }

    public void run ( ) {
        while ( true ) {
// myslenie
            System.out.println ( "Mysle ¦ " + mojNum) ;
            try {
                Thread.sleep ( ( long ) (5000 * Math.random( ) ) ) ;
            } catch ( InterruptedException e ) {
            }
            if (mojNum == 0) {
                widelec [ (mojNum+1)%MAX].acquireUninterruptibly ( ) ;
                widelec [mojNum].acquireUninterruptibly ( ) ;
            } else {
                widelec [mojNum].acquireUninterruptibly ( ) ;
                widelec [ (mojNum+1)%MAX].acquireUninterruptibly ( ) ;
            }
// jedzenie
            System.out.println ( "Zaczyna jesc "+mojNum) ;
            try {
                Thread.sleep ( ( long ) (3000 * Math.random( ) ) ) ;
            } catch ( InterruptedException e ) {
            }
            System.out.println ( "Konczy jesc "+mojNum) ;
            widelec [mojNum].release ( ) ;
            widelec [ (mojNum+1)%MAX].release ( ) ;
        }
    }
    public void rozpocznij(){
        System.out.println("");
        for ( int i =0; i<MAX; i++) {
            widelec [ i ]=new Semaphore ( 1 ) ;
        }
        for ( int i =0; i<MAX; i++) {
            new Filozof2(i).start();
        }
    }
}


class Filozof3 extends Thread {
    static int MAX;
    static Semaphore [] widelec = new Semaphore [MAX] ;
    int mojNum;
    Random losuj ;

    public Filozof3(int nr){
        mojNum=nr ;
        losuj = new Random(mojNum) ;
    }

    public void ustaw(int ilosc){
        MAX = ilosc;
        widelec = new Semaphore [MAX];
    }

    public void run ( ) {
        while ( true ) {
// myslenie
            System.out.println ( "Mysle ¦ " + mojNum) ;
            try {
                Thread.sleep ( ( long ) (5000 * Math.random( ) ) ) ;
            } catch ( InterruptedException e ) {
            }
            int strona = losuj.nextInt ( 2 ) ;
            boolean podnioslDwaWidelce = false ;
            do {
                if ( strona == 0) {
                    widelec [mojNum].acquireUninterruptibly ( ) ;
                    if( ! ( widelec [ (mojNum+1)%MAX].tryAcquire ( ) ) ) {
                        widelec[mojNum].release ( ) ;
                    } else {
                        podnioslDwaWidelce = true ;
                    }
                } else {
                    widelec[(mojNum+1)%MAX].acquireUninterruptibly ( ) ;
                    if ( ! (widelec[mojNum].tryAcquire ( ) ) ) {
                        widelec[(mojNum+1)%MAX].release ( ) ;
                    } else {
                        podnioslDwaWidelce = true ;
                    }
                }
            } while ( podnioslDwaWidelce == false ) ;
            System.out.println ( "Zaczyna jesc "+mojNum) ;
            try {
                Thread.sleep ( ( long ) (3000 * Math.random( ) ) ) ;
            } catch ( InterruptedException e ) {
            }
            System.out.println ( "Konczy jesc "+mojNum) ;
            widelec [mojNum].release ( ) ;
            widelec [ (mojNum+1)%MAX].release ( ) ;
        }
    }
    public void rozpocznij(){
        for ( int i =0; i<MAX; i++) {
            widelec [ i ]=new Semaphore ( 1 ) ;
        }
        for ( int i =0; i<MAX; i++) {
            new Filozof3(i).start();
        } } }


public class Filozofowie {
    public static void main(String[] args){
        System.out.println("Podaj który algorytm chcesz wybrać: ");
        System.out.println("1. Podstawowy");
        System.out.println("2. Niesymetryczny");
        System.out.println("3. Rzut monety");

        Scanner s = new Scanner(System.in);
        int x = s.nextInt();
        System.out.println("Podaj ilość filozofów w problemie: ");
        Scanner scanner = new Scanner(System.in);
        int ilosc = scanner.nextInt();
        if(ilosc < 2){
            System.out.println("Filozof nie ma z kim jeść!");
        }
        else if(ilosc > 200){
            System.out.println("Za dużo filozofów przy jednym stole!");
        }
        else{
            switch (x){
                case 1:
                    Filozof1 filozof1 = new Filozof1(1);
                    filozof1.ustaw(ilosc);
                    filozof1.rozpocznij();
                    break;
                case 2:
                    Filozof2 filozof2 = new Filozof2(1);
                    filozof2.ustaw(ilosc);
                    filozof2.rozpocznij();
                    break;
                case 3:
                    Filozof3 filozof3 = new Filozof3(1);
                    filozof3.ustaw(ilosc);
                    filozof3.rozpocznij();
                    break;
                default:
                    System.out.println("Podano zły argument!");
                    break;
            }
        }
    }
}
