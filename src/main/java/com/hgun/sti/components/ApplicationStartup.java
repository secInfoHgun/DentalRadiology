package com.hgun.sti.components;

import com.hgun.sti.models.Role;
import com.hgun.sti.models.Usuario;
import com.hgun.sti.repository.RoleRepository;
import com.hgun.sti.repository.UsuarioRepository;
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

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
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
            newUsuario.setAtivo(true);

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
            newUsuario.setAtivo(true);

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


        return;
    }

}