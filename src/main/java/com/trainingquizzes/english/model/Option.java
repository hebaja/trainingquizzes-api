package com.trainingquizzes.english.model;

public class Option {
	
        private String prompt;
        private boolean isCorrect;
        
        public Option() {}

        public String getPrompt() { return prompt; }

        public void setPrompt(String option0) {
            this.prompt = option0;
        }

		public boolean isCorrect() {
			return isCorrect;
		}

		public void setCorrect(boolean isCorrect) {
			this.isCorrect = isCorrect;
		}
        
}
