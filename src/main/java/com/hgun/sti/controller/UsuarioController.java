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
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Set;

@Controller
@RequestMapping("/dentista")
public class UsuarioController {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR')")
    @GetMapping
    public String pageListUsuarios(Model model, HttpServletRequest request){

        model.addAttribute("usuarioLogado", new GetCookie().getCookieUsuario(usuarioRepository,request));

        if(model.getAttribute("usuarios") == null){
            model.addAttribute("usuarios", usuarioRepository.findAllAtivo());
        }

        if(model.getAttribute("filterUsuario") == null){
            model.addAttribute("filterUsuario", new Usuario());
        }

        return "usuario/list-usuario";
    }

    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR')")
    @PostMapping
    public String pageListFilterUsuarios(@ModelAttribute Usuario filterUsuario, RedirectAttributes redirectAttributes){

        if(filterUsuario.getInputFilter() != null && !filterUsuario.getInputFilter().isEmpty() && !filterUsuario.getInputFilter().equals("")) {
            redirectAttributes.addFlashAttribute("usuarios", usuarioRepository.findAll());
        }

        redirectAttributes.addFlashAttribute("filterUsuario", filterUsuario);

        return "redirect:/dentista";
    }

    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR')")
    @GetMapping("/form")
    public String pageFormUsuario(Model model, HttpServletRequest request){

        if(model.getAttribute("usuarioRegister") == null){
            model.addAttribute("usuarioRegister", new Usuario());
        }
        if(model.getAttribute("erros") == null){
            model.addAttribute("erros", new UsuarioError());
        }

        model.addAttribute("roles", roleRepository.findAll());

        model.addAttribute("usuarioLogado", new GetCookie().getCookieUsuario(usuarioRepository,request));

        return "usuario/form-usuario";
    }

    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR')")
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

        usuario.setRoles(
                Set.copyOf(
                        Arrays.asList(
                                roleRepository.findByName("DENTISTA")
                        )
                )
        );

        usuarioRepository.save(usuario);

        return "redirect:/dentista";

    }

    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR')")
    @GetMapping("/edite/{id}")
    public String usuarioEdit(@PathVariable Long id, RedirectAttributes redirectAttributes) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        var usuario = usuarioRepository.findById(id).get();
        var pessoa = usuario.getPessoa();

        usuario.setPessoa(pessoa);

        redirectAttributes.addFlashAttribute("usuarioRegister", usuario);
        redirectAttributes.addFlashAttribute("erros", new UsuarioError());

        return "redirect:/dentista/form";

    }

    @PreAuthorize("hasAnyAuthority('ADMINISTRADOR')")
    @GetMapping("/delete/{id}")
    public String usduarioDelet(@PathVariable Long id){
        var usuario = usuarioRepository.findById(id).get();

        usuario.setAtivo(false);
        usuarioRepository.save(usuario);

        return "redirect:/dentista";
    }
}
