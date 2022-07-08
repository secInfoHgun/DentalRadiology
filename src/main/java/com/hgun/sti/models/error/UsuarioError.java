package com.hgun.sti.models.error;

import com.hgun.sti.models.Pessoa;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UsuarioError {
    private String login;
    private String senha;
    private String senhaConfirmada;
    private PessoaError pessoaError;

    public boolean isEmpty(){
        if(login != null && !login.isEmpty()){
            return false;
        }else if(senha != null && !senha.isEmpty()){
            return false;
        }else if(senhaConfirmada != null && !senhaConfirmada.isEmpty()){
            return false;
        }else if(pessoaError.isEmpty()){
            return false;
        }else{
            return true;
        }
    }
}
