package net.henryco.blinckserver.mvc.model.entity.relation.core.embeded;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import java.util.Date;

import static javax.persistence.TemporalType.TIMESTAMP;

/**
 * <h1>Meeting JSON:</h1>
 * <h2>
 *     {&nbsp;
 *         "time":				DATE / LONG, &nbsp;
 *         "active_after":		DATE / LONG, &nbsp;
 *         "venue":				CHAR[510]
 *     &nbsp;}
 * </h2>
 * @author Henry on 18/09/17.
 */
@Data @Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Meeting {


	private @Column(
			name = "time"
	) @Temporal(
			TIMESTAMP
	) Date time;


	private @Column(
			name = "active_after"
	) @JsonProperty(
			"active_after"
	) @Temporal(
			TIMESTAMP
	) Date activationTime;


	private @Column(
			name = "venue",
			length = 510
	) String venue;


}