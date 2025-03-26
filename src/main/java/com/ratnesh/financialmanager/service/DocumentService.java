package com.ratnesh.financialmanager.service;
import com.ratnesh.financialmanager.dto.*;
import com.ratnesh.financialmanager.repository.*;
import com.ratnesh.financialmanager.mapper.*;
import com.ratnesh.financialmanager.model.Document;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentService {
    
    private final DocumentRepository documentRepository;
    private final DocumentMapper documentMapper;
    
    public List<DocumentDTO> getAllDocuments() {
        return documentRepository.findAll().stream()
                .map(documentMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    public Optional<DocumentDTO> getDocumentById(UUID id) {
        return documentRepository.findById(id).map(documentMapper::toDTO);
    }
    
    @Transactional
    public DocumentDTO saveDocument(DocumentDTO documentDTO) {
        Document document = documentMapper.toEntity(documentDTO);
        return documentMapper.toDTO(documentRepository.save(document));
    }
    
    @Transactional
    public void deleteDocument(UUID id) {
        documentRepository.deleteById(id);
    }
    
    public List<DocumentDTO> getDocumentsByFamilyId(UUID familyId) {
        return documentRepository.findByFamily_Id(familyId).stream()
                .map(documentMapper::toDTO)
                .collect(Collectors.toList());
    }
}