/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.dherik.password.sequential;

/**
 *
 * @author dherik
 */
public class CheckSequential {

    private static final String S_ALPHAS = "abcdefghijklmnopqrstuvwxyz";
    private static final String S_NUMERICS = "01234567890";
    private static final String S_SYMBOLS = ")!@#$%^&*()";

    private static final int nMultConsecAlphaUC = 2;
    private static final int nMultConsecAlphaLC = 2;
    private static final int nMultConsecNumber = 2;
    private static final int nMultSeqAlpha = 3;
    private static final int nMultSeqNumber = 3;
    private static final int nMultSeqSymbol = 3;

    private static final int MAX_NUMERIC = 8;
    private static final int MAX_ALPHA = 23;
    private static final int MAX_SYMBOL = 8;

    private final String pwd;
    private int nScore;

    public CheckSequential(String pwd, int nScore) {
        this.pwd = pwd;
        this.nScore = nScore;
    }

    private int checkForSequentialAlphaPattern() {
        /* Check for sequential alpha string patterns (forward and reverse) */
        int nSeqAlpha = 0;
        for (int s = 0; s < MAX_ALPHA; s++) {
            String sFwd = S_ALPHAS.substring(s, s + 3);
            String sRev = new StringBuilder(sFwd).reverse().toString();
            if (IsInPassword(sFwd) || IsInPassword(sRev)) {
                nSeqAlpha++;
            }
        }
        return nSeqAlpha;
    }

    private int checkForSequentialNumericPattern() {
        /* Check for sequential numeric string patterns (forward and reverse) */
        int nSeqNumber = 0;
        for (int s = 0; s < MAX_NUMERIC; s++) {
            String sFwd = S_NUMERICS.substring(s, (s + 3));
            String sRev = new StringBuilder(sFwd).reverse().toString();
            if (IsInPassword(sFwd) || IsInPassword(sRev)) {
                nSeqNumber++;
            }
        }
        return nSeqNumber;
    }

    private int checkForSequentialSymbolPattern() {
        /* Check for sequential symbol string patterns (forward and reverse) */
        int nSeqSymbol = 0;
        for (int s = 0; s < MAX_SYMBOL; s++) {
            String sFwd = S_SYMBOLS.substring(s, (s + 3));
            String sRev = new StringBuilder(sFwd).reverse().toString();
            if (IsInPassword(sFwd) || IsInPassword(sRev)) {
                nSeqSymbol++;
            }
        }
        return nSeqSymbol;
    }

    private boolean IsInPassword(String string) {
        return pwd.toLowerCase().contains(string);
    }

    public int verifySequential(int nConsecAlphaUC, int nConsecAlphaLC, int nConsecNumber) {
        int nSeqAlpha = checkForSequentialAlphaPattern();
        int nSeqNumber = checkForSequentialNumericPattern();
        int nSeqSymbol = checkForSequentialSymbolPattern();
        nScore = verifySequentialFrom(nConsecAlphaUC, nMultConsecAlphaUC);
        nScore = verifySequentialFrom(nConsecAlphaLC, nMultConsecAlphaLC);
        nScore = verifySequentialFrom(nConsecNumber, nMultConsecNumber);
        nScore = verifySequentialFrom(nSeqAlpha, nMultSeqAlpha);
        nScore = verifySequentialFrom(nSeqNumber, nMultSeqNumber);
        nScore = verifySequentialFrom(nSeqSymbol, nMultSeqSymbol);
        return nScore;
    }

    private int verifySequentialFrom(int nSeqSymbol, int nMultSeqSymbol) {
        if (nSeqSymbol > 0) {  // Sequential symbol strings exist (3 characters or more)
            nScore = (nScore - (nSeqSymbol * nMultSeqSymbol));
        }
        return nScore;
    }

}
