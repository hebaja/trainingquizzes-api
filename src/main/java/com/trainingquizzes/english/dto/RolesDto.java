package com.trainingquizzes.english.dto;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;

public class RolesDto {
	
	private String role;
	
	public RolesDto(GrantedAuthority authority) {
		this.setRole(authority.getAuthority());
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	public static List<RolesDto> convertToList(Collection<? extends GrantedAuthority> authorities) {
		return authorities.stream().map(RolesDto::new).collect(Collectors.toList());
	}
	

}
