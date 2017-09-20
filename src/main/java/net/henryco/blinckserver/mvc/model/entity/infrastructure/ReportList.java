package net.henryco.blinckserver.mvc.model.entity.infrastructure;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.GenerationType.AUTO;

/** <h1>Report list JSON</h1>
 * 	<h2>
 * 	    {&nbsp;
 * 	        "id":				LONG, &nbsp;
 * 	        "reporter_id":		LONG, &nbsp;
 * 	        "reported_id":		LONG, &nbsp;
 * 	        "reason":			CHAR[255]
 * 	    &nbsp;}
 * 	</h2>
 * @author Henry on 28/08/17.
 */
@Entity @Data
@NoArgsConstructor
public class ReportList {


	private @Id @Column(
			unique = true
	) @GeneratedValue(
			strategy = AUTO
	) Long id;


	private @Column(
			name = "reporter_id",
			updatable = false,
			nullable = false
	) @JsonProperty(
			value = "reporter_id"
	) Long reporterId;


	private @Column(
			name = "reported_id",
			updatable = false,
			nullable = false
	) @JsonProperty(
			value = "reported_id"
	) Long reportedId;


	private @Column(
			name = "reason",
			updatable = false
	) String reason;


}