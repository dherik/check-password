/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.dherik.util;

import io.github.dherik.password.Complexity;
import io.github.dherik.password.ResultValidate;
import io.github.dherik.password.PasswordValidate;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author dherik
 */
public class PasswordValidateTest {
    
    @Test
    public void testeMuitoFraca() {
        String password = "1234";
        ResultValidate result = new PasswordValidate(password).checkPwd();
        assertEquals(Complexity.VeryWeak, result.getComplexity());
        assertEquals(4, result.getScore());
    }
    
    @Test
    public void testeMuitoFraca2() {
        String password = "11";
        ResultValidate result = new PasswordValidate(password).checkPwd();
        assertEquals(Complexity.VeryWeak, result.getComplexity());
        assertEquals(0, result.getScore());
    }
    
    @Test
    public void testeFraca() {
        String password = "1234a";
        ResultValidate result = new PasswordValidate(password).checkPwd();
        assertEquals(Complexity.Weak, result.getComplexity());
        assertEquals(38, result.getScore());
    }
    
    @Test
    public void testeBoa() {
        String password = "1234ab";
        ResultValidate result = new PasswordValidate(password).checkPwd();
        assertEquals(Complexity.Good, result.getComplexity());
        assertEquals(40, result.getScore());
    }
    
    @Test
    public void testeBoa2() {
        String password = "1234ab#";
        ResultValidate result = new PasswordValidate(password).checkPwd();
        assertEquals(Complexity.Good, result.getComplexity());
        assertEquals(52, result.getScore());
    }
    
    @Test
    public void testeBoa3() {
        String password = "AaSs3$";
        ResultValidate result = new PasswordValidate(password).checkPwd();
        assertEquals(Complexity.Good, result.getComplexity());
        assertEquals(52, result.getScore());
    }
    
    @Test
    public void testeForte() {
        
        String password = "1234ab#4";
        ResultValidate result = new PasswordValidate(password).checkPwd();
        assertEquals(Complexity.Strong, result.getComplexity());
        assertEquals(71, result.getScore());
    }
    
    @Test
    public void testeForte2() {
        
        String password = "@!@aA3";
        ResultValidate result = new PasswordValidate(password).checkPwd();
        assertEquals(Complexity.Strong, result.getComplexity());
        assertEquals(69, result.getScore());
    }
    
    @Test
    public void testeForte3() {
        
        String password = "4##@!aA";
        ResultValidate result = new PasswordValidate(password).checkPwd();
        assertEquals(Complexity.VeryStrong, result.getComplexity());
        assertEquals(83, result.getScore());
    }
    
    @Test
    public void testeForte4() {
        
        String password = "4##@!aAg";
        ResultValidate result = new PasswordValidate(password).checkPwd();
        assertEquals(Complexity.VeryStrong, result.getComplexity());
        assertEquals(99, result.getScore());
    }
    
    @Test
    public void testeForte5() {
        
        String password = "4##@!aAg4";
        ResultValidate result = new PasswordValidate(password).checkPwd();
        assertEquals(Complexity.VeryStrong, result.getComplexity());
        assertEquals(100, result.getScore());
    }
    
}
