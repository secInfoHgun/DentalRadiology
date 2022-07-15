package com.hgun.sti.controller;

import com.hgun.sti.components.GetCookie;
import com.hgun.sti.models.Exame;
import com.hgun.sti.models.Usuario;
import com.hgun.sti.repository.UsuarioRepository;
import com.hgun.sti.repository.tipos.InterproximalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/exame")
public class ExameController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private InterproximalRepository interproximalRepository;

//    LADO DO RADIOLOGISTA

    @PreAuthorize("hasAnyAuthority('RADIOLOGISTA')")
    @GetMapping
    public String pageListExames(Model model, HttpServletRequest request){

        return "exame/list-exame";
    }

    @PreAuthorize("hasAnyAuthority('RADIOLOGISTA')")
    @GetMapping("/view/{id}")
    public String pageExameView(@PathVariable Long id, Model model, HttpServletRequest request){

        return "exame/form-view-exame";
    }

    private List<Exame> filterPageExame(){
        return null;
    }




//    LADO DO DENTISTA

    @PreAuthorize("hasAnyAuthority('DENTISTA')")
    @GetMapping("/form")
    public String pageFormExames(Model model, HttpServletRequest request){

        var exame = new Exame();
        exame.setDentista(new GetCookie().getCookieUsuario(usuarioRepository,request));

        model.addAttribute("exame", exame);
        model.addAttribute("listInterproximais", interproximalRepository.findAll());

        return "exame/form-exame";
    }

    @PreAuthorize("hasAnyAuthority('DENTISTA')")
    @GetMapping("/salvar")
    public String salvarExames(@ModelAttribute Exame exame, Model model, HttpServletRequest request){


        return "redirect:/exame/form";
    }
}
