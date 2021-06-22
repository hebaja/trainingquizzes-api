package com.trainingquizzes.english.bean;

import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import com.trainingquizzes.english.model.Average;
import com.trainingquizzes.english.model.User;
import com.trainingquizzes.english.repository.UserRepository;

@Controller
@ViewScoped
public class AveragesBean {
	
	@Autowired
	private UserRepository userRepository;
	
	private List<Average> averages;
	private User user;
	String remoteUser = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
	
	public List<Average> getAverages() {
		return averages;
	}
	
	public User getUser() {
		return user;
	}

	@PostConstruct
	public void generateAverages() {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userInfo = authentication.getName();
		
		Optional<User> userOptional = userRepository.findByEmail(userInfo);
		user = userOptional.orElse(userRepository.findByUid(userInfo).orElse(null));
		
		averages = userRepository.getAveragesByEmail(user.getEmail());
		
		if(averages != null) {
			averages.forEach(average -> {
				if(average.getAverage() <= 4) {
	        		average.setBootstrapColor("bg-danger");
	        	} else if (average.getAverage() > 4 && average.getAverage() < 8) {
	        		average.setBootstrapColor("bg-warning");
	        	} else {
	        		average.setBootstrapColor("bg-success");
	        	}
			});
		} else {
			System.out.println("averages is null");
		}
	}
}
