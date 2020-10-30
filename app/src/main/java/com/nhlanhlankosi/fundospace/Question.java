package com.nhlanhlankosi.fundospace;

public class Question {

    private String questionImgUrl, questionText, option1, option2,
            option3, option4, answer, explanation, explanationImgUrl, topicId, chosenOption;

    public Question() {
    }

    public Question(String questionImgUrl, String questionText, String option1, String option2,
                    String option3, String option4, String answer, String explanation,
                    String explanationImgUrl, String topicId, String chosenOption) {

        this.questionImgUrl = questionImgUrl;
        this.questionText = questionText;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.answer = answer;
        this.explanation = explanation;
        this.explanationImgUrl = explanationImgUrl;
        this.topicId = topicId;
        this.chosenOption = chosenOption;

    }

    public String getQuestionImgUrl() {
        return questionImgUrl;
    }

    public void setQuestionImgUrl(String questionImgUrl) {
        this.questionImgUrl = questionImgUrl;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public String getOption4() {
        return option4;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getExplanationImgUrl() {
        return explanationImgUrl;
    }

    public void setExplanationImgUrl(String explanationImgUrl) {
        this.explanationImgUrl = explanationImgUrl;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getChosenOption() {
        return chosenOption;
    }

    public void setChosenOption(String chosenOption) {
        this.chosenOption = chosenOption;
    }
}
