package net.henryco.blinckserver.mvc.model.entity.security;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.henryco.blinckserver.util.entity.BlinckAuthorityEntity;
import org.hibernate.annotations.Proxy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Henry on 30/08/17.
 */
@Entity @Data
@NoArgsConstructor
@Proxy(lazy = false)
@Table(name = "ADMIN_PROFILE_AUTH")
public class AdminAuthProfile implements BlinckAuthorityEntity<String> {


	private @Id @Column(
			name = "username",
			unique = true
	) String id;


	private @Column(
			nullable = false
	) boolean enabled;


	private @Column(
			nullable = false
	) boolean locked;


	private @Column(
			nullable = false
	) boolean expired;


	private @Column(
			nullable = false
	) String authorities;


	private @Column
	String password;


}