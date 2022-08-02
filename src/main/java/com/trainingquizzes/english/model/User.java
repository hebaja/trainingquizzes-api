package com.trainingquizzes.english.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.trainingquizzes.english.enums.Roles;

@SuppressWarnings("serial")
@Entity
public class User implements UserDetails {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String uid;
	private String username;
	
	@Column(unique = true)
	private String email;
	private String password;
	private Boolean enabled;
	private String pictureUrl;
    
    @ElementCollection(fetch = FetchType.LAZY)
    private List<Account> accounts;
    
    @ElementCollection(fetch = FetchType.EAGER)
	private List<Authority> roles;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Exercise> exercises;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
	private List<Subject> subjects;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
	private List<Quest> quests;
	
	@ElementCollection
	private Set<Long> subscribedQuestsIds = new HashSet<>();
	
	public User(String uid, String password, String username, String email) {
		this.uid = uid;
		this.username = username;
		this.email = email;
	}
	
	public User() {}
	
	public User(String username, String email, String password, List<Authority> roles) {
		this.username = username;
		this.email = email;
		this.password = password;
		this.roles = roles;
	}

	public User(@NotEmpty String uid, @NotEmpty @Length(min = 4) String username, String email) {
		this.uid = uid;
		this.username = username;
		this.email = email;
	}

	public User(String username, @Email @NotBlank String email, String password, boolean enabled, List<Authority> roles, List<Account> accounts) {
		this.username = username;
		this.email = email;
		this.password = password;
		this.enabled = enabled;
		this.roles = roles;
		this.accounts = accounts;
	}
	
	public User(String username, @Email @NotBlank String email, String password, boolean enabled, List<Authority> roles, List<Account> accounts, Roles role) {
		this.username = username;
		this.email = email;
		this.password = password;
		this.enabled = enabled;
		this.roles = roles;
		this.accounts = accounts;
	}
	
	public void addSubscribedQuestsId(Long questId) {
		this.subscribedQuestsIds.add(questId);
	}

	public Long getId() {
		return id;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPassword() {
		return password;
	}
    
	public void setPassword(String password) {
		this.password = password;
	}
	
	public List<Authority> getRoles() {
		return roles;
	}
	
	public void setRoles(List<Authority> roles) {
		this.roles = roles;
	}
	
	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	
	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	}
	
	public List<Account> getAccounts() {
		return accounts;
	}
	
	public List<Exercise> getExercises() {
		return exercises;
	}

	public void setExercises(List<Exercise> exercises) {
		this.exercises = exercises;
	}
	
	public List<Subject> getSubjects() {
		return subjects;
	}

	public void setSubjects(List<Subject> subjects) {
		this.subjects = subjects;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = new ArrayList<>();
		roles.forEach(authority -> authorities.add(new SimpleGrantedAuthority(authority.getRole().toString())));
		
        return authorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	public List<Quest> getQuests() {
		return quests;
	}

	public void setQuests(List<Quest> quests) {
		this.quests = quests;
	}

	public Set<Long> getSubscribedQuestsIds() {
		return subscribedQuestsIds;
	}

	public void setSubscribedQuestsIds(Set<Long> subscribedQuestsIds) {
		this.subscribedQuestsIds = subscribedQuestsIds;
	}

}
