package com.trainingquizzes.english.model;

public class Option {
	
        private String prompt;
        private boolean isCorrect;
        
        private Option() {}

        private Option(String prompt, boolean isCorrect) {
            this.prompt = prompt;
            this.isCorrect = isCorrect;
        }

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
