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
public class ResultValidate {
    
    private final Complexity complexity;
    private final int score;

    ResultValidate(Complexity complexity, int score) {
        this.complexity = complexity;
        this.score = score;
    }

    public Complexity getComplexity() {
        return complexity;
    }

    public int getScore() {
        return score;
    }
    
}
