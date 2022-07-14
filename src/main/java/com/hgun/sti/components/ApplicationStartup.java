package com.hgun.sti.components;

import com.hgun.sti.models.Role;
import com.hgun.sti.models.Usuario;
import com.hgun.sti.models.tipos.Dente;
import com.hgun.sti.models.tipos.Interproximal;
import com.hgun.sti.models.tipos.Periapical;
import com.hgun.sti.models.tipos.Tomografia;
import com.hgun.sti.repository.RoleRepository;
import com.hgun.sti.repository.UsuarioRepository;
import com.hgun.sti.repository.tipos.DenteRepository;
import com.hgun.sti.repository.tipos.InterproximalRepository;
import com.hgun.sti.repository.tipos.PeriapicalRepository;
import com.hgun.sti.repository.tipos.TomografiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private InterproximalRepository interproximalRepository;

    @Autowired
    private PeriapicalRepository periapicalRepository;

    @Autowired
    private TomografiaRepository tomografiaRepository;

    @Autowired
    private DenteRepository denteRepository;

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {

        var listInterproximal = interproximalRepository.findAll();
        var listPeriapical = periapicalRepository.findAll();
        var listTomografia = tomografiaRepository.findAll();
        var listDente = denteRepository.findAll();

        var roles = roleRepository.findAll();

        String[] roleLabes = { "ADMINISTRADOR" , "DENTISTA" , "RADIOLOGISTA" };

        if(roles.isEmpty()){

            for (String i: roleLabes) {
                var newRole =  new Role();

                newRole.setName(i);
                newRole.setAtivo(true);

                roleRepository.save(newRole);
            }

            System.out.println("cadastrei as roles");
        }

        var odontoadm = usuarioRepository.getUsuarioByLogin("odontoadm");
        var odontoradio = usuarioRepository.getUsuarioByLogin("odontoradio");

        if(odontoadm == null){
            var newUsuario = new Usuario();

            newUsuario.setLogin("odontoadm");
            newUsuario.setSenha("$2a$12$7uVG6t0frbesgmtYIQ.Sf.37xPJduv189D1R2cRNi65pdAuG48Awa");
            newUsuario.setRoles(
                    Set.copyOf(
                            Arrays.asList(
                                    roleRepository.findByName("ADMINISTRADOR")
                            )
                    )
            );

            usuarioRepository.save(newUsuario);

            System.out.println("cadastrei o usuario de odontoadm");
        }
        if(odontoradio == null){
            var newUsuario = new Usuario();

            newUsuario.setLogin("odontoradio");
            newUsuario.setSenha("$2a$12$7uVG6t0frbesgmtYIQ.Sf.37xPJduv189D1R2cRNi65pdAuG48Awa");
            newUsuario.setRoles(
                    Set.copyOf(
                            Arrays.asList(
                                    roleRepository.findByName("RADIOLOGISTA")
                            )
                    )
            );

            usuarioRepository.save(newUsuario);

            System.out.println("cadastrei o usuario de odontoradio");
        }

        if(listInterproximal.isEmpty()){

            String[] nomes = { "Molares" , "Pré-molares" };

            for (String i: nomes) {
                var interproximal =  new Interproximal();
                interproximal.setNome(i);
                interproximalRepository.save(interproximal);
            }

            System.out.println("cadastrei os tipos de interproximal");
        }

        if(listPeriapical.isEmpty()){

            String[] nomes = { "Exame preriapical completo" , "Exame preriapical completo, com interproximais" , "Dentes assinalados"};

            for (String i: nomes) {
                var periapical =  new Periapical();
                periapical.setNome(i);
                periapicalRepository.save(periapical);
            }

            System.out.println("cadastrei os tipos de periapicais");
        }

        if(listTomografia.isEmpty()){

            String[] nomes = { "Total" , "Maxila" , "Mandíbula" , "Região" , "Endodontia / Fratura / Perfuração Radicular / Canais Acessórios" , "Lesão" , "Outros"};

            for (String i: nomes) {
                var tomografia =  new Tomografia();
                tomografia.setNome(i);
                tomografiaRepository.save(tomografia);
            }

            System.out.println("cadastrei os tipos de tomografia");
        }

        if(listDente.isEmpty()){
            int[] nomes = {
                    11,12,13,14,15,16,17,18,
                    21,22,23,24,25,26,27,28,
                    31,32,33,34,35,36,37,38,
                    41,42,43,44,45,46,47,48,
                    51,52,53,54,55,
                    61,62,63,64,65,
                    71,72,73,74,75,
                    81,82,83,84,85
            };

            for (int i: nomes) {
                var dente =  new Dente();
                dente.setNumero(i);
                denteRepository.save(dente);
            }

            System.out.println("cadastrei todos os tipos de dente");
        }

        return;
    }

}