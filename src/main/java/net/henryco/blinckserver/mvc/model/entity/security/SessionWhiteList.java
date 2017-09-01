package net.henryco.blinckserver.mvc.model.entity.security;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;

import static javax.persistence.GenerationType.AUTO;

/**
 * @author Henry on 01/09/17.
 */
@Entity @Data
@Proxy(lazy = false)
@NoArgsConstructor
public class SessionWhiteList {


	private @Id @Column(
			unique = true
	) @GeneratedValue(
			strategy = AUTO
	) Long id;


	private @Column(
			name = "user_id",
			unique = true,
			updatable = false
	) Long userId;


	private @Column(
			name = "admin_id",
			unique = true,
			updatable = false
	) String adminId;


}