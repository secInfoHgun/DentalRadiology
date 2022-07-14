package com.hgun.sti.controller;


import com.hgun.sti.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.springframework.web.util.WebUtils.getCookie;

@Controller
public class LoginController {

    @Autowired
    public UsuarioRepository usuarioRepository;

    @GetMapping("/")
    public String rotabarra(){
        return "redirect:entrar";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:login";
    }

    @GetMapping("/entrar")
    public String entra(HttpServletRequest request){
        var usuario = this.usuarioRepository.findById(
                Long.parseLong(
                        getCookie(request, "userId").getValue()
                )
        ).get();

        var roles = usuario.getRoles();

        for (var role : roles) {
            if(role.getName().equals("ADMINISTRADOR")){
                return "redirect:/dentista";
            }else if(role.getName().equals("DENTISTA")){
                return "redirect:/exame/form";
            }else if(role.getName().equals("RADIOLOGISTA")){
                return "redirect:/exame";
            }else{
                return "redirect:/error";
            }
        }
        return "redirect:/error";
    }
}
