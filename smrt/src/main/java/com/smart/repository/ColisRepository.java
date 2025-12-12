package com.smart.repository;

import org.springframework.data.domain.Page;
import com.smart.entity.Colis;
import com.smart.entity.Zone;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import com.smart.entity.Enum.PrioriteEnum;
import com.smart.entity.Enum.StatutColis;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;

@Repository
public interface ColisRepository extends JpaRepository<Colis, String> {
    List<Colis> findByStatut(StatutColis statut);
    List<Colis> findByVilleDestination(String villeDestination);
    List<Colis> findByPriorite(PrioriteEnum priorite);
    @Query("SELECT c FROM Colis c WHERE c.zoneLivraison.nom = :zoneNom")
    List<Colis> findByZoneNom(@Param("zoneNom") String zoneNom);

    @Query("SELECT DISTINCT c FROM Colis c LEFT JOIN c.historique h WHERE " +
            "(:statut IS NULL OR c.statut = :statut) AND " +
            "(:ville IS NULL OR c.villeDestination = :ville) AND " +
            "(:priorite IS NULL OR c.priorite = :priorite) AND " +
            "(:zoneId IS NULL OR c.zoneLivraison.id = :zoneId) AND " +
            "(:dateDebut IS NULL OR h.dateChangement >= :dateDebut) AND " +
            "(:dateFin IS NULL OR h.dateChangement <= :dateFin) AND " +
            "(:clientId IS NULL OR c.clientExpediteur.id = :clientId) AND " +
            "(:destinataireId IS NULL OR c.destinataire.id = :destinataireId) AND " +
            "(:livreurId IS NULL OR c.livreur.id = :livreurId)")
    Page<Colis> findByCritere(@Param("statut") StatutColis statut,
                               @Param("ville") String ville,
                               @Param("priorite") PrioriteEnum priorite,
                               @Param("zoneId") String zoneId,
                               @Param("dateDebut") LocalDateTime dateDebut,
                               @Param("dateFin") LocalDateTime dateFin,
                               Pageable pageable, @Param("clientId") String clientId, @Param("destinataireId") String destinataireId, @Param("livreurId") String livreurId);

    Page<Colis> findAll(Pageable pageable);

        @Query("SELECT c FROM Colis c WHERE lower(c.description) LIKE lower(concat('%', :keyword, '%')) OR lower(c.villeDestination) LIKE lower(concat('%', :keyword, '%'))")

        Page<Colis> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    

        @Query("SELECT COALESCE(SUM(c.poids), 0) FROM Colis c WHERE c.livreur.id = :livreurId AND c.zoneLivraison.id = :zoneId")

        Double getTotalPoidsByLivreurAndZone(@Param("livreurId") String livreurId, @Param("zoneId") String zoneId);

    

        @Query("SELECT COUNT(c) FROM Colis c WHERE c.livreur.id = :livreurId AND c.zoneLivraison.id = :zoneId")

                Long getNombreColisByLivreurAndZone(@Param("livreurId") String livreurId, @Param("zoneId") String zoneId);

        

            @Query("SELECT z.nom, COUNT(c) FROM Colis c JOIN c.zoneLivraison z GROUP BY z.id")

            List<Object[]> groupByZone();

        

            @Query("SELECT c.statut, COUNT(c) FROM Colis c GROUP BY c.statut")

            List<Object[]> groupByStatut();

        

            @Query("SELECT c.priorite, COUNT(c) FROM Colis c GROUP BY c.priorite")

            List<Object[]> groupByPriorite();
            

            List<Colis> findByClientExpediteurId(String clientId);
            List<Colis> findByDestinataireId(String destinataireId);
            List<Colis> findByLivreurId(String livreurId);


            // partie concerné par expediteur 
            // Pour récupérer les colis d'un client expéditeur
                @Query("SELECT c FROM Colis c WHERE c.clientExpediteur.id = :clientId")
                Page<Colis> findByClientExpediteurId(@Param("clientId") String clientId, Pageable pageable);

                // Pour récupérer les colis d'un client expéditeur par statut
                @Query("SELECT c FROM Colis c WHERE c.clientExpediteur.id = :clientId AND c.statut = :statut")
                Page<Colis> findByClientExpediteurIdAndStatut(
                    @Param("clientId") String clientId, 
                    @Param("statut") StatutColis statut,
                    Pageable pageable
                );

                // Pour compter les colis par statut pour un client
                @Query("SELECT c.statut, COUNT(c) FROM Colis c WHERE c.clientExpediteur.id = :clientId GROUP BY c.statut")
                List<Object[]> countByClientExpediteurIdGroupByStatut(@Param("clientId") String clientId);

                // livreur 

                /**
                 * Trouver les colis par ID de livreur et statut
                 */
                Page<Colis> findByLivreurIdAndStatut(String livreurId, StatutColis statut, Pageable pageable);

                    /**

                     * Trouver tous les colis d'un livreur (sans filtre de statut)

                     */

                    Page<Colis> findByLivreurId(String livreurId, Pageable pageable);

                

                    Long countByStatut(StatutColis statut);

                

                    List<Colis> findByLivreurIsNull();

                }