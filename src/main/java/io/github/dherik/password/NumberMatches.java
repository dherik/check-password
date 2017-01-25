/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.dherik.password;

/**
 *
 * @author dherik
 */
class NumberMatches {

    private final int nAlphaUC;
    private final int nAlphaLC;
    private final int nNumber;
    private final int nSymbol;
    private final int nMidChar;

    NumberMatches(int nAlphaUC, int nAlphaLC, int nNumber, int nSymbol, int nMidChar) {
        this.nAlphaUC = nAlphaUC;
        this.nAlphaLC = nAlphaLC;
        this.nNumber = nNumber;
        this.nSymbol = nSymbol;
        this.nMidChar = nMidChar;
    }

    /**
     * @return the nAlphaUC
     */
    int getnAlphaUC() {
        return nAlphaUC;
    }

    /**
     * @return the nAlphaLC
     */
    int getnAlphaLC() {
        return nAlphaLC;
    }

    /**
     * @return the nNumber
     */
    int getnNumber() {
        return nNumber;
    }

    /**
     * @return the nSymbol
     */
    int getnSymbol() {
        return nSymbol;
    }

    /**
     * @return the nMidChar
     */
    int getnMidChar() {
        return nMidChar;
    }

}
