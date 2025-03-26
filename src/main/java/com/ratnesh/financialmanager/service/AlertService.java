package com.ratnesh.financialmanager.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ratnesh.financialmanager.dto.AlertDTO;
import com.ratnesh.financialmanager.mapper.AlertMapper;
import com.ratnesh.financialmanager.model.Alert;
import com.ratnesh.financialmanager.repository.AlertRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AlertService {
    
    private final AlertRepository alertRepository;
    private final AlertMapper alertMapper;

    public List<AlertDTO> getAllAlerts() {
        return alertRepository.findAll().stream()
                .map(alertMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<AlertDTO> getAlertById(UUID id) {
        return alertRepository.findById(id).map(alertMapper::toDTO);
    }

    @Transactional
    public AlertDTO createAlert(AlertDTO alertDTO) {
        Alert alert = alertMapper.toEntity(alertDTO);
        Alert savedAlert = alertRepository.save(alert);
        return alertMapper.toDTO(savedAlert);
    }

    @Transactional
    public void deleteAlert(UUID id) {
        alertRepository.deleteById(id);
    }
}
