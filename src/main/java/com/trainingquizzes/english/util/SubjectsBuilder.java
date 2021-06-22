package com.trainingquizzes.english.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.trainingquizzes.english.model.SubjectOld;

public class SubjectsBuilder {
	
	private int order = 0;

    public List<SubjectOld> easyList() {
        List<SubjectOld> subjects = new ArrayList<SubjectOld>(Arrays.asList(
                new SubjectOld("Comparative and superlative", "easy_comparative_superlative.json", order++),
                new SubjectOld("First conditional", "easy_first_conditional.json", order++),
                new SubjectOld("Present simple and continuous", "easy_present_simple_continuous.json", order++),
                new SubjectOld("Present simple and perfect", "easy_present_simple_perfect.json", order++),
                new SubjectOld("Reported speech", "easy_reported_speech.json", order++),
                new SubjectOld("Simple past", "easy_simple_past.json", order++),
                new SubjectOld("Simple present", "easy_simple_present.json", order++),
                new SubjectOld("Verb to be", "easy_verb_to_be.json", order++)));
        return subjects;
    }

    public List<SubjectOld> mediumList() {
        List<SubjectOld> subjects = new ArrayList<SubjectOld>(Arrays.asList(
                new SubjectOld("Active and Passive voice", "medium_active_to_passive.json", order++),
                new SubjectOld("Adverbs", "medium_adverbs.json", order++),
                new SubjectOld("Comparative and Superlative", "medium_comparative_superlative.json", order++),
                new SubjectOld("Modals", "medium_modal_verbs.json", order++),
                new SubjectOld("Phrasal verbs", "medium_phrasal_verbs.json", order++),
                new SubjectOld("Prepositions", "medium_prepositions.json", order++),
                new SubjectOld("Reported speech", "medium_reported_speech.json", order++),
                new SubjectOld("Second conditional", "medium_second_conditional.json", order++),
                new SubjectOld("Third conditional", "medium_third_conditional.json", order++)));
        return subjects;
    }

    public List<SubjectOld> hardList() {
        List<SubjectOld> subjects = new ArrayList<SubjectOld>(Arrays.asList(
                new SubjectOld("Articles and determiners", "hard_articles_determiners.json", order++),
                new SubjectOld("Binomials", "hard_binomials.json", order++),
                new SubjectOld("Collocations", "hard_collocations.json", order++),
                new SubjectOld("Idiomatic expressions", "hard_idiomatic_expressions.json", order++),
                new SubjectOld("Linking words", "hard_linking_words.json", order++),
                new SubjectOld("Phrasal verbs", "hard_phrasal_verbs.json", order++),
                new SubjectOld("Similes", "hard_similes.json", order++),
                new SubjectOld("Word Formation", "hard_word_formation.json", order++)));
        return subjects;
    }

}
