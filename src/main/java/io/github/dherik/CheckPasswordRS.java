/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.dherik;

import io.github.dherik.dto.PasswordDto;
import io.github.dherik.dto.ResultDto;
import io.github.dherik.password.Complexity;
import static spark.Spark.*;
import static io.github.dherik.util.JsonTransformer.*;
import io.github.dherik.password.PasswordValidate;
import io.github.dherik.password.ResultValidate;


/**
 *
 * @author dherik
 */
class CheckPasswordRS {
    
    public static void main(String[] args) {
        staticFileLocation("/public");
        //apenas para iniciar o spark server
        get("/hello", (req, res) -> "Avaliador de segurança para senhas");
        post("/checkPwd", (req, res) -> {
            
            String body = req.body();
            
            PasswordDto passwordDto = fromJson(body, PasswordDto.class);

            ResultValidate result = new PasswordValidate(passwordDto.getPassword()).checkPwd();
            
            final Complexity complexity = result.getComplexity();
            
            ResultDto resultDto = new ResultDto(translate(complexity), result.getScore());
            
            return toJson(resultDto);
        });
    }
    
    private static String translate(Complexity complexity) {
        if (complexity == Complexity.VeryWeak) {
            return "Muito Fraca";
        } else if (complexity == Complexity.Weak) {
            return "Fraca";
        } else if (complexity == Complexity.Good) {
            return "Boa";
        } else if (complexity == Complexity.Strong) {
            return "Forte";
        } 
        return "Muito Forte";
    }
}
