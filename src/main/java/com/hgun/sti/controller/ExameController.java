package com.hgun.sti.controller;

import com.hgun.sti.components.GetCookie;
import com.hgun.sti.models.Exame;
import com.hgun.sti.repository.ExameRepository;
import com.hgun.sti.repository.UsuarioRepository;
import com.hgun.sti.repository.tipos.InterproximalRepository;
import com.hgun.sti.repository.tipos.PeriapicalRepository;
import com.hgun.sti.repository.tipos.TomografiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/exame")
public class ExameController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ExameRepository exameRepository;

    @Autowired
    private InterproximalRepository interproximalRepository;

    @Autowired
    private PeriapicalRepository periapicalRepository;

    @Autowired
    private TomografiaRepository tomografiaRepository;

//    LADO DO RADIOLOGISTA
    @PreAuthorize("hasAnyAuthority('RADIOLOGISTA')")
    @GetMapping
    public String pageListExames(Model model, HttpServletRequest request){

        model.addAttribute("usuarioLogado", new GetCookie().getCookieUsuario(usuarioRepository,request));

        if(model.getAttribute("exames") == null){
            model.addAttribute("exames", exameRepository.findAllAtivo());
        }

        if(model.getAttribute("filtro") == null){
            model.addAttribute("filtro", new Exame());
        }

        return "exame/list-exame";
    }

    @PreAuthorize("hasAnyAuthority('RADIOLOGISTA')")
    @PostMapping
    public String filterPageListExames(@ModelAttribute Exame filter, RedirectAttributes redirectAttributes) throws ParseException {
        Date dataInicio = null;
        Date dataFim;

        if(!filter.getDataInicioFilter().isEmpty() && filter.getDataInicioFilter() != null){
            dataInicio = new SimpleDateFormat("yyyy-MM-dd").parse(filter.getDataInicioFilter());
        }
        if(!filter.getDataFimFilter().isEmpty() && filter.getDataFimFilter() != null){
            dataFim = new SimpleDateFormat("yyyy-MM-dd").parse(LocalDate.parse(filter.getDataFimFilter()).plusDays(1).toString());
        }else{
            dataFim = new SimpleDateFormat("yyyy-MM-dd").parse(LocalDate.now().plusDays(1).toString());
        }

        var exames = exameRepository.findByDataBetween(dataInicio,dataFim);

        if(!filter.getAtivoFilter().isEmpty() && filter.getAtivoFilter() != null){
            var filtro = new ArrayList<Exame>();
            var ativo = Boolean.parseBoolean(filter.getAtivoFilter());
            var examesByStatus = exameRepository.findByStatus(ativo);

            for(var exame : exames){
                for(var exameByStatu : examesByStatus){
                    if(exame.getId() == exameByStatu.getId()){
                        filtro.add(exame);
                    }
                }
            }

            exames = filtro;
        }

        redirectAttributes.addFlashAttribute("exames", exames);
        redirectAttributes.addFlashAttribute("filtro", filter);

        return "redirect:/exame";
    }

    @GetMapping("/alterarStatus/{id}")
    public String alterarStatus(@PathVariable(name = "id") Long id) {
        var exame = exameRepository.findById(id).get();
        exame.setStatus(!exame.getStatus());
        exameRepository.save(exame);
        return "redirect:/exame/view/" + id;
    }

    @PreAuthorize("hasAnyAuthority('RADIOLOGISTA')")
    @GetMapping("/view/{id}")
    public String pageExameView(@PathVariable Long id, Model model, HttpServletRequest request){

        model.addAttribute("usuarioLogado", new GetCookie().getCookieUsuario(usuarioRepository,request));

        model.addAttribute("exame", exameRepository.findById(id).get());
        model.addAttribute("listInterproximais", interproximalRepository.findAll());
        model.addAttribute("listPeriapicais", periapicalRepository.findAll());
        model.addAttribute("listTomografias", tomografiaRepository.findAll());

        return "exame/form-view-exame";
    }


//    LADO DO DENTISTA
    @PreAuthorize("hasAnyAuthority('DENTISTA')")
    @GetMapping("/form")
    public String pageFormExames(Model model, HttpServletRequest request){

        var exame = new Exame();
        exame.setDentista(new GetCookie().getCookieUsuario(usuarioRepository,request));
        exame.setDataSolicitacao(new Date());

        model.addAttribute("exame", exame);
        model.addAttribute("listInterproximais", interproximalRepository.findAll());
        model.addAttribute("listPeriapicais", periapicalRepository.findAll());
        model.addAttribute("listTomografias", tomografiaRepository.findAll());

        return "exame/form-exame";
    }

    @PreAuthorize("hasAnyAuthority('DENTISTA')")
    @PostMapping("/salvar")
    public String salvarExames(@ModelAttribute Exame exame, RedirectAttributes redirectAttributes){

        exame.setDataSolicitacao(new Date());
        exameRepository.save(exame);

        redirectAttributes.addFlashAttribute("cadastrou", true);

        return "redirect:/exame/form";
    }
}
