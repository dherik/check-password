/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.dherik.password;

import io.github.dherik.password.sequential.CheckSequential;
import spark.utils.StringUtils;

/**
 *
 * @author dherik
 */
public class PasswordValidate {

    private static final int MAX_SCORE = 100;
    private static final int MIN_SCORE = 0;
    private static final int N_MULT_LENGTH = 4;

    private static final int N_MIN_PWD_LENGTH = 8;
    private static final String REGEX_ALPHA_UPPER = "[A-Z]";
    private static final String REGEX_ALPHA_LOWER = "[a-z]";
    private static final String REGEX_NUMBER = "[0-9]";
    private static final String REGEX_SYMBOL = "[^a-zA-Z0-9_]";

    private static final int nMultSymbol = 6;
    private static final int nMultNumber = 4;
    private static final int nMultMidChar = 2;
    
    private static final String[] arrCharsIds = {"nLength", "nAlphaUC", "nAlphaLC", "nNumber", "nSymbol"};

    private final String pwd;
    private final int nLength;
    private int nScore;
    private final String[] arrPwd;

    public PasswordValidate(String password) {
        this.pwd = password;
        this.nLength = pwd.length();
        this.nScore = initScore();
        this.arrPwd = pwd.replace("\\s+", "").split("\\s*");
    }

    public ResultValidate checkPwd() {

        if (StringUtils.isEmpty(pwd)) {
            return new ResultValidate(Complexity.VeryWeak, 0);
        }

        int nConsecAlphaUC = 0, nConsecAlphaLC = 0, nConsecNumber = 0, nConsecSymbol = 0;
        int nAlphaUC = 0, nAlphaLC = 0, nNumber = 0, nSymbol = 0, nMidChar = 0;

        CountPatternMatch countUP = counterPatternMatches(REGEX_ALPHA_UPPER, nConsecAlphaUC, nAlphaUC, nMidChar);
        
        CountPatternMatch countLC = counterPatternMatches(REGEX_ALPHA_LOWER, nConsecAlphaLC, nAlphaLC, nMidChar);
        
        CountPatternMatch countNumber = counterPatternMatches(REGEX_NUMBER, nConsecNumber, nNumber, nMidChar);
        nMidChar = countNumber.getnMidChar();
        
        CountPatternMatch countSymbol = counterPatternMatches(REGEX_SYMBOL, nConsecSymbol, nSymbol, nMidChar);
        nMidChar = countSymbol.getnMidChar();

        NumberMatches numberMatches = createNumberMatches(countLC, countUP, countSymbol, countNumber, nMidChar);
        
        nScore = applyGeneralPontAssignment(numberMatches);
        
        nScore = applyPointDeductionsForPoorPractices(numberMatches);
        
        nScore = checkSequentialChars(countLC, countUP, countNumber);

        nScore = verifyOneOrMoreRequiredCharsExist(numberMatches, nScore);

        return createFinalResult(nScore);
    }
    
    private RepCount countRepeated() {
        int nRepInc = 0, nRepChar = 0;
        for (int a = 0; a < arrPwd.length; a++) {
            RepCount nRep = calculateRepeatIncrement(nRepInc, nRepChar, a);
            nRepChar = nRep.getnRepChar();
            nRepInc = nRep.getnRepInc();
        }
        return new RepCount(nRepInc, nRepChar);
    }

    private int checkSequentialChars(CountPatternMatch countLC, CountPatternMatch countUP, CountPatternMatch countNumber) {
        int nConsecAlphaLC = countLC.getnConsec();
        int nConsecAlphaUC = countUP.getnConsec();
        int nConsecNumber = countNumber.getnConsec();
        return new CheckSequential(pwd, nScore).verifySequential(nConsecAlphaUC, nConsecAlphaLC, nConsecNumber);
    }

    private NumberMatches createNumberMatches(CountPatternMatch forCountLC, CountPatternMatch forCountUP, CountPatternMatch forCountSymbol, CountPatternMatch forCountNumber, int nMidChar) {
        int nAlphaLC = forCountLC.getN();
        int nAlphaUC = forCountUP.getN();
        int nSymbol = forCountSymbol.getN();
        int nNumber = forCountNumber.getN();
        return new NumberMatches(nAlphaUC, nAlphaLC, nNumber, nSymbol, nMidChar);
    }
    
    private CountPatternMatch counterPatternMatches(String regex, int nConsec, int n, int nMidChar) {
        Integer nTmp = null;
        for (int a = 0; a < arrPwd.length; a++) {
            if (arrPwd[a].matches(regex)) {
                nMidChar = countMidChar(a, arrPwd.length, nMidChar);
                NCount nc = countNConsec(nTmp, nConsec, a);
                nConsec = nc.getnConsec();
                nTmp = nc.getnTmp();
                n++;
            }
        }
        return new CountPatternMatch(nConsec, nTmp, n, nMidChar);
    }
    
    private RepCount calculateRepeatIncrement(int nRepInc, int nRepChar, int a) {
        /* Internal loop through password to check for repeat characters */
        boolean bCharExists = false;
        for (int b = 0; b < arrPwd.length; b++) {
            if (isRepeatedChar(arrPwd, a, b)) { /* repeat character exists */

                bCharExists = true;
                nRepInc = calculateIncrementDeduction(nRepInc, arrPwd.length, b, a);
            }
        }

        if (bCharExists) {
            nRepChar++;
            nRepInc = calculateIncrementDeduction(arrPwd.length, nRepChar, nRepInc);
        }
        
        return new RepCount(nRepInc, nRepChar);
    }

    private NCount countNConsec(Integer nTmp, int nConsec, int a) {
        if (nTmp != null) {
            if ((nTmp + 1) == a) {
                nConsec++;
            }
        }
        nTmp = a;
        return new NCount(nConsec, nTmp);
    }

    private int countMidChar(int a, int arrPwdLen, int nMidChar) {
        if (a > 0 && a < (arrPwdLen - 1)) {
            nMidChar++;
        }
        return nMidChar;
    }

    private int calculateMinimumRequiredChars() {
        int nMinReqChars;
        if (nLength >= N_MIN_PWD_LENGTH) {
            nMinReqChars = 3;
        } else {
            nMinReqChars = 4;
        }
        return nMinReqChars;
    }

    private int calculateIncrementDeduction(int arrPwdLen, int nRepChar, int nRepInc) {
        int nUnqChar;
        nUnqChar = arrPwdLen - nRepChar;
        nRepInc = (int) ((nUnqChar != 0) ? Math.ceil((double) nRepInc / (double) nUnqChar) : Math.ceil((double) nRepInc));
        return nRepInc;
    }

    private int verifyOneOrMoreRequiredCharsExist(NumberMatches numberMatches, int nScore) {
        int nRequirements = verifyMandatoryRequirements(numberMatches);
        int nMinReqChars = calculateMinimumRequiredChars();
        if (nRequirements > nMinReqChars) {  // One or more required characters exist
            nScore = (nScore + (nRequirements * 2));
        }
        return nScore;
    }

    private int initScore() {
        return nLength * N_MULT_LENGTH;
    }

    private static boolean isRepeatedChar(String[] arrPwd, int a, int b) {
        return arrPwd[a].equals(arrPwd[b]) && a != b;
    }

    private int applyGeneralPontAssignment(NumberMatches numberMatches) {
        int nAlphaUC = numberMatches.getnAlphaUC();
        /* General point assignment */
        if (nAlphaUC > 0 && nAlphaUC < nLength) {
            nScore = (nScore + ((nLength - nAlphaUC) * 2));
        }
        int nAlphaLC = numberMatches.getnAlphaLC();
        if (nAlphaLC > 0 && nAlphaLC < nLength) {
            nScore = (nScore + ((nLength - nAlphaLC) * 2));
        }
        int nNumber = numberMatches.getnNumber();
        if (nNumber > 0 && nNumber < nLength) {
            nScore = (nScore + (nNumber * nMultNumber));
        }
        int nSymbol = numberMatches.getnSymbol();
        if (nSymbol > 0) {
            nScore = (nScore + (nSymbol * nMultSymbol));
        }
        int nMidChar = numberMatches.getnMidChar();
        if (nMidChar > 0) {
            nScore = (nScore + (nMidChar * nMultMidChar));
        }
        return nScore;
    }

    private int applyPointDeductionsForPoorPractices(NumberMatches numberMatches) {
        RepCount repCount = countRepeated();
        /* Point deductions for poor practices */
        if ((numberMatches.getnAlphaLC() > 0 || numberMatches.getnAlphaUC() > 0) && numberMatches.getnSymbol() == 0 && numberMatches.getnNumber() == 0) {  // Only Letters
            nScore = (nScore - nLength);
        }
        if (numberMatches.getnAlphaLC() == 0 && numberMatches.getnAlphaUC() == 0 && numberMatches.getnSymbol() == 0 && numberMatches.getnNumber() > 0) {  // Only Numbers
            nScore = (nScore - nLength);
        }
        if (repCount.getnRepChar() > 0) {  // Same character exists more than once
            nScore = (nScore - repCount.getnRepInc());
        }
        return nScore;
    }


    private int verifyMandatoryRequirements(NumberMatches numberMatches) {
        
        int nAlphaLC = numberMatches.getnAlphaLC();
        int nAlphaUC = numberMatches.getnAlphaUC();
        int nNumber = numberMatches.getnNumber();
        int nSymbol = numberMatches.getnSymbol();
                
        int[] arrChars = {nLength, nAlphaUC, nAlphaLC, nNumber, nSymbol};
        int arrCharsLen = arrChars.length;
        int nReqChar = 0;
        for (int c = 0; c < arrCharsLen; c++) {
            int minVal;
            if ("nLength".equals(arrCharsIds[c])) {
                minVal = (N_MIN_PWD_LENGTH - 1);
            } else {
                minVal = 0;
            }
            if (arrChars[c] == (minVal + 1)) {
                nReqChar++;
            } else if (arrChars[c] > (minVal + 1)) {
                nReqChar++;
            }
        }
        return nReqChar;
    }
    

    private int calculateIncrementDeduction(int nRepInc, int arrPwdLen, int b, int a) {
        /*
         Calculate icrement deduction based on proximity to identical characters
         Deduction is incremented each time a new match is discovered
         Deduction amount is based on total password length divided by the
         difference of distance between currently selected match
         */
        nRepInc += Math.abs(arrPwdLen / (b - a));
        return nRepInc;
    }

    private static ResultValidate createFinalResult(int nScore) {
        ResultValidate result = null;
        /* Determine complexity based on overall score */
        if (nScore > MAX_SCORE) {
            nScore = MAX_SCORE;
        } else if (nScore < MIN_SCORE) {
            nScore = MIN_SCORE;
        }
        if (isVeryWeakRange(nScore)) {
            result = new ResultValidate(Complexity.VeryWeak, nScore);
        } else if (isWeakRange(nScore)) {
            result = new ResultValidate(Complexity.Weak, nScore);
        } else if (isGoodRange(nScore)) {
            result = new ResultValidate(Complexity.Good, nScore);
        } else if (isStrongRange(nScore)) {
            result = new ResultValidate(Complexity.Strong, nScore);
        } else if (isVeryStrongRange(nScore)) {
            result = new ResultValidate(Complexity.VeryStrong, nScore);
        }

        return result;
    }

    private static boolean isVeryWeakRange(int nScore) {
        return nScore >= MIN_SCORE && nScore < 20;
    }

    private static boolean isVeryStrongRange(int nScore) {
        return (nScore >= 80 && nScore <= MAX_SCORE);
    }

    private static boolean isStrongRange(int nScore) {
        return nScore >= 60 && nScore < 80;
    }

    private static boolean isGoodRange(int nScore) {
        return nScore >= 40 && nScore < 60;
    }

    private static boolean isWeakRange(int nScore) {
        return nScore >= 20 && nScore < 40;
    }

}

class CountPatternMatch {
    
    private final int nConsec;
    private final Integer nTmp;
    private final int n;
    private final int nMidChar;

    CountPatternMatch(int nConsec, Integer nTmp, int n, int nMidChar) {
        this.nConsec = nConsec;
        this.nTmp = nTmp;
        this.n = n;
        this.nMidChar = nMidChar;
    }

    int getN() {
        return n;
    }

    int getnConsec() {
        return nConsec;
    }

    public Integer getnTmp() {
        return nTmp;
    }

    int getnMidChar() {
        return nMidChar;
    }

}

class RepCount {
    private final int nRepInc;
    private final int nRepChar;

    RepCount(int nRepInc, int nRepChar) {
        this.nRepChar = nRepChar;
        this.nRepInc = nRepInc;
    }

    int getnRepChar() {
        return nRepChar;
    }

    int getnRepInc() {
        return nRepInc;
    }

}

class NCount {
    private final int nConsec;
    private final Integer nTmp;

    NCount(int nConsec, Integer nTmp) {
        this.nConsec = nConsec;
        this.nTmp = nTmp;
    }

    int getnConsec() {
        return nConsec;
    }

    Integer getnTmp() {
        return nTmp;
    }
    
}
