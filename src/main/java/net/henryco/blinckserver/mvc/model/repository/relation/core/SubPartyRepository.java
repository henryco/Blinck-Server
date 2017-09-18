package net.henryco.blinckserver.mvc.model.repository.relation.core;

import net.henryco.blinckserver.mvc.model.entity.relation.core.SubParty;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author Henry on 28/08/17.
 */
public interface SubPartyRepository extends JpaRepository<SubParty, Long> {


	List<SubParty> getAllByUsersIsContainingAndDetails_InQueueIsTrue(Long user);

	List<SubParty> getAllByUsersIsContainingAndPartyNotNullAndParty_Details_InQueueIsFalse(Long user);

	List<SubParty> getAllByUsersIsContaining(Long user);

	List<SubParty> getAllByDetails_Type_WantedAndDetails_Type_IdentAndDetails_Type_DimensionAndDetails_InQueue(
			String details_type_wanted,
			String details_type_ident,
			Integer details_dimension,
			Boolean details_inQueue
	);


	List<SubParty> getAllByDetails_Type_WantedAndDetails_Type_IdentAndDetails_Type_DimensionAndDetails_InQueue(
			String details_type_wanted,
			String details_type_ident,
			Integer details_dimension,
			Boolean details_inQueue,
			Pageable pageable
	);


	List<SubParty> getFirst100ByDetails_Type_WantedAndDetails_Type_IdentAndDetails_Type_DimensionAndDetails_InQueue(
			String details_type_wanted,
			String details_type_ident,
			Integer details_type_dimension,
			Boolean details_inQueue
	);

	Boolean existsByIdAndUsersIsContaining(Long id, Long user);
}