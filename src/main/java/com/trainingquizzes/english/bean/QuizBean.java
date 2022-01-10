package com.trainingquizzes.english.bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.access.EjbAccessException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import com.trainingquizzes.english.enums.LevelType;
import com.trainingquizzes.english.model.Exercise;
import com.trainingquizzes.english.model.ExercisesQuantity;
import com.trainingquizzes.english.model.Subject;
import com.trainingquizzes.english.model.Task;
import com.trainingquizzes.english.model.User;
import com.trainingquizzes.english.repository.ExerciseRepository;
import com.trainingquizzes.english.repository.SubjectRepository;
import com.trainingquizzes.english.repository.TaskRepository;
import com.trainingquizzes.english.repository.UserRepository;

@Controller
@ViewScoped
public class QuizBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ExerciseRepository exerciseRepository;
	
	@Autowired
	private SubjectRepository subjectRepository;
	
	@Autowired
	private TaskRepository taskRepository;

	private List<Subject> easySubjects = new ArrayList<Subject>();
	private List<Subject> mediumSubjects = new ArrayList<Subject>();
	private List<Subject> hardSubjects = new ArrayList<Subject>();
	private List<Subject> subjects;
	private Subject subject;
	private List<Task> chosenTasks;
	private List<Task> reducedTasks;
	
	private ExercisesQuantity exercisesQuantity;
	private Integer score = 0;
	private Integer count = 0;
	private String warning;
	private String warningReduced;
	private String warningCardClass;
	private String warningCardShadow;
	private String buttonNextLabel;
	private String buttonNextLabelReduced;
	private String buttonNextIcon;
	private String warningContentIcon;
	private Boolean endRender = false;
	private Boolean levelsRender = true;
	private Boolean subjectsRender = false;
	private Boolean quizRender = false;
	private Boolean nextTaskRender = false;
	private Boolean disableOptionButtons = false;
	private Boolean disableEasyButton = false;
	private Boolean disableMediumButton = false;
	private Boolean disableHardButton = false;
	private Boolean saveQuizAjaxDisabled = false;
	private Boolean loginToSaveRender = false;
	private Boolean nextQuestionButtonRender = true;
	private String remoteUser = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
	
	@PostConstruct
	private void init() {
		List<Subject> subjectsFromDatabase = subjectRepository.findAll();
		subjectsFromDatabase.forEach(subjectFromDataBase -> {
			if(subjectFromDataBase.getLevel() == LevelType.EASY) {
				easySubjects.add(subjectFromDataBase);
			}
			if(subjectFromDataBase.getLevel() == LevelType.MEDIUM) {
				mediumSubjects.add(subjectFromDataBase);
			}
			if(subjectFromDataBase.getLevel() == LevelType.HARD) {
				hardSubjects.add(subjectFromDataBase);
			}  
		});
		
	}

	public void easyButton() throws EjbAccessException, IOException {
		setScore(0);
		setSubjectsRender(true);
		setDisableEasyButton(true);
		setDisableMediumButton(false);
		setDisableHardButton(false);
		setLoginToSaveRender(false);
		setSubjects(easySubjects);
	}

	public void mediumButton() throws IOException {
		setScore(0);
		setSubjectsRender(true);
		setDisableEasyButton(false);
		setDisableMediumButton(true);
		setDisableHardButton(false);
		setLoginToSaveRender(false);
		setSubjects(mediumSubjects);
	}
	
	public void hardButton() throws IOException {
		setScore(0);
		setSubjectsRender(true);
		setDisableEasyButton(false);
		setDisableMediumButton(false);
		setDisableHardButton(true);
		setLoginToSaveRender(false);
		setSubjects(hardSubjects);
	}
	
	public void createExercise(Subject selectedSubject) {
		Optional<List<Task>> tasksReceivedOptional = taskRepository.findAllBySubject(selectedSubject);
		chosenTasks = tasksReceivedOptional.orElse(null);
		prepareExercise();
		this.subject = selectedSubject;
		setButtonNextLabel("Next question");
		setButtonNextLabelReduced("Next");
		setLevelsRender(false);
		setSubjectsRender(false);
		setQuizRender(true);
		setNextTaskRender(true);
		setButtonNextIcon("fas fa-step-forward");
	}
	
	public void updateExercise() {
		setWarningCardShadow("");
		if(getCount() < reducedTasks.size()) {
			setWarning(null);
			setWarningCardClass(null);
			setDisableOptionButtons(false);
			count++;
		}
		if(getCount() == 9) {
			setButtonNextLabel("Save result");
			setButtonNextLabelReduced("Save");
			setButtonNextIcon("fas fa-save");
		}
		if (getCount() == 10){
			setQuizRender(false);
			setNextTaskRender(false);
			setEndRender(true);
			saveExercise();
		}
	}
	
	public void checkOption(boolean isCorrect) {
		setNextTaskRender(true);
		setDisableOptionButtons(true);
		setWarningCardShadow("shadow");
		if(isCorrect) {
			setWarning("Right answer");
			setWarningReduced("Right");
			setWarningContentIcon("fas fa-check-circle");
			setWarningCardClass("animate__pulse col col-4 mx-auto text-center card bg-success");
			score++;
		} else {
			setWarningCardClass("animate__headShake col col-4 mx-auto text-center card bg-danger");
			setWarning("Wrong answer");
			setWarningReduced("Wrong");
			setWarningContentIcon("fas fa-times-circle");
		}
		if(getCount() == 9 && remoteUser == null) {
			setLoginToSaveRender(true);
			setNextQuestionButtonRender(false);
		}
	}
	
	public void restart() throws EjbAccessException, IOException {
		prepareExercise();
		setEndRender(false);
		setSubjectsRender(false);
		setLevelsRender(false);
		setQuizRender(true);
		setNextTaskRender(false);
		setWarning(null);
		setButtonNextLabel("Next question");
		setButtonNextLabelReduced("Next");
		setButtonNextIcon("fas fa-step-forward");
		setScore(0);
		setCount(0);
	}
	
	private void prepareExercise() {
		Collections.shuffle(chosenTasks);
		List<Task> finalTasks = chosenTasks.subList(0, 10);
		finalTasks.forEach(task -> {
			if(task.isShuffleOptions()) {
				Collections.shuffle(task.getOptions());
			}
		});
		setReducedTasks(finalTasks);
	}
	
	private void saveExercise() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userInfo = authentication.getName();
		
		Optional<User> userOptional = userRepository.findByEmail(userInfo);
		User user = userOptional.orElse(userRepository.findByUid(userInfo).orElse(null));
		
		if(user != null) {
			Exercise exercise = new Exercise(user, this.subject, this.subject.getLevel(), score);
			Optional<ExercisesQuantity> optionalExercisesQuantity = exerciseRepository.getQuantityOfTheSameExercise(user,
					exercise.getLevel(), exercise.getSubject());
			
			exercisesQuantity = optionalExercisesQuantity.orElse(null);
			
			checkIfExerciseQuantityIsNull(user, exercise);
			removeOlderExercise(user, exercise);
			
			exerciseRepository.save(exercise);
		} else {
			System.out.println("user is null");
		}
	}
	
	private void checkIfExerciseQuantityIsNull(User user, Exercise exercise) {
		if (exercisesQuantity == null) {
			exercisesQuantity = new ExercisesQuantity(user, exercise.getLevel(), exercise.getSubject(), 0);
		}
	}
	
	private void removeOlderExercise(User user, Exercise exercise) {
		if(exercisesQuantity.getQuantity() >= 10) {
			List<Exercise> exerciseList = exerciseRepository.getExercisesByUserLevelAndSubject(user,exercise.getLevel(), exercise.getSubject());
			Exercise exerciseToBeRemoved = exerciseList.get(0);
			exerciseRepository.delete(exerciseToBeRemoved);
		}
	}
	
	public void reset(){
		FacesContext.getCurrentInstance().getViewRoot().getViewMap().clear();
	}
	
	@PreAuthorize("isAuthenticated()")
	public void loginToSave() {
		setWarningCardShadow("");
		remoteUser = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
		setLoginToSaveRender(false);
		setNextQuestionButtonRender(true);
	}
	
	public List<Subject> getSubjects() {
		return subjects;
	}

	public void setSubjects(List<Subject> subjects) {
		this.subjects = subjects;
	}
	
	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public Boolean getNextQuestionButtonRender() {
		return nextQuestionButtonRender;
	}

	public void setNextQuestionButtonRender(Boolean nextQuestionButtonRender) {
		this.nextQuestionButtonRender = nextQuestionButtonRender;
	}

	public Boolean getLoginToSaveRender() {
		return loginToSaveRender;
	}

	public void setLoginToSaveRender(Boolean loginToSaveRender) {
		this.loginToSaveRender = loginToSaveRender;
	}

	public Boolean getSaveQuizAjaxDisabled() {
		return saveQuizAjaxDisabled;
	}

	public void setSaveQuizAjaxDisabled(Boolean saveQuizAjaxDisabled) {
		this.saveQuizAjaxDisabled = saveQuizAjaxDisabled;
	}

	public Boolean getDisableEasyButton() {
		return disableEasyButton;
	}

	public void setDisableEasyButton(Boolean disableEasyButton) {
		this.disableEasyButton = disableEasyButton;
	}

	public Boolean getDisableMediumButton() {
		return disableMediumButton;
	}

	public void setDisableMediumButton(Boolean disableMediumButton) {
		this.disableMediumButton = disableMediumButton;
	}

	public Boolean getDisableHardButton() {
		return disableHardButton;
	}

	public void setDisableHardButton(Boolean disableHardButton) {
		this.disableHardButton = disableHardButton;
	}

	public String getWarningCardClass() {
		return warningCardClass;
	}

	public void setWarningCardClass(String warningCardClass) {
		this.warningCardClass = warningCardClass;
	}

	public void setWarning(String warning) {
		this.warning = warning;
	}

	public Integer getCount() {
		return count;
	}
	
	public void setCount(Integer count) {
		this.count = count;
	}

	public String getWarning() {
		return warning;
	}
	
	public Integer getScore() {
		return score;
	}
	
	public void setScore(Integer score) {
		this.score = score;
	}

	public void setButtonNextLabel(String buttonNextLabel) {
		this.buttonNextLabel = buttonNextLabel;
	}
	
	public String getButtonNextLabel() {
		return buttonNextLabel;
	}
	
	public Boolean getLevelsRender() {
		return levelsRender;
	}
	
	public void setLevelsRender(Boolean levelsRender) {
		this.levelsRender = levelsRender;
	}

	public Boolean getSubjectsRender() {
		return subjectsRender;
	}
	
	public void setSubjectsRender(Boolean subjectsRender) {
		this.subjectsRender = subjectsRender;
	}

	public Boolean getEndRender() {
		return endRender;
	}
	
	public void setEndRender(Boolean endRender) {
		this.endRender = endRender;
	}

	public Boolean getQuizRender() {
		return quizRender;
	}
		
	public void setQuizRender(Boolean quizRender) {
		this.quizRender = quizRender;
	}

	public Boolean getNextTaskRender() {
		return nextTaskRender;
	}
	
	public void setNextTaskRender(Boolean nextTaskRender) {
		this.nextTaskRender = nextTaskRender;
	}

	public Boolean getDisableOptionButtons() {
		return disableOptionButtons;
	}

	public void setDisableOptionButtons(Boolean enableOptionButtons) {
		this.disableOptionButtons = enableOptionButtons;
	}
	
	public String getWarningContentIcon() {
		return warningContentIcon;
	}

	public void setWarningContentIcon(String warningContentIcon) {
		this.warningContentIcon = warningContentIcon;
	}

	public String getWarningCardShadow() {
		return warningCardShadow;
	}

	public void setWarningCardShadow(String warningCardShadow) {
		this.warningCardShadow = warningCardShadow;
	}

	public String getButtonNextIcon() {
		return buttonNextIcon;
	}

	public void setButtonNextIcon(String buttonNextIcon) {
		this.buttonNextIcon = buttonNextIcon;
	}
	
	public String getWarningReduced() {
		return warningReduced;
	}

	public void setWarningReduced(String warningReduced) {
		this.warningReduced = warningReduced;
	}

	public String getButtonNextLabelReduced() {
		return buttonNextLabelReduced;
	}

	public void setButtonNextLabelReduced(String buttonNextLabelReduced) {
		this.buttonNextLabelReduced = buttonNextLabelReduced;
	}

	public List<Task> getReducedTasks() {
		return reducedTasks;
	}

	public void setReducedTasks(List<Task> tasksFetched) {
		this.reducedTasks = tasksFetched;
	}

}
