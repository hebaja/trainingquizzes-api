var fallbackToStore = function() {
  window.location.replace('market://details?id=br.com.hebaja.englishtrainingquizzes');
};

var openAppRegister = function() {
  window.location.replace('my.special.scheme://details?id=user-register');
};

var openAppPassword = function() {
  window.location.replace('my.special.scheme://details?id=password-reset');
};

var openAppDeleteUser = function() {
  window.location.replace('my.special.scheme://details?id=user-removed');
};

var triggerAppOpenAfterRegister = function() {
  openAppRegister();
  setTimeout(fallbackToStore, 250);
};

var triggerAppOpenAfterPasswordAlter = function() {
	openAppPassword();
	setTimeout(fallbackToStore, 250);
}

var triggerAppOpenAfterDeleteUser = function() {
	openAppDeleteUser();
	setTimeout(fallbackToStore, 250);
}