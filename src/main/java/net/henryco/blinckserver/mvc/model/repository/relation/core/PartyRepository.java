package net.henryco.blinckserver.mvc.model.repository.relation.core;

import net.henryco.blinckserver.mvc.model.entity.relation.core.Party;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Temporal;

import java.util.Date;
import java.util.List;

import static javax.persistence.TemporalType.TIMESTAMP;

/**
 * @author Henry on 28/08/17.
 */
public interface PartyRepository extends JpaRepository<Party, Long> {


	List<Party> getAllByDetails_InQueueIsTrueAndDetails_Type_DimensionAndDetails_TypeIdentAndDetails_Type_Wanted(
			Integer details_type_dimension,
			String details_type_ident,
			String details_type_wanted
	);

	List<Party> getFirst100ByDetails_InQueueIsTrueAndDetails_Type_DimensionAndDetails_TypeIdentAndDetails_Type_Wanted(
			Integer details_type_dimension,
			String details_type_ident,
			String details_type_wanted
	);

	boolean existsByIdAndMeetingActivationTimeBefore(
			Long id,
			@Temporal(TIMESTAMP)
					Date meeting_activationTime
	);
}