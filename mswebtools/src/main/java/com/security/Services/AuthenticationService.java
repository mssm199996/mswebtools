package com.security.Services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.persistance.DomainModel.Account;

public abstract class AuthenticationService implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		Account account = this.findByUsername(username);

		if (account == null)
			throw new UsernameNotFoundException("Wrong username");

		return account;
	}

	public abstract Account findByUsername(String username);
}
