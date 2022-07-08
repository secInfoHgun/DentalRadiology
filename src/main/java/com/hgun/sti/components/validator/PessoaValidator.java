package com.hgun.sti.components.validator;

import com.hgun.sti.models.Pessoa;
import com.hgun.sti.models.error.PessoaError;

public class PessoaValidator {
    public static PessoaError validarPessoa(Pessoa pessoa) {

        var pessoaError = new PessoaError();

        if (pessoa.getNome() == null || pessoa.getNome().isEmpty()) {
            pessoaError.setNome("O nome não pode ser vazio!");
        }else if(pessoa.getNome().length() < 4){
            pessoaError.setNome("O nome está muito curto! (min: 4 caracteres)");
        }

        if (pessoa.getCpf() != null && !pessoa.getCpf().isEmpty()) {
            if(!pessoa.getCpf().matches("(^\\d{3}\\.\\d{3}\\.\\d{3}\\-\\d{2}$)")){
                pessoaError.setCpf("O CPF está inválido!");
            }
        }

        if (pessoa.getPreccp() != null && !pessoa.getPreccp().isEmpty()) {
            if(!pessoa.getPreccp().matches("^[\\$]?[-+]?[\\d\\.,]*[\\.,]?\\d+$")){
                pessoaError.setPreccp("O PRECCP deve conter apenas números!");
            }
        }

        if(pessoa.getIdade() == null || pessoa.getIdade() <= 0){
            pessoaError.setIdade("A idade não pode ser vazio ou igual a 0!");
        }

        return pessoaError;
    }

}
