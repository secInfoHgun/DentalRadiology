package com.hgun.sti.controller;

import com.hgun.sti.components.GetCookie;
import com.hgun.sti.components.validator.UsuarioValidator;
import com.hgun.sti.models.Usuario;
import com.hgun.sti.models.error.UsuarioError;
import com.hgun.sti.repository.RoleRepository;
import com.hgun.sti.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;

@Controller
@PreAuthorize("hasAnyAuthority('ADMINISTRADOR')")
@RequestMapping("/dentista")
public class DentistaController {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping
    public String pageListDentistas(Model model, HttpServletRequest request){

        model.addAttribute("usuarioLogado", new GetCookie().getCookieUsuario(usuarioRepository,request));

        if(model.getAttribute("dentistas") == null){
            model.addAttribute("dentistas", usuarioRepository.findAllAtivo());
        }

        return "dentista/list-dentista";
    }

    @GetMapping("/form")
    public String pageFormDentista(Model model, HttpServletRequest request){

        if(model.getAttribute("usuarioRegister") == null){
            model.addAttribute("usuarioRegister", new Usuario());
        }
        if(model.getAttribute("erros") == null){
            model.addAttribute("erros", new UsuarioError());
        }

        model.addAttribute("roles", roleRepository.findAll());

        model.addAttribute("usuarioLogado", new GetCookie().getCookieUsuario(usuarioRepository,request));

        return "dentista/form-dentista";
    }

    @PostMapping("/salvar")
    public String saveUsuario(@ModelAttribute Usuario usuario, HttpServletRequest request, RedirectAttributes redirectAttributes) throws ParseException {
        var usuarioLogado = new GetCookie().getCookieUsuario(usuarioRepository,request);
        var usuarios = usuarioRepository.findAllAtivo();

        if(usuario.getId() == usuarioLogado.getId() && usuario.getLogin().equals(usuarioLogado.getLogin())){
            usuarios.remove(usuarioLogado);
        }

        var pessoa = usuario.getPessoa();

        usuario.setPessoa(pessoa);

        var erros = UsuarioValidator.validarUsuario(usuario, usuarios);

        if(!erros.isEmpty()){
            redirectAttributes.addFlashAttribute("usuarioRegister", usuario);
            redirectAttributes.addFlashAttribute("erros", erros);
            return "redirect:/dentista/form";
        }

        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

        usuarioRepository.save(usuario);

        return "redirect:/usuario";

    }

    @GetMapping("/edite/{id}")
    public String dentistaEdit(@PathVariable Long id, RedirectAttributes redirectAttributes) throws ParseException {
        var usuario = usuarioRepository.findById(id).get();
        var pessoa = usuario.getPessoa();
        usuario.setPessoa(pessoa);

        redirectAttributes.addFlashAttribute("usuarioRegister", usuario);
        redirectAttributes.addFlashAttribute("erros", new UsuarioError());

        return "redirect:/dentista/form";

    }

    @GetMapping("/delete/{id}")
    public String usduarioDelet(@PathVariable Long id){
        var usuario = usuarioRepository.findById(id).get();

        usuario.setAtivo(false);
        usuarioRepository.save(usuario);

        return "redirect:/dentista";
    }
}
