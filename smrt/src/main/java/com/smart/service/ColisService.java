package com.smart.service;

import com.smart.dto.ColisDTO;
import com.smart.dto.ColisProduitDTO;
import com.smart.dto.ColisRequestDTO;
import com.smart.entity.*;
import com.smart.entity.Enum.PrioriteEnum;
import com.smart.entity.Enum.StatutColis;
import com.smart.mapper.*;
import com.smart.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ColisService {
    
    private final ColisRepository colisRepository;
    private final LivreurRepository livreurRepository;
    private final HistoriqueLivraisonRepository historiqueLivraisonRepository;
    private final ColisMapper colisMapper;
    private final HistoriqueLivraisonMapper historiqueLivraisonMapper;
    private final ColisProduitRepository colisProduitRepository;
    private final ProduitRepository produitRepository;
    private final DestinataireRepository destinataireRepository;
    private final ClientExpediteurRepository clientExpediteurRepository;
    private final ZoneRepository zoneRepository;
    private final ProduitMapper produitMapper;
    private final ColisProduitMapper colisProduitMapper;
    private final ClientExpediteurMapper clientExpediteurMapper;
    private final DestinataireMapper destinataireMapper;
    private final ZoneMapper zoneMapper;

    // ===========================================
    // 1. MÉTHODES CLIENT EXPÉDITEUR
    // ===========================================

    /**
 * Créer une nouvelle demande de livraison
 */
    @Transactional
        public ColisDTO createColisWithDetails(String clientId,ColisRequestDTO request) {
            // 1. Gérer le client expéditeur
            ClientExpediteur client = clientExpediteurRepository.findById(clientId)
                .orElseGet(() -> {
                    ClientExpediteur newClient = clientExpediteurMapper.toEntity(request.getClientExpediteur());
                    return clientExpediteurRepository.save(newClient);
                });

            // 2. Gérer le destinataire
            Destinataire destinataire = destinataireRepository.findByEmail(request.getDestinataire().getEmail())
                .orElseGet(() -> {
                    Destinataire newDest = destinataireMapper.toEntity(request.getDestinataire());
                    return destinataireRepository.save(newDest);
                });

            // 3. Gérer la zone
            Zone zone = null;
            if (request.getZone() != null) {
                zone = zoneRepository.findByNomAndCodePostal(
                    request.getZone().getNom(),
                    request.getZone().getCodePostal()
                ).orElseGet(() -> zoneRepository.save(zoneMapper.toEntity(request.getZone())));
            }

            // 4. Créer le colis
            Colis colis = new Colis();
            colis.setDescription(request.getDescription());
            colis.setPoids(request.getPoids());
            colis.setPriorite(PrioriteEnum.valueOf(request.getPriorite()));
            colis.setVilleDestination(request.getVilleDestination());
            colis.setStatut(StatutColis.CREE);
            colis.setClientExpediteur(client);
            colis.setDestinataire(destinataire);
            colis.setZoneLivraison(zone);

            // 5. Sauvegarder le colis
            colis = colisRepository.save(colis);

            // 6. Gérer les produits
            if (request.getProduits() != null) {
                for (ColisProduitDTO produitDTO : request.getProduits()) {
                    // Vérifier si le produit existe déjà
                    Produit produit = produitRepository.findByNomAndCategorie(
                        produitDTO.getProduit().getNom(),
                        produitDTO.getProduit().getCategorie()
                    ).orElseGet(() -> {
                        // Créer un nouveau produit s'il n'existe pas
                        Produit newProduit = produitMapper.toEntity(produitDTO.getProduit());
                        return produitRepository.save(newProduit);
                    });

                    // Créer la relation Colis_Produit
                    ColisProduit colisProduit = new ColisProduit();
                    ColisProduitKey key = new ColisProduitKey();
                    key.setColisId(colis.getId());
                    key.setProduitId(produit.getId());
                    colisProduit.setId(key);
                    colisProduit.setColis(colis);
                    colisProduit.setProduit(produit);
                    colisProduit.setQuantite(produitDTO.getQuantite());
                    colisProduit.setPrix(produit.getPrix() * produitDTO.getQuantite());
                    colisProduit.setDateAjout(LocalDate.now());
                    colisProduitRepository.save(colisProduit);
                }
            }

            // 7. Créer l'historique
            HistoriqueLivraison historique = new HistoriqueLivraison();
            historique.setColis(colis);
            historique.setStatut(StatutColis.CREE);
            historique.setDateChangement(LocalDateTime.now());
            historique.setCommentaire("Colis créé avec succès");
            historiqueLivraisonRepository.save(historique);

            return colisMapper.toDto(colis);
        }
    /**
     * Lister tous les colis d'un client expéditeur
     */
    public Page<ColisDTO> findColisByClientExpediteur(String clientId, Pageable pageable) {
        log.debug("Récupération des colis pour le client expéditeur: {}", clientId);
        return colisRepository.findByClientExpediteurId(clientId, pageable)
                .map(colisMapper::toDto);
    }

    /**
     * Filtrer les colis d'un client par statut
     */
    public Page<ColisDTO> findColisByClientExpediteurAndStatut(
            String clientId, 
            String statut, 
            Pageable pageable
    ) {
        log.debug("Récupération des colis pour le client {} avec statut: {}", clientId, statut);
        return colisRepository.findByClientExpediteurIdAndStatut(
                clientId, 
                StatutColis.valueOf(statut), 
                pageable
        ).map(colisMapper::toDto);
    }

    // ===========================================
    // 2. MÉTHODES DESTINATAIRE
    // ===========================================

    /**
     * Consulter un colis par son ID (pour destinataire)
     */
    public Optional<ColisDTO> findById(String id) {
        log.debug("Récupération du colis avec l'ID: {}", id);
        return colisRepository.findById(id)
                .map(colisMapper::toDto);
    }

    // ===========================================
    // 3. MÉTHODES LIVREUR
    // ===========================================

    /**
     * Mettre à jour le statut d'un colis
     */
    public ColisDTO updateStatus(String colisId, String newStatus) {
        Colis colis = colisRepository.findById(colisId)
                .orElseThrow(() -> new RuntimeException("Colis non trouvé"));
        colis.setStatut(StatutColis.valueOf(newStatus));
        return colisMapper.toDto(colisRepository.save(colis));
    }

    /**
     * Assigner un livreur à un colis
     */
    public ColisDTO assignLivreur(String colisId, String livreurId) {
        Colis colis = colisRepository.findById(colisId)
                .orElseThrow(() -> new RuntimeException("Colis non trouvé"));
        Livreur livreur = livreurRepository.findById(livreurId)
                .orElseThrow(() -> new RuntimeException("Livreur non trouvé"));
        colis.setLivreur(livreur);
        return colisMapper.toDto(colisRepository.save(colis));
    }

    /**
     * Trouver les colis par ID de livreur et statut (pour les livreurs)
     */
    public Page<ColisDTO> findByLivreurIdAndStatut(String livreurId, String statut, Pageable pageable) {
        log.debug("Récupération des colis pour le livreur: {} avec statut: {}", livreurId, statut);
        
        if (statut != null && !statut.isBlank()) {
            // Si un statut est fourni, on filtre par livreur ET statut
            return colisRepository.findByLivreurIdAndStatut(
                    livreurId, 
                    StatutColis.valueOf(statut.toUpperCase()), 
                    pageable
            ).map(colisMapper::toDto);
        } else {
            // Si pas de statut, on récupère tous les colis du livreur
            return colisRepository.findByLivreurId(livreurId, pageable)
                    .map(colisMapper::toDto);
        }
    }

    // ===========================================
    // 4. MÉTHODES GESTIONNAIRE LOGISTIQUE
    // ===========================================

    /**
     * Récupérer tous les colis (avec pagination)
     */
    public List<ColisDTO> findAll() {
        log.debug("Récupération de tous les colis");
        return colisRepository.findAll().stream()
                .map(colisMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Supprimer un colis
     */
    public void deleteById(String id) {
        log.info("Suppression du colis avec l'ID: {}", id);
        colisRepository.deleteById(id);
        log.info("Colis supprimé avec succès, ID: {}", id);
    }

    /**
     * Obtenir des statistiques
     */
    public Map<String, Object> getStatistiques(String livreurId, String zoneId) {
        Map<String, Object> stats = new HashMap<>();
        stats.put("poidsTotal", colisRepository.getTotalPoidsByLivreurAndZone(livreurId, zoneId));
        stats.put("nombreColis", colisRepository.getNombreColisByLivreurAndZone(livreurId, zoneId));
        return stats;
    }

    /**
     * Grouper les colis par critère
     */
    public Map<String, Long> groupBy(String field) {
        switch (field) {
            case "zone":
                return colisRepository.groupByZone().stream()
                        .collect(Collectors.toMap(
                                o -> (String) o[0], 
                                o -> (Long) o[1]
                        ));
            case "statut":
                return colisRepository.groupByStatut().stream()
                        .collect(Collectors.toMap(
                                o -> (String) o[0], 
                                o -> (Long) o[1]
                        ));
            case "priorite":
                return colisRepository.groupByPriorite().stream()
                        .collect(Collectors.toMap(
                                o -> (String) o[0], 
                                o -> (Long) o[1]
                        ));
            default:
                throw new IllegalArgumentException("Champ de regroupement invalide: " + field);
        }
    }

    // ===========================================
    // 5. MÉTHODES DE RECHERCHE ET FILTRAGE
    // ===========================================

    /**
     * Rechercher par critères
     */
    public List<ColisDTO> findByCritere(String statut, String ville, String priorite) {
        if (statut != null) {
            return colisRepository.findByStatut(StatutColis.valueOf(statut)).stream()
                    .map(colisMapper::toDto)
                    .collect(Collectors.toList());
        } else if (ville != null) {
            return colisRepository.findByVilleDestination(ville).stream()
                    .map(colisMapper::toDto)
                    .collect(Collectors.toList());
        } else if (priorite != null) {
            return colisRepository.findByPriorite(PrioriteEnum.valueOf(priorite)).stream()
                    .map(colisMapper::toDto)
                    .collect(Collectors.toList());
        } else {
            return findAll();
        }
    }

    /**
     * Recherche par mot-clé
     */
    public Page<ColisDTO> searchByKeyword(String keyword, Pageable pageable) {
        return colisRepository.searchByKeyword(keyword, pageable)
                .map(colisMapper::toDto);
    }

    // ===========================================
    // 6. GESTION DES PRODUITS DANS LES COLIS
    // ===========================================

    /**
     * Ajouter un produit à un colis
     */
    public ColisDTO addProduitToColis(String id, ColisProduitDTO colisProduitDTO) {
        Colis colis = colisRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Colis non trouvé"));
        Produit produit = produitRepository.findById(colisProduitDTO.getProduit().getId())
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));
        
        ColisProduit colisProduit = colisProduitMapper.toEntity(colisProduitDTO, colis, produit);
 colisProduitRepository.save(colisProduit);
        return colisMapper.toDto(colis);
    }

    // ===========================================
    // 7. HISTORIQUE DES COLIS
    // ===========================================

    /**
     * Obtenir l'historique d'un colis
     */
    public List<HistoriqueLivraison> getHistoriqueForColis(String colisId) {
        return historiqueLivraisonRepository.findHistoriqueByColisId(colisId);
    }

    // Autres méthodes utilitaires...

    public Page<ColisDTO> findAll(String statut, String ville, String priorite, String zoneId, 
                                LocalDateTime dateDebut, LocalDateTime dateFin, 
                                Pageable pageable, String clientId, String destinataireId, 
                                String livreurId) {
        log.debug("Récupération de tous les colis");
        StatutColis statutEnum = statut != null ? StatutColis.valueOf(statut) : null;
        PrioriteEnum prioriteEnum = priorite != null ? PrioriteEnum.valueOf(priorite) : null;
        return colisRepository.findByCritere(statutEnum, ville, prioriteEnum, zoneId, 
                                           dateDebut, dateFin, pageable, 
                                           clientId, destinataireId, livreurId)
                .map(colisMapper::toDto);
    }
}