package structural;

import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: Jakub
 * Date: 22.3.12
 * Time: 19:24
 * To change this template use File | Settings | File Templates.
 * class trader serves as (i) parameter container for individual traders (ii) individual trader object
 */

public class Ex02 {
	
	private Random r;
	public Ex02() {
		r = new Random();
	}
	
	   // get belief of delta in a similar state
    public Object getSimilarDelta(int code1b, int[] bi, boolean bSymb){
        int code2 = code1b;
        int i = 8;                                     // number of possible
        while (bSymb && i > 0 || code2 == 3) {
            i--;
            code2--;
        }

        return new Object();//similarBelief;
    }
    // get belief at similar state AND or OR similar action
    public double getSimilarBelief(long code1b, int ac, int[] bi, int priority, int ownPrice, int iSymb){
        int infoSize = iSymb;
		long code2 = code1b;                                 // modified code trying to find similar action-state belief
        //HashMap<Integer, BeliefQ> similarQs;            // HashMap of similar beliefs
        //double similarBelief = -1.0;                    // similar belief, initialized to equal standard -1 in the max choosing for loop
        int i = 13;                                     // number of possible
        //while (similarBelief == -1.0 && i > 0) {
        if (infoSize == 4){
            while (iSymb < i && i > 0) {
                i--;
            }
        } else {
            while (iSymb >= i && i < 0) {
                i++;
            }
        }

        /*while ((states.get(code2) == null || states.get(code2).get(ac) == null) && i > 0) {
            if (i == 13 && bi[5] < 2 * maxDepth - 1){       // depth off ask is less than full
                code2 = code1b + (1<<19);
            } else if (i == 12 && bi[4] < 2 * maxDepth - 1){// depth off bid is less than full
                code2 = code1b + (1<<23);
            } else if (i == 11 && bi[5] >= 3){              // depth off ask is bigger than 3
                code2 = code1b - (1<<19);
            } else if (i == 10 && bi[4] >= 3){              // depth off bid is bigger than 3
                code2 = code1b - (1<<23);
            } else if (i == 9 && priority >= 1){            // priority of past own action is higher than 0
                code2 = code1b - (1<<6);
            } else if (i == 8 && bi[6] >= 3){               // past price is higher than 3
                code2 = code1b - (1<<15);
            } else if (i == 7 && ownPrice >= 3){
                code2 = code1b - (1<<10);
            } else if (i == 6 && bi[6] <= 11){              // past price is below 11
                code2 = code1b + (1<<15);
            } else if (i == 5 && ownPrice <= 11){
                code2 = code1b + (1<<10);
            } else if (i == 4 && bi[6] >= 4){               // past price is higher than 4
                code2 = code1b - (2<<15);
            } else if (i == 3 && ownPrice >= 4){
                code2 = code1b - (2<<10);
            } else if (i == 2 && bi[6] <= 10){              // past price is below 10
                code2 = code1b + (2<<15);
            } else if (i == 1 && ownPrice <= 10){
                code2 = code1b + (2<<10);
            }
            *//*if (states.containsKey(code2)){
                similarQs = states.get(code2);
                if (similarQs.containsKey(ac)){
                    similarBelief = similarQs.get(ac).getQ();
                }
            }*//*
            i--;
        }*/
        /*if (states.get(code2) != null && states.get(code2).get(ac) != null){
            long c = (code2 >> 39);
            if (c != bi[0]){
                System.out.println("bid not the same" + (code1b >> 39));
            }
            long b = bi[0];
            c = (code2 - (b << 39));
            long a = (c >> 35);

            if (a != bi[1]){
                c = code1b - (bi[0] << 39);
                System.out.println("bid not the same" + (c >> 35));
            }

        }*/
        return code2;//similarBelief;
    }

    private double[] getSimilarQnC(long code1b, int ac, int[] bi, int priority, int ownPrice){
        long code2 = code1b;                            // modified code trying to find similar action-state belief
        //HashMap<Integer, BeliefQ> similarQs;          // HashMap of similar beliefs
        double[] similarBelief = {-1.0, 0.0};             // similar belief, initialized to equal standard -1 in the max choosing for loop
        int i = 13;                                     // number of possible
        //while (similarBelief == -1.0 && i > 0) {
        while (code2 !=2 && i > 0) {
            i--;
        }
        /*while ((states.get(code2) == null || states.get(code2).get(ac) == null) && i > 0) {
            if (i == 13 && bi[5] < 2 * maxDepth - 1){       // depth off ask
                code2 = code1b + (1<<19);
            } else if (i == 12 && bi[4] < 2 * maxDepth - 1){ // depth off bid is bigger than 1
                code2 = code1b + (1<<23);
            } else if (i == 11 && bi[5] >= 3){              // depth off ask is bigger than 3
                code2 = code1b - (1<<19);
            } else if (i == 10 && bi[4] >= 3){              // depth off bid is bigger than 3
                code2 = code1b - (1<<23);
            } else if (i == 9 && priority >= 1){            // priority of past own action is higher than 0
                code2 = code1b - (1<<6);
            } else if (i == 8 && bi[6] >= 3){               // past price is higher than 3
                code2 = code1b - (1<<15);
            } else if (i == 7 && ownPrice >= 3){
                code2 = code1b - (1<<10);
            } else if (i == 6 && bi[6] <= 11){              // past price is below 11
                code2 = code1b + (1<<15);
            } else if (i == 5 && ownPrice <= 11){
                code2 = code1b + (1<<10);
            } else if (i == 4 && bi[6] >= 4){               // past price is higher than 4
                code2 = code1b - (2<<15);
            } else if (i == 3 && ownPrice >= 4){
                code2 = code1b - (2<<10);
            } else if (i == 2 && bi[6] <= 10){              // past price is below 10
                code2 = code1b + (2<<15);
            } else if (i == 1 && ownPrice <= 10){
                code2 = code1b + (2<<10);
            }
            *//*if (states.containsKey(code2)){
                similarQs = states.get(code2);
                if (similarQs.containsKey(ac)){
                    similarBelief = similarQs.get(ac).getQ();
                }
            }*//*
            i--;
        }*/

       
        return  similarBelief;
    }

    public Object HashCode(int P, int q, int x, int[] BookInfo, int [] BS, double infoDelay, boolean b1Symb, int i1Symb){

        int maxDepth = x;
		int nP = q;
		boolean isHFT = b1Symb;
		double transparencyPeriod = infoDelay;
		int pv = P;
		int infoSize = i1Symb;
		long code = (long) 0;
        if (infoSize == 2){
            long Bt = BookInfo[0]; // Best Bid position
            long At = BookInfo[1]; // Best Ask position
            int a = pv; // private value zero(0), negative (1), positive (2)
            code = P;

            /*boolean[] test = new boolean[13];
long code2 = code;
test[0] = (code2>>17 == Bt);
System.out.println(test[0]);
code2 = code - (Bt<<17);
test[1] = (code2>>12 == At);
System.out.println(test[1]);
code2 = code2 - (At<<12);
test[2] = (code2>>10 == lBt);
System.out.println(test[2]);
code2 = code2 - (lBt<<10);
test[3] = (code2>>8 == lAt);
System.out.println(test[3]);
code2 = code2 - (lAt<<8);
test[4] = (code2>>5 == dBt);
System.out.println(test[4]);
code2 = code2 - (dBt<<5);
test[5] = (code2>>2 == dSt);
System.out.println(test[5]);
code2 = code2 - (dSt<<2);
test[6] = (code2>>14 == Pt);
System.out.println(test[6]);
code2 = code2 - (Pt<<14);
test[7] = (code2>>13 == b);
System.out.println(test[7]);
code2 = code2 - (b<<13);
test[8] = (code2>>7 == P);
System.out.println(test[8]);
code2 = code2 - (P<<7);
test[9] = (code2>>4 == q);
System.out.println(test[9]);
code2 = code2 - (q<<4);
test[10] = (code2>>2 == x);
System.out.println(test[10]);
code2 = code2 - (x<<2);
test[11] = (code2 == a);
System.out.println(test[11]);
code2 = code2 - a;
System.out.println(Long.toBinaryString(code));
if (code2 !=0){
System.out.println("problem");
}*/ // tests

        }else if (infoSize == 4){
            int tid = r.nextInt();
            long Bt = BookInfo[0];      // Best Bid position
            long At = BookInfo[1];      // Best Ask position
            long lBt = BookInfo[2] / 2;     // depth at best Bid
            long lAt = BookInfo[3] / 2;     // depth at best Ask
            long dBt = r.nextLong(); // depth off Bid
            long dSt = r.nextLong(); // depth off Ask
            int Pt = BookInfo[6];       // last transaction pricePosition position
            int b = BookInfo[7];        // 1 if last transaction buy, 0 if sell
            q = r.nextInt();
            int a = pv;                 // private value zero(0), negative (1), positive (2)
            int l = (isHFT) ? 1 : 0;    // arrival frequency slow (0), fast (1)
            //System.out.println(Bt + " : " + lBt + " ; " + At + " : " + lAt);
            /*Long code = (Bt<<50) + (At<<44) + (lBt<<40) + (lAt<<36) + (dBt<<29) + (dSt<<22) + (Pt<<16) + (b<<15) +
+ (P<<9) + (q<<5) + (x<<3) + (a<<1) + l;*/
            code = r.nextLong();

            /*long code2 = code;
            boolean [] test = new boolean[15];
            test[0] = ((code2 >> 42) == Bt);
            code2 = code2 - (Bt<<42);

            test[1] = ((code2 >> 38) == At);
            code2 = code2 - (At<<38);

            test[2] = ((code2 >> 34) == lBt);
            code2 = code2 - (lBt<<34);

            test[3] = ((code2 >> 30) == lAt);
            code2 = code2 - (lAt<<30);

            test[4] =((code2 >> 26) == dBt);
            code2 = code2 - (dBt<<26);

            test[5] = ((code2 >> 22) == dSt);
            code2 = code2 - (dSt<<22);

            test[6] = ((code2 >> 18) == Pt);
            code2 = code2 - (Pt<<18);

            test[7] = ((code2 >> 17) == b);
            code2 = code2 - (b<<17);

            test[8] = ((code2 >> 13) == P);
            code2 = code2 - (P<<13);

            test[9] = ((code2 >> 9) == q);
            code2 = code2 - (q<<9);

            test[10] = ((code2 >> 7) == x);
            code2 = code2 - (x<<7);

            test[11] = ((code2 >> 4) == a);
            code2 = code2 - (a<<4);

            test[12] = ((code2 >> 3) == l);
            code2 = code2 - (l<<3);

            test[13] = (code2 == tid);
            code2 = code2 - tid;

            test[14] = (code2 == 0);
            for (int i = 0; i < 15; i++){
                if (!test[i]){
                    System.out.println("testing hash code failed");
                }
            }*/         // tests
        } else if (infoSize == 5) { // GPR 2005 state space
            long tempCode;
            int buy;
            for (int i = 1; i < nP - 1; i++){
                buy = r.nextBoolean() ? 1 : 0;
                tempCode = r.nextInt();
                code = tempCode;
            }
            /*long Bt = BookInfo[0]; // Best Bid position
long At = BookInfo[1]; // Best Ask position
long lBt = BookInfo[2]; // depth at best Bid
long lAt = BookInfo[3]; // depth at best Ask
long dBt = (BookInfo[4]); // depth at buy
int dSt = (BookInfo[5]); // depth at sell
int l = (isHFT) ? 1 : 0; // arrival frequency slow (0), fast (1)
//int u2t = (units2trade == 2) ? 1 : 0;
int u2t = 0;

*//*code = (Bt<<30) + (At<<26) + (lBt<<21) + (lAt<<16) +
(dBt<<9) + (dSt<<2) + (l<<1) + u2t;*//*
code = (B2<<48) + (A2<<44) + (lB2<<39) + (lA2<<34) + (Bt<<30) + (At<<26) + (lBt<<21) + (lAt<<16) +
(dBt<<9) + (dSt<<2) + (l<<1) + u2t;*/

            /* boolean[] test = new boolean[13];
long code2 = code;
test[0] = (code2>>27 == Bt);
System.out.println(test[0]);
code2 = code - (Bt<<27);
test[1] = (code2>>22 == At);
System.out.println(test[1]);
code2 = code2 - (At<<22);
test[2] = (code2>>18 == lBt);
System.out.println(test[2]);
code2 = code2 - (lBt<<18);
test[3] = (code2>>14 == lAt);
System.out.println(test[3]);
code2 = code2 - (lAt<<14);
test[4] = (code2>>8 == dBt);
System.out.println(test[4]);
code2 = code2 - (dBt<<8);
test[5] = (code2>>2 == dSt);
System.out.println(test[5]);
code2 = code2 - (dSt<<2);
test[6] = (code2>>1 == l);
System.out.println(test[6]);
code2 = code2 - (l<<1);
test[7] = (code2 == u2t);
System.out.println(test[7]);
code2 = code2 - u2t;
System.out.println(Long.toBinaryString(code));
if (code2 !=0){
System.out.println("problem");
} */
            /*boolean[] test = new boolean[2 * (nP - 2)];
long code2 = code;
int buy2;
long bs;
for (int i = nP - 2; i > 0; i--){
buy2 = (BS[nP - 1 - i] > 0) ? 1 : 0;
test[nP - 3 + i] = (code2 >> ((i - 1) * 5 + 4)) == buy2;
code2 = code2 - (buy2 << ((i - 1) * 5 + 4));
bs = code2 >> ((i - 1) * 5);
test[i - 1] = bs == Math.abs(BS[nP - 1 - i]);
code2 = code2 - (bs<<(i - 1) * 5);
}
if (code2 !=0){
System.out.println("problem");
}*/// tests

        } else if (infoSize == 6) {
            long Bt = BookInfo[0]; // Best Bid position
            long At = BookInfo[1]; // Best Ask position
            long lBt = BookInfo[2] / 3; // depth at best Bid
            long lAt = BookInfo[3] / 3; // depth at best Ask
            int a = pv; // private value zero(0), negative (1), positive (2)
            code = r.nextLong();

        } else if (infoSize == 7){
            long Bt = BookInfo[0]; // Best Bid position
            long At = BookInfo[1]; // Best Ask position
            long lBt = BookInfo[2] / 2; // depth at best Bid
            long lAt = BookInfo[3] / 2; // depth at best Ask
            long dBt = (BookInfo[4] / maxDepth); // depth at buy
            int dSt = (BookInfo[5] / maxDepth); // depth at sell
            int Pt = BookInfo[6]; // last transaction pricePosition position
            int b = BookInfo[7]; // 1 if last transaction buy, 0 if sell
            int a = pv; // private value zero(0), negative (1), positive (2)
            int h = (isHFT) ? 1 : 0; // arrival frequency slow (0), fast (1)

            /*Long code = (Bt<<50) + (At<<44) + (lBt<<40) + (lAt<<36) + (dBt<<29) + (dSt<<22) + (Pt<<16) + (b<<15) +
+ (P<<9) + (q<<5) + (x<<3) + (a<<1) + l;*/
            code = r.nextLong();

        }
        else if (infoSize == 8){
            long Bt = BookInfo[0];      // Best Bid position
            long At = BookInfo[1];      // Best Ask position
            long lBt = BookInfo[2] / 2;     // depth at best Bid
            long lAt = BookInfo[3] / 2;     // depth at best Ask
            long dBt = r.nextLong(); // depth off Bid
            long dSt = r.nextLong(); // depth off Ask
            int Pt = BookInfo[6];       // last transaction pricePosition position
            int b = BookInfo[7];        // 1 if last transaction buy, 0 if sell
            q = r.nextInt();
            int a = pv;                 // private value zero(0), negative (1), positive (2)
            int l = (isHFT) ? 1 : 0;    // arrival frequency slow (0), fast (1)
            //System.out.println(Bt + " : " + lBt + " ; " + At + " : " + lAt);
            /*Long code = (Bt<<50) + (At<<44) + (lBt<<40) + (lAt<<36) + (dBt<<29) + (dSt<<22) + (Pt<<16) + (b<<15) +
+ (P<<9) + (q<<5) + (x<<3) + (a<<1) + l;*/
            code = r.nextLong();

            /*long code2 = code;
            boolean [] test = new boolean[14];
            test[0] = ((code2 >> 39) == Bt);
            code2 = code2 - (Bt<<39);

            test[1] = ((code2 >> 35) == At);
            code2 = code2 - (At<<35);

            test[2] = ((code2 >> 31) == lBt);
            code2 = code2 - (lBt<<31);

            test[3] = ((code2 >> 27) == lAt);
            code2 = code2 - (lAt<<27);

            test[4] =((code2 >> 23) == dBt);
            code2 = code2 - (dBt<<23);

            test[5] = ((code2 >> 19) == dSt);
            code2 = code2 - (dSt<<19);

            test[6] = ((code2 >> 15) == Pt);
            code2 = code2 - (Pt<<15);

            test[7] = ((code2 >> 14) == b);
            code2 = code2 - (b<<14);

            test[8] = ((code2 >> 10) == P);
            code2 = code2 - (P<<10);

            test[9] = ((code2 >> 6) == q);
            code2 = code2 - (q<<6);

            test[10] = ((code2 >> 4) == x);
            code2 = code2 - (x<<4);

            test[11] = ((code2 >> 1) == a);
            code2 = code2 - (a<<1);

            test[12] = (code2 == l);
            code2 = code2 - l;

            test[13] = (code2 == 0);
            for (int i = 0; i < 14; i++){
                if (test[i] == false){
                    System.out.println("testing hash code failed");
                }
            }*/         // tests
        } else if (infoSize == 9){
            long Bt = BookInfo[0];      // Best Bid position
            long At = BookInfo[1];      // Best Ask position
            long lBt = BookInfo[2] / 2;     // depth at best Bid
            long lAt = BookInfo[3] / 2;     // depth at best Ask
            long dBt = r.nextLong(); // depth off Bid
            long dSt = r.nextLong(); // depth off Ask
            int Pt = BookInfo[6];       // last transaction pricePosition position
            int b = BookInfo[7];        // 1 if last transaction buy, 0 if sell
            q = r.nextInt();
            int a = pv;                 // private value zero(0), negative (1), positive (2)
            int l = (isHFT) ? 1 : 0;    // arrival frequency slow (0), fast (1)
            //System.out.println(Bt + " : " + lBt + " ; " + At + " : " + lAt);
            /*Long code = (Bt<<50) + (At<<44) + (lBt<<40) + (lAt<<36) + (dBt<<29) + (dSt<<22) + (Pt<<16) + (b<<15) +
+ (P<<9) + (q<<5) + (x<<3) + (a<<1) + l;*/
            code = r.nextLong();

            /*long code2 = code;
            boolean [] test = new boolean[14];
            test[0] = ((code2 >> 42) == Bt);
            code2 = code2 - (Bt<<42);

            test[1] = ((code2 >> 37) == At);
            code2 = code2 - (At<<37);

            test[2] = ((code2 >> 33) == lBt);
            code2 = code2 - (lBt<<33);

            test[3] = ((code2 >> 29) == lAt);
            code2 = code2 - (lAt<<29);

            test[4] =((code2 >> 25) == dBt);
            code2 = code2 - (dBt<<25);

            test[5] = ((code2 >> 21) == dSt);
            code2 = code2 - (dSt<<21);

            test[6] = ((code2 >> 16) == Pt);
            code2 = code2 - (Pt<<16);

            test[7] = ((code2 >> 15) == b);
            code2 = code2 - (b<<15);

            test[8] = ((code2 >> 10) == P);
            code2 = code2 - (P<<10);

            test[9] = ((code2 >> 6) == q);
            code2 = code2 - (q<<6);

            test[10] = ((code2 >> 4) == x);
            code2 = code2 - (x<<4);

            test[11] = ((code2 >> 1) == a);
            code2 = code2 - (a<<1);

            test[12] = (code2 == l);
            code2 = code2 - l;

            test[13] = (code2 == 0);
            for (int i = 0; i < 14; i++){
                if (test[i] == false){
                    System.out.println("testing hash code failed");
                }
            }*/         // tests
        }

        return new Object();
    }
}
