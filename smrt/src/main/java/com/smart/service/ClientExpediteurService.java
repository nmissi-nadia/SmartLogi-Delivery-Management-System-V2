package com.smart.service;

import com.smart.entity.ClientExpediteur;
import com.smart.repository.ClientExpediteurRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ClientExpediteurService {
    private final ClientExpediteurRepository clientRepository;

    public ClientExpediteurService(ClientExpediteurRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<ClientExpediteur> getAllClients() { return clientRepository.findAll(); }
    public ClientExpediteur createClient(ClientExpediteur client) { return clientRepository.save(client); }
}
