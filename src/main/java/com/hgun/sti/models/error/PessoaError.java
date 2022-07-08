package com.hgun.sti.models.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PessoaError {
    private String nome;
    private String cpf;
    private String idade;
    private String preccp;

    public boolean isEmpty(){
        if(nome != null && !nome.isEmpty()){
            return false;
        }else if(cpf != null && !cpf.isEmpty()){
            return false;
        }else if(idade != null && !idade.isEmpty()){
            return false;
        }else if(preccp != null && !preccp.isEmpty()){
            return false;
        }else{
            return true;
        }
    }
}
