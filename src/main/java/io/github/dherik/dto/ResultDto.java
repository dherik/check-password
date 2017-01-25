/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.dherik.dto;

/**
 *
 * @author dherik
 */
public class ResultDto {
    
    private final String complexity;
    private final int score;

    public ResultDto(String complexity, int score) {
        this.complexity = complexity;
        this.score = score;
    }

    public String getComplexity() {
        return complexity;
    }

    public int getScore() {
        return score;
    }
}
