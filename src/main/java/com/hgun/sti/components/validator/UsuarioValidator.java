package com.hgun.sti.components.validator;

import com.hgun.sti.models.Usuario;
import com.hgun.sti.models.error.UsuarioError;

import java.util.List;

public class UsuarioValidator {
    public static UsuarioError validarUsuario(Usuario usuario, List<Usuario> usuariosAtivos) {

        var usuarioError = new UsuarioError();

        usuarioError.setPessoaError(PessoaValidator.validarPessoa(usuario.getPessoa()));

        if (usuario.getLogin() == null || usuario.getLogin().isEmpty()) {
            usuarioError.setLogin("O login não pode ser vazio!");
        }else if(usuario.getLogin().length() < 4){
            usuarioError.setLogin("O login do usuário está muito curto! (min: 4 caracteres)");
        }else{
            for (Usuario bdUser : usuariosAtivos) {
                if(bdUser.getLogin().equals(usuario.getLogin())){
                    usuarioError.setLogin("O login já existente!");
                    break;
                }
            }
        }

        if (usuario.getSenha() == null || usuario.getSenha().isEmpty()) {
            usuarioError.setSenha("A senha não pode ser vazia!");
        }

        if (usuario.getSenhaConfirmada() == null || usuario.getSenhaConfirmada().isEmpty()) {
            usuarioError.setSenhaConfirmada("A senha não pode ser vazia!");
        }else if(!usuario.getSenhaConfirmada().equals(usuario.getSenha())){
            usuarioError.setSenhaConfirmada("As senhas devem ser iguais!");
        }

        return usuarioError;
    }
}
