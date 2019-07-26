package com.persistance.Configuration;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.persistance.DomainModel.Account;

public abstract class OnDatabaseFirstCreationListener {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@PostConstruct
	public void fillDatabaseWithInitialData() {
		if (this.isFirstCreation()) {
			Account compte = this.createMainAccount();
			compte.setUsername("admin");
			compte.setPassword(this.passwordEncoder.encode("admin"));

			this.getAccountRepository().save(compte);
			this.onFirstCreationCallback();
		}
	}

	public boolean isFirstCreation() {
		return this.getAccountRepository().findAll().size() == 0;
	}

	public void onFirstCreationCallback() {
	}

	public abstract <T extends Account> JpaRepository<T, ?> getAccountRepository();

	public abstract Account createMainAccount();
}
