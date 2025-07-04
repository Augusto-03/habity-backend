package com.habity.habity_backend.service;

import com.habity.habity_backend.config.JwtUtil;
import com.habity.habity_backend.dto.RegistroHabitoDTO;
import com.habity.habity_backend.entity.Habito;
import com.habity.habity_backend.entity.RegistroHabito;
import com.habity.habity_backend.mapper.RegistroHabitoMapper;
import com.habity.habity_backend.repository.HabitoRepository;
import com.habity.habity_backend.repository.RegistroHabitoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RegistroHabitoService {

    @Autowired
    private RegistroHabitoRepository registroRepository;

    @Autowired
    private HabitoRepository habitoRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public List<RegistroHabitoDTO> obtenerPorHabito(Long habitoId) {
        return registroRepository.findByHabitoId(habitoId).stream()
                .map(RegistroHabitoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public RegistroHabitoDTO crearRegistro(RegistroHabitoDTO dto) {
        RegistroHabito entity = RegistroHabitoMapper.toEntity(dto);
        Habito habito = habitoRepository.findById(dto.habitoId).orElseThrow();
        entity.setHabito(habito);
        return RegistroHabitoMapper.toDTO(registroRepository.save(entity));
    }

    public List<RegistroHabitoDTO> obtenerPorUsuario(String token) {
        String email = jwtUtil.extractUsername(token);
        List<Habito> habitos = habitoRepository.findByUsuarioEmail(email);

        return habitos.stream()
                .flatMap(h -> registroRepository.findByHabitoId(h.getId()).stream())
                .map(RegistroHabitoMapper::toDTO)
                .collect(Collectors.toList());
    }
}
